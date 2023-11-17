package com.hippoddung.ribbit.network.bodys.responsebody


import com.google.gson.annotations.SerializedName

data class KakaoImageUrl(
    @SerializedName("image_url")
    val kakaoImageUrl: String?
)