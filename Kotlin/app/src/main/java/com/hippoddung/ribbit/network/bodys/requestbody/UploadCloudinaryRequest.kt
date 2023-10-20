package com.hippoddung.ribbit.network.bodys.requestbody

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody

data class UploadCloudinaryRequest(
    val file: MultipartBody.Part,
    @SerializedName("upload_preset")
    val uploadPreset: RequestBody,
    @SerializedName("cloud_name")
    val cloudName: RequestBody
)