package com.chat.manager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan
@EnableEurekaClient
@EnableScheduling
class ChatManagerTelegramApp

fun main() {
    runApplication<ChatManagerTelegramApp>()
}
