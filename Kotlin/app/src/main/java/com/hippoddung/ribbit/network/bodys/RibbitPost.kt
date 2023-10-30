package com.hippoddung.ribbit.network.bodys

data class RibbitPost(
    val content: String = "",
    val createdAt: String = "",
    val edited: Boolean = false,
    val editedAt: String?,
    val ethicrate: String?,
    val id: Int,
    val image: String?,
    val label: Int?,
    val liked: Boolean = false,
    val reply: Boolean = false,
    val replyTwits: List<RibbitPost?>?,   // listOf(): 비어있는 리스트를 리턴
    val retwit: Boolean = false,
    val retwitUsersId: List<Int?>?,
    val sentence: String?,
    val totalLikes: Int = 0,
    val totalReplies: Int = 0,
    val totalRetweets: Int = 0,
    val user: User,
    val video: String?,
    val viewCount: Int = 0
)