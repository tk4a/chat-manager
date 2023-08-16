package com.chat.manager.service

import com.chat.manager.enum.Message
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

@Service
class ButtonServiceImpl : ButtonService {

    override fun createHelpButton(chatId: Long) = SendMessage(chatId.toString(), Message.HELP.text)
}