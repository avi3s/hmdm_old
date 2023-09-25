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

package com.hmdm.license.guice.module;

import com.google.inject.servlet.ServletModule;
import com.hmdm.license.rest.resource.LicenseResource;
import com.hmdm.rest.filter.ApiOriginFilter;

/**
 * <p>A module used for configuring and setting up the REST API resources to be used for handling the requests for
 * license validation and management.</p>
 *
 * @author isv
 */
public class LicenseRestModule extends ServletModule {

    /**
     * <p>Constructs new <code>LicenseRestModule</code> instance. This implementation does nothing.</p>
     */
    public LicenseRestModule() {
    }

    /**
     * <p>Configures and sets up the filters and resources to be used for processing incoming requests.</p>
     */
    protected void configureServlets() {
        this.filter("/rest/*").through(ApiOriginFilter.class);
        this.filter("/api/*").through(ApiOriginFilter.class);
        
        this.bind(LicenseResource.class);
    }

}
