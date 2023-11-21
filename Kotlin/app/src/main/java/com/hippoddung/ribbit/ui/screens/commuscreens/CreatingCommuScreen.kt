package com.hippoddung.ribbit.ui.screens.commuscreens

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.screens.statescreens.ErrorScreen
import com.hippoddung.ribbit.ui.screens.statescreens.LoadingScreen
import com.hippoddung.ribbit.ui.viewmodel.CreatingCommuUiState
import com.hippoddung.ribbit.ui.viewmodel.CommuViewModel
import com.hippoddung.ribbit.ui.viewmodel.MyProfileUiState
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun CreatingCommuScreen(
    commuViewModel: CommuViewModel,
    userViewModel: UserViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    when (commuViewModel.creatingCommuUiState) {
        is CreatingCommuUiState.Ready -> {
            Log.d("HippoLog, CreatingCommuScreen", "Ready")
            InputCommuScreen(
                navController = navController,
                commuViewModel = commuViewModel,
                userViewModel = userViewModel,
                modifier = modifier
            )
        }

        is CreatingCommuUiState.Success -> {
            Log.d("HippoLog, CreatingCommuScreen", "Success")
            commuViewModel.getCommus()
            navController.navigate(RibbitScreen.CommuScreen.name)
            commuViewModel.creatingCommuUiState = CreatingCommuUiState.Ready
        }

        is CreatingCommuUiState.Loading -> {
            Log.d("HippoLog, CreatingCommuScreen", "Loading")
            LoadingScreen(modifier = modifier)
        }

        is CreatingCommuUiState.Error -> {
            Log.d("HippoLog, CreatingCommuScreen", "Error")
            ErrorScreen(modifier = modifier)
        }
    }
}

@Composable
fun InputCommuScreen(
    navController: NavHostController,
    commuViewModel: CommuViewModel,
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var inputCommuName by remember { mutableStateOf("") }
    var inputDescription by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    var privateMode by remember { mutableStateOf(false) }

    val imageLauncher = rememberLauncherForActivityResult(
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
            text = stringResource(R.string.create_commu),
            color = Color.Black,
            modifier = modifier
                .padding(bottom = 16.dp)
                .align(alignment = Alignment.Start)
        )
        TextField(
            value = inputCommuName,
            onValueChange = { inputCommuName = it },
            label = { Text(text = "Commu Name", modifier = modifier) },
            modifier = modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        TextField(
            value = inputDescription,
            onValueChange = { inputDescription = it },
            label = { Text(text = "Commu Description", modifier = modifier) },
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
                            modifier = modifier.size(200.dp)
                        )
                    }
                }
            }
        }

        Row(modifier = modifier) {
            Button(
                onClick = { imageLauncher.launch("image/*") },
                modifier.padding(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Image,
                    contentDescription = "Pick Image button.",
                    modifier = modifier
                )
            }

            val image = if (privateMode) Icons.Filled.Lock else Icons.Filled.LockOpen
            val description = if (privateMode) "Private" else "Public"

            Button(
                onClick = { privateMode = !privateMode },
                modifier = modifier.padding(14.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier
                        .width(100.dp)
                ) {
                    Icon(
                        imageVector = image,
                        contentDescription = description,
                        modifier = modifier
                    )
                    Text(
                        text = description,
                        textAlign = TextAlign.Center,
                        modifier = modifier.fillMaxWidth()
                    )
                }
            }

        }
        Row(modifier = modifier) {
            Button(
                onClick = { navController.navigate(RibbitScreen.CommuScreen.name) },
                modifier = modifier.padding(14.dp)
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    modifier = modifier
                )
            }
            Button(
                onClick = {
                    Log.d("HippoLog, CreatingCommuScreen", "myProfileUiState: ${(userViewModel.myProfileUiState as MyProfileUiState.Exist).myProfile}")
                    Log.d("HippoLog, CreatingCommuScreen", "myProfile: ${userViewModel.myProfile.value}")
                    commuViewModel.createCommu(
                        backgroundImage = bitmap.value,
                        description = inputDescription,
                        commuName = inputCommuName,
                        privateMode = privateMode,
//                        user = (userViewModel.myProfileUiState as MyProfileUiState.Exist).myProfile
                        user = userViewModel.myProfile.value
                    )
                },
                modifier = modifier.padding(14.dp)
            ) {
                Text(
                    text = stringResource(R.string.create_commu),
                    modifier = modifier
                )
            }
        }
        Spacer(modifier = modifier.height(150.dp))
    }
}