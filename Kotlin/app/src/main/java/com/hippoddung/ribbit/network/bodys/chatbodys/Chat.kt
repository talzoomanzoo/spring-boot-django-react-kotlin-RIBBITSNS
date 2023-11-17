package com.hippoddung.ribbit.network.bodys.chatbodys

data class Chat(
    val sender: String,
    val email: String,
    val roomId: String,
    val message: String,
    val type: String
)
