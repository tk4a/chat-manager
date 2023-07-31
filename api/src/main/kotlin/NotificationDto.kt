import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

data class NotificationDto(
    @NotNull
    val chatId: String,
    @NotNull
    val text: String,
    @Nullable
    val link: String?
)