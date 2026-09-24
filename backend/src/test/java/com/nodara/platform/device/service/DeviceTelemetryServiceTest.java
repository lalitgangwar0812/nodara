package com.nodara.platform.device.service;

import com.nodara.platform.device.dto.DeviceTelemetryRequest;
import com.nodara.platform.device.dto.DeviceTelemetryResponse;
import com.nodara.platform.device.entity.Device;
import com.nodara.platform.device.entity.DeviceTelemetry;
import com.nodara.platform.device.repository.DeviceRepository;
import com.nodara.platform.device.repository.DeviceTelemetryRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceTelemetryServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private DeviceTelemetryRepository deviceTelemetryRepository;

    @InjectMocks
    private DeviceTelemetryService deviceTelemetryService;

    @Test
    void shouldRecordTelemetryForExistingDevice() {
        Device device = new Device("test-hostname", "Linux", "6.1", "1.0.0");
        device.setId(1L);
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));
        when(deviceTelemetryRepository.save(any(DeviceTelemetry.class))).thenAnswer(invocation -> {
            DeviceTelemetry telemetry = invocation.getArgument(0);
            telemetry.setId(10L);
            telemetry.setRecordedAt(java.time.LocalDateTime.now());
            return telemetry;
        });

        DeviceTelemetryRequest request = new DeviceTelemetryRequest(
            new BigDecimal("42.75"),
            new BigDecimal("59.25"),
            new BigDecimal("81.5"),
            3600L
        );

        DeviceTelemetryResponse response = deviceTelemetryService.recordTelemetry(1L, request);

        assertNotNull(response);
        assertEquals(1L, response.getDeviceId());
        assertEquals(new BigDecimal("42.75"), response.getCpuUsage());
        assertEquals(new BigDecimal("59.25"), response.getRamUsage());
        assertEquals(new BigDecimal("81.5"), response.getDiskUsage());
        assertEquals(3600L, response.getUptimeSeconds());
        verify(deviceTelemetryRepository, times(1)).save(any(DeviceTelemetry.class));
    }

    @Test
    void shouldReturnNullWhenRecordingTelemetryForUnknownDevice() {
        when(deviceRepository.findById(999L)).thenReturn(Optional.empty());

        DeviceTelemetryRequest request = new DeviceTelemetryRequest(
            new BigDecimal("12.5"),
            new BigDecimal("20.0"),
            new BigDecimal("30.33"),
            120L
        );

        DeviceTelemetryResponse response = deviceTelemetryService.recordTelemetry(999L, request);

        assertNull(response);
        verify(deviceTelemetryRepository, never()).save(any(DeviceTelemetry.class));
    }
}
