package com.hippoddung.ribbit.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import com.hippoddung.ribbit.data.network.RibbitRepository
import com.hippoddung.ribbit.network.ApiResponse
import com.hippoddung.ribbit.network.bodys.RibbitPost
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed interface HomeUiState {
    data class Success(val posts: List<RibbitPost>) : HomeUiState
    object Error : HomeUiState
    object Loading : HomeUiState
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val ribbitRepository: RibbitRepository
) : BaseViewModel() {
    var homeUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
    val homeResponse: MutableLiveData<ApiResponse<List<RibbitPost>>> by lazy {
        MutableLiveData<ApiResponse<List<RibbitPost>>>()
    }

    init {
        getRibbitPosts(
            object : CoroutinesErrorHandler {
                override fun onError(message: String) {
                    Log.d("HippoLog, HomeViewModel", "Error! $message")
                }
            }
        )
    }

    fun getRibbitPosts(coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        homeResponse, coroutinesErrorHandler
    ) {
        Log.d("HippoLog, HomeViewModel", "getRibbitPosts")
        ribbitRepository.getPosts()
    }

    //    fun getRibbitPosts() {
//        viewModelScope.launch {
//            try {
//                val result = ribbitRepository.getPosts()
//                homeUiState = HomeUiState.Success(
//                    result
//                )
//            } catch (e: IOException) {
//                homeUiState = HomeUiState.Error
//                println(e.stackTrace)
//            } catch(e:  ExceptionInInitializerError){
//                homeUiState = HomeUiState.Error
//                println(e.stackTrace)
//            }
//        }
//    }
    fun settingHomeUiState(homeUiState: HomeUiState) {
        this.homeUiState = homeUiState
    }
}