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

sealed interface TokenUiState {
    data class Exist(private val token: String) : TokenUiState
    object Lack : TokenUiState
}
@HiltViewModel
class TokenViewModel @Inject constructor(
    private val tokenManager: TokenManager
): ViewModel() {
    var tokenUiState: TokenUiState by mutableStateOf(TokenUiState.Lack)
    var token = MutableLiveData<String?>()

    init{
        viewModelScope.launch(Dispatchers.IO){
            tokenManager.getToken().collect{
                withContext(Dispatchers.Main){
                    token.value = it
                    Log.d("HippoLog, TokenViewModel", "${token.value}")
                }
            }
            tokenUiState = if(token.value != null) {
                val token: String = token.value.toString()
                TokenUiState.Exist(token)
            }else{
                TokenUiState.Lack
            }
        }
    }

    fun saveToken(token: String){
        viewModelScope.launch(Dispatchers.IO){
            Log.d("HippoLog, saveToken", token)
            tokenManager.saveToken(token)
        }
    }

    fun deleteToken(){
        viewModelScope.launch(Dispatchers.IO){
            tokenManager.deleteToken()
            tokenUiState = TokenUiState.Lack
        }
    }
}