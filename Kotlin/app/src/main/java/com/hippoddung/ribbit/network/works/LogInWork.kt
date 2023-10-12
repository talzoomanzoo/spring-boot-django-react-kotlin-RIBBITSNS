//package com.hippoddung.ribbit.network.works
//
//import android.util.Log
//import com.hippoddung.ribbit.data.local.LoginUiState
//import com.hippoddung.ribbit.network.LogInApi
//import com.hippoddung.ribbit.network.bodys.Auth
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//class LogInWork(private val loginRequest: Auth) {
//    val service = LogInApi.retrofitService
//    val loginUiState = LoginUiState()
//
//    init{
//        LogIn()
//    }
//
//    fun LogIn() {
//        CoroutineScope(Dispatchers.IO).launch {
//            // POST request 를 보내고 response 를 받는다.
//            val response = service.logIn(loginRequest)
//
//            withContext(Dispatchers.Main) {
//                if (response.jwt != null) {
//                    val result = response
//                    val jwt = result.jwt
//                    loginUiState.jwt = jwt
//                    Log.d("로그인 성공", "$jwt, $result")
//                } else {
//                    Log.d("로그인 실패", response.toString())
//                }
//            }
//        }
//    }
//}