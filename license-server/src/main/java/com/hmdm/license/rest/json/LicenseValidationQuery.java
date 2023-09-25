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

package com.hmdm.license.rest.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * <p>A DTO carrying the details for license validation query.</p>
 *
 * @author isv
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LicenseValidationQuery implements Serializable {

    private static final long serialVersionUID = 2456220073211042753L;

    /**
     * <p>A domain which the request originated from.</p>
     */
    private String domain;

    /**
     * <p>An identifying number for device.</p>
     */
    private String deviceId;

    /**
     * <p>An optional IMEI number for device.</p>
     */
    private String imei;

    /**
     * <p>An optional serial number for device.</p>
     */
    private String serial;

    /**
     * <p>A timestamp of the license validation request.</p>
     */
    private String date;

    /**
     * <p>Constructs new <code>LicenseValidationQuery</code> instance. This implementation does nothing.</p>
     */
    public LicenseValidationQuery() {
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "LicenseValidationQuery{" +
                "domain='" + domain + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", imei='" + imei + '\'' +
                ", serial='" + serial + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

}
