//package com.hippoddung.ribbit.network.works
//
//import android.util.Log
//import com.hippoddung.ribbit.network.SignUpApi
//import com.hippoddung.ribbit.network.bodys.SignUpRequestBody
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//class SignUpWork(private val user: SignUpRequestBody) {
//    suspend fun SignUp() {
//        val service = SignUpApi.retrofitService
//
//        CoroutineScope(Dispatchers.IO).launch {
//            // POST request 를 보내고 response 를 받는다.
//            val response = service.addUser(user)
//            withContext(Dispatchers.Main) {
//                if (response.jwt != null) {
//                    val result = response
//                    Log.d("회원가입 성공", "$result")
//                } else {
//                    Log.d("회원가입 실패", response.toString())
//                }
//            }
//        }
//    }
//}
