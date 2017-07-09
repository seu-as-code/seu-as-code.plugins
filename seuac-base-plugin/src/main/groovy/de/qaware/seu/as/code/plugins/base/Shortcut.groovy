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

import org.codehaus.groovy.scriptom.ActiveXObject
import org.codehaus.groovy.scriptom.Scriptom

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * The abstraction to create shortcuts for different platforms.
 */
abstract class Shortcut {

    protected final String seuHome
    protected final SeuacLayout seuacLayout

    /**
     * Initialize with SEU home and layout instance.
     *
     * @param seuHome the SEU home
     * @param seuacLayout the SEU layout
     */
    protected Shortcut(String seuHome, SeuacLayout seuacLayout) {
        this.seuHome = seuHome
        this.seuacLayout = seuacLayout
    }

    /**
     * Used to create a shortcut with give name and executable.
     *
     * @param name the name
     * @param executable the executable
     */
    abstract void create(String name, String executable)

    /**
     * Factory method for platform specific Shortcut instances.
     *
     * @param seuHome the SEU home
     * @param layout the SEU layout instance
     * @return a Shortcut instance, never NULL
     */
    static Shortcut "for"(String seuHome, SeuacLayout layout) {
        if (Platform.isWindows()) {
            return new WindowsShortcut(seuHome, layout)
        } else if (Platform.isMacOs() || Platform.isUnix()) {
            return new SymlinkShortcut(seuHome, layout)
        } else {
            return new NoShortcut(seuHome, layout)
        }
    }

    /**
     * Implementation to create a Windows specific shortcut using scriptom.
     */
    static class WindowsShortcut extends Shortcut {
        WindowsShortcut(String seuHome, SeuacLayout layout) {
            super(seuHome, layout)
        }

        @Override
        void create(String name, String executable) {
            Scriptom.inApartment {
                def wshShell = new ActiveXObject("WScript.Shell")
                def shortcut = wshShell.CreateShortcut("${seuHome}\\${name}.lnk")
                shortcut.TargetPath = "${seuacLayout.software}\\${executable}.bat"
                shortcut.WorkingDirectory = "${seuacLayout.software}"
                shortcut.Save()
            }
        }
    }

    /**
     * Implementation using Symlinks for MacOS and Linux.
     */
    static class SymlinkShortcut extends Shortcut {
        SymlinkShortcut(String seuHome, SeuacLayout layout) {
            super(seuHome, layout)
        }

        @Override
        void create(String name, String executable) {
            Path link = Paths.get("${seuHome}/${name}")
            Path target = Paths.get("${seuacLayout.software}/${executable}")
            try {
                Files.createSymbolicLink(link, target)
            } catch (ignored) {
                // never mind, this should not break build
            }
        }
    }

    /**
     * Noop implementation.
     */
    static class NoShortcut extends Shortcut {
        NoShortcut(String seuHome, SeuacLayout layout) {
            super(seuHome, layout)
        }

        @Override
        void create(String name, String executable) {
            // nothing to do
        }
    }
}
