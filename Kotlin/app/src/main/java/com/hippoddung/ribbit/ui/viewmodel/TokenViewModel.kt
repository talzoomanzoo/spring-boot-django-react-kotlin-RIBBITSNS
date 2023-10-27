package com.hippoddung.ribbit.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hippoddung.ribbit.data.local.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

//sealed interface TokenUiState {
//    data class Exist(private val token: String) : TokenUiState
//    object Loading : TokenUiState
//    object Lack : TokenUiState
//}

@HiltViewModel
class TokenViewModel @Inject constructor(
    private val tokenManager: TokenManager
) : ViewModel() {
//    var tokenUiState: TokenUiState by mutableStateOf(TokenUiState.Lack) // token을 MainActivity의 Observer에서 관찰하기 때문에 state 업데이트를 MainActivity에 위임한다.
    // token의 상태를 관찰하여 바로 사용하는 방향으로 전환
    var token = MutableLiveData<String?>()  // LiveData 객체

    init {
        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.getToken()
                .collect {    // DataStore TokenManager.getToken()이 return하는 mapping된 flow를 잡는다.
                    withContext(Dispatchers.Main) {
                        token.value = it    // token Livedata 에 token값을 넣는다.
                        Log.d("HippoLog, TokenViewModel", "init DataStore 에서 토큰 보냄 ${token.value}")
                    }
                }
        }
    }

    fun saveToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.saveToken(token)
            Log.d("HippoLog, TokenViewModel", "DataStore 토큰 저장")
        }
    }

    suspend fun deleteToken() {
        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.deleteToken()  // DataStore의 저장된 토큰을 삭제
            token.postValue(null)   // token LiveData에 null값을 넣어준다.
            Log.d("HippoLog, saveToken", "DataStore, LiveDate 토큰 삭제")
        }
    }
}