# Linepush Gradle Plugin

![build-status](https://travis-ci.org/newnewcoder/linepush.svg?branch=master)

## About
It's a gradle plugin using [line-bot message push api](https://devdocs.line.me/en/#push-message) to send [line](https://line.me/en/) message.

## Getting Start
First, you need to register a [line-bot](https://business.line.me/en/services/bot) account, then get the token and room ID.

### Setting
In `build.gradle`, apply **linepush** plugin, and setting your line-bot information in **line** block.
~~~groovy
buildscript {
    repositories {
        maven {
            url "https://plugins.gradle.org/m2/"
        }
    }
    dependencies {
        classpath "gradle.plugin.com.github.newnewcoder:linepush:1.0.0"
    }
}
apply plugin: 'com.github.newnewcoder.linepush'

line {
    token = "<your linebot token>"
    sendTo = "<the roomID/userID/groupID you want to send>"
    text = "<the message you want to send>"
}
~~~

### How to Use
Start a bash, and run the command below
~~~sh
./gradlew send
~~~

other example is [here](https://github.com/newnewcoder/linepush/blob/master/sample/build.gradle)
