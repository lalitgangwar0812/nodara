package com.nodara.platform.device.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "devices", indexes = {
    @Index(name = "idx_devices_hostname", columnList = "hostname"),
    @Index(name = "idx_devices_last_heartbeat", columnList = "last_heartbeat")
})
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 255)
    private String hostname;

    @Column(nullable = false, length = 255)
    private String osName;

    @Column(nullable = false, length = 255)
    private String osVersion;

    @Column(nullable = false, length = 50)
    private String agentVersion;

    @Column(nullable = false)
    private LocalDateTime registeredAt;

    @Column(nullable = false)
    private LocalDateTime lastHeartbeat;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public Device() {
    }

    public Device(String hostname, String osName, String osVersion, String agentVersion) {
        this.hostname = hostname;
        this.osName = osName;
        this.osVersion = osVersion;
        this.agentVersion = agentVersion;
        LocalDateTime now = LocalDateTime.now();
        this.registeredAt = now;
        this.lastHeartbeat = now;
        this.createdAt = now;
        this.updatedAt = now;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getAgentVersion() {
        return agentVersion;
    }

    public void setAgentVersion(String agentVersion) {
        this.agentVersion = agentVersion;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }

    public LocalDateTime getLastHeartbeat() {
        return lastHeartbeat;
    }

    public void setLastHeartbeat(LocalDateTime lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.lastHeartbeat = LocalDateTime.now();
    }
}
