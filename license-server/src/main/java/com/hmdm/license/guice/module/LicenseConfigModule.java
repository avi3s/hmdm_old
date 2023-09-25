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

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import javax.servlet.ServletContext;

/**
 * <p>A module used for configuring the application services based on servlet context parameters.</p>
 *
 * @author isv
 */
public class LicenseConfigModule extends AbstractModule {

    /**
     * <p>The servlet context to apply the module to.</p>
     */
    private final ServletContext context;

    /**
     * <p>Constructs new <code>LicenseConfigModule</code> instance. This implementation does nothing.</p>
     */
    public LicenseConfigModule(ServletContext context) {
        this.context = context;
    }

    /**
     * <p>Configures the constants to be used by the injected services and components.</p>
     */
    protected void configure() {
        this.bindConstant().annotatedWith(Names.named("license.hash.secret")).to(
                this.context.getInitParameter("license.hash.secret")
        );
    }
}
