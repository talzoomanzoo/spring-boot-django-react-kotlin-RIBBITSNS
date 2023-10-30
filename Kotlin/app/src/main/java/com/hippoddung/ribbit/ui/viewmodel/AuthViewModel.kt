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
    object LoginLoading : AuthUiState
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
    var authUiState: AuthUiState by mutableStateOf(AuthUiState.LoginLoading)  // authUiState의 경우 <ApiResponse<AuthResponse>>를 관찰하는 observer에게 관리를 위임한다.
    // 현재 TokenUiState가 AthUiState와 동일하게 기능하기 때문에 AthUiState 를 사용하는 것을 중지하고 tokenUiState 를 사용하기로 한다.
    // LoginLoading(JWT를 받아오는 것에 시간이 걸림.)을 위해 다시 사용하기로 함.
    val authResponse: MutableLiveData<ApiResponse<AuthResponse>> by lazy {
        MutableLiveData<ApiResponse<AuthResponse>>()
    }
    var emailUiState: EmailUiState by mutableStateOf(EmailUiState.Lack)
        private set
    var pWUiState: PWUiState by mutableStateOf(PWUiState.Lack)
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            authManager.getEmail().collect { // DataStore 에서 mapping 해서 return 한 flow 를 잡아온다.
                withContext(Dispatchers.Main) {
                    if (it != null) {    // email 정보가 있는 경우
                        emailUiState = EmailUiState.Exist(it)   // emailUiState를 업데이트한다.
                        Log.d("HippoLog, AuthViewModel", "init DataStore 에서 emailUiState 업데이트 $emailUiState")
                    } else {
                        emailUiState = EmailUiState.Lack
                    }
                }
            }
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            authManager.getPW().collect {    // DataStore 에서 mapping 해서 return 한 flow 를 잡아온다.
                withContext(Dispatchers.Main) {
                    if (it != null) {    // pW 정보가 있는 경우
                        pWUiState = PWUiState.Exist(it) // pWUiState를 업데이트한다.
                        Log.d("HippoLog, AuthViewModel", "init DataStore 에서 pWUiState 업데이트 $pWUiState")
                    } else {
                        pWUiState = PWUiState.Lack
                    }
                }
            }
        }
    }

    fun saveLoginInfo(email: String, pW: String) {   // Login 시 DataStore에 email 과 pW를 저장.
        viewModelScope.launch(Dispatchers.IO) {
            authManager.saveEmail(email)
            authManager.savePW(pW)
            Log.d("HippoLog, AuthViewModel", email + pW + "로그인정보 저장")
        }
    }

    suspend fun deleteLoginInfo() {  // Logout 시 DataStore 에 저장된 email 과 pW를 삭제.
        viewModelScope.launch(Dispatchers.IO) {
            authUiState = AuthUiState.Logout
            authManager.deleteEmail()
            authManager.deletePW()
            emailUiState = EmailUiState.Lack
            pWUiState = PWUiState.Lack
            Log.d("HippoLog, AuthViewModel", "DataStore, state 로그인정보 삭제")
        }
    }

    fun signUp(auth: SignUpRequest, coroutinesErrorHandler: CoroutinesErrorHandler) =
        baseRequest(  // signUp network function
            authResponse, coroutinesErrorHandler
        ) {
            authRepository.signUp(auth)
        }

    fun login(authRequest: AuthRequest) {
        Log.d("HippoLog, AuthViewModel", "로그인 시도")
        authUiState = AuthUiState.LoginLoading
        loginRequest(authRequest)   // authResponse를 관찰하는 observer에서 authUiState를 업데이트하도록 함.
    }

    private fun loginRequest(
        authRequest: AuthRequest,
        coroutinesErrorHandler: CoroutinesErrorHandler = object : CoroutinesErrorHandler {
            override fun onError(message: String) {
                "Error! $message"
            }
        }
    ) = baseRequest(  // login network function
        authResponse, coroutinesErrorHandler
    ) {
        authRepository.login(authRequest)
    }
}