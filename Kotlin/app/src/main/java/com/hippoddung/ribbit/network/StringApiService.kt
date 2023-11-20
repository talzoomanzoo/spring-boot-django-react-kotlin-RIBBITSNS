package com.hippoddung.ribbit.network

import com.hippoddung.ribbit.network.bodys.requestbody.KarloUrl
import com.hippoddung.ribbit.network.bodys.responsebody.KakaoImageUrl
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface StringApiService {
    @GET("sendprompt")
    suspend fun getSendPrompt(
        @Query("keyword") query : String    // @Query 안의 String 값이 서버의 parameter 명과 같아야 함.
    ): KakaoImageUrl

    @Headers("Content-Type: application/json", "Accept: image/jpeg")
    @POST("webptojpg")
    suspend fun postWebpToJpg(
        @Body karlourl: KarloUrl
    ): ResponseBody
}