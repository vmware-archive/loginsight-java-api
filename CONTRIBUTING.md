

# Contributing to loginsight-java-api

Thank you for your interest in loginsight-java-api. We look forward for your support. You can contribute in any of the following ways
* Download and use it. "Star" the repository if you like the project
* Test and Report bugs if you find any
* Look at the pending issues https://github.com/vmware/loginsight-java-api/issues and contribute with code and documentation
* Please join us at [@gitter](https://gitter.im/vmware/loginsight-java-api) to discuss on any issues on using this API.
 
## Getting Started

* Download and install Java8 JDK
* Set the JAVA_HOME to Java 8 JDK directory
* Download and install Eclipse IDE (or choose an IDE you like)
* Fork the reposity and make changes. Once changes are done make a Pull request. Please follow the instructions in "Contribution Flow"


### Reporting Issues
Please use github issues https://github.com/vmware/loginsight-java-api/issues to raise new feature requests or bugs.


## Contribution Flow

This is a rough outline of what a contributor's workflow looks like:

- Create a topic branch from where you want to base your work
- Make commits of logical units
- Make sure your commit messages are in the proper format (see below)
- Push your changes to a topic branch in your fork of the repository
- Submit a pull request

Example:

``` shell
git checkout -b my-new-feature vmware/master
git commit -a
git push $USER my-new-feature
```

### Staying In Sync With Upstream

When your branch gets out of sync with the vmware/master branch, use the following to update:

``` shell
git checkout my-new-feature
git fetch -a
git rebase vmware/master
git push --force-with-lease $USER my-new-feature
```

### Updating pull requests

If your PR fails to pass CI or needs changes based on code review, you'll most likely want to squash these changes into
existing commits.

If your pull request contains a single commit or your changes are related to the most recent commit, you can simply
amend the commit.

``` shell
git add .
git commit --amend
git push --force-with-lease $USER my-new-feature
```

If you need to squash changes into an earlier commit, you can use:

``` shell
git add .
git commit --fixup <commit>
git rebase -i --autosquash vmware/master
git push --force-with-lease $USER my-new-feature
```

Be sure to add a comment to the PR indicating your new changes are ready to review, as GitHub does not generate a
notification when you git push.


### Formatting Commit Messages

We follow the conventions on [How to Write a Git Commit Message](http://chris.beams.io/posts/git-commit/).

Be sure to include any related GitHub issue references in the commit message.  See
[GFM syntax](https://guides.github.com/features/mastering-markdown/#GitHub-flavored-markdown) for referencing issues
and commits.

### Closing issues with Commit Messages

When a commit message contains the following keywords along with the issue number it will automatically close the issues.

* close
* closes
* closed
* fix
* fixes
* fixed
* resolve
* resolves
* resolved

For example, "This closes #34, closes #23" would close issues #34 and #23. For more information refer to [Closing Issues Via Commit Messages](https://help.github.com/articles/closing-issues-via-commit-messages/)


## Reporting Bugs and Creating Issues

When opening a new issue, try to roughly follow the commit message format conventions above.
