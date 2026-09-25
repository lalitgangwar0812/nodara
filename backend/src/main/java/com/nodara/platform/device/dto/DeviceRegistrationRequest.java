package com.nodara.platform.device.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DeviceRegistrationRequest {

    @Size(max = 36, message = "deviceUuid must not exceed 36 characters")
    private String deviceUuid;

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
        this(null, hostname, osName, osVersion, agentVersion);
    }

    public DeviceRegistrationRequest(String deviceUuid, String hostname, String osName, String osVersion, String agentVersion) {
        this.deviceUuid = deviceUuid;
        this.hostname = hostname;
        this.osName = osName;
        this.osVersion = osVersion;
        this.agentVersion = agentVersion;
    }

    // Getters and setters
    public String getDeviceUuid() {
        return deviceUuid;
    }

    public void setDeviceUuid(String deviceUuid) {
        this.deviceUuid = deviceUuid;
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
}
