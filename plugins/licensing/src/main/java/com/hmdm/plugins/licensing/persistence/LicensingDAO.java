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

package com.hmdm.plugins.licensing.persistence;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hmdm.persistence.AbstractDAO;
import com.hmdm.plugins.licensing.persistence.domain.LicensingPluginSettings;
import com.hmdm.plugins.licensing.persistence.mapper.LicensingMapper;

/**
 * <p>A DAO for {@link LicensingPluginSettings} domain objects.</p>
 *
 * @author isv
 */
@Singleton
public class LicensingDAO extends AbstractDAO<LicensingPluginSettings> {

    /**
     * <p>An ORM mapper for domain object type.</p>
     */
    private final LicensingMapper licensingMapper;

    /**
     * <p>Constructs new <code>LicensingDAO</code> instance. This implementation does nothing.</p>
     */
    @Inject
    public LicensingDAO(LicensingMapper licensingMapper) {
        this.licensingMapper = licensingMapper;
    }

    /**
     * <p>Gets the plugin settings for the customer account associated with the current user.</p>
     *
     * @return plugin settings for current customer account or <code>null</code> if there are no such settings found.
     */
    public LicensingPluginSettings getPluginSettings() {
        return getSingleRecord(this.licensingMapper::findPluginSettingsByCustomerId);
    }

    /**
     * <p>Saves the specified plugin settings applying the current security context.</p>
     *
     * @param settings plugin settings to be saved.
     */
    public void savePluginSettings(LicensingPluginSettings settings) {
        insertRecord(settings, this.licensingMapper::savePluginSettings);
    }

    /**
     * <p>Gets the plugin settings for the customer account referenced by the specified ID.</p>
     *
     * @return plugin settings for specified customer account or <code>null</code> if there are no such settings found.
     */
    public LicensingPluginSettings getPluginSettingsByCustomerId(int customerId) {
        return this.licensingMapper.findPluginSettingsByCustomerId(customerId);
    }
}
