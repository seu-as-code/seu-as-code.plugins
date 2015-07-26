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

import groovyx.net.http.HTTPBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

import static groovyx.net.http.ContentType.HTML

/**
 * This tasks creates an ASCII art banner and saves it to a file.
 *
 * @author mario-leander.reimer
 */
class CreateAsciiBannerTask extends DefaultTask {

    /**
     * The default filename fo the ASCII art banner file.
     */
    static final String DEFAULT_FILENAME = 'ascii-art.txt'

    @Input
    String projectName
    @OutputFile
    File bannerFile

    SeuacBanner settings

    /**
     * Initialize the group for this task.
     */
    CreateAsciiBannerTask() {
        group = 'SEU as Code'
        description = 'Create an ASCII art banner file from the project name.'
    }

    /**
     * Makes a HTTP request with all parameters to an online ascii art generator and
     * saves the response to the specified banner file.
     *
     * Exmaple: http://www.network-science.de/ascii/ascii.php?TEXT=QAware&FONT=slant&RICH=no&FORM=center&STRE=yes
     */
    @TaskAction
    void doCreateAsciiBanner() {
        // make the HTTP GET request with all the specified parameters
        def http = new HTTPBuilder('http://www.network-science.de')
        def ascii = http.get(path: '/ascii/ascii.php',
                contentType: HTML,
                query: [
                        TEXT: projectName,
                        FONT: settings.font,
                        RICH: settings.reflection,
                        FORM: settings.adjustment,
                        STRE: settings.stretch,
                        WIDT: settings.width
                ])

        // empty existing banner file
        if (bannerFile.exists()) {
            bannerFile.write('')
        }

        // here we strangely already have text
        def separator = System.getProperty("line.separator")
        ascii.toString().eachLine { String line, int nr ->
            if (nr == 1) {
                def indexOfName = line.lastIndexOf(projectName) + projectName.size()
                def firstLine = line.substring(indexOfName)
                bannerFile << firstLine + separator
            } else if (nr > 1 && !line.startsWith('Have fun.')) {
                bannerFile << line + separator
            }
        }

        // add final copyright line
        def now = Calendar.instance
        def copyright = "(c) ${now.get(Calendar.YEAR)} QAware GmbH"
        bannerFile << copyright.padLeft(settings.width - copyright.length() / 2)
    }
}
