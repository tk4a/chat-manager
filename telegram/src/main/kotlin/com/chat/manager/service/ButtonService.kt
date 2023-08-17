package com.chat.manager.service

import org.telegram.telegrambots.meta.api.methods.send.SendMessage

interface ButtonService {
    fun createHelpButton(chatId: Long): SendMessage
}