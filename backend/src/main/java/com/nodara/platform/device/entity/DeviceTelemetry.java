package com.nodara.platform.device.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "device_telemetry", indexes = {
    @Index(name = "idx_device_telemetry_device_id", columnList = "device_id"),
    @Index(name = "idx_device_telemetry_recorded_at", columnList = "recorded_at")
})
public class DeviceTelemetry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Column(name = "cpu_usage", nullable = false, precision = 5, scale = 2)
    private BigDecimal cpuUsage;

    @Column(name = "ram_usage", nullable = false, precision = 5, scale = 2)
    private BigDecimal ramUsage;

    @Column(name = "disk_usage", nullable = false, precision = 5, scale = 2)
    private BigDecimal diskUsage;

    @Column(name = "uptime_seconds", nullable = false)
    private Long uptimeSeconds;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public DeviceTelemetry() {
    }

    public DeviceTelemetry(Device device, BigDecimal cpuUsage, BigDecimal ramUsage, BigDecimal diskUsage, Long uptimeSeconds) {
        this.device = device;
        this.cpuUsage = cpuUsage;
        this.ramUsage = ramUsage;
        this.diskUsage = diskUsage;
        this.uptimeSeconds = uptimeSeconds;
        LocalDateTime now = LocalDateTime.now();
        this.recordedAt = now;
        this.createdAt = now;
    }

    @PrePersist
    protected void onCreate() {
        if (this.recordedAt == null) {
            this.recordedAt = LocalDateTime.now();
        }
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
