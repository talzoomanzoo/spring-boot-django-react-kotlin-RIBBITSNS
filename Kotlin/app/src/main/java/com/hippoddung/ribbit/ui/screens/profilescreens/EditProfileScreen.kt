package com.hippoddung.ribbit.ui.screens.profilescreens

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.network.bodys.User
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.screens.statescreens.ErrorScreen
import com.hippoddung.ribbit.ui.screens.statescreens.LoadingScreen
import com.hippoddung.ribbit.ui.viewmodel.EditingProfileUiState
import com.hippoddung.ribbit.ui.viewmodel.ProfileUiState
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel

@Composable
fun EditProfileScreen(
    navController: NavHostController,
    userViewModel: UserViewModel,
    myId: Int,
    modifier: Modifier = Modifier
) {
    when (userViewModel.editingProfileUiState) {
        is EditingProfileUiState.Ready -> {
            Log.d("HippoLog, EditProfileScreen", "Ready")
            EditProfileReadyScreen(
                navController = navController,
                userViewModel = userViewModel,
                modifier = modifier
            )
        }

        is EditingProfileUiState.Success -> {
            Log.d("HippoLog, EditProfileScreen", "Success")
            userViewModel.getProfile(myId)
            navController.navigate(RibbitScreen.ProfileScreen.name)
            userViewModel.editingProfileUiState = EditingProfileUiState.Ready
        }

        is EditingProfileUiState.Loading -> {
            Log.d("HippoLog, EditProfileScreen", "Loading")
            LoadingScreen(modifier = modifier)
        }

        is EditingProfileUiState.Error -> {
            Log.d("HippoLog, EditProfileScreen", "Error")
            ErrorScreen(modifier = modifier)
        }
    }
}

