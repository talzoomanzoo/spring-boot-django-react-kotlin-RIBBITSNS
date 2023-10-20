package com.hippoddung.ribbit.ui.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hippoddung.ribbit.data.network.RibbitRepository
import com.hippoddung.ribbit.data.network.UploadCloudinaryRepository
import com.hippoddung.ribbit.network.RibbitApiService
import com.hippoddung.ribbit.network.bodys.Auth
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.network.bodys.TwitCreateRequest
import com.hippoddung.ribbit.network.bodys.TwitCreateResponse
import com.hippoddung.ribbit.network.bodys.UploadCloudinary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

sealed interface TwitsCreateUiState {
    object Success : TwitsCreateUiState
    object Error : TwitsCreateUiState
    object Loading : TwitsCreateUiState
}

sealed interface UploadCloudinaryUiState {
    object Success : UploadCloudinaryUiState
    object Error : UploadCloudinaryUiState
    object Loading : UploadCloudinaryUiState
}

@HiltViewModel
class TwitsCreateViewModel @Inject constructor(
    private val ribbitRepository: RibbitRepository,
    private val uploadCloudinaryRepository: UploadCloudinaryRepository
) : BaseViewModel() {
    var twitsCreateUiState: TwitsCreateUiState by mutableStateOf(TwitsCreateUiState.Loading)
        private set

    var uploadCloudinaryUiState: UploadCloudinaryUiState by mutableStateOf(UploadCloudinaryUiState.Loading)
        private set

    fun twitCreate(twitCreateRequest: TwitCreateRequest) {
        viewModelScope.launch {
            try {
                ribbitRepository.twitCreate(twitCreateRequest)
            } catch (e: IOException) {
                twitsCreateUiState = TwitsCreateUiState.Error
                println(e.stackTrace)
            } catch (e: ExceptionInInitializerError) {
                twitsCreateUiState = TwitsCreateUiState.Error
                println(e.stackTrace)
            }
        }
        twitsCreateUiState = TwitsCreateUiState.Success
    }

    suspend fun uploadImageCloudinary(image: Bitmap): String {
        var imageUrl: String = ""
        try {
            val result = uploadCloudinaryRepository.uploadImageCloudinary(image)
            Log.d("HippoLog, TwitCreateViewModel, result", "$result")
            uploadCloudinaryUiState = UploadCloudinaryUiState.Success
            imageUrl = result.url
        } catch (e: IOException) {
            uploadCloudinaryUiState = UploadCloudinaryUiState.Error
            println(e.stackTrace)
        } catch (e: ExceptionInInitializerError) {
            uploadCloudinaryUiState = UploadCloudinaryUiState.Error
            println(e.stackTrace)
        }
        return imageUrl
    }
}
