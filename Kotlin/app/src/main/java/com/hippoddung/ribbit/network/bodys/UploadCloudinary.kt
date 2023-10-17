package com.hippoddung.ribbit.network.bodys

import android.graphics.Bitmap

data class UploadCloudinary(
    val file: Bitmap,
    val upload_preset: String = "instagram",
    val cloud_name: String = "dnbw04gbs"
)
