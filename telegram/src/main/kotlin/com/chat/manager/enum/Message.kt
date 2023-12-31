package com.chat.manager.enum

enum class Message(val text: String) {
    GREETING(
        """Привет, это chat-manager bot. Доступные функции:
        | /notify - создать уведомление
        | /help - помощь
    """.trimMargin()
    ),
    HELP("""Привет, это бот, который отправляет уведомления в чат
        | Доступные команды:
        | /start - запустить бота
        | /time - задать время (По умолчанию 10:59)
        | /msg - задать текст (По умолчанию Коллеги созвон)
        | /link - ссылка (По умолчанию ссылки нет)
        | /stop - остановить бота
    """.trimMargin()),
    START("""Привет, это чат бот, который отправляет уведомления в чат каждый рабочий день день.
        | По умолчанию время уведомления 10:59
        | Текст по умолчанию: Коллеги, созвон
        | Чтобы изменить время, воспользуйтесь командой /time HH:MM
        | Чтобы изменить сообщение /msg TEXT
        | Чтобы изменить ссылку /link LINK
        | Чтобы остановить бота /stop
    """.trimMargin()),
    START_ERROR_MSG("Чат уже добавлен в список для отправки уведомлений, если хотите остановить бота, воспользуйтесь командой /stop")
}