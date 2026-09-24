package com.nodara.platform.device.controller;

import com.nodara.platform.device.dto.DeviceRegistrationRequest;
import com.nodara.platform.device.dto.DeviceResponse;
import com.nodara.platform.device.dto.DeviceTelemetryRequest;
import com.nodara.platform.device.dto.DeviceTelemetryResponse;
import com.nodara.platform.device.service.DeviceService;
import com.nodara.platform.device.service.DeviceTelemetryService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/devices")
public class DeviceController {

    private final DeviceService deviceService;
    private final DeviceTelemetryService deviceTelemetryService;

    public DeviceController(DeviceService deviceService, DeviceTelemetryService deviceTelemetryService) {
        this.deviceService = deviceService;
        this.deviceTelemetryService = deviceTelemetryService;
    }

    /**
     * Register or update a device endpoint.
     * Idempotent: repeated calls with the same hostname update the device rather than creating duplicates.
     */
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeviceResponse registerDevice(@Valid @RequestBody DeviceRegistrationRequest request) {
        return deviceService.registerDevice(request);
    }

    /**
     * Get all registered devices.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DeviceResponse> listDevices() {
        return deviceService.getAllDevices();
    }

    /**
     * Get a device by ID.
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DeviceResponse getDevice(@PathVariable Long id) {
        DeviceResponse device = deviceService.getDeviceById(id);
        if (device == null) {
            throw new DeviceNotFoundException("Device with id " + id + " not found");
        }
        return device;
    }

    /**
     * Record a heartbeat for an existing device.
     */
    @PostMapping(value = "/{id}/heartbeat", produces = MediaType.APPLICATION_JSON_VALUE)
    public DeviceResponse heartbeatDevice(@PathVariable Long id) {
        DeviceResponse device = deviceService.heartbeatDevice(id);
        if (device == null) {
            throw new DeviceNotFoundException("Device with id " + id + " not found");
        }
        return device;
    }

    /**
     * Record telemetry for an existing device.
     */
    @PostMapping(value = "/{id}/telemetry", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeviceTelemetryResponse recordTelemetry(@PathVariable Long id, @Valid @RequestBody DeviceTelemetryRequest request) {
        DeviceTelemetryResponse telemetry = deviceTelemetryService.recordTelemetry(id, request);
        if (telemetry == null) {
            throw new DeviceNotFoundException("Device with id " + id + " not found");
        }
        return telemetry;
    }

    /**
     * Exception handler for device not found.
     */
    @ExceptionHandler(DeviceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleDeviceNotFound(DeviceNotFoundException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    /**
     * Exception handler for validation errors.
     */
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(org.springframework.web.bind.MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .findFirst()
            .orElse("Validation failed");
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message);
    }

    /**
     * Custom exception for device not found.
     */
    public static class DeviceNotFoundException extends RuntimeException {
        public DeviceNotFoundException(String message) {
            super(message);
        }
    }

    /**
     * Error response DTO.
     */
    public static class ErrorResponse {
        private int status;
        private String message;

        public ErrorResponse(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }
}
