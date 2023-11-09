package com.hippoddung.ribbit.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hippoddung.ribbit.data.network.RibbitRepository
import com.hippoddung.ribbit.data.network.UploadCloudinaryRepository
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.network.bodys.requestbody.TwitCreateRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.IOException
import java.time.LocalDateTime
import javax.inject.Inject

sealed interface CreatingPostUiState {
    object Ready : CreatingPostUiState
    object Loading : CreatingPostUiState
    object Success : CreatingPostUiState
    object Error : CreatingPostUiState
}

sealed interface EditingPostUiState {
    data class Ready(val post: RibbitPost) : EditingPostUiState
    data class Loading(val post: RibbitPost) : EditingPostUiState
    object Success : EditingPostUiState
    object Error : EditingPostUiState
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
class PostingViewModel @Inject constructor(
    private val ribbitRepository: RibbitRepository,
    private val uploadCloudinaryRepository: UploadCloudinaryRepository
) : ViewModel() {
    var creatingPostUiState: CreatingPostUiState by mutableStateOf(CreatingPostUiState.Ready)
    var editingPostUiState: EditingPostUiState by mutableStateOf(
        EditingPostUiState.Loading(
            RibbitPost()
        )
    )
    private var uploadImageCloudinaryUiState: UploadImageCloudinaryUiState by mutableStateOf(
        UploadImageCloudinaryUiState.None
    )
    private var uploadVideoCloudinaryUiState: UploadVideoCloudinaryUiState by mutableStateOf(
        UploadVideoCloudinaryUiState.None
    )

    fun createPost(
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
            runBlocking {
                launch(Dispatchers.IO) {
                    if (image != null) {
                        uploadImageCloudinaryUiState = UploadImageCloudinaryUiState.Loading
                        uploadImageCloudinary(image = image)
                        // 성공하면 uploadImageCloudinary 함수에서 UploadImageCloudinaryUiState.Success 로 업데이트함
                        when (uploadImageCloudinaryUiState) {
                            is UploadImageCloudinaryUiState.Success -> {
                                imageUrl =
                                    (uploadImageCloudinaryUiState as UploadImageCloudinaryUiState.Success).imageUrl
                            }

                            else -> {}  // 다른 경우 처리를 위함, 아직 미구현
                        }
                    }
                }
                launch(Dispatchers.IO) {
                    if (videoFile != null) {
                        uploadVideoCloudinaryUiState = UploadVideoCloudinaryUiState.Loading
                        uploadVideoCloudinary(videoFile = videoFile)
                        // 성공하면 uploadVideoCloudinary 함수에서 UploadVideoCloudinaryUiState.Success 로 업데이트함
                        when (uploadVideoCloudinaryUiState) {
                            is UploadVideoCloudinaryUiState.Success -> {
                                videoUrl =
                                    (uploadVideoCloudinaryUiState as UploadVideoCloudinaryUiState.Success).videoUrl
                            }

                            else -> {}  // 다른 경우 처리를 위함, 아직 미구현
                        }
                    }
                }
            }

            if (((uploadImageCloudinaryUiState is UploadImageCloudinaryUiState.Success) or (uploadImageCloudinaryUiState is (UploadImageCloudinaryUiState.None))
                        and (uploadVideoCloudinaryUiState is UploadVideoCloudinaryUiState.Success) or (uploadVideoCloudinaryUiState is UploadVideoCloudinaryUiState.None))
            ) {
                postCreatePost(
                    TwitCreateRequest(
                        content = inputText,
                        image = imageUrl,
                        video = videoUrl
                    )
                )
            } else {    // uploadImageCloudinaryUiState 와 uploadVideoCloudinaryUiState 가 다른 상태일 때 처리를 위함 미구현
            }
        }
    }

