package net.linepush.tasks

import com.linecorp.bot.model.action.Action
import com.linecorp.bot.model.action.MessageAction
import com.linecorp.bot.model.action.PostbackAction
import com.linecorp.bot.model.action.URIAction
import com.linecorp.bot.model.message.*
import com.linecorp.bot.model.message.imagemap.*
import com.linecorp.bot.model.message.template.ButtonsTemplate
import com.linecorp.bot.model.message.template.CarouselColumn
import com.linecorp.bot.model.message.template.CarouselTemplate
import com.linecorp.bot.model.message.template.ConfirmTemplate
import org.gradle.api.Task

/**
 * Created by Pinky on 2/13/17.
 */
class Text extends Default {
    def text = ''

    @Override
    Message makeMessage() {
        if (!text) {
            throw new IllegalArgumentException('text must be set')
        }
        new TextMessage(text)
    }
}

class Image extends Default {
    def originalContentUrl
    def previewImageUrl

    @Override
    Message makeMessage() {
        if (!originalContentUrl || !previewImageUrl) {
            throw new IllegalArgumentException('originalContentUrl, previewImageUrl must be set')
        }
        new ImageMessage(originalContentUrl, previewImageUrl)
    }
}

class Video extends Default {
    def originalContentUrl
    def previewImageUrl

    @Override
    Message makeMessage() {
        if (!originalContentUrl || !previewImageUrl) {
            throw new IllegalArgumentException('originalContentUrl, previewImageUrl must be set')
        }
        new VideoMessage(originalContentUrl, previewImageUrl)
    }
}

class Audio extends Default {
    def originalContentUrl
    def duration

    @Override
    Message makeMessage() {
        if (!originalContentUrl || !duration) {
            throw new IllegalArgumentException('originalContentUrl, duration must be set')
        }
        new AudioMessage(originalContentUrl, duration)
    }
}

class Location extends Default {
    def title
    def address
    def latitude
    def longitude

    @Override
    Message makeMessage() {
        if (!title || !address || !latitude || !longitude) {
            throw new IllegalArgumentException('title, address, latitude, longitude must be set')
        }
        new LocationMessage(title, address, latitude, longitude)
    }
}

class Sticker extends Default {
    def packageId
    def stickerId

    @Override
    Message makeMessage() {
        if (!packageId || !stickerId) {
            throw new IllegalArgumentException('packageId, stickerId must be set')
        }
        new StickerMessage(packageId, stickerId)
    }
}

class Imagemap extends Default {
    def baseUrl
    def altText

    Closure baseSize
    Closure[] action

    @Override
    void setActions(final List<org.gradle.api.Action<? super Task>> closure) {//
        action = closure
    }

    class ImagemapBaseSizeFake {
        def height, width
    }

    class ImagemapAreaFake {
        def height, width, x, y
    }

    class ImagemapActionFake {
        def type, text, linkUri
        Closure area

        ImagemapArea getArea() {
            def imagemapAreaFake = new ImagemapAreaFake()
            area.resolveStrategy = Closure.DELEGATE_FIRST
            area.delegate = imagemapAreaFake
            area()
            new ImagemapArea(imagemapAreaFake.x, imagemapAreaFake.y, imagemapAreaFake.width, imagemapAreaFake.height)
        }
    }

    @Override
    Message makeMessage() {
        def imagemapBaseSizeFake = new ImagemapBaseSizeFake()
        def imagemapActions
        baseSize.resolveStrategy = Closure.DELEGATE_FIRST
        baseSize.delegate = imagemapBaseSizeFake
        baseSize()
        def imagemapBaseSize = new ImagemapBaseSize(imagemapBaseSizeFake.height, imagemapBaseSizeFake.width)

        imagemapActions = action.collect { actionClosure ->
            def imagemapAction, imagemapActionFake = new ImagemapActionFake()

            actionClosure.resolveStrategy = Closure.DELEGATE_FIRST
            actionClosure.delegate = imagemapActionFake
            actionClosure()

            if (imagemapActionFake.type == 'uri') {
                imagemapAction = new URIImagemapAction(imagemapActionFake.linkUri, imagemapActionFake.area)
            } else if (imagemapActionFake.type == 'message') {
                imagemapAction = new MessageImagemapAction(imagemapActionFake.text, imagemapActionFake.area)
            } else {
                throw new IllegalArgumentException('invalid type')
            }
            imagemapAction
        }

        if (!baseUrl || !altText || !imagemapBaseSize || !imagemapActions) {
            throw new IllegalArgumentException('baseUrl, altText, baseSize, action must be set')
        }
        new ImagemapMessage(baseUrl, altText, imagemapBaseSize, imagemapActions)
    }
}

class Template extends Default {
    def altText

    Closure template

    def makeAction(actionClosure) {
        Action action, actionFake = new Action() {
            String type, label, text, data, uri

            @Override
            String getLabel() {
                return label
            }
        }

        actionClosure.resolveStrategy = Closure.DELEGATE_FIRST
        actionClosure.delegate = actionFake
        actionClosure()

        if (actionFake.type == 'message') {
            action = new MessageAction(actionFake.label, actionFake.text)
        } else if (actionFake.type == 'postback') {
            action = new PostbackAction(actionFake.label, actionFake.data, actionFake.text)
        } else if (actionFake.type == 'uri') {
            action = new URIAction(actionFake.label, actionFake.uri)
        } else {
            throw new IllegalArgumentException('invalid action.type')
        }
        action
    }

    @Override
    Message makeMessage() {
        com.linecorp.bot.model.message.template.Template templateReal, templateFake = new com.linecorp.bot.model.message.template.Template() {
            def type
            def thumbnailImageUrl, title, text
            def columns //array of closure

            Closure[] action

            void setActions(Closure[] actions) {
                this.action = actions
            }

            Closure[] getActions() {
                this.action
            }
        }
        template.resolveStrategy = Closure.DELEGATE_FIRST
        template.delegate = templateFake
        template()

        if (templateFake.type == 'confirm') {
            def templateActions = templateFake.actions.collect { makeAction(it) }
            templateReal = new ConfirmTemplate(templateFake.text, templateActions)

        } else if (templateFake.type == 'buttons') {
            def templateActions = templateFake.actions.collect { makeAction(it) }
            templateReal = new ButtonsTemplate(templateFake.thumbnailImageUrl, templateFake.title, templateFake.text, templateActions)
        } else if (templateFake.type == 'carousel') {
            List<CarouselColumn> carouselColumns = templateFake.columns.collect { columnClosure ->
                CarouselColumn carouselColumnFake = new CarouselColumn(null, null, null, null)
                carouselColumnFake.metaClass.actions = []
                carouselColumnFake.metaClass.text = ''
                carouselColumnFake.metaClass.title = ''
                carouselColumnFake.metaClass.thumbnailImageUrl = ''

                columnClosure.resolveStrategy = Closure.DELEGATE_FIRST
                columnClosure.delegate = carouselColumnFake
                columnClosure()

                def templateActions = carouselColumnFake.actions.collect { makeAction(it) }

                new CarouselColumn(carouselColumnFake.thumbnailImageUrl, carouselColumnFake.title, carouselColumnFake.text, templateActions)
            }

            templateReal = new CarouselTemplate(carouselColumns)
        } else {
            throw new IllegalArgumentException('invalid template.type')
        }

        if (!altText || !templateReal) {
            throw new IllegalArgumentException('altText, template must be set')
        }

        new TemplateMessage(altText, templateReal)
    }
}
