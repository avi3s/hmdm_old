/*
 *
 * Headwind MDM: Open Source Android MDM Software
 * https://h-mdm.com
 *
 * Copyright (C) 2019 Headwind Solutions LLC (http://h-sms.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.hmdm.plugins.deviceinfo.rest.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.hmdm.rest.json.LookupItem;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * <p>A DTO carrying the detailed info for a single device.</p>
 *
 * @author isv
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceInfo implements Serializable {

    private static final long serialVersionUID = 1063145760140357201L;
    
    @ApiModelProperty("A device ID")
    private int id;

    @ApiModelProperty("An unique device number")
    private String deviceNumber;

    @ApiModelProperty("A device description")
    private String description;

    @ApiModelProperty("A required IMEI for device")
    private String imeiRequired;

    @ApiModelProperty("An actual IMEI for device")
    private String imeiActual;

    @ApiModelProperty("A required phone number for device")
    private String phoneNumberRequired;

    @ApiModelProperty("An actual phone number for device")
    private String phoneNumberActual;

    @ApiModelProperty("A device model")
    private String model;

    @ApiModelProperty("An OS version installed on device")
    private String osVersion;

    @ApiModelProperty("A battery level for device (in percents)")
    private Integer batteryLevel;

    @ApiModelProperty("A timestamp of most recent update of device info (in milliseconds since epoch time)")
    private Long latestUpdateTime;

    @ApiModelProperty("An interval passed from the most recent update of device info from current time")
    private Long latestUpdateInterval;

    @ApiModelProperty(value = "A type of interval passed from the most recent update of device info from current time",
            allowableValues = "min,hour,day")
    private String latestUpdateIntervalType;

    @ApiModelProperty("A list of groups assigned to device")
    private List<LookupItem> groups;

    @ApiModelProperty("A flag indicating if admin permission is set on device")
    private Boolean adminPermission;

    @ApiModelProperty("A flag indicating if overlap permission is set on device")
    private Boolean overlapPermission;

    @ApiModelProperty("A flag indicating if history permission is set on device")
    private Boolean historyPermission;

    @ApiModelProperty("The most recent view of dynamic data for device")
    private DeviceDynamicInfoRecord latestDynamicData;

    @ApiModelProperty("A list of applications which already are installed or required to be installed on device")
    private List<DeviceInfoApplication> applications;

    @ApiModelProperty(value = "A flag indicating if MDM mode is ON or not")
    private Boolean mdmMode;

    @ApiModelProperty(value = "Headwind MDM launcher build variant")
    private String launcherType;

    @ApiModelProperty(value = "Package of default launcher on the device")
    private String launcherPackage;

    @ApiModelProperty(value = "Is Headwind MDM a default launcher")
    private Boolean defaultLauncher;

    /**
     * <p>Constructs new <code>DeviceInfo</code> instance. This implementation does nothing.</p>
     */
    public DeviceInfo() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    @JsonSetter
    public void setLatestUpdateTime(Long latestUpdateTime) {
        this.latestUpdateTime = latestUpdateTime != null && latestUpdateTime == 0L ? null : latestUpdateTime;
        if (latestUpdateTime != null && latestUpdateTime != 0L) {
            long diff = (System.currentTimeMillis() - latestUpdateTime) / 1000 / 60;
            if (diff >= 24 * 60) {
                this.latestUpdateInterval = diff / 24 / 60;
                this.latestUpdateIntervalType = "day";
            } else if (diff >= 60) {
                this.latestUpdateInterval = diff / 60;
                this.latestUpdateIntervalType = "hour";
            } else {
                this.latestUpdateInterval = diff;
                this.latestUpdateIntervalType = "min";
            }
        } else {
            this.latestUpdateInterval = null;
            this.latestUpdateIntervalType = null;
        }
    }

    public int getId() {
        return id;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public Long getLatestUpdateTime() {
        return latestUpdateTime;
    }

    public Long getLatestUpdateInterval() {
        return latestUpdateInterval;
    }

    public String getLatestUpdateIntervalType() {
        return latestUpdateIntervalType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImeiRequired() {
        return imeiRequired;
    }

    public void setImeiRequired(String imeiRequired) {
        this.imeiRequired = imeiRequired;
    }

    public String getImeiActual() {
        return imeiActual;
    }

    public void setImeiActual(String imeiActual) {
        this.imeiActual = imeiActual;
    }

    public String getPhoneNumberRequired() {
        return phoneNumberRequired;
    }

    public void setPhoneNumberRequired(String phoneNumberRequired) {
        this.phoneNumberRequired = phoneNumberRequired;
    }

    public String getPhoneNumberActual() {
        return phoneNumberActual;
    }

    public void setPhoneNumberActual(String phoneNumberActual) {
        this.phoneNumberActual = phoneNumberActual;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public Integer getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(Integer batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public List<LookupItem> getGroups() {
        return groups;
    }

    public void setGroups(List<LookupItem> groups) {
        this.groups = groups;
    }

    public Boolean getAdminPermission() {
        return adminPermission;
    }

    public void setAdminPermission(Boolean adminPermission) {
        this.adminPermission = adminPermission;
    }

    public Boolean getOverlapPermission() {
        return overlapPermission;
    }

    public void setOverlapPermission(Boolean overlapPermission) {
        this.overlapPermission = overlapPermission;
    }

    public Boolean getHistoryPermission() {
        return historyPermission;
    }

    public void setHistoryPermission(Boolean historyPermission) {
        this.historyPermission = historyPermission;
    }

    public Boolean getMdmMode() {
        return mdmMode;
    }

    public void setMdmMode(Boolean mdmMode) {
        this.mdmMode = mdmMode;
    }

    public String getLauncherType() {
        return launcherType;
    }

    public void setLauncherType(String launcherType) {
        this.launcherType = launcherType;
    }

    public String getLauncherPackage() {
        return launcherPackage;
    }

    public void setLauncherPackage(String launcherPackage) {
        this.launcherPackage = launcherPackage;
    }

    public Boolean getDefaultLauncher() {
        return defaultLauncher;
    }

    public void setDefaultLauncher(Boolean defaultLauncher) {
        this.defaultLauncher = defaultLauncher;
    }

    public DeviceDynamicInfoRecord getLatestDynamicData() {
        return latestDynamicData;
    }

    public void setLatestDynamicData(DeviceDynamicInfoRecord latestDynamicData) {
        this.latestDynamicData = latestDynamicData;
    }

    public List<DeviceInfoApplication> getApplications() {
        return applications;
    }

    public void setApplications(List<DeviceInfoApplication> applications) {
        this.applications = applications;
    }
}
