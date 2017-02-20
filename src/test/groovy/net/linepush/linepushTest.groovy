package net.linepush

import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project

/**
 * Created by Pinky on 2/19/17.
 */
class linepushTest {
    @Test
    void greeterPluginAddsGreetingTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'com.github.newnewcoder.linepush'
    }
}


