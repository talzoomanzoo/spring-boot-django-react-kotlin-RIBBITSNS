package com.hippoddung.ribbit.data.network

import android.graphics.Bitmap
import com.hippoddung.ribbit.network.RibbitApiService
import com.hippoddung.ribbit.network.UploadCloudinaryApiService
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.network.bodys.TwitCreateRequest
import com.hippoddung.ribbit.network.bodys.UploadCloudinary
import com.hippoddung.ribbit.network.bodys.UploadCloudinaryResponse
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
    suspend fun uploadImageCloudinary(image: Bitmap): UploadCloudinaryResponse {
        val bitmapRequestBody = BitmapRequestBody(image)
        val bitmapMultipartBody: MultipartBody.Part = MultipartBody.Part.createFormData("file", "hsb_image", bitmapRequestBody)
        val uploadPreset: RequestBody = "instagram".toRequestBody("text/plain".toMediaTypeOrNull())
        val cloudName: RequestBody = "dnbw04gbs".toRequestBody("text/plain".toMediaTypeOrNull())
        val uploadCloudinary = UploadCloudinary(file = bitmapMultipartBody, uploadPreset = uploadPreset, cloudName = cloudName)

        return uploadCloudinaryApiService.uploadImageCloudinary(uploadCloudinary.file, uploadCloudinary.uploadPreset, uploadCloudinary.cloudName)
    }
    inner class BitmapRequestBody(private val bitmap: Bitmap) : RequestBody() {
        override fun contentType(): MediaType = "image/jpeg".toMediaType()
        override fun writeTo(sink: BufferedSink) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 99, sink.outputStream())
        }
    }
}