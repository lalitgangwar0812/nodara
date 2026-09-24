package com.nodara.platform.device.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nodara.platform.device.entity.DeviceTelemetry;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DeviceTelemetryResponse {

    private Long id;
    private Long deviceId;
    private BigDecimal cpuUsage;
    private BigDecimal ramUsage;
    private BigDecimal diskUsage;
    private Long uptimeSeconds;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime recordedAt;

    public DeviceTelemetryResponse() {
    }

    public DeviceTelemetryResponse(DeviceTelemetry telemetry) {
        this.id = telemetry.getId();
        this.deviceId = telemetry.getDevice() != null ? telemetry.getDevice().getId() : null;
        this.cpuUsage = telemetry.getCpuUsage();
        this.ramUsage = telemetry.getRamUsage();
        this.diskUsage = telemetry.getDiskUsage();
        this.uptimeSeconds = telemetry.getUptimeSeconds();
        this.recordedAt = telemetry.getRecordedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public BigDecimal getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(BigDecimal cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public BigDecimal getRamUsage() {
        return ramUsage;
    }

    public void setRamUsage(BigDecimal ramUsage) {
        this.ramUsage = ramUsage;
    }

    public BigDecimal getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(BigDecimal diskUsage) {
        this.diskUsage = diskUsage;
    }

    public Long getUptimeSeconds() {
        return uptimeSeconds;
    }

    public void setUptimeSeconds(Long uptimeSeconds) {
        this.uptimeSeconds = uptimeSeconds;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }
}
