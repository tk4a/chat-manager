package com.chat.manager.configuration

import com.chat.manager.configuration.property.TelegramProperties
import com.chat.manager.enum.Command
import com.chat.manager.enum.Message
import com.chat.manager.parser.getCommand
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
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.logging.Logger

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
            val chatId = it.chatId.toString()
            it.text.let { text ->
                when(text.getCommand()) {
                    Command.START.name -> {
                        if (!chats.contains(chatId)) chats.add(chatId).also {
                            SendMessage(
                                chatId,
                                Message.START.text
                            )
                        } else SendMessage(
                            chatId,
                            Message.START_ERROR_MSG.text
                        )
                    }
                    Command.TIME.name -> {
                        val params = text.split(" ")[1].split(":")
                        chatIdToTime[chatId] = LocalDateTime.of(2023, 1, 1, params[0].toInt(), params[1].toInt())
                        execute(
                            SendMessage(
                                chatId,
                                "successfully set new time ${chatIdToTime[chatId]}"
                            )
                        )
                    }
                    Command.CHECK.name -> {
                        logger.info("receive message from = '${it.from.userName}', firstName = '${it.from.firstName}', chatId = ${it.chatId}")
                        execute(
                            SendMessage(
                                TEST_CHAT_ID,
                                "alive"
                            )
                        )
                    }
                    Command.CLEAR.name -> {
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
                    Command.SEND.name -> {
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
                    Command.MSG.name -> chatIdToMsg[chatId] = text.substringAfter(Command.MSG.name)
                    Command.LINK.name -> chatIdToLink[chatId] = text.substringAfter(Command.LINK.name).trim()
                    else -> execute(SendMessage(chatId, "Неизвестная команда"))
                }
            }
        }
        }

    @Scheduled(cron = "0 * * * * *")
    fun sendDailyNotification() {
        logger.info("Starting schedule")
        if (!listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY).contains(LocalDate.now().dayOfWeek)) {
            chats.forEach {
                val gif = InputFile(File("/home/centos/gifts")
                    .listFiles()
                    .filter { file -> file.name.endsWith(".mp4") }.toList().random()
                )
                logger.info("send daily message")
                if (chatIdToTime.containsKey(it) && chatIdToTime[it]?.minute == LocalDateTime.now(ZoneId.of("Europe/Moscow")).minute
                    && chatIdToTime[it]?.hour == LocalDateTime.now(ZoneId.of("Europe/Moscow")).hour)
                    sendMessage(it, gif) else {
                    if (DEFAULT_TIME.hour == LocalDateTime.now(ZoneId.of("Europe/Moscow")).minute && DEFAULT_TIME.minute == LocalDateTime.now(ZoneId.of("Europe/Moscow")).hour)
                        sendMessage(it, gif)
                }
            }
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

    private fun sendMessage(id: String, file: InputFile) {
        execute(SendMessage().apply {
            this.chatId = id
            this.text = chatIdToMsg[id] ?: DEFAULT_MSG
            chatIdToLink[id]?.let {
                this.replyMarkup = InlineKeyboardMarkup(
                    mutableListOf(mutableListOf(InlineKeyboardButton("Ссылка на дион").apply {
                        url = "https://dion.vc/event/aleksandr.bushmin-ved-msb-bp"
                    }))
                )
            }
        })
        execute(SendAnimation().apply {
            this.chatId = id
            this.animation = file
        })
    }

    companion object {
        val DEFAULT_TIME: LocalDateTime = LocalDateTime.of(2023, 1, 1, 10, 59);
        const val TEST_CHAT_ID = "-775775105"
        const val VED_CHAT_ID = "-1001645909834"
        const val MY_USERNAME = "art1m"
        const val DEFAULT_MSG = "Коллеги созвон"
        val chatIdToTime = HashMap<String, LocalDateTime>()
        val chatIdToMsg = HashMap<String, String>()
        val chatIdToLink = HashMap<String, String>()
        val chats = mutableListOf(TEST_CHAT_ID)
    }
}
