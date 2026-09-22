package com.nodara.platform.device.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DeviceRegistrationRequest {

    @NotBlank(message = "hostname is required")
    @Size(max = 255, message = "hostname must not exceed 255 characters")
    private String hostname;

    @NotBlank(message = "osName is required")
    @Size(max = 255, message = "osName must not exceed 255 characters")
    private String osName;

    @NotBlank(message = "osVersion is required")
    @Size(max = 255, message = "osVersion must not exceed 255 characters")
    private String osVersion;

    @NotBlank(message = "agentVersion is required")
    @Size(max = 50, message = "agentVersion must not exceed 50 characters")
    private String agentVersion;

    // Constructors
    public DeviceRegistrationRequest() {
    }

    public DeviceRegistrationRequest(String hostname, String osName, String osVersion, String agentVersion) {
        this.hostname = hostname;
        this.osName = osName;
        this.osVersion = osVersion;
        this.agentVersion = agentVersion;
    }

    // Getters and setters
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
}
