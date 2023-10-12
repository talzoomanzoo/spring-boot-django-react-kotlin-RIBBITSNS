package com.hippoddung.ribbit.data.network

import com.hippoddung.ribbit.network.AuthApiService
import com.hippoddung.ribbit.network.apiRequestFlow
import com.hippoddung.ribbit.network.bodys.Auth
import com.hippoddung.ribbit.network.bodys.SignUpRequestBody
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService
) {
    fun login(auth: Auth) = apiRequestFlow {
        authApiService.login(auth)
    }
    fun signUp(auth: SignUpRequestBody) = apiRequestFlow {
        authApiService.signUp(auth)
    }
}