/*
 *
 *    Copyright 2015 QAware GmbH
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package de.qaware.seu.as.code.plugins.base

import static de.qaware.seu.as.code.plugins.base.SeuacBanner.defaultBanner
import static de.qaware.seu.as.code.plugins.base.SeuacDatastore.defaultDatastore
import static de.qaware.seu.as.code.plugins.base.SeuacLayout.defaultLayout

/**
 * This is the SEU as Code extension for our SeuacBasePlugin.
 *
 * @author alex.christ
 */
class SeuacExtension {
    String seuHome
    String projectName

    SeuacLayout layout
    SeuacDatastore datastore
    SeuacBanner banner

    /**
     * Short hand method to set the SEU home.
     *
     * @param aSeuHome the SEU home
     */
    void seuHome(String aSeuHome) {
        this.seuHome = aSeuHome
    }

    /**
     * Short hand method to set the project name.
     *
     * @param aProjectName the project name
     */
    void projectName(String aProjectName) {
        this.projectName = aProjectName
    }

    /**
     * Short hand method to set the project layout.
     *
     * @param aLayout the project layout
     */
    void layout(SeuacLayout aLayout) {
        this.layout = aLayout
    }

    /**
     * Get the layout for this extension. If not layout has been
     * set explicitly, we will return the default.
     *
     * @return the SeuacLayout
     */
    SeuacLayout getLayout() {
        layout ?: defaultLayout(seuHome)
    }

    /**
     * Short hand method to set the project layout as closure.
     *
     * @param closure the configuration closure
     */
    void layout(Closure closure) {
        layout = new SeuacLayout()
        closure.delegate = layout
        closure()
    }

    /**
     * Short hand method to set the datastore.
     *
     * @param aLayout the datastore
     */
    void datastore(SeuacDatastore aDatastore) {
        this.datastore = aDatastore
    }

    /**
     * Get the datastore for this extension. If no datastore has been
     * set explicitly, we will return the default.
     *
     * @return the SeuacLayout
     */
    SeuacDatastore getDatastore() {
        datastore ?: defaultDatastore()
    }

    /**
     * Short hand method to set the datastore as closure.
     *
     * @param closure the datastore configuration closure
     */
    void datastore(Closure closure) {
        datastore = new SeuacDatastore()
        closure.delegate = datastore
        closure()
    }

    /**
     * Convenience setter for the banner.
     *
     * @param theBanner the banner
     */
    void banner(SeuacBanner theBanner) {
        this.banner = theBanner
    }

    /**
     * Get the banner for this extension. If no banner has been
     * set explicitly, we will return the default.
     *
     * @return the SeuacBanner
     */
    SeuacBanner getBanner() {
        banner ?: defaultBanner()
    }

    /**
     * Short hand method to set the banner as closure.
     *
     * @param closure the banner configuration closure
     */
    void banner(Closure closure) {
        banner = defaultBanner()
        closure.delegate = banner
        closure()
    }
}
