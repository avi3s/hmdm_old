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
import java.util.List;

/**
 * <p>A domain object representing a single license.</p>
 *
 * @author isv
 */
public class License implements Serializable {

    private static final long serialVersionUID = 586023413406421503L;

    /**
     * <p>An unique ID of a license record.</p>
     */
    private Integer id;

    /**
     * <p>A type of the license.</p>
     */
    private LicenseType licenseType;

    /**
     * <p>An API key linking the license to customer.</p>
     */
    private String apiKey;

    /**
     * <p>The maximum allowed number of devices. <code>null</code> or zero-value means no limit.</p>
     */
    private Integer deviceLimit;

    /**
     * <p>A timestamp for license expiration expressed as milliseconds from Epoch time.</p>
     */
    private long expires;

    /**
     * <p>A list of domain names associated with the license.</p>
     */
    private List<String> domains;

    /**
     * <p>Constructs new <code>License</code> instance. This implementation does nothing.</p>
     */
    public License() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LicenseType getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(LicenseType licenseType) {
        this.licenseType = licenseType;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Integer getDeviceLimit() {
        return deviceLimit;
    }

    public void setDeviceLimit(Integer deviceLimit) {
        this.deviceLimit = deviceLimit;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

    public List<String> getDomains() {
        return domains;
    }

    public void setDomains(List<String> domains) {
        this.domains = domains;
    }

    @Override
    public String toString() {
        return "License{" +
                "id=" + id +
                ", licenseType=" + licenseType +
                ", apiKey='" + apiKey + '\'' +
                ", deviceLimit=" + deviceLimit +
                ", expires=" + expires +
                ", domains=" + domains +
                '}';
    }
}
