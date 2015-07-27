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

import org.gradle.api.tasks.TaskAction
import org.tmatesoft.svn.core.SVNDepth
import org.tmatesoft.svn.core.wc.SVNRevision

/**
 * The task implementation class to update a given SVN repository.
 *
 * @author lreimer
 */
class SvnUpdateTask extends AbstractSvnTask {
    @TaskAction
    def doUpdate() {
        withExceptionHandling {
            def svnUpdateClient = createSvnUpdateClient(username, password)
            svnUpdateClient.doUpdate(directory, SVNRevision.HEAD, SVNDepth.INFINITY, false, false)
        }
    }
}
