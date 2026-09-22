package com.nodara.platform.device.service;

import com.nodara.platform.device.dto.DeviceRegistrationRequest;
import com.nodara.platform.device.dto.DeviceResponse;
import com.nodara.platform.device.entity.Device;
import com.nodara.platform.device.repository.DeviceRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    /**
     * Register or update a device by hostname (idempotent operation).
     * If a device with the given hostname already exists, update its lastHeartbeat and agentVersion.
     * Otherwise, create a new device record.
     */
    @Transactional
    public DeviceResponse registerDevice(DeviceRegistrationRequest request) {
        Device device = deviceRepository.findByHostname(request.getHostname())
            .orElseGet(() -> new Device(
                request.getHostname(),
                request.getOsName(),
                request.getOsVersion(),
                request.getAgentVersion()
            ));

        // Update existing device
        if (device.getId() != null) {
            device.setOsVersion(request.getOsVersion());
            device.setAgentVersion(request.getAgentVersion());
            device.setLastHeartbeat(LocalDateTime.now());
            device.setUpdatedAt(LocalDateTime.now());
        }

        Device savedDevice = deviceRepository.save(device);
        return new DeviceResponse(savedDevice);
    }

    /**
     * Get all registered devices.
     */
    public List<DeviceResponse> getAllDevices() {
        return deviceRepository.findAll()
            .stream()
            .map(DeviceResponse::new)
            .toList();
    }

    /**
     * Get a device by ID.
     */
    public DeviceResponse getDeviceById(Long id) {
        return deviceRepository.findById(id)
            .map(DeviceResponse::new)
            .orElse(null);
    }

    /**
     * Get a device by hostname.
     */
    public DeviceResponse getDeviceByHostname(String hostname) {
        return deviceRepository.findByHostname(hostname)
            .map(DeviceResponse::new)
            .orElse(null);
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
                return new DeviceResponse(deviceRepository.save(device));
            })
            .orElse(null);
    }
}
