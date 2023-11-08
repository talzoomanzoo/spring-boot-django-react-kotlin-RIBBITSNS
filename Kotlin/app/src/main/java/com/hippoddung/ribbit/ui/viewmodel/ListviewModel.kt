package com.hippoddung.ribbit.ui.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hippoddung.ribbit.data.network.ListRepository
import com.hippoddung.ribbit.data.network.UploadCloudinaryRepository
import com.hippoddung.ribbit.network.bodys.RibbitListItem
import com.hippoddung.ribbit.network.bodys.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed interface ListUiState {
    data class Success(val listItems: List<RibbitListItem>) : ListUiState
    data class Error(val errorCode: String) : ListUiState
    object Loading : ListUiState
}

sealed interface ListClassificationUiState {
    object PublicList : ListClassificationUiState
    object PrivateList : ListClassificationUiState
}

sealed interface ListIdUiState {
    data class Success(val listItem: RibbitListItem) : ListIdUiState
    data class Error(val errorCode: String) : ListIdUiState
    object Loading : ListIdUiState
}

sealed interface CreatingListUiState {
    object Ready : CreatingListUiState
    object Loading : CreatingListUiState
    object Success : CreatingListUiState
    object Error : CreatingListUiState
}

sealed interface EditingListUiState {
    data class Ready(val listItem: RibbitListItem) : EditingListUiState
    data class Loading(val listItem: RibbitListItem) : EditingListUiState
    object Success : EditingListUiState
    object Error : EditingListUiState
}

sealed interface DeleteListUiState {
    object Ready : DeleteListUiState
    object Success : DeleteListUiState
    data class Error(val errorCode: String) : DeleteListUiState
    object Loading : DeleteListUiState
}

