package com.hippoddung.ribbit.ui.viewmodel

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.hippoddung.ribbit.data.network.ChatRepository
import com.hippoddung.ribbit.network.BASE_IP
import com.hippoddung.ribbit.network.bodys.chatbodys.ChatDto
import com.hippoddung.ribbit.network.bodys.chatbodys.ChatRoomDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {
    val url = "ws://$BASE_IP/ws/websocket" // 리액트에서 사용하는 라이브러리가 없어서 뒤에 websocket을 붙여줘야 함.
    val stompClient =  Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)

    var selectedRoomIdState: String by mutableStateOf("")

    private var _chatRooms = MutableStateFlow<List<ChatRoomDto>>(emptyList())
    val chatRooms: StateFlow<List<ChatRoomDto>> = _chatRooms.asStateFlow()

    private val _chatHistory = MutableStateFlow<List<ChatDto>>(emptyList())
    val chatHistory: StateFlow<List<ChatDto>> = _chatHistory

    private val _messages = MutableStateFlow<List<String>>(emptyList())
    val messages: StateFlow<List<String>> = _messages

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("CheckResult")
    fun runStomp(){

        stompClient.topic("/topic/$selectedRoomIdState").subscribe { topicMessage ->
            Log.i("message Recieve", topicMessage.payload)
        }

        val headerList = arrayListOf<StompHeader>()
//        headerList.add(StompHeader("inviteCode","test0912"))
        headerList.add(StompHeader("Content-Type","text/plain",))
        stompClient.connect(headerList)

        stompClient.lifecycle().subscribe { lifecycleEvent ->
            when (lifecycleEvent.type) {
                LifecycleEvent.Type.OPENED -> {
                    Log.i("HippoLog ChatViewModel", "runStomp Opened")
                }
                LifecycleEvent.Type.CLOSED -> {
                    Log.i("HippoLog ChatViewModel", "runStomp Closed")

                }
                LifecycleEvent.Type.ERROR -> {
                    Log.i("HippoLog ChatViewModel", "runStomp Error")
                    Log.e("HippoLog ChatViewModel", "runStomp Error: " + lifecycleEvent.exception.toString())
                }
                else ->{
                    Log.i("HippoLog ChatViewModel", "runStomp Else" + lifecycleEvent.message)
                }
            }
        }

        val data = ChatDto()

        stompClient.send("/stream/chat/send", data.toString()).subscribe()
    }

    fun getChatRooms() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val chatRoomsFromServer = chatRepository.getAllChatRooms()
                _chatRooms.value = chatRoomsFromServer
                Log.i("HippoLog ChatViewModel", "getChatRooms Success")
            } catch (e: Exception) {
                // Handle error
                Log.e("HippoLog ChatViewModel", "getChatRooms Error: " + e.message)
            }
        }
    }

    fun createChatRoom(chatRoom: ChatRoomDto) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                chatRepository.createChatRoom(chatRoom)
                // You can update the state or perform any action with the created chat room
                Log.i("HippoLog ChatViewModel", "createChatRoom Success")
            } catch (e: Exception) {
                // Handle error
                Log.e("HippoLog ChatViewModel", "createChatRoom Error: " + e.message)
            }
        }
    }

    fun getChatHistory(roomId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val chatHistoryFromServer = chatRepository.getChatHistory(roomId)
                Log.i("HippoLog ChatViewModel", "getChatHistory Success:  $chatHistoryFromServer")
                _chatHistory.value = chatHistoryFromServer
                Log.i("HippoLog ChatViewModel", "getChatHistory Success:  ${chatHistory.value}")
            } catch (e: Exception) {
                // Handle error
                Log.e("HippoLog ChatViewModel", "getChatHistory Error: " + e.message)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessage(email: String,message: String, sender: String){
        val data = ChatDto(roomId = selectedRoomIdState, sender = sender, email = email, message = message)

        stompClient.send("/app/savechat/$selectedRoomIdState", data.toJsonObject().toString()).subscribe()
        Log.d("HippoLog ChatViewModel", "sendMessage: $message")
    }

}