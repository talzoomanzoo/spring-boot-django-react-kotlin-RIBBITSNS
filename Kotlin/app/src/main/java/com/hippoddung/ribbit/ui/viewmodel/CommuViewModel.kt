package com.hippoddung.ribbit.ui.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hippoddung.ribbit.data.network.CommuRepository
import com.hippoddung.ribbit.data.network.UploadCloudinaryRepository
import com.hippoddung.ribbit.network.bodys.RibbitCommuItem
import com.hippoddung.ribbit.network.bodys.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed interface CommuUiState {
    data class Success(val commuItems: List<RibbitCommuItem>) : CommuUiState
    data class Error(val errorCode: String) : CommuUiState
    object Loading : CommuUiState
}

sealed interface CommuClassificationUiState {
    object PublicCommu : CommuClassificationUiState
    object PrivateCommu : CommuClassificationUiState
}

sealed interface CommuIdUiState {
    data class Success(val commuItem: RibbitCommuItem) : CommuIdUiState
    data class Error(val errorCode: String) : CommuIdUiState
    object Loading : CommuIdUiState
}

sealed interface CreatingCommuUiState {
    object Ready : CreatingCommuUiState
    object Loading : CreatingCommuUiState
    object Success : CreatingCommuUiState
    object Error : CreatingCommuUiState
}

sealed interface EditingCommuUiState {
    data class Ready(val commuItem: RibbitCommuItem) : EditingCommuUiState
    data class Loading(val commuItem: RibbitCommuItem) : EditingCommuUiState
    object Success : EditingCommuUiState
    object Error : EditingCommuUiState
}

sealed interface DeleteCommuUiState {
    object Ready : DeleteCommuUiState
    object Success : DeleteCommuUiState
    data class Error(val errorCode: String) : DeleteCommuUiState
    object Loading : DeleteCommuUiState
}

