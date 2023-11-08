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
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.network.bodys.User
import com.hippoddung.ribbit.network.bodys.requestbody.TwitCreateRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import javax.inject.Inject

sealed interface ListUiState {
    data class Success(val listItems: List<RibbitListItem>) : ListUiState
    data class Error(val errorCode: String) : ListUiState
    object Loading : ListUiState
}

sealed interface ListClassificationUiState {
    object ListHome : ListClassificationUiState
    object Following : ListClassificationUiState
    object TopViews : ListClassificationUiState
    object TopLikes : ListClassificationUiState
}

sealed interface ListIdUiState {
    data class Success(val list: RibbitListItem) : ListIdUiState
    data class Error(val errorCode: String) : ListIdUiState
    object Loading : ListIdUiState
}

sealed interface CreatingListUiState {
    object Ready : CreatingListUiState
    object Loading : CreatingListUiState
    object Success : CreatingListUiState
    object Error : CreatingListUiState
}

@HiltViewModel
class ListViewModel @Inject constructor(
    private val listRepository: ListRepository,
    private val uploadCloudinaryRepository: UploadCloudinaryRepository
) : ViewModel() {
    var listUiState: ListUiState by mutableStateOf(ListUiState.Loading)
    var listClassificationUiState: ListClassificationUiState by mutableStateOf(
        ListClassificationUiState.ListHome
    )
    var listIdUiState: ListIdUiState by mutableStateOf(ListIdUiState.Loading)
    var creatingListUiState: CreatingListUiState by mutableStateOf(CreatingListUiState.Ready)

    private var uploadImageCloudinaryUiState: UploadImageCloudinaryUiState by mutableStateOf(
        UploadImageCloudinaryUiState.None
    )
        private set

    fun getLists() {  // 모든 Post를 불러오는 메소드
        viewModelScope.launch(Dispatchers.IO) {
            listUiState = ListUiState.Loading
            listClassificationUiState = ListClassificationUiState.ListHome
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
            } else {    // uploadImageCloudinaryUiState와 uploadVideoCloudinaryUiState 가 다른 상태일 때 처리를 위함 미구현
            }
        }
    }

    fun getListIdList(listId: Int) {    // PostDetail을 불러오는 함수
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

    private suspend fun uploadImageCloudinary(image: Bitmap?) {
//        viewModelScope.launch(Dispatchers.IO) {   // 호출함수에서 coroutin으로 접근하기 때문에 여기서는 그냥 지연함수로 둠
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

}