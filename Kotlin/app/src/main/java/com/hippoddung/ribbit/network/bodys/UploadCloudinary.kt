package com.hippoddung.ribbit.network.bodys

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody

data class UploadCloudinary(
    val file: MultipartBody.Part,
    @SerializedName("upload_preset")
    val uploadPreset: RequestBody,
    @SerializedName("cloud_name")
    val cloudName: RequestBody
)

data class UploadCloudinaryResponse(
    val url: String
)