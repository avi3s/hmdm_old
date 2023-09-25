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

package com.hmdm.plugins.deviceexport.persistence.domain;

import java.io.Serializable;
import java.util.List;

/**
 * <p>An exported form of the device.</p>
 *
 * @author isv
 */
public class DeviceExportRecord implements Serializable {

    private static final long serialVersionUID = 4579976413532485644L;

    /**
     * <p>An ID of a device.</p>
     */
    private int id;

    /**
     * <p>An unique number identifying the device among others.</p>
     */
    private String deviceNumber;

    /**
     * <p>An IMEI of device.</p>
     */
    private String imei;

    /**
     * <p>A phone number of device.</p>
     */
    private String phone;

    /**
     * <p>Device description.</p>
     */
    private String description;

    /**
     * <p>An ID of a configuration set for device.</p>
     */
    private int configurationId;

    /**
     * <p>A name of configuration set for device.</p>
     */
    private String configurationName;

    /**
     * <p>A version of the launcher application set for device configuration.</p>
     */
    private String launcherVersion;

    /**
     * <p>A flag indicating if launcher application is granted an administrator permission on device.</p>
     */
    private Boolean adminPermission;

    /**
     * <p>A flag indicating if launcher application is granted permission to overlap other applications on device.</p>
     */
    private Boolean overlapPermission;

    /**
     * <p>A flag indicating if launcher application is granted permission to access usage history on device.</p>
     */
    private Boolean historyPermission;

    /**
     * <p>A list of applications set on application as reported by application.</p>
     */
    private List<DeviceExportApplicationDeviceView> applications;

    /**
     * <p>A flag indicating if device has synchronized data with server application at least once.</p>
     */
    private boolean infoAvailable;

    /**
     * <p>Constructs new <code>DeviceExportRecord</code> instance. This implementation does nothing.</p>
     */
    public DeviceExportRecord() {
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConfigurationName() {
        return configurationName;
    }

    public void setConfigurationName(String configurationName) {
        this.configurationName = configurationName;
    }

    public String getLauncherVersion() {
        return launcherVersion;
    }

    public void setLauncherVersion(String launcherVersion) {
        this.launcherVersion = launcherVersion;
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

    public List<DeviceExportApplicationDeviceView> getApplications() {
        return applications;
    }

    public void setApplications(List<DeviceExportApplicationDeviceView> applications) {
        this.applications = applications;
    }

    public int getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(int configurationId) {
        this.configurationId = configurationId;
    }

    public boolean isInfoAvailable() {
        return infoAvailable;
    }

    public void setInfoAvailable(boolean infoAvailable) {
        this.infoAvailable = infoAvailable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DeviceExportRecord{" +
                "deviceNumber='" + deviceNumber + '\'' +
                ", id='" + id + '\'' +
                ", imei='" + imei + '\'' +
                ", phone='" + phone + '\'' +
                ", configurationId='" + configurationId + '\'' +
                ", configurationName='" + configurationName + '\'' +
                ", launcherVersion='" + launcherVersion + '\'' +
                ", adminPermission=" + adminPermission +
                ", overlapPermission=" + overlapPermission +
                ", historyPermission=" + historyPermission +
                ", infoAvailable=" + infoAvailable +
                '}';
    }
}
