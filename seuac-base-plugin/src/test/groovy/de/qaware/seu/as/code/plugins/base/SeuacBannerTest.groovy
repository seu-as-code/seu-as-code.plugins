/*
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
        when:
        banner.font = 'Arial'
        then:
        expect banner.font, equalTo('Arial')
        when:
        banner.font 'Times'
        then:
        expect banner.font, equalTo('Times')
    }

    def "Setting Reflection with setter and convenience method"() {
        when:
        banner.reflection = 'yes'
        then:
        expect banner.reflection, equalTo('yes')
        when:
        banner.reflection 'no'
        then:
        expect banner.reflection, equalTo('no')
    }

    def "Setting Adjustment with setter and convenience method"() {
        when:
        banner.adjustment = 'left'
        then:
        expect banner.adjustment, equalTo('left')
        when:
        banner.adjustment 'right'
        then:
        expect banner.adjustment, equalTo('right')
    }

    def "Setting Stretch with setter and convenience method"() {
        when:
        banner.stretch = 'no'
        then:
        expect banner.stretch, equalTo('no')
        when:
        banner.stretch 'yes'
        then:
        expect banner.stretch, equalTo('yes')
    }

    def "Setting Width with setter and convenience method"() {
        when:
        banner.width = 123
        then:
        expect banner.width, equalTo(123)
        when:
        banner.width 235
        then:
        expect banner.width, equalTo(235)
    }

    def "Check Default banner creation and settings"() {
        given:
        banner = SeuacBanner.defaultBanner()

        expect:
        that banner, notNullValue()
        that banner.width, equalTo(80)
        that banner.font, equalTo('slant')
        that banner.stretch, equalTo('yes')
        that banner.adjustment, equalTo('center')
        that banner.reflection, equalTo('no')
    }
}
