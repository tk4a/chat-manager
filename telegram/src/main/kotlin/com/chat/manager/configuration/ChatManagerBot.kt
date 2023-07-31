package com.chat.manager.configuration

import com.chat.manager.configuration.property.TelegramProperties
import com.chat.manager.service.ButtonService
import com.chat.manager.service.NotificationService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import java.io.File
import java.io.PrintWriter
import java.lang.StringBuilder
import java.nio.file.Files
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.logging.Logger
import kotlin.io.path.Path

@Component
class ChatManagerBot(
    val property: TelegramProperties,
    val buttonService: ButtonService,
    val notificationService: NotificationService
) : TelegramLongPollingBot() {

    val logger: Logger = Logger.getLogger(ChatManagerBot::class.java.name)

    override fun getBotToken() = property.token
    override fun getBotUsername() = property.username

    override fun onUpdateReceived(update: Update?) {
        update?.message?.let { it ->
            it.text.let { text ->
                logger.info("receive message from = '${it.from.userName}', firstName = '${it.from.firstName}', chatId = ${it.chatId}")
                if (text.contains("/check") && it.from.userName == MY_USERNAME) {
                    execute(
                        SendMessage(
                            TEST_CHAT_ID,
                            "alive"
                        )
                    )
                }
                if (text.contains("/clear") && it.from.userName == MY_USERNAME) {
                    val writer = PrintWriter(File("telegram.txt"))
                    writer.write("")
                    writer.close()
                    execute(
                        SendMessage(
                            TEST_CHAT_ID,
                            "successfully cleared"
                        )
                    )
                }

                if (text.contains("/send") && it.from.userName == MY_USERNAME) {
                    val params = text.split("-")
                    chats.forEach { id ->
                        execute(
                            SendMessage().apply {
                                this.chatId = id
                                this.text = params[1]
                                this.replyMarkup = InlineKeyboardMarkup(
                                    mutableListOf(mutableListOf(InlineKeyboardButton(params[2]).apply {
                                        url = params[3]
                                    }))
                                )
                            }
                        )
                    }
                }

                if (text.contains("/time")) {
                    val params = text.split(" ")[1].split(":")
                    chatIdToTime[it.chatId.toString()] = LocalDateTime.of(2023, 1, 1, params[0].toInt(), params[1].toInt())
                    execute(
                        SendMessage(
                            it.chatId.toString(),
                            "successfully set new time ${chatIdToTime[it.chatId.toString()]}"
                        )
                    )
                }

                if (text.contains("/log") && it.from.userName == MY_USERNAME) {
                    val log = StringBuilder()
                    val path = Path("telegram.txt")
                    val lines = Files.readAllLines(path)
                    lines.reverse()
                    val size = if (lines.size > 10) 10 else lines.size - 1
                    (0..size).onEach { i ->
                        log.append(lines[i])
                        log.append(System.lineSeparator())
                    }
                    execute(
                        SendMessage(
                            TEST_CHAT_ID,
                            log.toString()
                        )
                    )
                }

                if (text.contains("/info")) {
                    execute(
                        SendMessage(
                            it.chatId.toString(),
                            INFO_MSG
                        )
                    )
                }

                if (text.contains("/msg")) {
                    val msg: String = text.split("/msg")[1]
                    chatIdToMSg[it.chatId.toString()] = msg
                    execute(
                        SendMessage(
                            it.chatId.toString(),
                            "Msg updated to $msg"
                        )
                    )
                }
            }
        }
//            update?.message?.let { message ->
//                val chatId = message.chatId
//                val text = message.text
//                when (text.getCommand()) {
//
//                    Command.START.text -> execute(buttonService.createGreetingButton(chatId))
//
//                    Command.HELP.text -> execute(buttonService.createHelpButton(chatId))
//
//                    Command.NOTIFY.text -> {
//                        var errorMessage = ""
//                        val incomingParameters = text.split(" ")
//                        if (incomingParameters.size < 5) errorMessage = "Кажется чего то нехватает"
//                        val date = text.parseDate()?.let {
//                            errorMessage =
//                                """
//                                | Неправильный формат даты или времени!
//                                | ДАТА - В формате ЧИСЛО МЕСЯЦ ГОД, с любым разделителем.
//                                | Пример 10/01/2023, 10-01-2023, 10.01.2023
//                            """.trimIndent()
//                        }
//                        val time = text.parseTime()?.let {
//                            errorMessage =
//                                """
//                                | Неправильный формат даты или времени!
//                                | ДАТА - В формате ЧИСЛО МЕСЯЦ ГОД, с любым разделителем.
//                                | Пример 10/01/2023, 10-01-2023, 10.01.2023
//                            """.trimIndent()
//                        }
//                        val link = incomingParameters.last()
//                        notificationService.createNotification(message.toDto())
//                            .doOnNext { println("Successfully created notification with id = '$it'") }
//                            .subscribe()
//                    }
//
//                    Command.NOTIFICATIONS.text, -> execute(
//                        SendMessage(
//                            chatId.toString(),
//                            notificationService.getAll().map { notification -> notification.text }.toString()
//                        )
//                    )
//                    else -> {
//                        execute(SendChatAction(chatId.toString(), Action.TYPE.description))
//                        Thread.sleep(1000L)
//                        execute(SendMessage(chatId.toString(), "Вы ввели несуществующую команду. Попробуйте снова. Для помощи нажмите: /help"))
//                    }
//                }
//        }
//                "S" -> {
//                    execute(SendMessage().apply {
//                        this.chatId = it.chatId.toString()
//                        this.text = "Все на дейли"
//                        this.replyMarkup = InlineKeyboardMarkup(
//                            mutableListOf(mutableListOf(InlineKeyboardButton("Ссылка на дион").apply {
//                                callbackData = "HelloData"
//                                url = "https://dion.vc/event/aleksandr.bushmin-ved-msb-bp"
//                            }))
////                            mutableListOf(mutableListOf(InlineKeyboardButton("Ссылка на дион", "https://dion.vc/event/aleksandr.bushmin-ved-msb-bp", "Ss", null, "qwe", "qwe2", false, LoginUrl("https://dion.vc/event/aleksandr.bushmin-ved-msb-bp", "Hello world", "Hello bot", false), WebAppInfo("https://dion.vc/event/aleksandr.bushmin-ved-msb-bp"))))
//                    )
//                })
        }

    @Scheduled(cron = "0 * * * * *")
    fun sendDailyNotification() {
        logger.info("Starting schedule")
        if (!listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY).contains(LocalDate.now().dayOfWeek) &&
            LocalDateTime.now(ZoneId.of("Europe/Moscow")).minute == time.minute && LocalDateTime.now(ZoneId.of("Europe/Moscow")).hour == time.hour
        ) {
            logger.info("send daily message")
            val gif = InputFile(File("/home/centos/gifts")
                .listFiles()
                .filter { it.name.endsWith(".mp4") }.toList().random()
            )
            chats.filter { !chatIdToTime.keys.contains(it) }.forEach {
                execute(SendMessage().apply {
                    this.chatId = it
                    this.text = "Коллеги, созвон!"
                    this.replyMarkup = InlineKeyboardMarkup(
                        mutableListOf(mutableListOf(InlineKeyboardButton("Ссылка на дион").apply {
                            url = "https://dion.vc/event/aleksandr.bushmin-ved-msb-bp"
                        }))
                    )
                })
                execute(SendAnimation().apply {
                    this.chatId = it
                    this.animation = gif
                })
            }
            val writer = PrintWriter(File("telegram.txt"))
            writer.write("")
            writer.close()
            logger.info("successfully cleared after daily")
        }
        val now = LocalDateTime.now(ZoneId.of("Europe/Moscow"))
        chats
            .filter { chatIdToTime[it]?.run { this.minute == now.minute && this.hour == now.hour } ?: false }
            .forEach {
                execute(
                    SendMessage(
                        it,
                        chatIdToMSg[it] ?: DEFAULT_MSG
                    )
                )
                execute(
                    SendAnimation(
                        it,
                        InputFile(File("/home/centos/gifts")
                            .listFiles()
                            .filter { file -> file.name.endsWith(".mp4") }.toList().random()
                        )
                    )
                )
            }
        val forCheck = LocalDateTime.of(2023, 1, 1, 21, 0,0)
        if (LocalDateTime.now(ZoneId.of("Europe/Moscow")).minute == forCheck.minute && LocalDateTime.now(ZoneId.of("Europe/Moscow")).hour == forCheck.hour) {
            execute(
                SendMessage(
                    TEST_CHAT_ID,
                    "Bot is alive, everything is all right"
                )
            )
        }
    }

    private fun sendErrorMessage(chatId: String, msg: String) {
        execute(SendMessage(chatId, msg))
    }

    companion object {
        var time: LocalDateTime = LocalDateTime.of(2023, 1, 1, 10, 59);
        const val TEST_CHAT_ID = "-775775105"
        const val VED_CHAT_ID = "-1001645909834"
        const val MY_USERNAME = "art1m"
        const val INFO_MSG =
            """Привет, это бот который ежедневно отправляет уведомления в чат
                 и вот что он умеет:
                 /time - Установить время в которое будет отправлено сообщение в формате HH:MM, Например: 11:30
            """
        const val DEFAULT_MSG = "Коллеги созвон"
        val chatIdToTime = HashMap<String, LocalDateTime>()
        val chatIdToMSg = HashMap<String, String>()
        val chats = listOf(VED_CHAT_ID, TEST_CHAT_ID)
    }
}
