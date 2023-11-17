package com.hippoddung.ribbit

import com.hippoddung.ribbit.network.BASE_IP
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.Request
import okhttp3.WebSocket
import okio.ByteString
import java.util.concurrent.TimeUnit

const val WS_BASE_URL =
    "ws://$BASE_IP/ws"

class WebSocketChatClient {
    private var webSocket: WebSocket? = null

    // Callback for message updates
    private var messageObserver: ((String) -> Unit)? = null

    fun setMessageObserver(observer: (String) -> Unit) {
        messageObserver = observer
    }

    fun connect() {
        val client = OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS) // Infinite read timeout for WebSocket
            .build()

        val request = Request.Builder()
            .url(WS_BASE_URL)
            .build()

        webSocket = client.newWebSocket(request, WebSocketListener())

        // You can also enqueue a message to be sent after the connection is established
        // webSocket?.send("Hello, server!")
    }

    fun disconnect() {
        webSocket?.close(1000, "User disconnected")
    }

    fun sendMessage(message: String) {
        webSocket?.send(message)
    }

    private inner class WebSocketListener : okhttp3.WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            // Connection opened
            // You can send a message here if needed
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            // Handle incoming text messages from the server
            messageObserver?.invoke(text)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            // Handle incoming binary messages from the server
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            // Connection is about to close
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            // Connection closed
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            // Handle errors
        }

    }
}