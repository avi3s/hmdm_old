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

package com.hmdm.plugins.deviceimport.rest.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * <p>A DTO carrying the details on a single device with details parsed from Excel file.</p>
 *
 * @author isv
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceImportStatusItem implements Serializable {

    private static final long serialVersionUID = -3841321666137879567L;

    /**
     * <p>An optional ID of device with same device number already existing in DB.</p>
     */
    private Integer existingDeviceId;

    /**
     * <p>A mandatory device number extracted from the Excel file.</p>
     */
    private String deviceNumber;

    /**
     * <p>An optional device IMEI extracted from the Excel file.</p>
     */
    private String imei;

    /**
     * <p>An optional device phone number extracted from the Excel file.</p>
     */
    private String phoneNumber;

    /**
     * <p>An optional device description extracted from the Excel file.</p>
     */
    private String description;

    /**
     * <p>A number of occurrences of preceding records with same device number as this one in Excel file.</p>
     */
    private int count;

    /**
     * <p>Constructs new <code>DeviceImportStatusItem</code> instance. This implementation does nothing.</p>
     */
    public DeviceImportStatusItem() {
    }

    public Integer getExistingDeviceId() {
        return existingDeviceId;
    }

    public void setExistingDeviceId(Integer existingDeviceId) {
        this.existingDeviceId = existingDeviceId;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "DeviceImportStatusItem{" +
                "existingDeviceId=" + existingDeviceId +
                ", deviceNumber='" + deviceNumber + '\'' +
                ", imei='" + imei + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", description='" + description + '\'' +
                ", count=" + count +
                '}';
    }
}
