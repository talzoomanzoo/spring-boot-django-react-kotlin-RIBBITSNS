package com.hippoddung.ribbit.data.network

import com.hippoddung.ribbit.network.AuthApiService
import com.hippoddung.ribbit.network.apiRequestFlow
import com.hippoddung.ribbit.network.bodys.requestbody.AuthRequest
import com.hippoddung.ribbit.network.bodys.requestbody.SignUpRequest
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService
) {
    fun login(authRequest: AuthRequest) = apiRequestFlow {
        authApiService.login(authRequest)
    }
    fun signUp(auth: SignUpRequest) = apiRequestFlow {
        authApiService.signUp(auth)
    }
}