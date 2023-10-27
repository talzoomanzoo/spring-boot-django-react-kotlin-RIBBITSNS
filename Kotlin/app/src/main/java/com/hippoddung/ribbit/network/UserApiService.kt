package com.hippoddung.ribbit.network

import com.hippoddung.ribbit.network.bodys.User
import retrofit2.http.GET

interface UserApiService {
    @GET("api/users/profile")
    suspend fun getUserProfile(): User
}