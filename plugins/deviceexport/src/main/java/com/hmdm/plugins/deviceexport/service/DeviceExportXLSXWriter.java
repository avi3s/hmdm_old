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
import org.apache.ibatis.cursor.Cursor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>A writer for exported device data using format of XLSX files.</p>
 */
public class DeviceExportXLSXWriter implements DeviceExportWriter {

    /**
     * <p>Constructs new <code>DeviceExportXLSXWriter</code> instance. This implementation does nothing.</p>
     */
    public DeviceExportXLSXWriter() {
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
        writeXLSX(devices, output, locale, configurations, devicesApps);
    }

    /**
     * <p>Exports the specified devices into Excel workbook which is written to specified stream.</p>
     *
     * @param devices        a stream of devices to be exported.
     * @param output         a stream to write the generated content to.
     * @param locale         a locale to be used for localizing the generated content.
     * @param configurations the details for available configurations.
     * @param devicesApps    a mapping from device IDs to list of device applications.
     * @throws IOException if an I/O error occurs.
     */
    private void writeXLSX(Cursor<DeviceExportRecord> devices, OutputStream output, String locale,
                           Map<Integer, List<DeviceExportApplicationConfigurationView>> configurations,
                           Map<Integer, List<DeviceExportApplicationDeviceView>> devicesApps) throws IOException {

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Devices");

            // Create a header row describing what the columns mean
            CellStyle boldStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            boldStyle.setFont(font);
            boldStyle.setAlignment(HorizontalAlignment.CENTER);

            if (locale.contains("_")) {
                locale = locale.substring(0, locale.indexOf("_"));
            }

            ResourceBundle translations = ResourceBundle.getBundle(
                    "plugin_deviceexport_translations", new Locale(locale), new ResourceBundleUTF8Control()
            );
            
            Row headerRow = sheet.createRow(0);
            addStringCells(headerRow,
                    Arrays.asList(
                            translations.getString("device.export.header.devicenumber"),
                            translations.getString("device.export.header.imei"),
                            translations.getString("device.export.header.phone"),
                            translations.getString("device.export.header.description"),
                            translations.getString("device.export.header.configuration"),
                            translations.getString("device.export.header.launcher.version"),
                            translations.getString("device.export.header.permission.status"),
                            translations.getString("device.export.header.installation.status")
                    ),
                    boldStyle);

            CellStyle commonStyle = workbook.createCellStyle();
            commonStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            commonStyle.setWrapText(true);

            AtomicInteger rowNum = new AtomicInteger(0);
            devices.forEach(device -> {
                Row row = sheet.createRow(rowNum.incrementAndGet());
                device.setApplications(devicesApps.get(device.getId()));
                addCells(device, row, translations, configurations, commonStyle);
            });

            sheet.setColumnWidth(0, 6000);
            sheet.setColumnWidth(1, 6000);
            sheet.setColumnWidth(2, 6000);
            sheet.setColumnWidth(3, 6000);
            sheet.setColumnWidth(4, 6000);
            sheet.setColumnWidth(5, 6000);
            sheet.setColumnWidth(6, 16000);
            sheet.setColumnWidth(7, 16000);

            workbook.write(output);
        }
    }

    /**
     * <p>Writes the data for specified device into specified row in Excel workbook.</p>
     *
     * @param device         a device to be exported.
     * @param row            a row in Excel workbook to write the data to.
     * @param translations   a locale to be used for localizing the generated content.
     * @param configurations the details for available configurations.
     * @param commonStyle    a style for applying to all cells.
     */
    private void addCells(DeviceExportRecord device, Row row, ResourceBundle translations,
                          Map<Integer, List<DeviceExportApplicationConfigurationView>> configurations,
                          CellStyle commonStyle) {

        Cell numberCell = row.createCell(0, CellType.STRING);
        numberCell.setCellStyle(commonStyle);
        numberCell.setCellValue(device.getDeviceNumber());

        Cell imeiCell = row.createCell(1, CellType.STRING);
        imeiCell.setCellStyle(commonStyle);
        imeiCell.setCellValue(stripTrailingQuotes.apply(device.getImei()));

        Cell phoneNumberCell = row.createCell(2, CellType.STRING);
        phoneNumberCell.setCellStyle(commonStyle);
        phoneNumberCell.setCellValue(stripTrailingQuotes.apply(device.getPhone()));

        Cell descriptionCell = row.createCell(3, CellType.STRING);
        descriptionCell.setCellStyle(commonStyle);
        descriptionCell.setCellValue(stripTrailingQuotes.apply(device.getDescription()));

        Cell configNameCell = row.createCell(4, CellType.STRING);
        configNameCell.setCellStyle(commonStyle);
        configNameCell.setCellValue(device.getConfigurationName());

        Cell launcherVersionCell = row.createCell(5, CellType.STRING);
        launcherVersionCell.setCellStyle(commonStyle);
        launcherVersionCell.setCellValue(stripTrailingQuotes.apply(device.getLauncherVersion()));

        // Permissions status
        final List<String> permissionStatusKeys = encodePermissionsStatus.apply(device);
        StringBuilder b = new StringBuilder();
        for (String key : permissionStatusKeys) {
            if (b.length() > 0) {
                b.append("\n");
            }
            b.append(translations.getString(key));
        }

        Cell permissionsCell = row.createCell(6, CellType.STRING);
        permissionsCell.setCellStyle(commonStyle);
        permissionsCell.setCellValue(b.toString());

        // Installation status
        Cell installationsCell = row.createCell(7, CellType.STRING);
        installationsCell.setCellStyle(commonStyle);

        if (device.isInfoAvailable()) {
            final String status = evaluateDeviceAppInstallationStatus(device, translations, configurations);
            installationsCell.setCellValue(status);
        }
    }

    /**
     * <p>Writes the cells with specified content to specified row in Excel workbook.</p>
     *
     * @param row     a row in Excel workbook to write the data to.
     * @param strings the contents of the cells.
     * @param style   a style to be applied to cells.
     */
    private static void addStringCells(Row row, List<String> strings, CellStyle style) {
        for (int i = 0; i < strings.size(); i++) {
            Cell cell = row.createCell(i, CellType.STRING);
            cell.setCellValue(strings.get(i));
            cell.setCellStyle(style);
        }
    }
}
