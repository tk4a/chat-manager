package com.chat.manager.enum

enum class Command(val text: String) {
    START("/start"),
    HELP("/help"),
    NOTIFY("/notify"),
    NOTIFICATIONS("/notifications"),
    TIME("/time"),
    CHECK("/check"),
    CLEAR("/clear"),
    SEND("/send"),
    MSG("/message"),
    LINK("/link")
}