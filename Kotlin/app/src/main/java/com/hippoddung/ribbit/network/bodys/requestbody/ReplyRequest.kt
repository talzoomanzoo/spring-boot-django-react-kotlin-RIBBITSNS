package com.hippoddung.ribbit.network.bodys.requestbody


data class ReplyRequest(
    val content: String = "",
    val createdAt: String? = null,
    val image: String? = null,
    val twitId: Int
)