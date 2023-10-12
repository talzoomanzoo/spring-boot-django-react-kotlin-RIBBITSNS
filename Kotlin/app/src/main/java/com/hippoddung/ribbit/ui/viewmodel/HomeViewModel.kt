package com.hippoddung.ribbit.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hippoddung.ribbit.network.RibbitApiService
import com.hippoddung.ribbit.network.bodys.RibbitPost
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

sealed interface HomeUiState {
    data class Success(val posts: List<RibbitPost>) : HomeUiState
    object Error : HomeUiState
    object Loading : HomeUiState
}
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val ribbitApiService: RibbitApiService
) : ViewModel() {
    var homeUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    init {
        getRibbitPosts()
    }

    fun getRibbitPosts() {
        viewModelScope.launch {
            try {
                val result = ribbitApiService.getPosts()
                homeUiState = HomeUiState.Success(
                    result
                )
            } catch (e: IOException) {
                homeUiState = HomeUiState.Error
                println(e.stackTrace)
            } catch(e:  ExceptionInInitializerError){
                homeUiState = HomeUiState.Error
                println(e.stackTrace)
            }
        }
    }
}