@HiltViewModel
class ListViewModel @Inject constructor(
    private val listRepository: ListRepository,
    private val uploadCloudinaryRepository: UploadCloudinaryRepository
) : ViewModel() {
    var listUiState: ListUiState by mutableStateOf(ListUiState.Loading)
    var listClassificationUiState: ListClassificationUiState by mutableStateOf(
        ListClassificationUiState.PublicList
    )
    var listIdUiState: ListIdUiState by mutableStateOf(ListIdUiState.Loading)
    var creatingListUiState: CreatingListUiState by mutableStateOf(CreatingListUiState.Ready)
    var editingListUiState: EditingListUiState by mutableStateOf(EditingListUiState.Loading(
        RibbitListItem()
    ))

    var deleteListIdState: Int? by mutableStateOf(null)
    var deleteListUiState: DeleteListUiState by mutableStateOf(DeleteListUiState.Ready)
    var deleteListClickedUiState: Boolean by mutableStateOf(false)
    private var uploadImageCloudinaryUiState: UploadImageCloudinaryUiState by mutableStateOf(
        UploadImageCloudinaryUiState.None
    )

    fun getLists() {  // 모든 Post 를 불러오는 메소드
        viewModelScope.launch(Dispatchers.IO) {
            listUiState = ListUiState.Loading
            listClassificationUiState = ListClassificationUiState.PublicList
            Log.d("HippoLog, ListViewModel", "getLists1, $listUiState")
            listUiState = try {
                ListUiState.Success(listRepository.getLists())
            } catch (e: IOException) {
                Log.d("HippoLog, ListViewModel", "getLists2, ${e.stackTrace}, ${e.message}")
                ListUiState.Error(e.message.toString())
            } catch (e: ExceptionInInitializerError) {
                Log.d("HippoLog, ListViewModel", "getLists3, ${e.stackTrace}, ${e.message}")
                ListUiState.Error(e.message.toString())
            } catch (e: HttpException) {
                Log.d("HippoLog, ListViewModel", "getLists4, ${e.stackTrace}, ${e.code()}, $e")
                if (e.code() == 500) {
                    ListUiState.Error(e.code().toString())
                } else {
                    ListUiState.Error(e.message.toString())
                }
            }
            Log.d("HippoLog, ListViewModel", "getList5, $listUiState")
        }
    }

    fun createList(
        backgroundImage: Bitmap?,
        description: String?,
        listName: String?,
        privateMode: Boolean?,
        user: User?
    ) {
        var imageUrl: String? = null

        viewModelScope.launch(Dispatchers.IO) {
            creatingListUiState = CreatingListUiState.Loading
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
                postCreateList(
                    RibbitListItem(
                        backgroundImage = imageUrl,
                        description = description,
                        listName = listName,
                        privateMode = privateMode,
                        user = user
                    )
                )
            } else {    // uploadImageCloudinaryUiState 와 uploadVideoCloudinaryUiState 가 다른 상태일 때 처리를 위함 미구현
            }
        }
    }

    private suspend fun postCreateList(ribbitListItem: RibbitListItem) {
        try {
            listRepository.postCreateList(ribbitListItem)
        } catch (e: IOException) {
            creatingListUiState = CreatingListUiState.Error
            println(e.stackTrace)
        } catch (e: ExceptionInInitializerError) {
            creatingListUiState = CreatingListUiState.Error
            println(e.stackTrace)
        }
        creatingListUiState = CreatingListUiState.Success
    }

    fun editList(
        backgroundImage: Bitmap?,
        description: String?,
        listName: String?,
        privateMode: Boolean?
    ) {
        var imageUrl: String? = null

        viewModelScope.launch(Dispatchers.IO) {
            editingListUiState = (editingListUiState as EditingListUiState.Ready).listItem.let { EditingListUiState.Loading(it) }
            val listItem = (editingListUiState as EditingListUiState.Loading).listItem
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
                listItem.backgroundImage = imageUrl
                listItem.description = description
                listItem.listName = listName
                listItem.privateMode = privateMode
                postEditList(listItem)
            } else {    // uploadImageCloudinaryUiState 와 uploadVideoCloudinaryUiState 가 다른 상태일 때 처리를 위함 미구현
            }
        }
    }

    private suspend fun postEditList(ribbitListItem: RibbitListItem) {
        try {
            listRepository.postEditList(ribbitListItem)
        } catch (e: IOException) {
            editingListUiState = EditingListUiState.Error
            println(e.stackTrace)
        } catch (e: ExceptionInInitializerError) {
            editingListUiState = EditingListUiState.Error
            println(e.stackTrace)
        }
        editingListUiState = EditingListUiState.Success
    }

    fun getListIdList(listId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            listIdUiState = ListIdUiState.Loading
            Log.d("HippoLog, ListViewModel", "getListIdList, $listIdUiState")
            listIdUiState = try {
                ListIdUiState.Success(listRepository.getListIdPost(listId))
            } catch (e: IOException) {
                Log.d("HippoLog, ListViewModel", "getListIdList: ${e.stackTrace}, ${e.message}")
                ListIdUiState.Error(e.message.toString())

            } catch (e: ExceptionInInitializerError) {
                Log.d("HippoLog, ListViewModel", "getListIdList: ${e.stackTrace}, ${e.message}")
                ListIdUiState.Error(e.message.toString())

            } catch (e: HttpException) {
                Log.d("HippoLog, ListViewModel", "getListIdList: ${e.stackTrace}, ${e.code()}, $e")
                if (e.code() == 500) {
                    ListIdUiState.Error(e.code().toString())
                } else {
                    ListIdUiState.Error(e.message.toString())
                }
            }
            Log.d("HippoLog, ListViewModel", "getListIdList, $listIdUiState")
        }
    }

    private suspend fun uploadImageCloudinary(image: Bitmap?) {
//        viewModelScope.launch(Dispatchers.IO) {   // 호출함수에서 coroutine 으로 접근하기 때문에 여기서는 그냥 지연함수로 둠
        UploadImageCloudinaryUiState.None
        if (image != null) {
            UploadImageCloudinaryUiState.Loading
            try {
                val result = uploadCloudinaryRepository.uploadImageCloudinary(image)
                Log.d("HippoLog, ListViewModel", "result: $result")
                uploadImageCloudinaryUiState = UploadImageCloudinaryUiState.Success(
                    imageUrl = result.url
                )
            } catch (e: Exception) {
                uploadImageCloudinaryUiState = UploadImageCloudinaryUiState.Error(e)
                Log.d("HippoLog, ListViewModel", "error: ${e.stackTrace}, ${e.message}")
            }
        }
//        }
    }

    suspend fun deleteListIdList(listId: Int) { // Post 를 삭제하는 메소드, 정상적인 페이지 노출을 위해 동기함수로 구성
        Log.d("HippoLog, GetCardViewModel", "deleteRibbitPost, $listId")
        try {
            deleteListUiState = DeleteListUiState.Loading
            listRepository.deleteListIdList(listId)
        } catch (e: IOException) {
            Log.d("HippoLog, GetCardViewModel", "${e.stackTrace}, ${e.message}")
            DeleteListUiState.Error(e.message.toString())

        } catch (e: ExceptionInInitializerError) {
            Log.d("HippoLog, GetCardViewModel", "${e.stackTrace}, ${e.message}")
            DeleteListUiState.Error(e.message.toString())

        } catch (e: HttpException) {
            Log.d("HippoLog, GetCardViewModel", "${e.stackTrace}, ${e.code()}, $e")
            if (e.code() == 500) {
                DeleteListUiState.Error(e.code().toString())
            } else {
                DeleteListUiState.Error(e.message.toString())
            }
        }
        Log.d("HippoLog, GetCardViewModel", "deleteListUiState")
    }
}