data class RibbitPost(
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
    val user: User,
    val video: String
)