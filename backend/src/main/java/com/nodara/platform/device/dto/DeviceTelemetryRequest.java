package com.nodara.platform.device.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class DeviceTelemetryRequest {

    @NotNull(message = "cpuUsage is required")
    @DecimalMin(value = "0.0", message = "cpuUsage must be at least 0")
    @DecimalMax(value = "100.0", message = "cpuUsage must be at most 100")
    private BigDecimal cpuUsage;

    @NotNull(message = "ramUsage is required")
    @DecimalMin(value = "0.0", message = "ramUsage must be at least 0")
    @DecimalMax(value = "100.0", message = "ramUsage must be at most 100")
    private BigDecimal ramUsage;

    @NotNull(message = "diskUsage is required")
    @DecimalMin(value = "0.0", message = "diskUsage must be at least 0")
    @DecimalMax(value = "100.0", message = "diskUsage must be at most 100")
    private BigDecimal diskUsage;

    @NotNull(message = "uptimeSeconds is required")
    @PositiveOrZero(message = "uptimeSeconds must be zero or greater")
    private Long uptimeSeconds;

    public DeviceTelemetryRequest() {
    }

    public DeviceTelemetryRequest(BigDecimal cpuUsage, BigDecimal ramUsage, BigDecimal diskUsage, Long uptimeSeconds) {
        this.cpuUsage = cpuUsage;
        this.ramUsage = ramUsage;
        this.diskUsage = diskUsage;
        this.uptimeSeconds = uptimeSeconds;
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
}
