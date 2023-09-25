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

package com.hmdm.license.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javax.inject.Named;
import com.hmdm.license.rest.json.LicenseValidationQuery;
import com.hmdm.license.rest.json.LicenseValidationResult;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>A service providing the methods for evaluating the hash-values for license management related requests.</p>
 *
 * @author isv
 */
@Singleton
public class HashService {

    /**
     * <p>A logger used for debugging purposes.</p>
     */
    private static final Logger logger = LoggerFactory.getLogger(HashService.class);

    /**
     * <p>A configurable hash-secret used for evaluating the hash-value.</p>
     */
    private final String hashSecret;

    /**
     * <p>Constructs new <code>HashService</code> instance. This implementation does nothing.</p>
     */
    @Inject
    public HashService(@Named("license.hash.secret") String hashSecret) {
        this.hashSecret = hashSecret;
    }

    /**
     * <p>Calculates the SHA-1 hash value for the specified license validation query.</p>
     *
     * @param query a license validation query to calculate hash value for.
     * @return an SHA-1 hash value for specified query.
     */
    public String calculateHash(LicenseValidationQuery query) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"date\":");
        json.append("\"");
        json.append(query.getDate());
        json.append("\"");
        json.append(",");
        json.append("\"domain\":");
        json.append("\"");
        json.append(query.getDomain());
        json.append("\"");
        json.append(",");
        json.append("\"deviceId\":");
        json.append("\"");
        json.append(query.getDeviceId());
        json.append("\"");
        if (query.getImei() != null) {
            json.append(",");
            json.append("\"imei\":");
            json.append("\"");
            json.append(query.getImei());
            json.append("\"");
        }
        if (query.getSerial() != null) {
            json.append(",");
            json.append("\"serial\":");
            json.append("\"");
            json.append(query.getSerial());
            json.append("\"");
        }
        json.append("}");

        final String data = (this.hashSecret + json).replaceAll("\\s", "");
        final String hash = DigestUtils.sha1Hex(data);

        logger.debug("Calculating SHA-1 hash for license validation query: {} => {}", data, hash);

        return hash;
    }

    /**
     * <p>Calculates the SHA-1 hash value for the specified license validation result.</p>
     *
     * @param result a license validation result to calculate hash value for.
     * @return an SHA-1 hash value for specified result.
     */
    public String calculateHash(LicenseValidationResult result) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"date\":");
        json.append("\"");
        json.append(result.getDate());
        json.append("\"");
        json.append(",");
        json.append("\"result\":");
        json.append(result.isResult());
        if (result.isResult()) {
            json.append(",");
            json.append("\"expiry\":");
            json.append("\"");
            json.append(result.getExpiry());
            json.append("\"");
        } else {
            json.append(",");
            json.append("\"error\":");
            json.append("\"");
            json.append(result.getError());
            json.append("\"");
        }
        json.append("}");

        final String data = (this.hashSecret + json).replaceAll("\\s", "");
        final String hash = DigestUtils.sha1Hex(data);

        logger.debug("Calculating SHA-1 hash for license validation result: {} => {}", data, hash);

        return hash;
    }
}
