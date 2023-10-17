package com.hippoddung.ribbit.network


import com.hippoddung.ribbit.network.bodys.UploadCloudinary
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

const val CLOUDINARY_URL =
    "https://api.cloudinary.com/v1_1/dnbw04gbs/"

interface UploadCloudinaryApiService {

    @Multipart
    @POST("image/upload")
    suspend fun uploadImageCloudinary(
        @Part file: MultipartBody.Part,
        @Part("upload_preset") upload_preset: RequestBody,
        @Part("cloud_name") cloud_name: RequestBody
    ): String

    @POST("video/upload")
    suspend fun uploadVideoCloudinary(): String

//    const fileData=await res.json();
//    console.log("url : ", fileData.url.toString());
//    return fileData.url.toString();
}