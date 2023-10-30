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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.screens.screenitems.InputTextField
import com.hippoddung.ribbit.ui.screens.statescreens.ErrorScreen
import com.hippoddung.ribbit.ui.screens.statescreens.LoadingScreen
import com.hippoddung.ribbit.ui.viewmodel.HomeViewModel
import com.hippoddung.ribbit.ui.viewmodel.TwitsCreateUiState
import com.hippoddung.ribbit.ui.viewmodel.TwitsCreateViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

@Composable
fun TwitCreateScreen(
    twitsCreateViewModel: TwitsCreateViewModel,
    homeViewModel: HomeViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    when (twitsCreateViewModel.twitsCreateUiState) {
        is TwitsCreateUiState.Ready -> {
            Log.d("HippoLog, TwitCreateScreen", "Ready")
            InputTwitScreen(
                navController = navController,
                twitsCreateViewModel = twitsCreateViewModel,
                homeViewModel = homeViewModel,
                modifier = modifier.fillMaxSize()
            )
        }

        is TwitsCreateUiState.Success -> {
            Log.d("HippoLog, TwitCreateScreen", "Success")
            runBlocking {
                launch { navController.navigate(RibbitScreen.HomeScreen.name) }
            }
            twitsCreateViewModel.twitsCreateUiState = TwitsCreateUiState.Ready
        }

        is TwitsCreateUiState.Loading -> {
            Log.d("HippoLog, TwitCreateScreen", "Loading")
            LoadingScreen()
        }

        is TwitsCreateUiState.Error -> {
            Log.d("HippoLog, TwitCreateScreen", "Error")
            ErrorScreen(modifier = modifier.fillMaxSize())
        }

        else -> {}
    }
}

@Composable
fun InputTwitScreen(
    navController: NavHostController,
    twitsCreateViewModel: TwitsCreateViewModel,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var inputText by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var videoUri by remember { mutableStateOf<Uri?>(null) }
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    var videoAbsolutePath by remember { mutableStateOf<String?>(null) }
    var videoFile by remember { mutableStateOf<File?>(null) }

    val imageLauncher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? -> imageUri = uri }
    val videoLauncher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? -> videoUri = uri }

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
        if (imageUri != null) {
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
        }else{}
        if (videoUri != null) {
            videoAbsolutePath = twitsCreateViewModel.getFilePathFromUri(context, videoUri!!)
            videoFile = videoAbsolutePath?.let { File(it) }
            Row {
                Icon(
                    imageVector = Icons.Default.VideoLibrary,
                    contentDescription = "Video Uri",
                    modifier = Modifier.padding(8.dp)
                )
                Text(videoFile!!.name, modifier = Modifier.padding(8.dp))
            }
        }else{}
        Row {
            Button(
                onClick = { imageLauncher.launch("image/*") },
                Modifier.padding(14.dp)
            ) {
                Icon(Icons.Filled.Image, "Pick Image button.")
            }
            Button(
                onClick = { videoLauncher.launch("video/*") },
                Modifier.padding(14.dp)
            ) {
                Icon(Icons.Filled.OndemandVideo, "Pick Video button.")
            }
        }
        Row {
            Button(
                onClick = { navController.navigate(RibbitScreen.HomeScreen.name) },
                Modifier.padding(14.dp)
            ) {
                Text(text = stringResource(R.string.Cancel))
            }
            Button(
                onClick = {
                    twitsCreateViewModel.createTwit(
                        image = bitmap.value,
                        videoFile = videoFile,
                        inputText = inputText,
                    )
                },
                Modifier.padding(14.dp)
            ) {
                Text(text = stringResource(R.string.twit_create))
            }
        }
        Spacer(modifier = Modifier.height(150.dp))
    }
}