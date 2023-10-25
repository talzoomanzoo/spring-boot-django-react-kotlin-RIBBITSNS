package com.hippoddung.ribbit.network.bodys.requestbody

import com.hippoddung.ribbit.network.bodys.User

data class TwitCreateRequest(
    val content: String? = null,
    val createdAt: String? = null,
    val edited: Boolean = false,
    val editedAt: String? = null,
    val id: Int? = null,
    val image: String? = null,
    val liked: Boolean = false,
    val replyTwits: List<String>? = null,
    val retwit: Boolean = false,
    val retwitUsersId: List<Int>? = null,
    val totalLikes: Int = 0,
    val totalReplies: Int = 0,
    val totalRetweets: Int = 0,
    val user: User? = null,
    val video: String ?= null,
    val viewCount: Int = 0
)