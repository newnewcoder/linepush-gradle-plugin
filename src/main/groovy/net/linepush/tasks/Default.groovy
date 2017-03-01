package net.linepush.tasks

import com.linecorp.bot.client.LineMessagingServiceBuilder
import com.linecorp.bot.model.PushMessage
import com.linecorp.bot.model.message.Message
import com.linecorp.bot.model.message.TextMessage
import com.linecorp.bot.model.response.BotApiResponse
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import retrofit2.Response

/**
 * Linebot trait. Use implementation to send line message.
 *
 * @author newnewcoder
 * @version 1.0.0
 */
trait Linebot {
    def token = '' //set for linebot token
    def sendTo = '' //set for roomID/groupID/userID

    abstract Message makeMessage()

    def send() {
        def message = makeMessage()
        if (!token || !sendTo) {
            throw new IllegalArgumentException('token or roomID/groupID/userID must be setting.')
        }

        LineMessagingServiceBuilder
                .create(token)
                .build()
                .pushMessage(new PushMessage(sendTo, message))
                .execute()
    }
}

/**
 * The default linebot task. Use it to send default text message.
 *
 * @author newnewcoder
 * @version 1.0.0
 */
class Default extends DefaultTask implements Linebot {
    @TaskAction
    sendMessage() {
        println 'sending...'
        Response<BotApiResponse> response = send()
        println "${response.code()} ${response.message()}"
    }

    @Override
    Message makeMessage() {
        return new TextMessage('hello world.')
    }
}