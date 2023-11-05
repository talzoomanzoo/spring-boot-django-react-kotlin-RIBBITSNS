package com.hippoddung.ribbit.data.network

import com.hippoddung.ribbit.network.UserApiService
import com.hippoddung.ribbit.network.bodys.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApiService: UserApiService
) {
    suspend fun getMyProfile() = userApiService.getMyProfile()
    suspend fun getProfile(userId: Int) = userApiService.getProfile(userId)
    suspend fun putEditingProfile(user: User) = userApiService.putEditingProfile(user)
}