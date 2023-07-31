package com.chat.manager.client

import NotificationDto
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface NotificationClient {
    fun create(request: NotificationDto): Mono <String>
    fun getByChatId(chatId: String): Flux<NotificationDto>
}