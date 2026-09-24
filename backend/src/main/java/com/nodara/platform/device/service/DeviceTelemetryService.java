package com.nodara.platform.device.service;

import com.nodara.platform.device.dto.DeviceTelemetryRequest;
import com.nodara.platform.device.dto.DeviceTelemetryResponse;
import com.nodara.platform.device.entity.Device;
import com.nodara.platform.device.entity.DeviceTelemetry;
import com.nodara.platform.device.repository.DeviceRepository;
import com.nodara.platform.device.repository.DeviceTelemetryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeviceTelemetryService {

    private final DeviceRepository deviceRepository;
    private final DeviceTelemetryRepository deviceTelemetryRepository;

    public DeviceTelemetryService(DeviceRepository deviceRepository, DeviceTelemetryRepository deviceTelemetryRepository) {
        this.deviceRepository = deviceRepository;
        this.deviceTelemetryRepository = deviceTelemetryRepository;
    }

    @Transactional
    public DeviceTelemetryResponse recordTelemetry(Long deviceId, DeviceTelemetryRequest request) {
        return deviceRepository.findById(deviceId)
            .map(device -> {
                DeviceTelemetry telemetry = new DeviceTelemetry(
                    device,
                    request.getCpuUsage(),
                    request.getRamUsage(),
                    request.getDiskUsage(),
                    request.getUptimeSeconds()
                );
                return new DeviceTelemetryResponse(deviceTelemetryRepository.save(telemetry));
            })
            .orElse(null);
    }
}
