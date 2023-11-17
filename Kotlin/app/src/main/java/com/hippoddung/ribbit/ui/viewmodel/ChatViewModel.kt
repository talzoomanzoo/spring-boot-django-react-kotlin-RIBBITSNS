package com.hippoddung.ribbit.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.hippoddung.ribbit.WebSocketChatClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {

    private val _messages = MutableStateFlow<List<String>>(emptyList())
    val messages: StateFlow<List<String>> = _messages

    private val webSocketClient = WebSocketChatClient()

    init {
        // Connect to the WebSocket server
        try {
            webSocketClient.connect()
        } catch (e: Exception) {
            Log.d("HippoLog ChatViewModel", "init, ${e.message}")
        }
    }

    fun sendMessage(message: String) {
        // Send the message to the server
        try {
            webSocketClient.sendMessage(message)
        } catch (e: Exception) {
            Log.d("HippoLog ChatViewModel", "sendMessage, ${e.message}")
        }
    }

    fun observeMessages(observer: (String) -> Unit) {
        // Observe messages from the WebSocket server
        webSocketClient.setMessageObserver { message ->
            // Update the state flow with the new message
            _messages.value = _messages.value + message

            // Notify the observer
            observer(message)
        }
    }

    override fun onCleared() {
        // Disconnect from the WebSocket server when the ViewModel is cleared
        try {
            webSocketClient.disconnect()
        } catch (e: Exception) {
            Log.d("HippoLog ChatViewModel", "sendMessage, ${e.message}")
        }
        super.onCleared()
    }
}