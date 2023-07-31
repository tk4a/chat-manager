package com.chat.manager.service

import NotificationDto
import reactor.core.publisher.Mono

interface NotificationService {
    fun createNotification(notification: NotificationDto): Mono<String>
    fun save(notification: NotificationDto)
    fun getAll(): List<NotificationDto>
    fun sendNotification(chatId: String)
}