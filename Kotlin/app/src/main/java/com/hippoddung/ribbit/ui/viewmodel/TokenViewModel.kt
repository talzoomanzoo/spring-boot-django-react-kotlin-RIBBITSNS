package com.hippoddung.ribbit.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hippoddung.ribbit.data.local.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TokenViewModel @Inject constructor(
    private val tokenManager: TokenManager
): ViewModel() {
    var token = MutableLiveData<String?>()

    init{
        viewModelScope.launch(Dispatchers.IO){
            tokenManager.getToken().collect{
                withContext(Dispatchers.Main){
                    token.value = it
                    Log.d("HippoLog, TokenViewModel", "${token.value}")
                }
            }
            try {
                val token: String = token.value!!
                saveToken(token)
            }catch (e: Exception){
                println(e)
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
        }
    }
}