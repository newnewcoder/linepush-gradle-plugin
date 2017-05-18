# Linepush Gradle Plugin

![travis-ci](https://travis-ci.org/newnewcoder/linepush-gradle-plugin.svg?branch=master)
[![license](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![download](https://api.bintray.com/packages/newnewcoder/generic/linepush-gradle-plugin/images/download.svg) ](https://bintray.com/newnewcoder/generic/linepush-gradle-plugin/_latestVersion)

## About
It's a gradle plugin using [line-bot message push api](https://devdocs.line.me/en/#push-message) to send [line](https://line.me/en/) message.

## Getting Start
First, you need to register a [line-bot](https://business.line.me/en/services/bot) account, then get the token and room ID.

### Setting
In `build.gradle`, apply **linepush** plugin, and setting your line-bot information in **line** block.
~~~groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath "com.github.newnewcoder:linepush:1.0.0"
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
