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

package com.hmdm.plugins.licensing.persistence.mapper;

import com.hmdm.plugins.licensing.persistence.domain.LicensingPluginSettings;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>An ORM mapper for {@link LicensingPluginSettings} domain object.</p>
 *
 * @author isv
 */
public interface LicensingMapper {

    @Select("SELECT settings.* " +
            "FROM plugin_licensing_settings settings " +
            "WHERE customerId = #{customerId}")
    LicensingPluginSettings findPluginSettingsByCustomerId(@Param("customerId") Integer customerId);

    @Insert({
            "INSERT INTO plugin_licensing_settings (apiKey, customerId) VALUES (#{apiKey}, #{customerId}) " +
                    "ON CONFLICT ON CONSTRAINT plugin_licensing_settings_customer_unique DO " +
                    "UPDATE SET " +
                    "apiKey = EXCLUDED.apiKey"
    })
    void savePluginSettings(LicensingPluginSettings settings);
}
