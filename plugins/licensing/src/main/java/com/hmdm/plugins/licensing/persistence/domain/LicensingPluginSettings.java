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

package com.hmdm.plugins.licensing.persistence.domain;

import com.hmdm.persistence.domain.CustomerData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * <p>A domain object representing a single collection of plugin settings per customer account.</p>
 *
 * @author isv
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "A collection of 'Licensing' plugin settings")
public class LicensingPluginSettings implements CustomerData {

    @ApiModelProperty("An ID of the record")
    private Integer id;

    // An ID of a customer account which these settings correspond to
    @ApiModelProperty(hidden = true)
    private int customerId;

    @ApiModelProperty("The API-key to pass to license server")
    private String apiKey;

    /**
     * <p>Constructs new <code>LicensingSettings</code> instance. This implementation does nothing.</p>
     */
    public LicensingPluginSettings() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int getCustomerId() {
        return customerId;
    }

    @Override
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String toString() {
        return "LicensingSettings{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", apiKey='" + apiKey + '\'' +
                '}';
    }
}
