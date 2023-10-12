package com.hippoddung.ribbit.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hippoddung.ribbit.data.network.AuthRepository
import com.hippoddung.ribbit.network.ApiResponse
import com.hippoddung.ribbit.network.bodys.Auth
import com.hippoddung.ribbit.network.bodys.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
): BaseViewModel() {

    private val _loginResponse = MutableLiveData<ApiResponse<LoginResponse>>()
    val loginResponse = _loginResponse

    fun login(auth: Auth, coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _loginResponse,
        coroutinesErrorHandler
    ) {
        authRepository.login(auth)
    }
}