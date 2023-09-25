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
import com.hmdm.license.persistence.domain.License;
import com.hmdm.license.persistence.domain.LicenseType;
import com.hmdm.license.persistence.domain.LicenseValidationLog;
import com.hmdm.license.persistence.mapper.LicenseMapper;
import com.hmdm.license.rest.json.LicenseValidationRequest;
import com.hmdm.license.rest.json.LicenseValidationResponse;
import com.hmdm.license.rest.json.LicenseValidationResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>A service providing the methods for license validation and management.</p>
 *
 * @author isv
 */
@Singleton
public class LicenseService {

    /**
     * <p>A logger used for debugging purposes.</p>
     */
    private static final Logger logger = LoggerFactory.getLogger(LicenseService.class);

    /**
     * <p>A service used for evaluating the hash values for license validation requests and responses.</p>
     */
    private final HashService hashService;

    /**
     * <p>A mapper to be used for maintaing the license data in DB.</p>
     */
    private final LicenseMapper licenseMapper;

    /**
     * <p>A standalone service to be used for inserting log records into database.</p>
     */
    private final ExecutorService loggingService = Executors.newFixedThreadPool(2);

    /**
     * <p>Constructs new <code>LicenseService</code> instance. This implementation does nothing.</p>
     */
    @Inject
    public LicenseService(HashService hashService, LicenseMapper licenseMapper) {
        this.hashService = hashService;
        this.licenseMapper = licenseMapper;

        Runtime.getRuntime().addShutdownHook(new Thread(this.loggingService::shutdown));
    }

