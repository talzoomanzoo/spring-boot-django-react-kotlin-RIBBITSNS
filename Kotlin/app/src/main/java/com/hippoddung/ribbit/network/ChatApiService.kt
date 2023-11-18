package com.hippoddung.ribbit.network

import com.hippoddung.ribbit.network.bodys.chatbodys.ChatDto
import com.hippoddung.ribbit.network.bodys.chatbodys.ChatRoomDto
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ChatApiService {
    @GET("/allrooms")
    suspend fun getAllChatRooms(): List<ChatRoomDto>

    @POST("/createroom")
    suspend fun createChatRoom(
        @Body chatRoom: ChatRoomDto
    ): ChatRoomDto

    @POST("/getchat")
    suspend fun getChatHistory(@Body requestBody: RequestBody): List<ChatDto>
}