package com.hippoddung.ribbit.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import com.hippoddung.ribbit.data.network.AuthRepository
import com.hippoddung.ribbit.network.ApiResponse
import com.hippoddung.ribbit.network.bodys.Auth
import com.hippoddung.ribbit.network.bodys.LoginResponse
import com.hippoddung.ribbit.network.bodys.SignUpRequestBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


sealed interface AuthUiState {
    object Login : AuthUiState
    object Logout : AuthUiState
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel() {
    var authUiState: AuthUiState by mutableStateOf(AuthUiState.Logout)
    val loginResponse: MutableLiveData<ApiResponse<LoginResponse>> by lazy {
        MutableLiveData<ApiResponse<LoginResponse>>()
    }

    fun signUp(auth: SignUpRequestBody, coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        loginResponse, coroutinesErrorHandler
    ) {
        Log.d("HippoLog, AuthViewModle", "${auth}")
        Log.d("HippoLog, AuthViewModle", "${loginResponse.value}")

        authRepository.signUp(auth)
    }

    fun login(auth: Auth, coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        loginResponse, coroutinesErrorHandler
    ) {
        Log.d("HippoLog, AuthViewModle, loginResponse", "${loginResponse.value}")
        Log.d("HippoLog, AuthViewModle, authUiState", "${authUiState.toString()}")

        authRepository.login(auth)
    }
}