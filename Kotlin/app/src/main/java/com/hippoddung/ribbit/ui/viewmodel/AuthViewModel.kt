package com.hippoddung.ribbit.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hippoddung.ribbit.data.local.AuthManager
import com.hippoddung.ribbit.data.network.AuthRepository
import com.hippoddung.ribbit.network.ApiResponse
import com.hippoddung.ribbit.network.bodys.User
import com.hippoddung.ribbit.network.bodys.requestbody.AuthRequest
import com.hippoddung.ribbit.network.bodys.requestbody.SignUpRequest
import com.hippoddung.ribbit.network.bodys.responsebody.AuthResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


sealed interface AuthUiState {
    object Login : AuthUiState
    object Logout : AuthUiState
}

sealed interface EmailUiState {
    data class Exist(val email: String) : EmailUiState
    object Lack : EmailUiState
}

sealed interface PWUiState {
    data class Exist(val pW: String) : PWUiState
    object Lack : PWUiState
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authManager: AuthManager,
    private val authRepository: AuthRepository
) : BaseViewModel() {
    var authUiState: AuthUiState by mutableStateOf(AuthUiState.Logout)
    val authResponse: MutableLiveData<ApiResponse<AuthResponse>> by lazy {
        MutableLiveData<ApiResponse<AuthResponse>>()
    }
    var emailUiState: EmailUiState by mutableStateOf(EmailUiState.Lack)
        private set
    var pWUiState: PWUiState by mutableStateOf(PWUiState.Lack)
        private set

    init{
        viewModelScope.launch(Dispatchers.IO){
            authManager.getEmail().collect{
                withContext(Dispatchers.Main){
                    if(it != null) {
                        emailUiState = EmailUiState.Exist(it)
                        Log.d("HippoLog, AuthViewModel", "$emailUiState")
                    } else{
                        emailUiState = EmailUiState.Lack
                    }
                }
            }
        }
    }

    init{
        viewModelScope.launch(Dispatchers.IO){
            authManager.getPW().collect{
                withContext(Dispatchers.Main){
                    if(it != null) {
                        pWUiState = PWUiState.Exist(it)
                        Log.d("HippoLog, AuthViewModel", "$pWUiState")
                    }else{
                        pWUiState = PWUiState.Lack
                    }
                }
            }
        }
    }

    fun saveLoginInfo(email: String, pW: String){
        viewModelScope.launch(Dispatchers.IO){
            Log.d("HippoLog, saveLoginInfo", email)
            authManager.saveEmail(email)
            Log.d("HippoLog, saveLoginInfo", pW)
            authManager.savePW(pW)
        }
    }

    fun deleteLoginInfo(){
        viewModelScope.launch(Dispatchers.IO){
            authManager.deleteEmail()
            emailUiState = EmailUiState.Lack
            authManager.deletePW()
            pWUiState = PWUiState.Lack
        }
    }

    fun signUp(auth: SignUpRequest, coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        authResponse, coroutinesErrorHandler
    ) {
        authRepository.signUp(auth)
    }

    fun login(authRequest: AuthRequest, coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        authResponse, coroutinesErrorHandler
    ) {
        authRepository.login(authRequest)
    }
}