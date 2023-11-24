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
import com.hippoddung.ribbit.ui.RibbitScreen
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
    data class Success(val post: RibbitPost) : CreatingPostUiState
    object Error : CreatingPostUiState
}

sealed interface EditingPostUiState {
    data class Ready(val post: RibbitPost, val index: Int) : EditingPostUiState
    data class Loading(val post: RibbitPost, val index: Int) : EditingPostUiState
    data class Success(val post: RibbitPost, val index: Int) : EditingPostUiState
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

sealed interface AnalyzingPostEthicUiState {
    object Loading : AnalyzingPostEthicUiState
    data class Success(val post: RibbitPost, val index: Int) : AnalyzingPostEthicUiState
    data class Error(val error: String) : AnalyzingPostEthicUiState
}

@HiltViewModel
class PostingViewModel @Inject constructor(
    private val ribbitRepository: RibbitRepository,
    private val uploadCloudinaryRepository: UploadCloudinaryRepository
) : ViewModel() {
    var creatingPostUiState: CreatingPostUiState by mutableStateOf(CreatingPostUiState.Ready)
    var editingPostUiState: EditingPostUiState by mutableStateOf(
        EditingPostUiState.Loading(RibbitPost(), 0)
    )
    var analyzingPostEthicUiState: AnalyzingPostEthicUiState by mutableStateOf(
        AnalyzingPostEthicUiState.Loading
    )

    private var uploadImageCloudinaryUiState: UploadImageCloudinaryUiState by mutableStateOf(
        UploadImageCloudinaryUiState.None
    )
    private var uploadVideoCloudinaryUiState: UploadVideoCloudinaryUiState by mutableStateOf(
        UploadVideoCloudinaryUiState.None
    )

    var currentScreenState = mutableStateOf(RibbitScreen.HomeScreen)
    fun setCurrentScreen(screen: RibbitScreen) {
        currentScreenState.value = screen
    }

    val currentCommuIdState: Int? by mutableStateOf(null)

    fun createPost(
        image: Bitmap?,
        videoFile: File?,
        inputText: String,
        commuId: Int?
    ) {
        Log.d("HippoLog, PostingViewModel", "currentScreenState: $currentScreenState")

        var imageUrl: String? = null
        var videoUrl: String? = null

        viewModelScope.launch(Dispatchers.IO) {
            creatingPostUiState = CreatingPostUiState.Loading
            uploadImageCloudinaryUiState = UploadImageCloudinaryUiState.None
            uploadVideoCloudinaryUiState = UploadVideoCloudinaryUiState.None
            runBlocking {   // 이미지와 비디오가 있는 경우 url을 리턴받기 위해 기다려야 함.
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
                    RibbitPost(
                        content = inputText,
                        image = imageUrl,
                        video = videoUrl
                    ),
                    commuId // commu 에서 쓰지 않을 경우 null 값이 들어간다.
                )
            } else {    // uploadImageCloudinaryUiState 와 uploadVideoCloudinaryUiState 가 다른 상태일 때 처리를 위함 미구현
            }
        }
    }

    private suspend fun postCreatePost(ribbitPost: RibbitPost, commuId: Int?) {
        if (commuId == null) { // commuId를 받지 않은 경우, 일반 post
            creatingPostUiState =
                try {
                    CreatingPostUiState.Success(post = ribbitRepository.postCreatePost(ribbitPost))
                } catch (e: IOException) {
                    CreatingPostUiState.Error
                } catch (e: ExceptionInInitializerError) {
                    CreatingPostUiState.Error
                }
        } else {  // commuId를 받은 경우 commu post
            creatingPostUiState =
                try {
                    CreatingPostUiState.Success(
                        post = ribbitRepository.postCreateCommuPost(
                            ribbitPost = ribbitPost,
                            commuId = commuId
                        )
                    )
                } catch (e: IOException) {
                    CreatingPostUiState.Error
                } catch (e: ExceptionInInitializerError) {
                    CreatingPostUiState.Error
                }
        }
        if (creatingPostUiState is CreatingPostUiState.Success) { // creatingPostUiState 가 Success 되면 윤리검사 함수를 실행
            postCreatePostEthic((creatingPostUiState as CreatingPostUiState.Success).post)
        }
    }

    private fun postCreatePostEthic(ribbitPost: RibbitPost, index: Int = 0) {
        viewModelScope.launch(Dispatchers.IO) {
            analyzingPostEthicUiState = AnalyzingPostEthicUiState.Loading
            Thread.sleep(2000)
            analyzingPostEthicUiState =
                try {
                    AnalyzingPostEthicUiState.Success(
                        post = ribbitRepository.postCreatePostEthic(
                            ribbitPost
                        ),
                        index = index
                    )
                } catch (e: IOException) {
                    Log.d("HippoLog PostingViewModel", "postCreatePostEthic, ${e.message}")
                    AnalyzingPostEthicUiState.Error(e.message.toString())
                } catch (e: ExceptionInInitializerError) {
                    Log.d("HippoLog PostingViewModel", "postCreatePostEthic, ${e.message}")
                    AnalyzingPostEthicUiState.Error(e.message.toString())
                }
            Log.d(
                "HippoLog PostingViewModel",
                "postCreatePostEthic, Success, $analyzingPostEthicUiState"
            )
        }
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
                EditingPostUiState.Loading((editingPostUiState as EditingPostUiState.Ready).post, (editingPostUiState as EditingPostUiState.Ready).index)
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
                post.editedAt =
                    LocalDateTime.now().toString()  // 수정 여부 및 수정 시각은 프론트에서 직접 값을 넣어서 보냄.
                postEditPost(
                    post
                )
            } else {    // uploadImageCloudinaryUiState 와 uploadVideoCloudinaryUiState 가 다른 상태일 때 처리를 위함 미구현
            }

            if (editingPostUiState is EditingPostUiState.Success) { // editingPostUiState 가 Success 되면 윤리검사 함수를 실행
                postCreatePostEthic((editingPostUiState as EditingPostUiState.Success).post, (editingPostUiState as EditingPostUiState.Success).index)
            }
        }
    }

    private suspend fun postEditPost(post: RibbitPost) {
        editingPostUiState =
            try {
                EditingPostUiState.Success(ribbitRepository.postEditPost(post),(editingPostUiState as EditingPostUiState.Loading).index)
            } catch (e: IOException) {
                EditingPostUiState.Error
            } catch (e: ExceptionInInitializerError) {
                EditingPostUiState.Error
            }
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