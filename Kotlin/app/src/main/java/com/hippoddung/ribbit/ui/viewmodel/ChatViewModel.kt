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
import com.hippoddung.ribbit.WS_URL
import com.hippoddung.ribbit.data.network.ChatRepository
import com.hippoddung.ribbit.network.bodys.chatbodys.ChatRoomDto
import com.hippoddung.ribbit.network.bodys.chatbodys.FullResponse
import com.hippoddung.ribbit.network.bodys.chatbodys.MessageBody
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {
    private val stompClient: StompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, WS_URL)

    private var subscribedRooms: MutableMap<String, Disposable> = mutableMapOf()

    var selectedRoomIdState: String by mutableStateOf("")
    var selectedRoomNameState: String by mutableStateOf("")

    private var _chatRooms = MutableStateFlow<List<ChatRoomDto>>(emptyList())
    val chatRooms: StateFlow<List<ChatRoomDto>> = _chatRooms.asStateFlow()

    private val _chatHistory = MutableStateFlow<List<MessageBody>>(emptyList())
    val chatHistory: StateFlow<List<MessageBody>> = _chatHistory

    init {
        connectStomp()
    }

    private fun connectStomp() {
        val headerList = arrayListOf<StompHeader>()
        headerList.add(StompHeader("Content-Type", "text/plain"))
        stompClient.connect(headerList)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun subscribeRoom(roomId: String) {
        // 구독중이지 않은 경우 구독 function 으로 넘긴다.
        if (roomId !in subscribedRooms.keys) {
            subscribeStomp(roomId)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("CheckResult")
    fun subscribeStomp(roomId: String) {

        val disposableStomp = stompClient.topic("/topic/$roomId")
            .subscribe { topicMessage ->    // topicMessage: JSONString
                Log.i("HippoLog ChatViewModel", "received message: " + topicMessage.payload)
                try {
                    val topicMessageStr = topicMessage.payload
                    val jsonTree = Json.parseToJsonElement(topicMessageStr)

                    // Check if the message has the "headers" property
                    if (jsonTree.jsonObject.containsKey("headers")) {
                        // It's in the extended format, so deserialize as FullResponse
//                    val fullResponse = Json.decodeFromString<FullResponse>(topicMessageStr)
                        // Use the parsed data as needed
                        _chatHistory.value =
                            _chatHistory.value + Json.decodeFromString<FullResponse>(topicMessageStr).body
                        Log.i(
                            "HippoLog ChatViewModel",
                            "received message2-1:  ${chatHistory.value}"
                        )
                        // Handle the extended format as needed
                    } else {
                        Log.i(
                            "HippoLog ChatViewModel",
                            "received message2-2:  ${chatHistory.value}"
                        )
                        // It's in the simple format, so deserialize as MessageBody directly
//                    val messageBody = Json.decodeFromString<MessageBody>(topicMessageStr)
                        // Use the parsed body as needed
//                    println("Body: $messageBody")
                        // Handle the simple format as needed
                    }
                } catch (e: Exception) {
                    // Handle the case where the message doesn't match either format
                    Log.i("HippoLog ChatViewModel", "Exception: ${e.message}")
                }
            }

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
                    Log.e(
                        "HippoLog ChatViewModel",
                        "runStomp Error: " + lifecycleEvent.exception.toString()
                    )
                }

                else -> {
                    Log.i("HippoLog ChatViewModel", "runStomp Else" + lifecycleEvent.message)
                }
            }
        }
        // 여러 방을 구독하고 관리하기 위해 Map에 담아준다.
        subscribedRooms[roomId] = disposableStomp
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

    suspend fun getChatHistory(roomId: String) {
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessage(email: String, message: String, sender: String) {
        val data = MessageBody(
            roomId = selectedRoomIdState,
            sender = sender,
            email = email,
            message = message
        )

        stompClient.send("/app/savechat/$selectedRoomIdState", data.toJsonObject().toString())
            .subscribe()
        Log.d("HippoLog ChatViewModel", "sendMessage: $message")
    }

}