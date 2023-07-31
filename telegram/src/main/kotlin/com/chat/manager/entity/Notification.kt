package com.chat.manager.entity

import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

data class Notification(
    val notificationId: String,
    @NotNull
    val chatId: String,
    @NotNull
    val text: String,
    @Nullable
    val link: String?
)
