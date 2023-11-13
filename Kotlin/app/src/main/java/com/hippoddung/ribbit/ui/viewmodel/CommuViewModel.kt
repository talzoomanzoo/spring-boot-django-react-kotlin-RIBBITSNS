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
    object AllCommuPosts : CommuClassificationUiState
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

sealed interface ManageCommuUiState {
    data class ReadyManage(val commuItem: RibbitCommuItem) : ManageCommuUiState
    data class ReadyEdit(val commuItem: RibbitCommuItem) : ManageCommuUiState
    data class Loading(val commuItem: RibbitCommuItem) : ManageCommuUiState
    object Success : ManageCommuUiState
    object Error : ManageCommuUiState
}

sealed interface DeleteCommuUiState {
    object Ready : DeleteCommuUiState
    object Success : DeleteCommuUiState
    data class Error(val errorCode: String) : DeleteCommuUiState
    object Loading : DeleteCommuUiState
}

sealed interface SignupCommuIdUiState {
    object Loading : SignupCommuIdUiState
    data class Success(val commuItem: RibbitCommuItem) : SignupCommuIdUiState
    data class Error(val errorCode: String) : SignupCommuIdUiState
}

sealed interface CommuSignupOkUiState {
    data class Success(val commuItem: RibbitCommuItem) : CommuSignupOkUiState
    data class Error(val errorCode: String) : CommuSignupOkUiState
    object Loading : CommuSignupOkUiState
}

sealed interface SignoutCommuIdUiState {
    data class Success(val commuItem: RibbitCommuItem) : SignoutCommuIdUiState
    data class Error(val errorCode: String) : SignoutCommuIdUiState
    object Loading : SignoutCommuIdUiState
}

