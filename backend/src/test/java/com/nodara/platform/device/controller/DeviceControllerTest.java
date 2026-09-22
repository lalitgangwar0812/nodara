package com.nodara.platform.device.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nodara.platform.device.dto.DeviceRegistrationRequest;
import com.nodara.platform.device.dto.DeviceResponse;
import com.nodara.platform.device.service.DeviceService;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DeviceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DeviceService deviceService;

    private DeviceRegistrationRequest testRequest;
    private DeviceResponse testResponse;

    @BeforeEach
    void setUp() {
        testRequest = new DeviceRegistrationRequest(
            "test-hostname",
            "Windows",
            "10",
            "1.0.0"
        );

        testResponse = new DeviceResponse();
        testResponse.setId(1L);
        testResponse.setHostname("test-hostname");
        testResponse.setOsName("Windows");
        testResponse.setOsVersion("10");
        testResponse.setAgentVersion("1.0.0");
        testResponse.setRegisteredAt(LocalDateTime.now());
        testResponse.setLastHeartbeat(LocalDateTime.now());
    }

    @Test
    void shouldRegisterDeviceSuccessfully() throws Exception {
        // Arrange
        when(deviceService.registerDevice(any(DeviceRegistrationRequest.class))).thenReturn(testResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/devices/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.hostname").value("test-hostname"))
            .andExpect(jsonPath("$.osName").value("Windows"))
            .andExpect(jsonPath("$.osVersion").value("10"))
            .andExpect(jsonPath("$.agentVersion").value("1.0.0"));
    }

    @Test
    void shouldReturnBadRequestWhenHostnameIsEmpty() throws Exception {
        // Arrange
        DeviceRegistrationRequest invalidRequest = new DeviceRegistrationRequest(
            "",
            "Windows",
            "10",
            "1.0.0"
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/devices/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void shouldListAllDevices() throws Exception {
        // Arrange
        DeviceResponse device2 = new DeviceResponse();
        device2.setId(2L);
        device2.setHostname("another-hostname");
        device2.setOsName("Linux");
        device2.setOsVersion("5.10");
        device2.setAgentVersion("1.0.0");
        device2.setRegisteredAt(LocalDateTime.now());
        device2.setLastHeartbeat(LocalDateTime.now());

        when(deviceService.getAllDevices()).thenReturn(Arrays.asList(testResponse, device2));

        // Act & Assert
        mockMvc.perform(get("/api/v1/devices")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].hostname").value("test-hostname"))
            .andExpect(jsonPath("$[1].hostname").value("another-hostname"))
            .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldReturnEmptyListWhenNoDevices() throws Exception {
        // Arrange
        when(deviceService.getAllDevices()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/v1/devices")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void shouldHeartbeatDeviceSuccessfully() throws Exception {
        // Arrange
        testResponse.setLastHeartbeat(LocalDateTime.now());
        when(deviceService.heartbeatDevice(1L)).thenReturn(testResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/devices/1/heartbeat")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.hostname").value("test-hostname"));
    }

    @Test
    void shouldReturnNotFoundWhenHeartbeatDeviceDoesNotExist() throws Exception {
        // Arrange
        when(deviceService.heartbeatDevice(999L)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/api/v1/devices/999/heartbeat")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404));
    }
}
