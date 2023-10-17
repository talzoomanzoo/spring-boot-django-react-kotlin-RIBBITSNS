package com.hippoddung.ribbit.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import com.hippoddung.ribbit.data.network.AuthRepository
import com.hippoddung.ribbit.network.ApiResponse
import com.hippoddung.ribbit.network.bodys.Auth
import com.hippoddung.ribbit.network.bodys.AuthResponse
import com.hippoddung.ribbit.network.bodys.SignUpRequestBody
import dagger.hilt.android.lifecycle.HiltViewModel
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
    val authResponse: MutableLiveData<ApiResponse<AuthResponse>> by lazy {
        MutableLiveData<ApiResponse<AuthResponse>>()
    }

    fun signUp(auth: SignUpRequestBody, coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        authResponse, coroutinesErrorHandler
    ) {
        authRepository.signUp(auth)
    }

    fun login(auth: Auth, coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        authResponse, coroutinesErrorHandler
    ) {
        authRepository.login(auth)
    }
}