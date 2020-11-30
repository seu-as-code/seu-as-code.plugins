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
package de.qaware.seu.as.code.plugins.git

import org.eclipse.jgit.api.errors.GitAPIException
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.options.Option
import org.gradle.api.tasks.OutputDirectory

/**
 * Base class for all concrete Git tasks.
 *
 * @author lreimer
 */
abstract class AbstractGitTask extends DefaultTask {

    @Option(option = "gitTimeout", description = "The Git command timeout.")
    protected int gitTimeout

    /**
     * Initialize task with group.
     */
    AbstractGitTask() {
        group = 'Version Control'
    }

    @OutputDirectory
    File directory

    String username
    String password

    protected def withExceptionHandling(String message, Closure c) {
        try {
            c()
        } catch (GitAPIException e) {
            throw new GradleException(message, e)
        } catch (IOException ioe) {
            throw new GradleException(message, ioe)
        }
        this
    }

    protected def always(Closure c) {
        c()
    }

    protected UsernamePasswordCredentialsProvider createCredentialsProvider() {
        new UsernamePasswordCredentialsProvider(username ?: '', password ?: '')
    }
}
