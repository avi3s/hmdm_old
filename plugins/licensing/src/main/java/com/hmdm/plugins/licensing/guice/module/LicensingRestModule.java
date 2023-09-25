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

package com.hmdm.plugins.licensing.guice.module;

import com.google.inject.servlet.ServletModule;
import com.hmdm.plugin.rest.PluginAccessFilter;
import com.hmdm.plugins.licensing.rest.LicensingResource;
import com.hmdm.rest.filter.AuthFilter;
import com.hmdm.security.jwt.JWTFilter;

/**
 * <p>A <code>Guice</code> module for <code>Licensing Plugin</code> REST resources.</p>
 *
 * @author isv
 */
public class LicensingRestModule extends ServletModule {

    /**
     * <p>Constructs new <code>LicensingRestModule</code> instance. This implementation does nothing.</p>
     */
    public LicensingRestModule() {
    }

    /**
     * <p>Configures the <code>Licensing Plugin</code> REST resources.</p>
     */
    protected void configureServlets() {
        this.filter("/rest/plugins/licensing/private/*").through(JWTFilter.class);
        this.filter("/rest/plugins/licensing/private/*").through(AuthFilter.class);
        this.filter("/rest/plugins/licensing/private/*").through(PluginAccessFilter.class);
        this.bind(LicensingResource.class);
    }
}
