package com.hippoddung.ribbit.ui.screens

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hippoddung.ribbit.network.bodys.TwitCreateRequest
import com.hippoddung.ribbit.network.bodys.UploadCloudinary
import com.hippoddung.ribbit.ui.viewmodel.TwitsCreateViewModel

@Composable
fun PickImageScreen(
    twitsCreateViewModel: TwitsCreateViewModel = hiltViewModel()
) {
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current
    val bitmap =  remember {
        mutableStateOf<Bitmap?>(null)
    }
    var uploadCloudinary = bitmap.value?.let { UploadCloudinary(file = it) }

    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }
    Column() {
        Button(onClick = {
            launcher.launch("image/*")
        }) {
            Text(text = "Pick image")
        }

        Spacer(modifier = Modifier.height(12.dp))

        imageUri?.let {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images
                    .Media.getBitmap(context.contentResolver,it)

            } else {
                val source = ImageDecoder
                    .createSource(context.contentResolver,it)
                bitmap.value = ImageDecoder.decodeBitmap(source)
            }

            bitmap.value?.let {  btm ->
                Image(bitmap = btm.asImageBitmap(),
                    contentDescription =null,
                    modifier = Modifier.size(400.dp))
                if (uploadCloudinary != null) {
                    twitsCreateViewModel.uploadImageCloudinary(uploadCloudinary = uploadCloudinary)
                }
            }
        }
    }
}