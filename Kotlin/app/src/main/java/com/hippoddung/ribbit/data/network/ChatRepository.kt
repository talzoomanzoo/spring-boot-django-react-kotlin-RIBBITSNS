package com.hippoddung.ribbit.data.network

import com.google.gson.Gson
import com.hippoddung.ribbit.network.ChatApiService
import com.hippoddung.ribbit.network.bodys.chatbodys.ChatDto
import com.hippoddung.ribbit.network.bodys.chatbodys.ChatRoomDto
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val chatApiService: ChatApiService
) {
    suspend fun getAllChatRooms() = chatApiService.getAllChatRooms()
    suspend fun createChatRoom(chatRoom: ChatRoomDto) = chatApiService.createChatRoom(chatRoom)
    suspend fun getChatHistory(roomId: String): List<ChatDto> {
        val requestBody: RequestBody = roomId.toRequestBody("application/json".toMediaTypeOrNull())

        return chatApiService.getChatHistory(requestBody)
    }
}