package com.hippoddung.ribbit.data.network

import com.hippoddung.ribbit.network.RibbitApiService
import com.hippoddung.ribbit.network.apiRequestFlow
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.network.bodys.TwitCreateRequest
import javax.inject.Inject

class RibbitRepository @Inject constructor(
    private val ribbitApiService: RibbitApiService
) {
    suspend fun getPosts():List<RibbitPost> {
        return ribbitApiService.getPosts()
    }
    suspend fun twitCreate(twitCreateRequest: TwitCreateRequest) {
        ribbitApiService.twitCreate(twitCreateRequest)
    }
}