    /**
     * <p>Formats the specified date to textual value.</p>
     *
     * @param date a date to be formatted.
     * @return a textual presentation of date.
     */
    public static String formatDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
        dateFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.of("UTC")));
        return dateFormat.format(date);
    }

    /**
     * <p>Parses the date from the specified text.</p>
     *
     * @param date a textual presentation of date.
     * @return a parsed date.
     * @throws ParseException if date is not properly formatted.
     */
    public Date parseDate(String date) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
        return dateFormat.parse(date);
    }

    /**
     * <p>Handles the specified request for license validation.</p>
     *
     * @param request a request for license validation.
     * @return a result of license validation.
     */
    public LicenseValidationResponse handleLicenseValidationRequest(@NotNull LicenseValidationRequest request) {

        LicenseValidationResult result;
        final Instant now = Instant.now();
        License license = null;

        final String requestHash = this.hashService.calculateHash(request.getQuery());
        if (!requestHash.equalsIgnoreCase(request.getHash())) {
            logger.debug("Hashes do not match. Expected: {}, actual: {}", requestHash, request.getHash());
            result = LicenseValidationResult.onFailure("error.invalid.hash");
        } else {
            license = this.licenseMapper.findLicenseByAPIkey(request.getKey());
            if (license == null) {
                logger.debug("No license found matching the API-key: {}", request.getKey());
                result = LicenseValidationResult.onFailure("error.invalid.apikey");
            } else if (license.getDomains() == null || license.getDomains().indexOf(request.getQuery().getDomain()) < 0) {
                logger.debug("Requested domain {} is not found among registered domains for license {}",
                        request.getQuery().getDomain(), license.getDomains());
                result = LicenseValidationResult.onFailure("error.invalid.domain");
            } else if ((license.getLicenseType() == LicenseType.PROFESSIONAL
                    || license.getLicenseType() == LicenseType.ENTERPRISE) && license.getExpires() <= now.toEpochMilli()) {
                logger.debug("The license has expired at: {}", formatDate(new Date(license.getExpires())));
                result = LicenseValidationResult.onFailure("error.license.expired");
            } else {
                logger.debug("License validation successful for request: {}", request);
                
                // Evaluate the expiration time. Maximum up to 1 month ahead
                final ZoneId utc = ZoneId.of("UTC");
                final LocalDateTime expires = LocalDateTime.ofInstant(Instant.ofEpochMilli(license.getExpires()), utc);
                final LocalDateTime oneMonthAhead = LocalDateTime.ofInstant(now, utc)
                        .plus(1, ChronoUnit.MONTHS);
                final LocalDateTime expiry;
                if (expires.isAfter(oneMonthAhead)) {
                    expiry = oneMonthAhead.
                            with(ChronoField.HOUR_OF_DAY, 23).
                            with(ChronoField.MINUTE_OF_HOUR, 59).
                            with(ChronoField.SECOND_OF_MINUTE, 59).
                            with(ChronoField.MILLI_OF_SECOND, 999)
                    ;
                } else {
                    expiry = expires;
                }

                result = LicenseValidationResult.onSuccess(
                        formatDate(new Date(expiry.toInstant(ZoneOffset.UTC).toEpochMilli()))
                );
            }
        }

        final String resultHash = this.hashService.calculateHash(result);

        final LicenseValidationResponse response = new LicenseValidationResponse();
        response.setQuery(result);
        response.setHash(resultHash);

        // Log the request
        this.loggingService.submit(new InsertLogRecordTask(request, response, now, license));

        return response;
    }

    /**
     * <p>Creates a response to be sent to client in case of internal server error encountered.</p>
     *
     * @return a response to be sent to client to notify on internal server error.
     */
    public LicenseValidationResponse onInternalServerError() {
        final LicenseValidationResult result = LicenseValidationResult.onFailure("error.internal");
        final String resultHash = this.hashService.calculateHash(result);

        final LicenseValidationResponse response = new LicenseValidationResponse();
        response.setQuery(result);
        response.setHash(resultHash);

        return response;
    }

    /**
     * <p>A standalone task for inserting a log record into license validation log.</p>
     */
    private class InsertLogRecordTask implements Runnable {

        /**
         * <p>A processed request for license validation to be logged.</p>
         */
        private final LicenseValidationRequest request;

        /**
         * <p>A result of processing request for license validation.</p>
         */
        private final LicenseValidationResponse response;

        /**
         * <p>A time of receiving the request.</p>
         */
        private final Instant receiveTime;

        /**
         * <p>A license mapped to request.</p>
         */
        private final License license;

        /**
         * <p>Constructs new <code>InsertLogRecordTask</code> instance.</p>
         */
        private InsertLogRecordTask(LicenseValidationRequest request,
                                    LicenseValidationResponse response,
                                    Instant receiveTime,
                                    License license) {
            this.request = request;
            this.response = response;
            this.receiveTime = receiveTime;
            this.license = license;
        }

        /**
         * <p>Inserts a record into licence validation log table in DB.</p>
         */
        @Override
        public void run() {
            try {
                StringWriter sw = new StringWriter();
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writeValue(sw, request);

                LicenseValidationLog logRecord = new LicenseValidationLog();
                logRecord.setApiKey(request.getKey());
                logRecord.setRequest(sw.toString());
                logRecord.setReceived(new Date(this.receiveTime.toEpochMilli()));

                if (response.getQuery().isResult()) {
                    logRecord.setDeviceId(request.getQuery().getDeviceId());
                    logRecord.setImei(request.getQuery().getImei());
                    logRecord.setSerialNumber(request.getQuery().getSerial());
                    logRecord.setSeverity("INFO");
                } else {
                    if (request.getQuery() != null) {
                        logRecord.setDeviceId(request.getQuery().getDeviceId());
                    }
                    logRecord.setErrorCode(response.getQuery().getError());
                    logRecord.setSeverity("ERROR");
                }

                licenseMapper.insertLogRecord(logRecord);
                licenseMapper.updateDeviceCount(logRecord.getId());
                licenseMapper.updateSeverity(logRecord.getId());
            } catch (Exception e) {
                logger.error("Failed to insert license log record. Request: {}, response: {}", request, response, e);
            }
        }
    }
}
