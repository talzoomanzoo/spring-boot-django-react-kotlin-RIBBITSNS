package com.hippoddung.ribbit.ui.screens

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults.shape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.network.bodys.TwitCreateRequest
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.viewmodel.HomeUiState
import com.hippoddung.ribbit.ui.viewmodel.TwitsCreateViewModel
import com.hippoddung.ribbit.ui.viewmodel.UploadCloudinaryUiState
import kotlinx.coroutines.runBlocking

@Composable
fun TwitCreateScreen(
    navController: NavHostController,
    twitsCreateViewModel: TwitsCreateViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    var imageUrl: String = ""
    var videoUrl: String = ""

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? -> imageUri = uri }

    Column(
        modifier = modifier
            .padding(40.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.twit_create),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(alignment = Alignment.Start)
        )
        InputTextField(
            value = inputText,
            onValueChange = { inputText = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(modifier = Modifier.size(200.dp)) {
            imageUri?.let {
                if (Build.VERSION.SDK_INT < 28) {
                    bitmap.value = MediaStore.Images
                        .Media.getBitmap(context.contentResolver, it)
                } else {
                    val source = ImageDecoder
                        .createSource(context.contentResolver, it)
                    bitmap.value = ImageDecoder.decodeBitmap(source)
                }

                bitmap.value?.let { btm ->
                    Image(
                        bitmap = btm.asImageBitmap(),
                        contentScale = ContentScale.Fit,
                        contentDescription = null,
                        modifier = Modifier.size(200.dp)
                    )
                }
            }
        }
        Row(modifier) {
            Button(
                onClick = { launcher.launch("image/*") },
                modifier.padding(14.dp)
            ) {
                Icon(Icons.Filled.Image, "Pick Image button.")
            }
            Button(
                onClick = { },
                modifier.padding(14.dp)
            ) {
                Text(text = stringResource(R.string.text))
            }
        }
        Row(modifier) {
            Button(
                onClick = { navController.navigate(RibbitScreen.HomeScreen.name) },
                modifier.padding(14.dp)
            ) {
                Text(text = stringResource(R.string.Cancel))
            }
            Button(
                onClick = {
                    if (bitmap.value != null) {
                        runBlocking {
                            val imageUrlResponse = twitsCreateViewModel.uploadImageCloudinary(image = bitmap.value!!)
                            Log.d("HippoLog, TwitCreateScreen", "image: ${bitmap.value}")
                            Log.d(
                                "HippoLog, TwitCreateScreen",
                                "uploadCloudinaryUiState: ${twitsCreateViewModel.uploadCloudinaryUiState}"
                            )

                            when (twitsCreateViewModel.uploadCloudinaryUiState) {
                                is UploadCloudinaryUiState.Success -> {
                                    imageUrl = imageUrlResponse
                                    Log.d("HippoLog, TwitCreateScreen", "imageUrl: $imageUrl")
                                }
                                else -> null
                            }
                            twitsCreateViewModel.twitCreate(
                                TwitCreateRequest(
                                    content = inputText,
                                    image = imageUrl,
                                    video = videoUrl
                                )
                            )
                            navController.navigate(RibbitScreen.HomeScreen.name)
                        }
                    }
                    navController.navigate(RibbitScreen.HomeScreen.name)
                },
                modifier.padding(14.dp)
            ) {
                Text(text = stringResource(R.string.twit_create))
            }
        }
        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Composable
fun InputTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        singleLine = false,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
        label = { Text(stringResource(R.string.twit_create)) },
        value = value,
        onValueChange = onValueChange,
        minLines = 5,
        modifier = modifier,
    )
}