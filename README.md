# Linepush Gradle Plugin
 
## About
It's a gradle plugin using [line-bot message push api](https://devdocs.line.me/en/#push-message) to send [line](https://line.me/en/) message.

## Getting Start
First, you need to register a [line-bot](https://business.line.me/en/services/bot) account, then get the token and room ID.

### Setting
In `build.gradle`, apply **linepush** plugin, and setting your line-bot information in **line** block.
~~~groovy
buildscript {
    repositories {
        mavenCentral()
        maven {
            url mavenUrl
            credentials {
                username repoUser
                password repoPassword
            }
        }
    }
    dependencies {
        classpath 'com.github.newnewcoder:linepush:0.0.1-SNAPSHOT'
    }
}
apply plugin: 'linepush'

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
