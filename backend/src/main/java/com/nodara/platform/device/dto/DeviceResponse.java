package com.nodara.platform.device.dto;

import com.nodara.platform.device.entity.Device;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

public class DeviceResponse {

    private Long id;
    private String hostname;
    private String osName;
    private String osVersion;
    private String agentVersion;
    
    @JsonFormat(shape = com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime registeredAt;
    
    @JsonFormat(shape = com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastHeartbeat;

    // Constructors
    public DeviceResponse() {
    }

    public DeviceResponse(Device device) {
        this.id = device.getId();
        this.hostname = device.getHostname();
        this.osName = device.getOsName();
        this.osVersion = device.getOsVersion();
        this.agentVersion = device.getAgentVersion();
        this.registeredAt = device.getRegisteredAt();
        this.lastHeartbeat = device.getLastHeartbeat();
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
}
