package net.linepush

import net.linepush.tasks.Default
import net.linepush.tasks.Text
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Gradle plugin for sending line message.
 *
 * @author newnewcoder
 * @version 1.0.0
 */
class LinepushPlugin implements Plugin<Project> {
    static final String EXT_NAME = 'line'

    @Override
    void apply(Project project) {

        project.extensions.create(EXT_NAME, LinepushPluginExtension)

        project.task('send', type: Text) {
            description = 'send message with linebot'
        }

        project.afterEvaluate {
            def extension = project.extensions.findByName(EXT_NAME)
            project.tasks.withType(Default).all { task ->
                task.token = extension.token
                task.sendTo = extension.sendTo
            }
            project.tasks.withType(Text).all { task ->
                if(!task.text){
                    task.text = extension.text
                }
            }
        }
    }
}

/**
 * Plugin extension for setting linebot message api.
 *
 * @author newnewcoder
 * @version 1.0.0
 */
class LinepushPluginExtension {
    String token = 'linebot token'
    String sendTo = 'line room id that you want to send message'
    String text = 'this is simple text message.'
}
