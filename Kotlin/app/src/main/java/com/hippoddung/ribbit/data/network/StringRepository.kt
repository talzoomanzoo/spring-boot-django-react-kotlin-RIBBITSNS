package com.hippoddung.ribbit.data.network

import com.hippoddung.ribbit.network.StringApiService
import com.hippoddung.ribbit.network.bodys.requestbody.KarloUrl
import javax.inject.Inject

class StringRepository @Inject constructor(
    private val stringApiService: StringApiService
) { suspend fun getSendPrompt(query: String) = stringApiService.getSendPrompt(query)
    suspend fun postWebpToJpg(karlourl: KarloUrl) = stringApiService.postWebpToJpg(karlourl)
}