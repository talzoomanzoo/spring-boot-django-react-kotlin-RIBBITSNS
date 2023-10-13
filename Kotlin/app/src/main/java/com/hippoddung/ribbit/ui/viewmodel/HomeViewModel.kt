package com.hippoddung.ribbit.ui.viewmodel

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hippoddung.ribbit.data.network.RibbitRepository
import com.hippoddung.ribbit.network.bodys.RibbitPost
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
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
    private val ribbitRepository: RibbitRepository
) : ViewModel() {
    var homeUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    var twitCreateOnClick = mutableStateOf({})

    init {
        getRibbitPosts()
    }

    fun getRibbitPosts() {
        viewModelScope.launch {
            try {
                val result = ribbitRepository.getPosts()
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