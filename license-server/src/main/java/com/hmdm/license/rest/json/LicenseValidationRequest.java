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
 * <p>A DTO carrying the details for an incoming request for license validation.</p>
 *
 * @author isv
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LicenseValidationRequest implements Serializable {

    private static final long serialVersionUID = -7145624988649423843L;

    /**
     * <p>The parameters for license validation.</p>
     */
    private LicenseValidationQuery query;

    /**
     * <p>A SHA-1 hash string used for validating the integrity of the incoming request.</p>
     */
    private String hash;

    /**
     * <p>An API key identifying the customer.</p>
     */
    private String key;

    /**
     * <p>Constructs new <code>LicenseValidationRequest</code> instance. This implementation does nothing.</p>
     */
    public LicenseValidationRequest() {
    }

    public LicenseValidationQuery getQuery() {
        return query;
    }

    public void setQuery(LicenseValidationQuery query) {
        this.query = query;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "LicenseValidationRequest{" +
                "query=" + query +
                ", hash='" + hash + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
