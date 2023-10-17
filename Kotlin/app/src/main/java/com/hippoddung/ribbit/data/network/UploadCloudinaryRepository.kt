package com.hippoddung.ribbit.data.network

import android.graphics.Bitmap
import com.hippoddung.ribbit.network.RibbitApiService
import com.hippoddung.ribbit.network.UploadCloudinaryApiService
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.network.bodys.TwitCreateRequest
import com.hippoddung.ribbit.network.bodys.UploadCloudinary
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.BufferedSink
import javax.inject.Inject

class UploadCloudinaryRepository @Inject constructor(
    private val uploadCloudinaryApiService: UploadCloudinaryApiService
) {
    suspend fun uploadImageCloudinary(uploadCloudinary: UploadCloudinary): String {
        val bitmapRequestBody = uploadCloudinary.file.let { BitmapRequestBody(it) }
        val bitmapMultipartBody: MultipartBody.Part = MultipartBody.Part.createFormData("file", null, bitmapRequestBody)
        val upload_preset: RequestBody = uploadCloudinary.upload_preset.toRequestBody("text/plain".toMediaTypeOrNull())
        val cloud_name: RequestBody = uploadCloudinary.cloud_name.toRequestBody("text/plain".toMediaTypeOrNull())


        return uploadCloudinaryApiService.uploadImageCloudinary(bitmapMultipartBody, upload_preset, cloud_name)
    }
    inner class BitmapRequestBody(private val bitmap: Bitmap) : RequestBody() {
        override fun contentType(): MediaType = "image/jpeg".toMediaType()
        override fun writeTo(sink: BufferedSink) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 99, sink.outputStream())
        }
    }
}