@HiltViewModel
class CommuViewModel @Inject constructor(
    private val commuRepository: CommuRepository,
    private val uploadCloudinaryRepository: UploadCloudinaryRepository
) : ViewModel() {
    var commuUiState: CommuUiState by mutableStateOf(CommuUiState.Loading)
    var commuClassificationUiState: CommuClassificationUiState by mutableStateOf(
        CommuClassificationUiState.PublicCommu
    )
    var commuIdUiState: CommuIdUiState by mutableStateOf(CommuIdUiState.Loading)
    var creatingCommuUiState: CreatingCommuUiState by mutableStateOf(CreatingCommuUiState.Ready)
    var editingCommuUiState: EditingCommuUiState by mutableStateOf(EditingCommuUiState.Loading(
        RibbitCommuItem()
    ))

    var deleteCommuIdState: Int? by mutableStateOf(null)
    var deleteCommuUiState: DeleteCommuUiState by mutableStateOf(DeleteCommuUiState.Ready)
    var deleteCommuClickedUiState: Boolean by mutableStateOf(false)
    private var uploadImageCloudinaryUiState: UploadImageCloudinaryUiState by mutableStateOf(
        UploadImageCloudinaryUiState.None
    )

    var searchingUserClickedUiState: Boolean by mutableStateOf(false)

    fun getCommus() {  // 모든 Post 를 불러오는 메소드
        viewModelScope.launch(Dispatchers.IO) {
            commuUiState = CommuUiState.Loading
            commuClassificationUiState = CommuClassificationUiState.PublicCommu
            Log.d("HippoLog, CommuViewModel", "getCommus1, $commuUiState")
            commuUiState = try {
                CommuUiState.Success(commuRepository.getCommus())
            } catch (e: IOException) {
                Log.d("HippoLog, CommuViewModel", "getCommus2, ${e.stackTrace}, ${e.message}")
                CommuUiState.Error(e.message.toString())
            } catch (e: ExceptionInInitializerError) {
                Log.d("HippoLog, CommuViewModel", "getCommus3, ${e.stackTrace}, ${e.message}")
                CommuUiState.Error(e.message.toString())
            } catch (e: HttpException) {
                Log.d("HippoLog, CommuViewModel", "getCommus4, ${e.stackTrace}, ${e.code()}, $e")
                if (e.code() == 500) {
                    CommuUiState.Error(e.code().toString())
                } else {
                    CommuUiState.Error(e.message.toString())
                }
            }
            Log.d("HippoLog, CommuViewModel", "getCommu5, $commuUiState")
        }
    }

    fun createCommu(
        backgroundImage: Bitmap?,
        description: String?,
        commuName: String?,
        privateMode: Boolean?,
        user: User?
    ) {
        var imageUrl: String? = null

        viewModelScope.launch(Dispatchers.IO) {
            creatingCommuUiState = CreatingCommuUiState.Loading
            uploadImageCloudinaryUiState = UploadImageCloudinaryUiState.None
            runBlocking {
                launch(Dispatchers.IO) {
                    if (backgroundImage != null) {
                        uploadImageCloudinaryUiState = UploadImageCloudinaryUiState.Loading
                        uploadImageCloudinary(image = backgroundImage)
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
            }

            if (
                (uploadImageCloudinaryUiState is UploadImageCloudinaryUiState.Success) or (uploadImageCloudinaryUiState is UploadImageCloudinaryUiState.None)
            ) {
                postCreateCommu(
                    RibbitCommuItem(
                        backgroundImage = imageUrl,
                        description = description,
                        comName = commuName,
                        privateMode = privateMode,
                        user = user
                    )
                )
            } else {    // uploadImageCloudinaryUiState 와 uploadVideoCloudinaryUiState 가 다른 상태일 때 처리를 위함 미구현
            }
        }
    }

    private suspend fun postCreateCommu(ribbitCommuItem: RibbitCommuItem) {
        try {
            commuRepository.postCreateCommu(ribbitCommuItem)
        } catch (e: IOException) {
            creatingCommuUiState = CreatingCommuUiState.Error
            println(e.stackTrace)
        } catch (e: ExceptionInInitializerError) {
            creatingCommuUiState = CreatingCommuUiState.Error
            println(e.stackTrace)
        }
        creatingCommuUiState = CreatingCommuUiState.Success
    }

    fun editCommu(
        backgroundImage: Bitmap?,
        description: String?,
        commuName: String?,
        privateMode: Boolean?
    ) {
        var imageUrl: String? = null

        viewModelScope.launch(Dispatchers.IO) {
            editingCommuUiState = (editingCommuUiState as EditingCommuUiState.Ready).commuItem.let { EditingCommuUiState.Loading(it) }
            val commuItem = (editingCommuUiState as EditingCommuUiState.Loading).commuItem
            uploadImageCloudinaryUiState = UploadImageCloudinaryUiState.None
            runBlocking {
                launch(Dispatchers.IO) {
                    if (backgroundImage != null) {
                        uploadImageCloudinaryUiState = UploadImageCloudinaryUiState.Loading
                        uploadImageCloudinary(image = backgroundImage)
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
            }

            if (
                (uploadImageCloudinaryUiState is UploadImageCloudinaryUiState.Success) or (uploadImageCloudinaryUiState is UploadImageCloudinaryUiState.None)
            ) {
                commuItem.backgroundImage = imageUrl
                commuItem.description = description
                commuItem.comName = commuName
                commuItem.privateMode = privateMode
                postEditCommu(commuItem)
            } else {    // uploadImageCloudinaryUiState 와 uploadVideoCloudinaryUiState 가 다른 상태일 때 처리를 위함 미구현
            }
        }
    }

    private suspend fun postEditCommu(ribbitCommuItem: RibbitCommuItem) {
        try {
            commuRepository.postEditCommu(ribbitCommuItem)
        } catch (e: IOException) {
            editingCommuUiState = EditingCommuUiState.Error
            println(e.stackTrace)
        } catch (e: ExceptionInInitializerError) {
            editingCommuUiState = EditingCommuUiState.Error
            println(e.stackTrace)
        }
        editingCommuUiState = EditingCommuUiState.Success
    }

    fun getCommuIdCommu(commuId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            commuIdUiState = CommuIdUiState.Loading
            Log.d("HippoLog, CommuViewModel", "getCommuIdCommu, $commuIdUiState")
            commuIdUiState = try {
                CommuIdUiState.Success(commuRepository.getCommuIdPost(commuId))
            } catch (e: IOException) {
                Log.d("HippoLog, CommuViewModel", "getCommuIdCommu: ${e.stackTrace}, ${e.message}")
                CommuIdUiState.Error(e.message.toString())

            } catch (e: ExceptionInInitializerError) {
                Log.d("HippoLog, CommuViewModel", "getCommuIdCommu: ${e.stackTrace}, ${e.message}")
                CommuIdUiState.Error(e.message.toString())

            } catch (e: HttpException) {
                Log.d("HippoLog, CommuViewModel", "getCommuIdCommu: ${e.stackTrace}, ${e.code()}, $e")
                if (e.code() == 500) {
                    CommuIdUiState.Error(e.code().toString())
                } else {
                    CommuIdUiState.Error(e.message.toString())
                }
            }
            Log.d("HippoLog, CommuViewModel", "getCommuIdCommu, $commuIdUiState")
        }
    }

    private suspend fun uploadImageCloudinary(image: Bitmap?) {
//        viewModelScope.launch(Dispatchers.IO) {   // 호출함수에서 coroutine 으로 접근하기 때문에 여기서는 그냥 지연함수로 둠
        UploadImageCloudinaryUiState.None
        if (image != null) {
            UploadImageCloudinaryUiState.Loading
            try {
                val result = uploadCloudinaryRepository.uploadImageCloudinary(image)
                Log.d("HippoLog, CommuViewModel", "result: $result")
                uploadImageCloudinaryUiState = UploadImageCloudinaryUiState.Success(
                    imageUrl = result.url
                )
            } catch (e: Exception) {
                uploadImageCloudinaryUiState = UploadImageCloudinaryUiState.Error(e)
                Log.d("HippoLog, CommuViewModel", "error: ${e.stackTrace}, ${e.message}")
            }
        }
//        }
    }

    suspend fun deleteCommuIdCommu(commuId: Int) { // Post 를 삭제하는 메소드, 정상적인 페이지 노출을 위해 동기함수로 구성
        Log.d("HippoLog, GetCardViewModel", "deleteRibbitPost, $commuId")
        try {
            deleteCommuUiState = DeleteCommuUiState.Loading
            commuRepository.deleteCommuIdCommu(commuId)
        } catch (e: IOException) {
            Log.d("HippoLog, GetCardViewModel", "${e.stackTrace}, ${e.message}")
            DeleteCommuUiState.Error(e.message.toString())

        } catch (e: ExceptionInInitializerError) {
            Log.d("HippoLog, GetCardViewModel", "${e.stackTrace}, ${e.message}")
            DeleteCommuUiState.Error(e.message.toString())

        } catch (e: HttpException) {
            Log.d("HippoLog, GetCardViewModel", "${e.stackTrace}, ${e.code()}, $e")
            if (e.code() == 500) {
                DeleteCommuUiState.Error(e.code().toString())
            } else {
                DeleteCommuUiState.Error(e.message.toString())
            }
        }
        Log.d("HippoLog, GetCardViewModel", "deleteCommuUiState")
    }

    fun postAddUser(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val commuId = (commuIdUiState as CommuIdUiState.Success).commuItem.id!! // 반드시 있는 경우임.
            try {
                Log.d("HippoLog, CommuViewModel", "postAddUser")
                commuRepository.postAddUser(commuId, userId)
            } catch (e: IOException) {
                Log.d("HippoLog, CommuViewModel", "postAddUser error: ${e.message}")
            } catch (e: ExceptionInInitializerError) {
                Log.d("HippoLog, CommuViewModel", "postAddUser error: ${e.message}")
            }
        }
    }

    fun signupCommu(commuId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("HippoLog, CommuViewModel", "signupCommu")
                commuRepository.signupCommu(commuId)
            } catch (e: IOException) {
                Log.d("HippoLog, CommuViewModel", "signupCommu error: ${e.message}")
            } catch (e: ExceptionInInitializerError) {
                Log.d("HippoLog, CommuViewModel", "signupCommu error: ${e.message}")
            }
        }
    }
}