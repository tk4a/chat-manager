package com.chat.manager.mapper

import NotificationDto
import com.chat.manager.entity.Notification
import org.telegram.telegrambots.meta.api.objects.Message
import java.util.UUID

fun Notification.toDto() =
    NotificationDto(
        this.chatId,
        this.text,
        this.link
    )

fun NotificationDto.toEntity() =
    Notification(
        UUID.randomUUID().toString(),
        this.chatId,
        this.text,
        this.link
    )

fun Message.toDto() =
    NotificationDto(
        this.chatId.toString(),
        this.text,
        this.text.substringAfterLast(" ")
    )