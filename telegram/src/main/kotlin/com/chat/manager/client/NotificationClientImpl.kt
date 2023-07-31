package com.chat.manager.client

import NotificationDto
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class NotificationClientImpl(
    @Qualifier("notificationClient")
    val client: WebClient
) : NotificationClient {
    override fun create(request: NotificationDto): Mono<String> =
        client.post()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body(Mono.just(request), NotificationDto::class.java)
            .retrieve()
            .bodyToMono(String::class.java)

    override fun getByChatId(chatId: String): Flux<NotificationDto> {
        TODO("Not yet implemented")
    }

}