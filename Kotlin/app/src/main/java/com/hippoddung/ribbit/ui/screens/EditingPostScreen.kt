package com.hippoddung.ribbit.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.screens.carditems.RibbitImage
import com.hippoddung.ribbit.ui.screens.carditems.RibbitVideo
import com.hippoddung.ribbit.ui.screens.textfielditems.InputTextField
import com.hippoddung.ribbit.ui.screens.statescreens.ErrorScreen
import com.hippoddung.ribbit.ui.screens.statescreens.LoadingScreen
import com.hippoddung.ribbit.ui.viewmodel.EditingPostUiState
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.PostingViewModel
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun EditingPostScreen(
    getCardViewModel: GetCardViewModel,
    postingViewModel: PostingViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    when (postingViewModel.editingPostUiState) {
        is EditingPostUiState.Ready -> {
            Log.d("HippoLog, EditingPostScreen", "Ready, ${postingViewModel.editingPostUiState}")
            EditPostScreen(
                navController = navController,
                postingViewModel = postingViewModel,
                getCardViewModel = getCardViewModel,
                modifier = modifier
            )
        }

        is EditingPostUiState.Success -> {
            Log.d("HippoLog, EditingPostScreen", "Success, ${postingViewModel.editingPostUiState}")
            getCardViewModel.getRibbitPosts()
            navController.navigate(RibbitScreen.HomeScreen.name)
            postingViewModel.editingPostUiState = EditingPostUiState.Ready(RibbitPost())
        }

        is EditingPostUiState.Loading -> {
            Log.d("HippoLog, EditingPostScreen", "Loading, ${postingViewModel.editingPostUiState}")
            LoadingScreen(modifier = modifier)
        }

        is EditingPostUiState.Error -> {
            Log.d("HippoLog, EditingPostScreen", "Error, ${postingViewModel.editingPostUiState}")
            ErrorScreen(modifier = modifier)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditPostScreen(
    navController: NavHostController,
    postingViewModel: PostingViewModel,
    getCardViewModel: GetCardViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var post by remember { mutableStateOf(RibbitPost()) }// state check 실행을 위해 initializing
    if (postingViewModel.editingPostUiState is EditingPostUiState.Ready) {   // navigation 으로 이동시 현재 스크린을 backStack 으로 보내면서 재실행, state casting 오류가 발생, state check 삽입
        post = (postingViewModel.editingPostUiState as EditingPostUiState.Ready).post
    }
    var inputText by remember { mutableStateOf(post.content) }
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
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.edit_ribbit),
            color = Color.Black,
            modifier = modifier
                .padding(bottom = 16.dp)
                .align(alignment = Alignment.Start)
        )
        InputTextField(
            value = inputText,
            onValueChange = { inputText = it },
            modifier = modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = modifier.height(4.dp))
        if (imageUri != null) {
            Box(modifier = modifier.size(200.dp)) {
                imageUri?.let {
                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap.value = MediaStore.Images
                            .Media.getBitmap(context.contentResolver, it)   // getBitmap: deprecated
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
                            modifier = modifier
                                .size(200.dp)
                                .clickable { imageLauncher.launch("image/*") }
                        )
                    }
                }
            }
        } else {
            if (post.image != null) {
                Box(
                    modifier = modifier
                        .clickable(
                            onClick = {
                                imageLauncher.launch("image/*")
                                Log.d("HippoLog, EditingPostScreen", "Clicked RibbitImage")
                            }
                        )
                ) {
                    RibbitImage(
                        image = post.image!!,
                        modifier = modifier
                    )
                }
            } else {
                Button(
                    onClick = { imageLauncher.launch("image/*") },
                    modifier.padding(14.dp)
                ) {
                    Row(modifier = modifier) {
                        Icon(
                            imageVector = Icons.Filled.Image,
                            contentDescription = "Select Image",
                            modifier = modifier
                        )
                        Text(text = " Select Image", modifier = modifier)
                    }
                }
            }
        }
        if (videoUri != null) {
            videoAbsolutePath = postingViewModel.getFilePathFromUri(context, videoUri!!)
            videoFile = videoAbsolutePath?.let { File(it) }
            Row(modifier = modifier) {
                Icon(
                    imageVector = Icons.Default.VideoLibrary,
                    contentDescription = "Video Uri",
                    modifier = modifier.padding(8.dp)
                )
                Text(
                    text = videoFile!!.name,
                    modifier = modifier.padding(8.dp)
                )
            }
        } else {
            if (post.video != null) {
                RibbitVideo(
                    videoUrl = post.video!!,
                    getCardViewModel = getCardViewModel,
                    modifier = modifier
                        .clickable { videoLauncher.launch("video/*") }
                )
            } else {
                Button(
                    onClick = { videoLauncher.launch("video/*") },
                    modifier = modifier.padding(14.dp)
                ) {
                    Row(modifier = modifier) {
                        Icon(
                            imageVector = Icons.Filled.OndemandVideo,
                            contentDescription = "Select Video",
                            modifier = modifier
                        )
                        Text(text = " Select Video", modifier = modifier)
                    }
                }
            }
        }

        Row(modifier = modifier) {
            Button(
                onClick = { navController.navigate(RibbitScreen.HomeScreen.name) },
                modifier = modifier.padding(14.dp)
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    modifier = modifier
                )
            }
            Button(
                onClick = {
                    postingViewModel.editPost(
                        image = bitmap.value,
                        videoFile = videoFile,
                        inputText = inputText,
                        edited = true
                    )
                },
                modifier = modifier.padding(14.dp)
            ) {
                Text(
                    text = stringResource(R.string.edit_ribbit),
                    modifier = modifier
                )
            }
        }
        Spacer(modifier = modifier.height(150.dp))
    }
}