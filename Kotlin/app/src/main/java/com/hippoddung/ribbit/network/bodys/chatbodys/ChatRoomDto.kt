package com.hippoddung.ribbit.network.bodys.chatbodys

data class ChatRoomDto(
    val id: Long? = null,
    val roomId: String = "",
    val name: String = "",
    val creator: String,
    val creatorEmail: String
)
