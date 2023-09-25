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

package com.hmdm.plugins.licensing.rest;

import javax.inject.Inject;
import javax.inject.Singleton;
import com.hmdm.persistence.UnsecureDAO;
import com.hmdm.persistence.domain.Device;
import com.hmdm.persistence.domain.Settings;
import com.hmdm.plugin.service.PluginStatusCache;
import com.hmdm.plugins.licensing.persistence.LicensingDAO;
import com.hmdm.plugins.licensing.persistence.domain.LicensingPluginSettings;
import com.hmdm.plugins.licensing.rest.json.LicenseInternalErrorResponse;
import com.hmdm.rest.json.Response;
import com.hmdm.security.SecurityContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static com.hmdm.plugins.licensing.LicensingPluginConfigurationImpl.PLUGIN_ID;

/**
 * <p>A resource to be used as proxy for license validation requests.</p>
 *
 * @author isv
 */
@Singleton
@Path("/plugins/licensing")
@Api(tags = {"License validation"})
public class LicensingResource {

    private static final Logger logger = LoggerFactory.getLogger(LicensingResource.class);

    private WebTarget webResource;
    private String restPath;
    private UnsecureDAO unsecuredAO;
    private LicensingDAO licensingDAO;
    private PluginStatusCache pluginStatusCache;

    /**
     * <p>Constructs new <code>LicensingResource</code> instance. This implementation does nothing.</p>
     */
    public LicensingResource() {
    }

    /**
     * <p>Constructs new <code>LicenseResource</code> instance. This implementation does nothing.</p>
     */
    @Inject
    public LicensingResource(UnsecureDAO settingsDAO,
                             LicensingDAO licensingDAO,
                             PluginStatusCache pluginStatusCache) {
        Client client = JerseyClientBuilder.newClient(  );
        this.webResource = client.target("http://app.h-mdm.com/license" );


//        this.webResource = Client.create().resource(licenseServerUrl);
        
//        this.webResource.property(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 2500);
//        this.webResource.setProperty(ClientConfig.PROPERTY_READ_TIMEOUT, 10000);

        this.restPath = "/rest/license";
        this.unsecuredAO = settingsDAO;
        this.licensingDAO = licensingDAO;
        this.pluginStatusCache = pluginStatusCache;
    }

    // =================================================================================================================
    @ApiOperation(
            value = "Validate license",
            notes = "Validates the given license against the License server",
            response = Response.class
    )
    @POST
    @Path("/license/validate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Object validateLicense(Map<String, Object> validationRequest) throws IOException {
        logger.debug("License validation request: {}", validationRequest);
        try {
            if (validationRequest.containsKey("query")) {
                final Object query = validationRequest.get("query");
                if (query instanceof Map) {
                    Map queryMap = (Map) query;
                    if (queryMap.containsKey("deviceId")) {
                        Object deviceId = queryMap.get("deviceId");
                        final Device dbDevice = this.unsecuredAO.getDeviceByNumber(String.valueOf(deviceId));
                        if (dbDevice != null) {
                            SecurityContext.init(dbDevice.getCustomerId());
                            try {
                                if (this.pluginStatusCache.isPluginDisabled(PLUGIN_ID)) {
                                    logger.error("Rejecting request from device {} due to disabled plugin", deviceId);
                                    return Response.PLUGIN_DISABLED();
                                }

                                final LicensingPluginSettings settings = this.licensingDAO.getPluginSettingsByCustomerId(dbDevice.getCustomerId());
                                if (settings != null) {
                                    validationRequest.put("key", settings.getApiKey());
                                }
                            } finally {
                                SecurityContext.release();
                            }
                        }
                    }
                }
            }

            ObjectMapper mapper = new ObjectMapper();
            logger.info("Re-transmitting license validation request to license server: {}", mapper.writeValueAsString(validationRequest));

            final javax.ws.rs.core.Response response = getJSONRequestBuilder("/validate").post(Entity.entity(validationRequest, MediaType.APPLICATION_JSON_TYPE));

            final Map map = response.readEntity(Map.class);

            logger.info("License server responded with: {}", mapper.writeValueAsString(map));

            return map;
        } catch (Exception e) {
            logger.error("Unexpected error when communicating to license manager server", e);
            return new LicenseInternalErrorResponse();
        }
    }

    /**
     * <p>Gets the builder for specified web resource.</p>
     *
     * @param addingPath an additional path to form the resource path.
     * @return a builder for web resource client.
     */
    private Invocation.Builder getJSONRequestBuilder(String addingPath) {
        WebTarget resource;
        if (addingPath != null) {
            resource = webResource.path(this.restPath + addingPath);
        } else {
            resource = webResource.path(this.restPath);
        }

        return resource.request(MediaType.APPLICATION_JSON);
    }

    /**
     * <p>Gets the plugin settings for customer account associated with current user. If there are none found in DB
     * then returns default ones.</p>
     *
     * @return plugin settings for current customer account.
     */
    @ApiOperation(
            value = "Get settings",
            notes = "Gets the plugin settings for current user. If there are none found in DB then returns default ones.",
            response = LicensingPluginSettings.class,
            authorizations = {@Authorization("Bearer Token")}
    )
    @GET
    @Path("/private/licensing-plugin-settings")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSettings() {
        return Response.OK(
                Optional.ofNullable(this.licensingDAO.getPluginSettings())
                        .orElse(new LicensingPluginSettings())
        );
    }

    // =================================================================================================================
    @ApiOperation(
            value = "Save settings",
            notes = "Save the Licensing plugin settings",
            response = Settings.class
    )
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/private/licensing-plugin-settings")
    public Response updateAPIKey(LicensingPluginSettings settings) {
        try {
            this.licensingDAO.savePluginSettings(settings);
            return Response.OK();
        } catch (Exception e) {
            logger.error("Unexpected error when saving Licensing plugin settings", e);
            return Response.INTERNAL_ERROR();
        }
    }

}
