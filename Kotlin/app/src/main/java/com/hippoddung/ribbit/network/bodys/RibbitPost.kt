package com.hippoddung.ribbit.network.bodys

data class RibbitPost(
    val content: String,
    val createdAt: String,
    val edited: Boolean,
    val editedAt: String? = null,
    val id: Int = 0,
    val image: String = "",
    val liked: Boolean = false,
    val replyTwits: List<String>? = null,
    val retwit: Boolean = false,
    val retwitUsersId: List<Int>? = null,
    val totalLikes: Int = 0,
    val totalReplies: Int = 0,
    val totalRetweets: Int = 0,
    val user: User,
    val video: String = "",
    val viewCount: Int
)