package com.nodara.platform.device.service;

import com.nodara.platform.device.dto.DeviceRegistrationRequest;
import com.nodara.platform.device.dto.DeviceResponse;
import com.nodara.platform.device.entity.Device;
import com.nodara.platform.device.repository.DeviceRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private DeviceService deviceService;

    private DeviceRegistrationRequest testRequest;

    @BeforeEach
    void setUp() {
        testRequest = new DeviceRegistrationRequest(
            "test-hostname",
            "Windows",
            "10",
            "1.0.0"
        );
    }

    @Test
    void shouldRegisterNewDevice() {
        // Arrange
        when(deviceRepository.findByHostname("test-hostname")).thenReturn(Optional.empty());
        Device savedDevice = new Device("test-hostname", "Windows", "10", "1.0.0");
        savedDevice.setId(1L);
        when(deviceRepository.save(any(Device.class))).thenReturn(savedDevice);

        // Act
        DeviceResponse response = deviceService.registerDevice(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals("test-hostname", response.getHostname());
        assertEquals("Windows", response.getOsName());
        assertEquals("10", response.getOsVersion());
        assertEquals("1.0.0", response.getAgentVersion());
        verify(deviceRepository, times(1)).save(any(Device.class));
    }

    @Test
    void shouldUpdateExistingDeviceByHostname() {
        // Arrange
        Device existingDevice = new Device("test-hostname", "Windows", "10", "1.0.0");
        existingDevice.setId(1L);
        when(deviceRepository.findByHostname("test-hostname")).thenReturn(Optional.of(existingDevice));
        when(deviceRepository.save(any(Device.class))).thenReturn(existingDevice);

        DeviceRegistrationRequest updateRequest = new DeviceRegistrationRequest(
            "test-hostname",
            "Windows",
            "11",
            "1.0.1"
        );

        // Act
        DeviceResponse response = deviceService.registerDevice(updateRequest);

        // Assert
        assertNotNull(response);
        assertEquals("test-hostname", response.getHostname());
        assertEquals("11", response.getOsVersion());
        assertEquals("1.0.1", response.getAgentVersion());
        verify(deviceRepository, times(1)).save(any(Device.class));
    }

    @Test
    void shouldNotCreateDuplicateDeviceForSameHostname() {
        // Arrange
        Device existingDevice = new Device("test-hostname", "Windows", "10", "1.0.0");
        existingDevice.setId(1L);
        when(deviceRepository.findByHostname("test-hostname")).thenReturn(Optional.of(existingDevice));
        when(deviceRepository.save(any(Device.class))).thenReturn(existingDevice);

        // Act
        DeviceResponse response1 = deviceService.registerDevice(testRequest);
        DeviceResponse response2 = deviceService.registerDevice(testRequest);

        // Assert
        assertEquals(response1.getId(), response2.getId());
        verify(deviceRepository, times(2)).save(any(Device.class));
        verify(deviceRepository, times(2)).findByHostname("test-hostname");
    }
}
