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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.hmdm.license.service.LicenseService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>A DTO carrying the details for license validation results.</p>
 *
 * @author isv
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"date", "result", "expiry", "error"})
public class LicenseValidationResult implements Serializable {

    private static final long serialVersionUID = -37842461728781173L;
    
    /**
     * <p>A timestamp of the license validation.</p>
     */
    private final String date;

    /**
     * <p>A timestamp for license expiration. This field is set in case of successful license validation only.</p>
     */
    private final String expiry;

    /**
     * <p>A flag indicating if license validation was successful or not.</p>
     */
    private final boolean result;

    /**
     * <p>An error code referencing the cause of license validation failure.</p>
     */
    private final String error;

    /**
     * <p>A factory method to create the instance in case of successful license validation.</p>
     *
     * @param expiry a timestamp for license expiration.
     * @return a successful license validation result.
     */
    public static LicenseValidationResult onSuccess(String expiry) {
        return new LicenseValidationResult(LicenseService.formatDate(new Date()), expiry, true, null);
    }

    /**
     * <p>A factory method to create the instance in case of successful license validation.</p>
     *
     * @param error a code referencing the cause of the validation failure.
     * @return a failed license validation result.
     */
    public static LicenseValidationResult onFailure(String error) {
        return new LicenseValidationResult(LicenseService.formatDate(new Date()), null, false, error);
    }

    /**
     * <p>Constructs new <code>LicenseValidationResult</code> instance. This implementation does nothing.</p>
     */
    private LicenseValidationResult(String date, String expiry, boolean result, String error) {
        this.date = date;
        this.expiry = expiry;
        this.result = result;
        this.error = error;
    }

    public String getDate() {
        return date;
    }

    public String getExpiry() {
        return expiry;
    }

    public boolean isResult() {
        return result;
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        return "LicenseValidationResult{" +
                "date='" + date + '\'' +
                ", expiry='" + expiry + '\'' +
                ", result=" + result +
                ", error='" + error + '\'' +
                '}';
    }
}
