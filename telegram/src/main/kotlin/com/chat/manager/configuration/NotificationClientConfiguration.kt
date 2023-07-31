package com.chat.manager.configuration

import com.chat.manager.configuration.property.NotificationClientProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class NotificationClientConfiguration(
    val properties: NotificationClientProperties
) {

    @Bean(name = ["notificationClient"])
    fun webClient(): WebClient =
        WebClient
            .builder()
            .baseUrl(properties.url + "/notification")
            .build()
}