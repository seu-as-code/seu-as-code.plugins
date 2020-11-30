/*
 *    Copyright (C) 2015 QAware GmbH
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

import org.gradle.api.tasks.Input

/**
 * Simple data type to represent the ASCII art banner configuration.
 *
 * @author lreimer
 */
class SeuacBanner {
    @Input
    String font = 'slant'
    @Input
    String reflection = 'no'
    @Input
    String adjustment = 'center'
    @Input
    String stretch = 'yes'
    @Input
    int width = 80

    /**
     * Convenience setter for the font to use.
     *
     * @param aFont a sdupported font
     */
    void font(String aFont) {
        this.font = aFont
    }

    /**
     * Convenience setter for the reflection to use.
     *
     * @param aReflection the reflection, either yes or no
     */
    void reflection(String aReflection) {
        this.reflection = aReflection
    }

    /**
     * Convenience setter for the adjustment to use.
     *
     * @param theAdjustment the adjustment, either left or center or right
     */
    void adjustment(String theAdjustment) {
        this.adjustment = theAdjustment
    }

    /**
     * Convenience setter for the stretch to use.
     *
     * @param theStretch the stretch, either yes or no
     */
    void stretch(String theStretch) {
        this.stretch = theStretch
    }

    /**
     * Convenience setter for the width to use.
     *
     * @param theWidth the width
     */
    void width(int theWidth) {
        this.width = theWidth
    }

    /**
     * Factory method to create the default banner settings.
     *
     * @return the default banner settings
     */
    static SeuacBanner defaultBanner() {
        new SeuacBanner()
    }
}
