package com.chat.manager.service

import com.chat.manager.configuration.property.TelegramProperties
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.DefaultAbsSender
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

@Service
class MessageSender(
    val telegramProperties: TelegramProperties
) : Sender, DefaultAbsSender(DefaultBotOptions()) {
    fun sendMessage(chatId: String, messageId: String) {
        execute(SendMessage(chatId, "MOCK"))
    }

    override fun getBotToken() = telegramProperties.token
}