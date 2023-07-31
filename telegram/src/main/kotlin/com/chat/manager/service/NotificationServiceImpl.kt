package com.chat.manager.service

import NotificationDto
import com.chat.manager.client.NotificationClient
import com.chat.manager.entity.Notification
import com.chat.manager.mapper.toDto
import com.chat.manager.mapper.toEntity
import org.springframework.stereotype.Service

@Service
class NotificationServiceImpl(
    val sender: MessageSender,
    val client: NotificationClient
) : NotificationService {
    override fun createNotification(notification: NotificationDto) = client.create(notification)

    override fun save(notification: NotificationDto) {
        notifications.add(notification.toEntity())
    }

    override fun getAll(): List<NotificationDto> {
        return notifications.map { it.toDto() }
    }

    override fun sendNotification(chatId: String) {
        sender.sendMessage(chatId, "MOCK")
    }

    companion object DB {
        val notifications = ArrayList<Notification>()
    }
}