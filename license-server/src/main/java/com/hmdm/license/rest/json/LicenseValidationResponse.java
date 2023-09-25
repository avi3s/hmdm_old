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
 * <p>A response on request for license validation.</p>
 *
 * @author isv
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LicenseValidationResponse implements Serializable {

    private static final long serialVersionUID = -4149898095365883994L;

    /**
     * <p>The details for license validation results.</p>
     */
    private LicenseValidationResult query;

    /**
     * <p>A SHA-1 hash string used for validating the integrity of the response.</p>
     */
    private String hash;

    /**
     * <p>Constructs new <code>LicensValidationResponse</code> instance. This implementation does nothing.</p>
     */
    public LicenseValidationResponse() {
    }

    public LicenseValidationResult getQuery() {
        return query;
    }

    public void setQuery(LicenseValidationResult query) {
        this.query = query;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public String toString() {
        return "LicenseValidationResponse{" +
                "query=" + query +
                ", hash='" + hash + '\'' +
                '}';
    }
}
