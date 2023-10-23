package com.hippoddung.ribbit.data.network

import com.hippoddung.ribbit.network.RibbitApiService
import com.hippoddung.ribbit.network.apiRequestFlow
import com.hippoddung.ribbit.network.bodys.requestbody.TwitCreateRequest
import javax.inject.Inject

class RibbitRepository @Inject constructor(
    private val ribbitApiService: RibbitApiService
) {
    fun getPosts() = apiRequestFlow {
        ribbitApiService.getPosts()
    }
    suspend fun twitCreate(twitCreateRequest: TwitCreateRequest) {
        ribbitApiService.twitCreate(twitCreateRequest)
    }
}