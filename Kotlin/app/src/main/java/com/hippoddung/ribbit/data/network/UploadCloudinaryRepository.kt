package com.hippoddung.ribbit.data.network

import android.graphics.Bitmap
import com.hippoddung.ribbit.network.UploadCloudinaryApiService
import com.hippoddung.ribbit.network.bodys.requestbody.UploadCloudinaryRequest
import com.hippoddung.ribbit.network.bodys.responsebody.UploadCloudinaryResponse
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.BufferedSink
import java.io.File
import javax.inject.Inject

class UploadCloudinaryRepository @Inject constructor(
    private val uploadCloudinaryApiService: UploadCloudinaryApiService
) {
    suspend fun uploadImageCloudinary(image: Bitmap): UploadCloudinaryResponse {
        val bitmapRequestBody = BitmapRequestBody(image)
        val bitmapMultipartBody: MultipartBody.Part = MultipartBody.Part.createFormData("file", "hsb_image", bitmapRequestBody)
        val uploadPreset: RequestBody = "vo09fwkv".toRequestBody("text/plain".toMediaTypeOrNull())
        val cloudName: RequestBody = "dedvvc7cr".toRequestBody("text/plain".toMediaTypeOrNull())
        val uploadCloudinary = UploadCloudinaryRequest(file = bitmapMultipartBody, uploadPreset = uploadPreset, cloudName = cloudName)

        return uploadCloudinaryApiService.uploadImageCloudinary(uploadCloudinary.file, uploadCloudinary.uploadPreset, uploadCloudinary.cloudName)
    }
    inner class BitmapRequestBody(private val bitmap: Bitmap) : RequestBody() {
        override fun contentType(): MediaType = "image/jpeg".toMediaType()
        override fun writeTo(sink: BufferedSink) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 99, sink.outputStream())
        }
    }

    suspend fun uploadVideoCloudinary(videoFile: File): UploadCloudinaryResponse {
        val videoRequestBody = videoFile.asRequestBody("video/mp4".toMediaType())
        val videoMultipartBody = MultipartBody.Part.createFormData("file", videoFile.name, videoRequestBody)
        val uploadPreset: RequestBody = "vo09fwkv".toRequestBody("text/plain".toMediaTypeOrNull())
        val cloudName: RequestBody = "dedvvc7cr".toRequestBody("text/plain".toMediaTypeOrNull())
        val uploadCloudinary = UploadCloudinaryRequest(file = videoMultipartBody, uploadPreset = uploadPreset, cloudName = cloudName)

        return uploadCloudinaryApiService.uploadVideoCloudinary(uploadCloudinary.file, uploadCloudinary.uploadPreset, uploadCloudinary.cloudName)
    }
}