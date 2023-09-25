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

package com.hmdm.license.rest.resource;

import javax.inject.Inject;
import javax.inject.Singleton;
import com.hmdm.license.rest.json.LicenseValidationRequest;
import com.hmdm.license.rest.json.LicenseValidationResponse;
import com.hmdm.license.rest.json.LicenseValidationResult;
import com.hmdm.license.service.LicenseService;
import com.hmdm.rest.json.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

/**
 * <p>An API for license validation and management.</p>
 *
 * @author isv
 */
@Singleton
@Path("/license")
public class LicenseResource {

    /**
     * <p>A logger to be used for logging the events.</p>
     */
    private static final Logger logger  = LoggerFactory.getLogger(LicenseResource.class);

    /**
     * <p>A service to be used for license validation.</p>
     */
    private final LicenseService licenseService;

    /**
     * <p>Constructs new <code>LicenseResource</code> instance. This implementation does nothing.</p>
     */
    @Inject
    public LicenseResource(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    /**
     * <p>Validates the specified license.</p>
     *
     * @param validationRequest a request with details for license validation.
     * @return a response to be returned to client.
     */
    @POST
    @Path("/validate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public LicenseValidationResponse handleLicenseValidationQuery(LicenseValidationRequest validationRequest) {
        final String requestId = UUID.randomUUID().toString();
        logger.info("Processing license validation request #{} : {}", requestId, validationRequest);
        try {
            LicenseValidationResponse response = this.licenseService.handleLicenseValidationRequest(validationRequest);
            logger.info("Response to license validation request #{} : {}", requestId, response);
            return response;
        } catch (Exception e) {
            logger.error("Unexpected error when handling the license validation request: {}", validationRequest, e);
            return this.licenseService.onInternalServerError();
        }
    }
}
