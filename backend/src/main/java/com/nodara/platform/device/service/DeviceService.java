package com.nodara.platform.device.service;

import com.nodara.platform.device.dto.DeviceRegistrationRequest;
import com.nodara.platform.device.dto.DeviceResponse;
import com.nodara.platform.device.entity.Device;
import com.nodara.platform.device.repository.DeviceRepository;
import com.nodara.platform.device.repository.DeviceTelemetryRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceTelemetryRepository deviceTelemetryRepository;

    @Value("${nodara.heartbeat.timeout-seconds:90}")
    private long heartbeatTimeoutSeconds = 90L;

    public DeviceService(DeviceRepository deviceRepository, DeviceTelemetryRepository deviceTelemetryRepository) {
        this.deviceRepository = deviceRepository;
        this.deviceTelemetryRepository = deviceTelemetryRepository;
    }

    /**
     * Register or update a device by stable device UUID when available, otherwise by hostname.
     * Existing devices keep their original registeredAt and update their lastHeartbeat metadata.
     */
    @Transactional
    public DeviceResponse registerDevice(DeviceRegistrationRequest request) {
        String normalizedDeviceUuid = normalizeDeviceUuid(request.getDeviceUuid());
        String hostname = request.getHostname() == null ? null : request.getHostname().trim();
        LocalDateTime now = LocalDateTime.now();

        Device device = findExistingDevice(normalizedDeviceUuid, hostname).orElseGet(() -> {
            Device newDevice = new Device(
                normalizedDeviceUuid != null ? normalizedDeviceUuid : UUID.randomUUID().toString(),
                hostname,
                request.getOsName(),
                request.getOsVersion(),
                request.getAgentVersion()
            );
            newDevice.setCreatedAt(now);
            newDevice.setUpdatedAt(now);
            return newDevice;
        });

        if (device.getId() != null) {
            device.setHostname(hostname);
            device.setOsName(request.getOsName());
            device.setOsVersion(request.getOsVersion());
            device.setAgentVersion(request.getAgentVersion());
            if (normalizedDeviceUuid != null) {
                device.setDeviceUuid(normalizedDeviceUuid);
            }
            device.setLastHeartbeat(now);
            device.setUpdatedAt(now);
        }

        Device savedDevice = deviceRepository.save(device);
        return new DeviceResponse(savedDevice, heartbeatTimeoutSeconds);
    }

    private Optional<Device> findExistingDevice(String normalizedDeviceUuid, String hostname) {
        if (normalizedDeviceUuid != null && !normalizedDeviceUuid.isBlank()) {
            Optional<Device> byUuid = deviceRepository.findByDeviceUuid(normalizedDeviceUuid);
            if (byUuid.isPresent()) {
                return byUuid;
            }
        }

        if (hostname != null && !hostname.isBlank()) {
            return deviceRepository.findByHostname(hostname);
        }

        return Optional.empty();
    }

    private String normalizeDeviceUuid(String deviceUuid) {
        if (deviceUuid == null) {
            return null;
        }

        String trimmed = deviceUuid.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    /**
     * Get all registered devices.
     */
    public List<DeviceResponse> getAllDevices() {
        return deviceRepository.findAll()
            .stream()
            .map(this::mapToDeviceResponse)
            .toList();
    }

    /**
     * Get a device by ID.
     */
    public DeviceResponse getDeviceById(Long id) {
        return deviceRepository.findById(id)
            .map(this::mapToDeviceResponse)
            .orElse(null);
    }

    /**
     * Get a device by hostname.
     */
    public DeviceResponse getDeviceByHostname(String hostname) {
        return deviceRepository.findByHostname(hostname)
            .map(this::mapToDeviceResponse)
            .orElse(null);
    }

    private DeviceResponse mapToDeviceResponse(Device device) {
        DeviceResponse response = new DeviceResponse(device, heartbeatTimeoutSeconds);
        deviceTelemetryRepository.findFirstByDevice_IdOrderByRecordedAtDesc(device.getId())
            .ifPresent(telemetry -> response.setLatestTelemetry(new DeviceResponse.DeviceTelemetrySummary(telemetry)));
        return response;
    }

    /**
     * Update a device heartbeat timestamp.
     */
    @Transactional
    public DeviceResponse heartbeatDevice(Long id) {
        return deviceRepository.findById(id)
            .map(device -> {
                LocalDateTime now = LocalDateTime.now();
                device.setLastHeartbeat(now);
                device.setUpdatedAt(now);
                return new DeviceResponse(deviceRepository.save(device), heartbeatTimeoutSeconds);
            })
            .orElse(null);
    }
}