@HiltViewModel
class CommuViewModel @Inject constructor(
    private val commuRepository: CommuRepository,
    private val uploadCloudinaryRepository: UploadCloudinaryRepository
) : ViewModel() {
    var commuUiState: CommuUiState by mutableStateOf(CommuUiState.Loading)
    var commuClassificationUiState: CommuClassificationUiState by mutableStateOf(
        CommuClassificationUiState.AllCommuPosts
    )
    var commuIdUiState: CommuIdUiState by mutableStateOf(CommuIdUiState.Loading)
    var creatingCommuUiState: CreatingCommuUiState by mutableStateOf(CreatingCommuUiState.Ready)
    var manageCommuUiState: ManageCommuUiState by mutableStateOf(
        ManageCommuUiState.Loading(
            RibbitCommuItem()
        )
    )
    var commuSignupOkUiState: CommuSignupOkUiState by mutableStateOf(CommuSignupOkUiState.Loading)

    var deleteCommuIdState: Int? by mutableStateOf(null)
    var deleteCommuUiState: DeleteCommuUiState by mutableStateOf(DeleteCommuUiState.Ready)
    var deleteCommuClickedUiState: Boolean by mutableStateOf(false)
    private var uploadImageCloudinaryUiState: UploadImageCloudinaryUiState by mutableStateOf(
        UploadImageCloudinaryUiState.None
    )

    var signupCommuClickedUiState: Boolean by mutableStateOf(false)
    var signupCommuIdUiState: SignupCommuIdUiState by mutableStateOf(SignupCommuIdUiState.Loading)
    var signupCommuState: RibbitCommuItem by mutableStateOf(RibbitCommuItem())

    var signoutCommuClickedUiState: Boolean by mutableStateOf(false)
    var signoutCommuIdState: Int? by mutableStateOf(null)
    var signoutCommuIdUiState: SignoutCommuIdUiState by mutableStateOf(SignoutCommuIdUiState.Loading)

    var searchingUserClickedUiState: Boolean by mutableStateOf(false)

    fun getCommus() {  // 모든 Post 를 불러오는 메소드
        viewModelScope.launch(Dispatchers.IO) {
            commuUiState = CommuUiState.Loading
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
            manageCommuUiState =
                (manageCommuUiState as ManageCommuUiState.ReadyEdit).commuItem.let {
                    ManageCommuUiState.Loading(it)
                }
            val commuItem = (manageCommuUiState as ManageCommuUiState.Loading).commuItem
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
            manageCommuUiState = ManageCommuUiState.Error
            println(e.stackTrace)
        } catch (e: ExceptionInInitializerError) {
            manageCommuUiState = ManageCommuUiState.Error
            println(e.stackTrace)
        }
        manageCommuUiState = ManageCommuUiState.Success
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
                Log.d(
                    "HippoLog, CommuViewModel",
                    "getCommuIdCommu: ${e.stackTrace}, ${e.code()}, $e"
                )
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
            if (commuIdUiState is CommuIdUiState.Success) {
                val commuId =
                    (commuIdUiState as CommuIdUiState.Success).commuItem.id!! // 반드시 있는 경우임.
                try {
                    Log.d("HippoLog, CommuViewModel", "postAddUser, CommuIdUiState")
                    commuRepository.postAddUser(commuId, userId)
                } catch (e: IOException) {
                    Log.d(
                        "HippoLog, CommuViewModel",
                        "postAddUser, CommuIdUiState error: ${e.message}"
                    )
                } catch (e: ExceptionInInitializerError) {
                    Log.d(
                        "HippoLog, CommuViewModel",
                        "postAddUser, CommuIdUiState error: ${e.message}"
                    )
                }
            }
            if (manageCommuUiState is ManageCommuUiState.ReadyManage) {
                val commuId =
                    (manageCommuUiState as ManageCommuUiState.ReadyManage).commuItem.id!!  // 반드시 있는 경우임.
                try {
                    Log.d("HippoLog, CommuViewModel", "postAddUser, ManageCommuUiState")
                    commuRepository.postAddUser(commuId, userId)
                } catch (e: IOException) {
                    Log.d(
                        "HippoLog, CommuViewModel",
                        "postAddUser, ManageCommuUiState error: ${e.message}"
                    )
                } catch (e: ExceptionInInitializerError) {
                    Log.d(
                        "HippoLog, CommuViewModel",
                        "postAddUser, ManageCommuUiState error: ${e.message}"
                    )
                }
            }
        }
    }

    fun signupCommu(commuItem: RibbitCommuItem) {
        signupCommuState = commuItem
        viewModelScope.launch(Dispatchers.IO) {
            signupCommuIdUiState = SignupCommuIdUiState.Loading
            signupCommuIdUiState =
                try {
                    Log.d("HippoLog, CommuViewModel", "signupCommu")
                    SignupCommuIdUiState.Success(commuRepository.signupCommu(commuItem.id!!)) // id는 반드시 있음.
                } catch (e: IOException) {
                    Log.d("HippoLog, CommuViewModel", "signupCommu error: ${e.message}")
                    SignupCommuIdUiState.Error(e.message.toString())
                } catch (e: ExceptionInInitializerError) {
                    Log.d("HippoLog, CommuViewModel", "signupCommu error: ${e.message}")
                    SignupCommuIdUiState.Error(e.message.toString())
                }
        }
    }

    fun postSignupOk(commuId: Int, userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            commuSignupOkUiState = CommuSignupOkUiState.Loading
            commuSignupOkUiState =
                try {
                    Log.d("HippoLog, CommuViewModel", "postSignupOk")
                    CommuSignupOkUiState.Success(
                        commuRepository.postSignupOk(
                            commuId,
                            userId
                        )
                    ) // id는 반드시 있음.
                } catch (e: IOException) {
                    Log.d("HippoLog, CommuViewModel", "postSignupOk error: ${e.message}")
                    CommuSignupOkUiState.Error(e.message.toString())
                } catch (e: ExceptionInInitializerError) {
                    Log.d("HippoLog, CommuViewModel", "postSignupOk error: ${e.message}")
                    CommuSignupOkUiState.Error(e.message.toString())
                }
        }
    }

    suspend fun postSignoutCommuId(commuId: Int) {
        signoutCommuIdUiState = SignoutCommuIdUiState.Loading
        signoutCommuIdUiState =
            try {
                Log.d("HippoLog, CommuViewModel", "postSignoutCommuId")
                SignoutCommuIdUiState.Success(commuRepository.postSignoutCommuId(commuId)) // id는 반드시 있음.
            } catch (e: IOException) {
                Log.d("HippoLog, CommuViewModel", "postSignoutCommuId error: ${e.message}")
                SignoutCommuIdUiState.Error(e.message.toString())
            } catch (e: ExceptionInInitializerError) {
                Log.d("HippoLog, CommuViewModel", "postSignoutCommuId error: ${e.message}")
                SignoutCommuIdUiState.Error(e.message.toString())
            }
    }
}