package com.hippoddung.ribbit.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import com.hippoddung.ribbit.data.network.AuthRepository
import com.hippoddung.ribbit.network.ApiResponse
import com.hippoddung.ribbit.network.bodys.requestbody.AuthRequest
import com.hippoddung.ribbit.network.bodys.requestbody.SignUpRequest
import com.hippoddung.ribbit.network.bodys.responsebody.AuthResponse
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