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

// Localization completed
angular.module('plugin-licensing', ['ngResource', 'ui.bootstrap', 'ui.router', 'ngTagsInput', 'ncy-angular-breadcrumb'])
    .config(function ($stateProvider) {
        // TODO : #5937 : Localization : localize ncyBreadcrumb.label
        try {
            $stateProvider.state('plugin-settings-licensing', {
                url: "/" + 'plugin-settings-licensing',
                templateUrl: 'app/components/main/view/content.html',
                controller: 'TabController',
                ncyBreadcrumb: {
                    label: '{{"breadcrumb.plugin.licensing.main" | localize}}', //label to show in breadcrumbs
                },
                resolve: {
                    openTab: function () {
                        return 'plugin-settings-licensing'
                    }
                },
            });
        } catch (e) {
            console.log('An error when adding state ' + 'plugin-settings-licensing', e);
        }
    })
    .factory('pluginLicensingService', function ($resource) {
        return $resource('', {}, {
            getSettings: {url: 'rest/plugins/licensing/private/licensing-plugin-settings', method: 'GET'},
            saveSettings: {url: 'rest/plugins/licensing/private/licensing-plugin-settings', method: 'PUT'},
        });
    })
    .controller('PluginLicensingSettingsController', function ($scope, $rootScope, pluginLicensingService, localization) {
        $scope.successMessage = undefined;
        $scope.errorMessage = undefined;

        $rootScope.settingsTabActive = true;
        $rootScope.pluginsTabActive = false;

        $scope.settings = {};

        pluginLicensingService.getSettings(function (response) {
            if (response.status === 'OK') {
                $scope.settings = response.data;
            } else {
                $scope.errorMessage = localization.localize('error.internal.server');
            }
        });

        $scope.save = function () {
            $scope.successMessage = undefined;
            $scope.errorMessage = undefined;

            pluginLicensingService.saveSettings($scope.settings, function (response) {
                if (response.status === 'OK') {
                    $scope.successMessage = localization.localize('success.plugin.licensing.settings.saved');
                } else {
                    $scope.errorMessage = localization.localizeServerResponse(response);
                }
            });
        }
    })
    .run(function (localization) {
        localization.loadPluginResourceBundles("licensing");
    });


