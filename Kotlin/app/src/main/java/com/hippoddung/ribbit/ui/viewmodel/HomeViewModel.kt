package com.hippoddung.ribbit.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.hippoddung.ribbit.data.network.RibbitRepository
import com.hippoddung.ribbit.network.bodys.RibbitPost
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed interface HomeUiState {
    data class Success(val posts: List<RibbitPost>) : HomeUiState
    data class Error(val errorCode: String) : HomeUiState
    object Loading : HomeUiState
}
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val ribbitRepository: RibbitRepository
) : BaseViewModel() {
    var homeUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
//    val homeResponse: MutableLiveData<ApiResponse<List<RibbitPost>>> by lazy {
//        MutableLiveData<ApiResponse<List<RibbitPost>>>()
//    }

//    init {
//        getRibbitPosts(
////            object : CoroutinesErrorHandler {
////                override fun onError(message: String) {
////                    Log.d("HippoLog, HomeViewModel", "Error! $message")
////                }
////            }
//        )
//    }

//    fun getRibbitPosts(coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
//        homeResponse, coroutinesErrorHandler
//    ) {
//        Log.d("HippoLog, HomeViewModel", "getRibbitPosts")
//        ribbitRepository.getPosts()
//    }

    fun getRibbitPosts() {
        viewModelScope.launch {
            homeUiState = HomeUiState.Loading
            homeUiState = try {
                HomeUiState.Success(ribbitRepository.getPosts())
            } catch (e: IOException) {
                Log.d("HippoLog, HomeViewModel", "${e.stackTrace}, ${e.message}")
                HomeUiState.Error(e.message.toString())

            } catch (e: ExceptionInInitializerError) {
                Log.d("HippoLog, HomeViewModel", "${e.stackTrace}, ${e.message}")
                HomeUiState.Error(e.message.toString())

            } catch (e: HttpException){
                Log.d("HippoLog, HomeViewModel", "${e.stackTrace}, ${e.code()}, $e")
                if (e.code() == 500){
                    HomeUiState.Error(e.code().toString())
                }else {
                    HomeUiState.Error(e.message.toString())
                }
            }
            Log.d("HippoLog, HomeViewModel", "$homeUiState")
        }
    }
}