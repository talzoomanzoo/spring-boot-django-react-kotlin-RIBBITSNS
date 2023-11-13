package com.hippoddung.ribbit.data.network

import com.hippoddung.ribbit.network.CommuApiService
import com.hippoddung.ribbit.network.bodys.RibbitCommuItem
import javax.inject.Inject

class CommuRepository @Inject constructor(
    private val commuApiService: CommuApiService
) {
    suspend fun getCommus() = commuApiService.getCommus()
    suspend fun postCreateCommu(ribbitCommuItem: RibbitCommuItem) = commuApiService.postCreateCommu(ribbitCommuItem)
    suspend fun getCommuIdPost(commuId: Int) = commuApiService.getCommuIdPost(commuId)
    suspend fun deleteCommuIdCommu(commuId: Int) = commuApiService.deleteCommuIdCommu(commuId)
    suspend fun postEditCommu(commuItem: RibbitCommuItem) = commuApiService.postEditCommu(commuItem)
    suspend fun postAddUser(commuId: Int, userId: Int) = commuApiService.postAddUser(commuId, userId)
    suspend fun signupCommu(commuId: Int) = commuApiService.signupCommu(commuId)


}