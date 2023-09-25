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

package com.hmdm.license.persistence.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>A domain object representing a single record from license validation log.</p>
 *
 * @author isv
 */
public class LicenseValidationLog implements Serializable {

    private static final long serialVersionUID = -778945007723583595L;

    /**
     * <p>An ID of a log record.</p>
     */
    private Integer id;

    /**
     * <p>A timestamp of logging the request.</p>
     */
    private Date logged;

    /**
     * <p>A timestamp of receiving the request.</p>
     */
    private Date received;

    /**
     * <p>An API key identifying the customer.</p>
     */
    private String apiKey;

    /**
     * <p>A content of the request.</p>
     */
    private String request;

    /**
     * <p>An error code in case of failed validation.</p>
     */
    private String errorCode;

    /**
     * <p>An identifier of a device.</p>
     */
    private String deviceId;

    /**
     * <p>An IMEI of a device.</p>
     */
    private String imei;

    /**
     * <p>A serial number of a device.</p>
     */
    private String serialNumber;

    /**
     * <p>A number of unique devices which sent the license validation requests within current month.</p>
     */
    private int deviceCount;

    /**
     * <p>A severity level for log record.</p>
     */
    private String severity;

    /**
     * <p>Constructs new <code>LicenseValidationLog</code> instance. This implementation does nothing.</p>
     */
    public LicenseValidationLog() {
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(int deviceCount) {
        this.deviceCount = deviceCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getReceived() {
        return received;
    }

    public void setReceived(Date received) {
        this.received = received;
    }

    public Date getLogged() {
        return logged;
    }

    public void setLogged(Date logged) {
        this.logged = logged;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    @Override
    public String toString() {
        return "LicenseValidationLog{" +
                "id=" + id +
                ", logged=" + logged +
                ", received=" + received +
                ", apiKey='" + apiKey + '\'' +
                ", request='" + request + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", imei='" + imei + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", deviceCount=" + deviceCount +
                ", severity='" + severity + '\'' +
                '}';
    }
}
