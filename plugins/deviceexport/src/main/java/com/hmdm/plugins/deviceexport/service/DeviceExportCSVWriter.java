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

package com.hmdm.plugins.deviceexport.service;

import com.hmdm.plugins.deviceexport.persistence.domain.DeviceExportApplicationConfigurationView;
import com.hmdm.plugins.deviceexport.persistence.domain.DeviceExportApplicationDeviceView;
import com.hmdm.plugins.deviceexport.persistence.domain.DeviceExportRecord;
import com.hmdm.util.ResourceBundleUTF8Control;
import com.opencsv.CSVWriter;
import org.apache.ibatis.cursor.Cursor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * <p>A writer for exported device data using format of CSV files.</p>
 */
public class DeviceExportCSVWriter implements DeviceExportWriter {

    /**
     * <p>Constructs new <code>DeviceExportCSVWriter</code> instance. This implementation does nothing.</p>
     */
    public DeviceExportCSVWriter() {
        // Empty
    }

    /**
     * <p>Writes the data for the specified exported devices to specified output.</p>
     *
     * @param devices        a cursor over the list of the devices to be exported.
     * @param output         a stream to write the data for the exported devices to.
     * @param locale         a locale to be used for customizing the output.
     * @param configurations a mapping from configuration ID to list of applications set for the configuration.
     * @param devicesApps    a mapping from device ID to list of applications installed on device.
     * @throws IOException if an I/O error occurs while writting device data.
     */
    @Override
    public void exportDevices(Cursor<DeviceExportRecord> devices,
                              OutputStream output,
                              String locale,
                              Map<Integer, List<DeviceExportApplicationConfigurationView>> configurations,
                              Map<Integer, List<DeviceExportApplicationDeviceView>> devicesApps) throws IOException {

        if (locale.contains("_")) {
            locale = locale.substring(0, locale.indexOf('_'));
        }

        ResourceBundle translations = ResourceBundle.getBundle(
                "plugin_deviceexport_translations", new Locale(locale), new ResourceBundleUTF8Control()
        );

        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(output))) {
            // adding header to csv
            String[] headers = {
                    translations.getString("device.export.header.devicenumber"),
                    translations.getString("device.export.header.imei"),
                    translations.getString("device.export.header.phone"),
                    translations.getString("device.export.header.description"),
                    translations.getString("device.export.header.configuration"),
                    translations.getString("device.export.header.launcher.version"),
                    translations.getString("device.export.header.permission.status"),
                    translations.getString("device.export.header.installation.status")
            };
            writer.writeNext(headers);

            // Output devices data
            devices.forEach(device -> {
                device.setApplications(devicesApps.get(device.getId()));
                writeDevice(device, translations, configurations, writer);
            });
        }
    }

    /**
     * <p>Writes the data for specified device as row in CSV file.</p>
     *
     * @param device         a device to be exported.
     * @param translations   a locale to be used for localizing the generated content.
     * @param configurations the details for available configurations.
     * @param writer         a writer for CSV file.
     */
    private void writeDevice(DeviceExportRecord device,
                             ResourceBundle translations,
                             Map<Integer, List<DeviceExportApplicationConfigurationView>> configurations,
                             CSVWriter writer) {

        final List<String> permissionStatusKeys = encodePermissionsStatus.apply(device);
        StringBuilder permissions = new StringBuilder();
        for (String key : permissionStatusKeys) {
            if (permissions.length() > 0) {
                permissions.append("\n");
            }
            permissions.append(translations.getString(key));
        }

        String[] deviceData = {
                device.getDeviceNumber(),
                stripTrailingQuotes.apply(device.getImei()),
                stripTrailingQuotes.apply(device.getPhone()),
                stripTrailingQuotes.apply(device.getDescription()),
                device.getConfigurationName(),
                stripTrailingQuotes.apply(device.getLauncherVersion()),
                permissions.toString(),
                device.isInfoAvailable() ? evaluateDeviceAppInstallationStatus(device, translations, configurations) : ""
        };

        writer.writeNext(deviceData, true);
    }
}
