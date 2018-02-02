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
package de.qaware.seu.as.code.plugins.svn

import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.tmatesoft.svn.core.SVNDepth
import org.tmatesoft.svn.core.SVNURL
import org.tmatesoft.svn.core.wc.SVNRevision

/**
 * The task implementation class to checkout a given SVN repository.
 *
 * @author lreimer
 */
class SvnCheckoutTask extends AbstractSvnTask {

    @Input
    String url

    @TaskAction
    def doCheckout() {
        withExceptionHandling {
            SVNURL repositoryURL = SVNURL.parseURIEncoded(url)
            def svnUpdateClient = createSvnUpdateClient(username, password)

            SVNRevision svnRevision = (revision == null ? SVNRevision.HEAD : SVNRevision.parse(revision))

            if (svnRevision == SVNRevision.UNDEFINED) {
                throw new GradleException("Invalid SVN revision: " + this.revision)
            }

            svnUpdateClient.doCheckout(repositoryURL, directory, SVNRevision.HEAD,
                    svnRevision, SVNDepth.INFINITY, false)
        }
    }
}
