package com.hippoddung.ribbit.network.bodys.requestbody


import com.google.gson.annotations.SerializedName

data class ReplyRequest(
    val content: String = "",
    val createdAt: String? = null,
    val image: String? = null,
    val twitId: Int
)