package com.hippoddung.ribbit.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.hippoddung.ribbit.data.network.RibbitRepository
import com.hippoddung.ribbit.data.network.UploadCloudinaryRepository
import com.hippoddung.ribbit.network.bodys.requestbody.ReplyRequest
import com.hippoddung.ribbit.network.bodys.requestbody.TwitCreateRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import javax.inject.Inject

sealed interface ReplyUiState {
    object Ready : ReplyUiState
    object Loading : ReplyUiState
    object Success : ReplyUiState
    object Error : ReplyUiState
}

@HiltViewModel
class ReplyViewModel @Inject constructor(
    private val ribbitRepository: RibbitRepository,
) : BaseViewModel() {
    var replyUiState: ReplyUiState by mutableStateOf(ReplyUiState.Ready)

    fun reply(inputText: String, postId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            replyUiState = ReplyUiState.Loading
            val replyRequest = ReplyRequest(content = inputText, twitId = postId)
            try {
                ribbitRepository.reply(replyRequest)
            } catch (e: IOException) {
                replyUiState = ReplyUiState.Error
                println(e.stackTrace)
            } catch (e: ExceptionInInitializerError) {
                replyUiState = ReplyUiState.Error
                println(e.stackTrace)
            }
            replyUiState = ReplyUiState.Success
        }
    }
}