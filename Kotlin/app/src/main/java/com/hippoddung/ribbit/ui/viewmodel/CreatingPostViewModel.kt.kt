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
import com.hippoddung.ribbit.network.bodys.requestbody.TwitCreateRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import javax.inject.Inject

sealed interface CreatingPostUiState {
    object Ready : CreatingPostUiState
    object Loading : CreatingPostUiState
    object Success : CreatingPostUiState
    object Error : CreatingPostUiState
}

sealed interface UploadImageCloudinaryUiState {
    object Loading : UploadImageCloudinaryUiState
    data class Success(val imageUrl: String) : UploadImageCloudinaryUiState
    data class Error(val error: Exception) : UploadImageCloudinaryUiState
    object None : UploadImageCloudinaryUiState
}

sealed interface UploadVideoCloudinaryUiState {
    object Loading : UploadVideoCloudinaryUiState
    data class Success(val videoUrl: String) : UploadVideoCloudinaryUiState
    data class Error(val error: Exception) : UploadVideoCloudinaryUiState
    object None : UploadVideoCloudinaryUiState
}

@HiltViewModel
class TwitsCreateViewModel @Inject constructor(
    private val ribbitRepository: RibbitRepository,
    private val uploadCloudinaryRepository: UploadCloudinaryRepository
) : BaseViewModel() {
    var creatingPostUiState: CreatingPostUiState by mutableStateOf(CreatingPostUiState.Ready)
    var uploadImageCloudinaryUiState: UploadImageCloudinaryUiState by mutableStateOf(
        UploadImageCloudinaryUiState.None
    )
        private set
    var uploadVideoCloudinaryUiState: UploadVideoCloudinaryUiState by mutableStateOf(
        UploadVideoCloudinaryUiState.None
    )
        private set

    fun createTwit(
        image: Bitmap?,
        videoFile: File?,
        inputText: String
    ) {
        var imageUrl: String? = null
        var videoUrl: String? = null

        viewModelScope.launch(Dispatchers.IO) {
            creatingPostUiState = CreatingPostUiState.Loading
            uploadImageCloudinaryUiState = UploadImageCloudinaryUiState.None
            uploadVideoCloudinaryUiState = UploadVideoCloudinaryUiState.None
            if (image != null) {
                uploadImageCloudinaryUiState = UploadImageCloudinaryUiState.Loading
                uploadImageCloudinary(image = image)
                // 성공하면 uploadImageCloudinary 함수에서 UploadImageCloudinaryUiState.Success 로 업데이트함

                when (uploadImageCloudinaryUiState) {
                    is UploadImageCloudinaryUiState.Success -> {
                        imageUrl =
                            (uploadImageCloudinaryUiState as UploadImageCloudinaryUiState.Success).imageUrl
                    }

                    else -> {}
                }
            } else {
            }
            if (videoFile != null) {
                uploadVideoCloudinaryUiState = UploadVideoCloudinaryUiState.Loading

                uploadVideoCloudinary(videoFile = videoFile)
                // 성공하면 uploadVideoCloudinary 함수에서 UploadVideoCloudinaryUiState.Success 로 업데이트함

                when (uploadVideoCloudinaryUiState) {
                    is UploadVideoCloudinaryUiState.Success -> {
                        videoUrl =
                            (uploadVideoCloudinaryUiState as UploadVideoCloudinaryUiState.Success).videoUrl
                    }

                    else -> {}
                }
            } else {
            }


            if (((uploadImageCloudinaryUiState is UploadImageCloudinaryUiState.Success) or (uploadImageCloudinaryUiState is (UploadImageCloudinaryUiState.None))
                        and (uploadVideoCloudinaryUiState is UploadVideoCloudinaryUiState.Success) or (uploadVideoCloudinaryUiState is UploadVideoCloudinaryUiState.None))
            ) {
                twitCreate(
                    TwitCreateRequest(
                        content = inputText,
                        image = imageUrl,
                        video = videoUrl
                    )
                )
            } else {
            }
        }
    }

    suspend fun twitCreate(twitCreateRequest: TwitCreateRequest) {
            try {
                ribbitRepository.postCreatePost(twitCreateRequest)
            } catch (e: IOException) {
                creatingPostUiState = CreatingPostUiState.Error
                println(e.stackTrace)
            } catch (e: ExceptionInInitializerError) {
                creatingPostUiState = CreatingPostUiState.Error
                println(e.stackTrace)
            }
        creatingPostUiState = CreatingPostUiState.Success
    }

    fun getFilePathFromUri(context: Context, uri: Uri): String? {
        // The column containing the file path
        val projection = arrayOf(MediaStore.Video.Media.DATA)

        val cursor = context.contentResolver.query(uri, projection, null, null, null)

        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            return it.getString(columnIndex)
        }
        return null
    }

    suspend fun uploadImageCloudinary(image: Bitmap?) {
        viewModelScope.launch(Dispatchers.IO) {
            UploadImageCloudinaryUiState.None
            if (image != null) {
                UploadImageCloudinaryUiState.Loading
                try {
                    val result = uploadCloudinaryRepository.uploadImageCloudinary(image)
                    Log.d("HippoLog, TwitCreateViewModel, result", "$result")
                    uploadImageCloudinaryUiState = UploadImageCloudinaryUiState.Success(
                        imageUrl = result.url
                    )
                } catch (e: Exception) {
                    uploadImageCloudinaryUiState = UploadImageCloudinaryUiState.Error(e)
                    Log.d("HippoLog, TwitCreateViewModel, Error", "${e.stackTrace}, ${e.message}")
                }
            } else {
            }
        }
    }

    suspend fun uploadVideoCloudinary(videoFile: File?) {
        viewModelScope.launch(Dispatchers.IO) {
            UploadVideoCloudinaryUiState.None
            if (videoFile != null) {
                UploadVideoCloudinaryUiState.Loading
                try {
                    val result =
                        uploadCloudinaryRepository.uploadVideoCloudinary(videoFile = videoFile)
                    Log.d("HippoLog, TwitCreateViewModel, result", "$result")
                    uploadVideoCloudinaryUiState = UploadVideoCloudinaryUiState.Success(
                        videoUrl = result.url
                    )
                } catch (e: Exception) {
                    uploadVideoCloudinaryUiState = UploadVideoCloudinaryUiState.Error(e)
                    Log.d("HippoLog, TwitCreateViewModel, Error", "${e.stackTrace}, ${e.message}")
                }
            } else {
            }
        }
    }
}