@Composable
fun EditProfileReadyScreen(
    navController: NavHostController,
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var myProfile by remember { mutableStateOf(User(email = "")) }// state check 실행을 위해 initializing
    if (userViewModel.profileUiState is ProfileUiState.Exist) {   // navigation 으로 이동시 현재 스크린을 backStack 으로 보내면서 재실행, state casting 오류가 발생, state check 삽입
        myProfile = (userViewModel.profileUiState as ProfileUiState.Exist).user
    }
    var inputFullName by remember { mutableStateOf(myProfile.fullName ?: "") }
    var inputBio by remember { mutableStateOf(myProfile.bio ?: "") }
    var inputWebsite by remember { mutableStateOf(myProfile.website ?: "") }
    var inputEducation by remember { mutableStateOf(myProfile.education ?: "") }
    var inputBirthDate by remember { mutableStateOf(myProfile.birthDate ?: "") }

    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    val profileBitmap = remember { mutableStateOf<Bitmap?>(null) }

    var profileBackgroundImageUri by remember { mutableStateOf<Uri?>(null) }
    val profileBackgroundBitmap = remember { mutableStateOf<Bitmap?>(null) }

    val profileImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> profileImageUri = uri }  // 이미지 표시를 위해 변수에 할당

    val backgroundImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> profileBackgroundImageUri = uri }  // 이미지 표시를 위해 변수에 할당

    Scaffold(
        modifier = modifier,
        topBar = {
            EditProfileScreenTopBar(
                userViewModel = userViewModel,
                navController = navController,
                inputFullName = inputFullName,
                inputBio = inputBio,
                inputWebsite = inputWebsite,
                inputEducation = inputEducation,
                inputBirthDate = inputBirthDate,
                profileImage = profileBitmap.value,
                profileBackgroundImage = profileBackgroundBitmap.value,
                modifier = modifier
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    userViewModel.editProfile(
                        inputFullName = inputFullName,
                        inputBio = inputBio,
                        inputWebsite = inputWebsite,
                        inputEducation = inputEducation,
                        inputBirthDate = inputBirthDate,
                        profileImage = profileBitmap.value,
                        profileBackgroundImage = profileBackgroundBitmap.value
                    )
                },
                modifier = modifier
                    .padding(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Floating action button.",
                    modifier = modifier
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column( // profile 최외곽 composable
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // profile images
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(150.dp)
                ) {
                    // profile background image
                    if (profileBackgroundImageUri != null) {
                        profileBackgroundImageUri?.let {
                            if (Build.VERSION.SDK_INT < 28) {
                                profileBackgroundBitmap.value =
                                    MediaStore.Images.Media.getBitmap(
                                        context.contentResolver,
                                        it
                                    )   // getBitmap: deprecated
                            } else {
                                val source = ImageDecoder
                                    .createSource(context.contentResolver, it)
                                profileBackgroundBitmap.value =
                                    ImageDecoder.decodeBitmap(source)
                            }
                            profileBackgroundBitmap.value?.let { btm ->
                                Image(
                                    bitmap = btm.asImageBitmap(),
                                    contentScale = ContentScale.Crop,
                                    contentDescription = null,
                                    modifier = modifier
                                        .fillMaxWidth()
                                        .height(100.dp)
                                        .clickable { backgroundImageLauncher.launch("image/*") },
                                )
                            }
                        }
                    } else {
                        AsyncImage(
                            model = ImageRequest.Builder(context = LocalContext.current).data(
                                if (userViewModel.profileUiState is ProfileUiState.Exist) {
                                    (userViewModel.profileUiState as ProfileUiState.Exist).user.backgroundImage
                                        ?: "https://res.heraldm.com/content/image/2015/06/15/20150615000967_0.jpg"
                                } else {
                                }
                            )
                                .crossfade(true).build(),
                            contentDescription = stringResource(R.string.user_image),
                            modifier = modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .clickable { backgroundImageLauncher.launch("image/*") },
                            placeholder = painterResource(R.drawable.loading_img),
                            error = painterResource(R.drawable.ic_broken_image),
                            alignment = Alignment.Center,
                            contentScale = ContentScale.Crop,
                        )
                    }
                    Row(
                        modifier = modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .height(100.dp)
                    ) {
                        // profile image
                        if (profileImageUri != null) {
                            profileImageUri?.let {
                                if (Build.VERSION.SDK_INT < 28) {
                                    profileBitmap.value = MediaStore.Images
                                        .Media.getBitmap(
                                            context.contentResolver,
                                            it
                                        )   // getBitmap: deprecated
                                } else {
                                    val source = ImageDecoder
                                        .createSource(context.contentResolver, it)
                                    profileBitmap.value = ImageDecoder.decodeBitmap(source)
                                }
                                profileBitmap.value?.let { btm ->
                                    Image(
                                        bitmap = btm.asImageBitmap(),
                                        contentScale = ContentScale.Crop,
                                        contentDescription = null,
                                        modifier = modifier
                                            .size(100.dp)
                                            .clip(CircleShape)
                                            .clickable { profileImageLauncher.launch("image/*") },
                                    )
                                }
                            }
                        } else {
                            AsyncImage(
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .data(
                                        if (userViewModel.profileUiState is ProfileUiState.Exist) {
                                            (userViewModel.profileUiState as ProfileUiState.Exist).user.image
                                                ?: "https://img.animalplanet.co.kr/news/2020/01/13/700/sfu2275cc174s39hi89k.jpg"
                                        } else {
                                        }
                                    )
                                    .crossfade(true).build(),
                                contentDescription = stringResource(R.string.user_image),
                                modifier = modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .clickable { profileImageLauncher.launch("image/*") },
                                placeholder = painterResource(R.drawable.loading_img),
                                error = painterResource(R.drawable.ic_broken_image),
                                contentScale = ContentScale.Crop,
                            )
                        }

                        // email
                        if (userViewModel.profileUiState is ProfileUiState.Exist) {
                            // navigation 중 backstack 으로 보내면서 재실행, state casting 이 정상적으로 되지 않아 fatal error 가 발생. state check 를 넣음
                            Box(
                                modifier = modifier
                                    .fillMaxSize()
                                    .padding(4.dp)
                            ) {
                                Box(
                                    modifier = modifier
                                        .align(Alignment.BottomStart)
                                        .wrapContentSize()
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = (userViewModel.profileUiState as ProfileUiState.Exist).user.email,
                                        modifier = modifier
                                    )
                                }
                            }
                        }
                    }
                }

                // inputFullName
                TextField(
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    label = {
                        Text(
                            text = stringResource(R.string.full_name)
                        )
                    },
                    value = inputFullName,
                    onValueChange = { inputFullName = it },
                    modifier = modifier
                        .padding(bottom = 12.dp)
                        .fillMaxWidth(),
                )

                // inputBio
                TextField(
                    singleLine = false,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
                    label = {
                        Text(
                            text = stringResource(R.string.bio)
                        )
                    },
                    value = inputBio,
                    onValueChange = { inputBio = it },
                    minLines = 3,
                    modifier = modifier
                        .padding(bottom = 12.dp)
                        .fillMaxWidth(),
                )

                // inputWebsite
                TextField(
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    label = {
                        Text(
                            text = stringResource(R.string.website)
                        )
                    },
                    value = inputWebsite,
                    onValueChange = { inputWebsite = it },
                    modifier = modifier
                        .padding(bottom = 12.dp)
                        .fillMaxWidth(),
                )

                // inputEducation
                TextField(
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    label = {
                        Text(
                            text = stringResource(R.string.education)
                        )
                    },
                    value = inputEducation,
                    onValueChange = { inputEducation = it },
                    modifier = modifier
                        .padding(bottom = 12.dp)
                        .fillMaxWidth(),
                )

                // inputEducation
                TextField(
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = {
                        Text(
                            text = stringResource(R.string.birth_date)
                        )
                    },
                    value = inputBirthDate,
                    onValueChange = { inputBirthDate = it },
                    modifier = modifier
                        .padding(bottom = 12.dp)
                        .fillMaxWidth(),
                )
                Spacer(modifier = modifier.height(150.dp))
            }
        }
    }
}