package com.chat.manager.configuration.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("com.chat-manager.telegram")
class TelegramProperties(
    val token: String,
    val username: String
)
