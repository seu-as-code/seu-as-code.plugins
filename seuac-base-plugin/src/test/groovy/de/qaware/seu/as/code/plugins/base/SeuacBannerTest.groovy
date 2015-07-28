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

import spock.lang.Specification

import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.notNullValue
import static spock.util.matcher.HamcrestSupport.expect
import static spock.util.matcher.HamcrestSupport.that

/**
 * Simple unit test for the SeuacBanner.
 *
 * @author lreimer
 */
class SeuacBannerTest extends Specification {
    SeuacBanner banner

    void setup() {
        banner = new SeuacBanner()
    }

    def "Setting Font with setter and convenience mathod"() {
        when: "we set the font to Arial"
        banner.font = 'Arial'
        then: "the font is Arial"
        expect banner.font, equalTo('Arial')

        when: "we set the font to Times"
        banner.font 'Times'
        then: "the font is Times"
        expect banner.font, equalTo('Times')
    }

    def "Setting Reflection with setter and convenience method"() {
        when: "we set the reflection to yes"
        banner.reflection = 'yes'
        then: "the reflection is yes"
        expect banner.reflection, equalTo('yes')

        when: "we set the reflection to no"
        banner.reflection 'no'
        then: "the reflections is no"
        expect banner.reflection, equalTo('no')
    }

    def "Setting Adjustment with setter and convenience method"() {
        when: "we set the adjustment to left"
        banner.adjustment = 'left'
        then: "the adjustment is left"
        expect banner.adjustment, equalTo('left')

        when: "we set the adjustment to right"
        banner.adjustment 'right'
        then: "the adjustment is right"
        expect banner.adjustment, equalTo('right')
    }

    def "Setting Stretch with setter and convenience method"() {
        when: "we set the stretch to no"
        banner.stretch = 'no'
        then: "the stretch is no"
        expect banner.stretch, equalTo('no')

        when: "we set the stretch to yes"
        banner.stretch 'yes'
        then: "the stretch is yes"
        expect banner.stretch, equalTo('yes')
    }

    def "Setting Width with setter and convenience method"() {
        when: "we set the width to 123"
        banner.width = 123
        then: "the width is 123"
        expect banner.width, equalTo(123)

        when: "we set the width to 235"
        banner.width 235
        then: "the width is 235"
        expect banner.width, equalTo(235)
    }

    def "Check Default banner creation and settings"() {
        given: "a default banner instance"
        banner = SeuacBanner.defaultBanner()

        expect: "the default values to be set"
        that banner, notNullValue()
        that banner.width, equalTo(80)
        that banner.font, equalTo('slant')
        that banner.stretch, equalTo('yes')
        that banner.adjustment, equalTo('center')
        that banner.reflection, equalTo('no')
    }
}
