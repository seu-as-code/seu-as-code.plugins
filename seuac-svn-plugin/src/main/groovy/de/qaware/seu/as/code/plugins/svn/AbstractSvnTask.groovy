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
package de.qaware.seu.as.code.plugins.svn

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.tmatesoft.svn.core.SVNException
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager
import org.tmatesoft.svn.core.wc.ISVNOptions
import org.tmatesoft.svn.core.wc.SVNClientManager
import org.tmatesoft.svn.core.wc.SVNUpdateClient
import org.tmatesoft.svn.core.wc.SVNWCUtil

/**
 * Base class for all concrete SVN tasks.
 *
 * @author lreimer
 */
abstract class AbstractSvnTask extends DefaultTask {

    /**
     * Initialize task with group.
     */
    AbstractSvnTask() {
        group = 'Version Control'
    }

    @OutputDirectory
    File directory

    @Input
    String password

    @Input
    String username

    /**
     * Factory method to create a new SVN update client instance. If username and password
     * are provided they will be used for authentication.
     *
     * @param username the SVN username, default is empty
     * @param password the SVN password, default is empty
     * @return a new SVNUpdateClient instance
     */
    protected def SVNUpdateClient createSvnUpdateClient(def username = '', def password = '') {
        ISVNOptions options = SVNWCUtil.createDefaultOptions(true)
        ISVNAuthenticationManager authManager = null
        if (username) {
            authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password)
        }

        SVNClientManager clientManager = SVNClientManager.newInstance(options, authManager)
        SVNUpdateClient updateClient = clientManager.updateClient;
        updateClient.ignoreExternals = false

        updateClient
    }

    protected def withExceptionHandling(Closure c) {
        try {
            c()
        } catch (SVNException e) {
            throw new GradleException(e.message, e)
        }
    }
}
