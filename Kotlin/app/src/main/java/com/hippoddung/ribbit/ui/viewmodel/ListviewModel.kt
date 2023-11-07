package com.hippoddung.ribbit.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hippoddung.ribbit.data.network.ListRepository
import com.hippoddung.ribbit.data.network.UploadCloudinaryRepository
import com.hippoddung.ribbit.network.bodys.RibbitListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
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
    var creatingListUiState: CreatingListUiState by mutableStateOf(CreatingListUiState.Ready)

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
}