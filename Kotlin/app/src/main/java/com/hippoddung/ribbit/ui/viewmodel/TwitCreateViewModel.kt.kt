package com.hippoddung.ribbit.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hippoddung.ribbit.data.network.RibbitRepository
import com.hippoddung.ribbit.network.RibbitApiService
import com.hippoddung.ribbit.network.bodys.TwitCreateRequest
import com.hippoddung.ribbit.network.bodys.TwitCreateResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

sealed interface TwitsCreateUiState {
    object Success : TwitsCreateUiState
    object Error : TwitsCreateUiState
    object Loading : TwitsCreateUiState
}

@HiltViewModel
class TwitsCreateViewModel @Inject constructor(
    private val ribbitRepository: RibbitRepository
) : ViewModel() {
    var twitsCreateUiState: TwitsCreateUiState by mutableStateOf(TwitsCreateUiState.Loading)
        private set

    fun twitCreate(twitCreateRequest: TwitCreateRequest) {
        viewModelScope.launch {
            try {
                ribbitRepository.twitCreate(twitCreateRequest)
                twitsCreateUiState = TwitsCreateUiState.Success
            } catch (e: IOException) {
                twitsCreateUiState = TwitsCreateUiState.Error
                println(e.stackTrace)
            } catch(e:  ExceptionInInitializerError){
                twitsCreateUiState = TwitsCreateUiState.Error
                println(e.stackTrace)
            }
        }
    }
}