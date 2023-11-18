package com.hippoddung.ribbit.network.bodys.chatbodys

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
data class ChatDto constructor(
    val id: Long? = null,
    val type: MessageType = MessageType.TALK,   // 서버에서 구현하려다가 그만둔 기능
    val roomId: String? = null,
    val sender: String? = null,
    val email: String? = null,
    val message: String = "",
    val time: String? = null
) {
    enum class MessageType {
        ENTER, TALK
    }

    fun toJsonObject(): JsonObject {
        return JsonObject(
            mapOf(
                "id" to JsonPrimitive(id),
                "type" to JsonPrimitive(type.name),
                "roomId" to JsonPrimitive(roomId),
                "sender" to JsonPrimitive(sender),
                "email" to JsonPrimitive(email),
                "message" to JsonPrimitive(message),
                "time" to JsonPrimitive(time)
            )
        )
    }
}

