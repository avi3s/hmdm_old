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

package com.hmdm.license.persistence.mapper;

import com.hmdm.license.persistence.domain.License;
import com.hmdm.license.persistence.domain.LicenseValidationLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

/**
 * <p>$</p>
 *
 * @author isv
 */
public interface LicenseMapper {

    License findLicenseByAPIkey(@Param("apiKey") String apiKey);

    @Insert("INSERT INTO licenseValidationLog (" +
            "    received, " +
            "    severity, " +
            "    apiKey, " +
            "    request, " +
            "    errorCode, " +
            "    deviceId, " +
            "    imei, " +
            "    serialNumber " +
            ") VALUES (" +
            "    #{received}, " +
            "    #{severity}, " +
            "    #{apiKey}, " +
            "    #{request}, " +
            "    #{errorCode}, " +
            "    #{deviceId}, " +
            "    #{imei}, " +
            "    #{serialNumber} " +
            ")")
    @SelectKey( statement = "SELECT currval('licenseValidationLog_id_seq')", keyColumn = "id", keyProperty = "id", before = false, resultType = int.class )
    void insertLogRecord(LicenseValidationLog logRecord);

    @Update("UPDATE licenseValidationLog SET deviceCount = (" +
            "     SELECT COUNT(DISTINCT deviceId) " +
            "     FROM licenseValidationLog log2 " +
            "     WHERE log2.apiKey = licenseValidationLog.apiKey " +
            "     AND EXTRACT(MONTH FROM log2.received) = EXTRACT(MONTH FROM licenseValidationLog.received) " +
            "     AND EXTRACT(YEAR FROM log2.received) = EXTRACT(YEAR FROM licenseValidationLog.received) " +
            ") WHERE id = #{id}")
    void updateDeviceCount(@Param("id") int logRecordId);

    @Update("UPDATE licenseValidationLog " +
            "SET severity = 'WARNING' " +
            "WHERE id = #{id} " +
            "AND (SELECT deviceLimit " +
            "     FROM licenses " +
            "     WHERE licenses.apiKey = licenseValidationLog.apiKey) < licenseValidationLog.deviceCount")
    void updateSeverity(@Param("id") int logRecordId);
}
