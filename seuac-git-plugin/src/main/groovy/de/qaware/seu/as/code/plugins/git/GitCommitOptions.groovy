package de.qaware.seu.as.code.plugins.git

/**
 * Simple data class to model additional options for the GitCommitTask.
 *
 * @author lreimer
 */
class GitCommitOptions {
    String message = ''

    boolean all = true
    boolean noVerify
    boolean amend

    private GitUser author
    private GitUser committer

    /**
     * Set the author information.
     *
     * @param closure the configuration closure
     */
    void author(Closure closure) {
        this.author = new GitUser()
        closure.delegate = this.author
        closure()
    }

    /**
     * Return the author.
     *
     * @return the author, may be NULL if unset
     */
    GitUser getAuthor() {
        return this.author
    }

    /**
     * Set the committer information.
     *
     * @param closure the configuration closure
     */
    void committer(Closure closure) {
        this.committer = new GitUser()
        closure.delegate = this.committer
        closure()
    }

    /**
     * Return the committer.
     *
     * @return the committer, may be NULL
     */
    GitUser getCommitter() {
        return committer
    }
}