    private suspend fun postCreatePost(twitCreateRequest: TwitCreateRequest) {
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun editPost(
        image: Bitmap?,
        videoFile: File?,
        inputText: String,
        edited: Boolean
    ) {
        var imageUrl: String? = null
        var videoUrl: String? = null

        viewModelScope.launch(Dispatchers.IO) {
            editingPostUiState =
                EditingPostUiState.Loading((editingPostUiState as EditingPostUiState.Ready).post)
            val post = (editingPostUiState as EditingPostUiState.Loading).post
            uploadImageCloudinaryUiState = UploadImageCloudinaryUiState.None
            uploadVideoCloudinaryUiState = UploadVideoCloudinaryUiState.None
            runBlocking {
                launch(Dispatchers.IO) {
                    if (image != null) {
                        uploadImageCloudinaryUiState = UploadImageCloudinaryUiState.Loading
                        uploadImageCloudinary(image = image)
                        // 성공하면 uploadImageCloudinary 함수에서 UploadImageCloudinaryUiState.Success 로 업데이트함
                        when (uploadImageCloudinaryUiState) {
                            is UploadImageCloudinaryUiState.Success -> {
                                imageUrl =
                                    (uploadImageCloudinaryUiState as UploadImageCloudinaryUiState.Success).imageUrl
                            }

                            else -> {}  // 다른 경우 처리를 위함, 아직 미구현
                        }
                    } else {
                        // 서버에서 null 을 받으면 null 그대로 처리하는 문제 발견.
                        // 수정 전 이미지가 있으면서 이미지를 수정하지 않은 경우 이전 imageUrl 을 다시 넣어주는 작업을 진행
                        if ((editingPostUiState as EditingPostUiState.Loading).post.image != null) {
                            imageUrl = (editingPostUiState as EditingPostUiState.Loading).post.image
                        }
                    }
                }
                launch(Dispatchers.IO) {
                    if (videoFile != null) {
                        uploadVideoCloudinaryUiState = UploadVideoCloudinaryUiState.Loading
                        uploadVideoCloudinary(videoFile = videoFile)
                        // 성공하면 uploadVideoCloudinary 함수에서 UploadVideoCloudinaryUiState.Success 로 업데이트함
                        when (uploadVideoCloudinaryUiState) {
                            is UploadVideoCloudinaryUiState.Success -> {
                                videoUrl =
                                    (uploadVideoCloudinaryUiState as UploadVideoCloudinaryUiState.Success).videoUrl
                            }

                            else -> {}  // 다른 경우 처리를 위함, 아직 미구현
                        }
                    } else {
                        if ((editingPostUiState as EditingPostUiState.Loading).post.video != null) {
                            videoUrl = (editingPostUiState as EditingPostUiState.Loading).post.video
                        }
                    }
                }
            }

            if (((uploadImageCloudinaryUiState is UploadImageCloudinaryUiState.Success) or (uploadImageCloudinaryUiState is (UploadImageCloudinaryUiState.None))
                        and (uploadVideoCloudinaryUiState is UploadVideoCloudinaryUiState.Success) or (uploadVideoCloudinaryUiState is UploadVideoCloudinaryUiState.None))
            ) {
                post.content = inputText
                post.image = imageUrl
                post.video = videoUrl
                post.edited = edited
                post.editedAt = LocalDateTime.now().toString()  // 수정 여부 및 수정 시각은 프론트에서 직접 값을 넣어서 보냄.
                postEditPost(
                    post
                )
            } else {    // uploadImageCloudinaryUiState 와 uploadVideoCloudinaryUiState 가 다른 상태일 때 처리를 위함 미구현
            }
        }
    }

    private suspend fun postEditPost(post: RibbitPost) {
        try {
            ribbitRepository.postEditPost(post)
        } catch (e: IOException) {
            editingPostUiState = EditingPostUiState.Error
            println(e.stackTrace)
        } catch (e: ExceptionInInitializerError) {
            editingPostUiState = EditingPostUiState.Error
            println(e.stackTrace)
        }
        editingPostUiState = EditingPostUiState.Success
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

    private suspend fun uploadImageCloudinary(image: Bitmap?) {
//        viewModelScope.launch(Dispatchers.IO) {   // 호출함수에서 coroutin 으로 접근하기 때문에 여기서는 그냥 지연함수로 둠
        UploadImageCloudinaryUiState.None
        if (image != null) {
            UploadImageCloudinaryUiState.Loading
            try {
                val result = uploadCloudinaryRepository.uploadImageCloudinary(image)
                Log.d("HippoLog, PostingViewModel", "result: $result")
                uploadImageCloudinaryUiState = UploadImageCloudinaryUiState.Success(
                    imageUrl = result.url
                )
            } catch (e: Exception) {
                uploadImageCloudinaryUiState = UploadImageCloudinaryUiState.Error(e)
                Log.d("HippoLog, PostingViewModel", "error: ${e.stackTrace}, ${e.message}")
            }
        }
//        }
    }

    private suspend fun uploadVideoCloudinary(videoFile: File?) {
        UploadVideoCloudinaryUiState.None
        if (videoFile != null) {
            UploadVideoCloudinaryUiState.Loading
            try {
                val result =
                    uploadCloudinaryRepository.uploadVideoCloudinary(videoFile = videoFile)
                Log.d("HippoLog, PostingViewModel", "result: $result")
                uploadVideoCloudinaryUiState = UploadVideoCloudinaryUiState.Success(
                    videoUrl = result.url
                )
            } catch (e: Exception) {
                uploadVideoCloudinaryUiState = UploadVideoCloudinaryUiState.Error(e)
                Log.d("HippoLog, PostingViewModel", "error: ${e.stackTrace}, ${e.message}")
            }
        }
    }
}