package com.hippoddung.ribbit.data.network

import com.hippoddung.ribbit.network.UserApiService
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApiService: UserApiService
) {
    suspend fun getUserProfile() = userApiService.getUserProfile()
    suspend fun getProfile(userId: Int) = userApiService.getProfile(userId)
}