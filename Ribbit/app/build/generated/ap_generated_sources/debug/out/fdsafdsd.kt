
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class fdsafdsd(
    val content: String,
    val createdAt: String,
    val id: Int,
    val image: String,
    val liked: Boolean,
    val replyTwits: List<String>,
    val retwit: Boolean,
    val retwitUsersId: List<Int>,
    val totalLikes: Int,
    val totalReplies: Int,
    val totalRetweets: Int,
    val user: UserXX,
    val video: String
)