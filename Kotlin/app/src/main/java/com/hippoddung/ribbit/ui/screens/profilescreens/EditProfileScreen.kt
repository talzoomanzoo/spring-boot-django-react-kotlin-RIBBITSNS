package com.hippoddung.ribbit.ui.screens.profilescreens

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
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.network.bodys.User
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.screens.statescreens.ErrorScreen
import com.hippoddung.ribbit.ui.screens.statescreens.LoadingScreen
import com.hippoddung.ribbit.ui.viewmodel.EditingProfileUiState
import com.hippoddung.ribbit.ui.viewmodel.GetAiImageUrlUiState
import com.hippoddung.ribbit.ui.viewmodel.ProfileUiState
import com.hippoddung.ribbit.ui.viewmodel.ReplyClickedUiState
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel
import kotlinx.coroutines.runBlocking

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

@SuppressLint("StateFlowValueCalledInComposition")
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
    var inputFullName by remember { mutableStateOf(myProfile.fullName) }
    var inputBio by remember { mutableStateOf(myProfile.bio ?: "") }
    var inputWebsite by remember { mutableStateOf(myProfile.website ?: "") }
    var inputEducation by remember { mutableStateOf(myProfile.education ?: "") }
    var inputBirthDate by remember { mutableStateOf(myProfile.birthDate ?: "") }

    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    var profileBitmap by remember { mutableStateOf<Bitmap?>(null) }

    var profileBackgroundImageUri by remember { mutableStateOf<Uri?>(null) }
    var profileBackgroundBitmap by remember { mutableStateOf<Bitmap?>(null) }

    var profileImageIsClicked by remember { mutableStateOf(false) }
    var profileBackgroundImageIsClicked by remember { mutableStateOf(false) }

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
                profileImage = profileBitmap,
                profileBackgroundImage = profileBackgroundBitmap,
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
                        profileImage = profileBitmap,
                        profileBackgroundImage = profileBackgroundBitmap
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
                                profileBackgroundBitmap =
                                    MediaStore.Images.Media.getBitmap(
                                        context.contentResolver,
                                        it
                                    )   // getBitmap: deprecated
                            } else {
                                val source = ImageDecoder
                                    .createSource(context.contentResolver, it)
                                profileBackgroundBitmap =
                                    ImageDecoder.decodeBitmap(source)
                            }
                        }
                    }
                    if (profileBackgroundBitmap != null) {
                        profileBackgroundBitmap?.let { btm ->
                            Image(
                                bitmap = btm.asImageBitmap(),
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                                modifier = modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .clickable { profileBackgroundImageIsClicked = true },
                            )
                        }
                    } else {
                        if (myProfile.backgroundImage != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(context = LocalContext.current).data(
                                    myProfile.backgroundImage
//                                ?: "https://res.heraldm.com/content/image/2015/06/15/20150615000967_0.jpg"
                                )
                                    .crossfade(true).build(),
                                contentDescription = stringResource(R.string.user_image),
                                modifier = modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .clickable { profileBackgroundImageIsClicked = true },

                                placeholder = painterResource(R.drawable.loading_img),
                                error = painterResource(R.drawable.ic_broken_image),
                                alignment = Alignment.TopStart,
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.pond),
                                contentDescription = "default profile background image",
                                modifier = modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .clickable { profileBackgroundImageIsClicked = true },
                                alignment = Alignment.Center,
                                contentScale = ContentScale.Crop
                            )
                        }
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
                                    profileBitmap = MediaStore.Images
                                        .Media.getBitmap(
                                            context.contentResolver,
                                            it
                                        )   // getBitmap: deprecated
                                } else {
                                    val source = ImageDecoder
                                        .createSource(context.contentResolver, it)
                                    profileBitmap = ImageDecoder.decodeBitmap(source)
                                }
                            }
                        }
                        if (profileBitmap != null) {
                            profileBitmap?.let { btm ->
                                Image(
                                    bitmap = btm.asImageBitmap(),
                                    contentScale = ContentScale.Crop,
                                    contentDescription = null,
                                    modifier = modifier
                                        .size(100.dp)
                                        .clip(CircleShape)
                                        .clickable { profileImageIsClicked = true },
                                )
                            }
                        } else {
                            if (myProfile.image != null) {
                                AsyncImage(
                                    model = ImageRequest.Builder(context = LocalContext.current)
                                        .data(
                                            myProfile.image
//                                        ?: "https://img.animalplanet.co.kr/news/2020/01/13/700/sfu2275cc174s39hi89k.jpg"

                                        )
                                        .crossfade(true).build(),
                                    contentDescription = stringResource(R.string.user_image),
                                    modifier = modifier
                                        .size(100.dp)
                                        .clip(CircleShape)
                                        .clickable { profileImageIsClicked = true },
                                    placeholder = painterResource(R.drawable.loading_img),
                                    error = painterResource(R.drawable.ic_broken_image),
                                    contentScale = ContentScale.Crop,
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.frog_8341850_1280),
                                    contentDescription = "default profile image",
                                    modifier = modifier
                                        .size(100.dp)
                                        .clip(CircleShape)
                                        .clickable { profileImageIsClicked = true },
                                    contentScale = ContentScale.Crop
                                )
                            }
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
            if (profileBackgroundImageIsClicked) {
                Dialog(onDismissRequest = { profileBackgroundImageIsClicked = false }) {
                    var aiImageIsClicked by remember { mutableStateOf(false) }
                    if (!aiImageIsClicked) {
                        // 이미지 생성방식 선택 dialog
                        Column(
                            modifier = modifier
                                .background(
                                    color = MaterialTheme.colorScheme.background,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .size(300.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = ("How would you like to create the background image?"),
                                fontSize = 16.sp,
                                modifier = modifier
                                    .padding(start = 28.dp, top = 28.dp, end = 0.dp, bottom = 0.dp)
                                    .align(alignment = Alignment.Start)
                            )
                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 28.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Button(
                                    onClick = {
                                        aiImageIsClicked = true
                                    },
                                    modifier = modifier
                                ) {
                                    Text(
                                        text = "AI Image",
                                        modifier = modifier
                                    )
                                }
                                Button(
                                    onClick = {
                                        backgroundImageLauncher.launch("image/*")
                                    },
                                    modifier = modifier
                                ) {
                                    Text(
                                        text = "your phone",
                                        modifier = modifier
                                    )
                                }
                            }
                        }
                    } else {
                        // ai background image 생성 dialog
                        when (userViewModel.getAiImageUiState) {
                            is GetAiImageUrlUiState.Error -> {}
                            is GetAiImageUrlUiState.Loading -> {
                                Column(modifier = modifier) {
                                    Text(text = "Loading your AI Image", modifier = modifier)
                                    Image(
                                        modifier = modifier.size(200.dp),
                                        painter = painterResource(R.drawable.loading_img),
                                        contentDescription = stringResource(id = R.string.loading)
                                    )
                                }
                            }

                            is GetAiImageUrlUiState.Success -> {
                                profileBackgroundBitmap =
                                    (userViewModel.getAiImageUiState as GetAiImageUrlUiState.Success).aiImageBitmap
                                userViewModel.getAiImageUiState = GetAiImageUrlUiState.Ready
                                profileBackgroundImageIsClicked = false
                            }

                            is GetAiImageUrlUiState.Ready -> {
                                var inputAiKeywords by remember { mutableStateOf("") }
                                Column(
                                    modifier = modifier
                                        .background(
                                            color = MaterialTheme.colorScheme.background,
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = ("Input keywords to create AI image"),
                                        fontSize = 16.sp,
                                        modifier = modifier
                                            .padding(
                                                start = 28.dp,
                                                top = 28.dp,
                                                end = 0.dp,
                                                bottom = 0.dp
                                            )
                                            .align(alignment = Alignment.Start)
                                    )
                                    TextField(
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                        label = {
                                            Text(
                                                text = stringResource(R.string.keyword)
                                            )
                                        },
                                        value = inputAiKeywords,
                                        onValueChange = { inputAiKeywords = it },
                                        modifier = modifier
                                            .padding(bottom = 12.dp)
                                            .fillMaxWidth(),
                                    )
                                    Row(
                                        modifier = modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight()
                                            .padding(bottom = 28.dp),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        Button(
                                            onClick = {
                                                aiImageIsClicked = false
                                            },
                                            modifier = modifier
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.cancel),
                                                modifier = modifier
                                            )
                                        }
                                        Button(
                                            onClick = {
                                                // ai 그림 생성 통신
                                                userViewModel.getAiImage(inputAiKeywords)

                                            },
                                            modifier = modifier
                                        ) {
                                            Text(
                                                text = "Request",
                                                modifier = modifier
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (profileImageIsClicked) {
                Dialog(onDismissRequest = { profileImageIsClicked = false }) {
                    var aiImageIsClicked by remember { mutableStateOf(false) }
                    if (!aiImageIsClicked) {
                        // 이미지 생성방식 선택 dialog
                        Column(
                            modifier = modifier
                                .background(
                                    color = MaterialTheme.colorScheme.background,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .size(300.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = ("How would you like to create the background image?"),
                                fontSize = 16.sp,
                                modifier = modifier
                                    .padding(start = 28.dp, top = 28.dp, end = 0.dp, bottom = 0.dp)
                                    .align(alignment = Alignment.Start)
                            )
                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 28.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Button(
                                    onClick = {
                                        aiImageIsClicked = true
                                    },
                                    modifier = modifier
                                ) {
                                    Text(
                                        text = "AI Image",
                                        modifier = modifier
                                    )
                                }
                                Button(
                                    onClick = {
                                        profileImageLauncher.launch("image/*")
                                        profileImageIsClicked = false
                                    },
                                    modifier = modifier
                                ) {
                                    Text(
                                        text = "your phone",
                                        modifier = modifier
                                    )
                                }
                            }
                        }
                    } else {
                        // ai image 생성 dialog
                        when (userViewModel.getAiImageUiState) {
                            is GetAiImageUrlUiState.Error -> {}
                            is GetAiImageUrlUiState.Loading -> {
                                Column(modifier = modifier) {
                                    Text(text = "Loading your AI Image", modifier = modifier)
                                    Image(
                                        modifier = modifier.size(200.dp),
                                        painter = painterResource(R.drawable.loading_img),
                                        contentDescription = stringResource(id = R.string.loading)
                                    )
                                }
                            }

                            is GetAiImageUrlUiState.Success -> {
                                profileBitmap =
                                    (userViewModel.getAiImageUiState as GetAiImageUrlUiState.Success).aiImageBitmap
                                userViewModel.getAiImageUiState = GetAiImageUrlUiState.Ready
                                profileImageIsClicked = false
                            }

                            is GetAiImageUrlUiState.Ready -> {
                                var inputAiKeywords by remember { mutableStateOf("") }
                                Column(
                                    modifier = modifier
                                        .background(
                                            color = MaterialTheme.colorScheme.background,
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = ("Input keywords to create AI image"),
                                        fontSize = 16.sp,
                                        modifier = modifier
                                            .padding(
                                                start = 28.dp,
                                                top = 28.dp,
                                                end = 0.dp,
                                                bottom = 0.dp
                                            )
                                            .align(alignment = Alignment.Start)
                                    )
                                    TextField(
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                        label = {
                                            Text(
                                                text = stringResource(R.string.keyword)
                                            )
                                        },
                                        value = inputAiKeywords,
                                        onValueChange = { inputAiKeywords = it },
                                        modifier = modifier
                                            .padding(bottom = 12.dp)
                                            .fillMaxWidth(),
                                    )
                                    Row(
                                        modifier = modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 28.dp),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        Button(
                                            onClick = {
                                                aiImageIsClicked = false
                                            },
                                            modifier = modifier
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.cancel),
                                                modifier = modifier
                                            )
                                        }
                                        Button(
                                            onClick = {
                                                // ai 그림 생성 통신
                                                userViewModel.getAiImage(inputAiKeywords)

                                            },
                                            modifier = modifier
                                        ) {
                                            Text(
                                                text = "Request",
                                                modifier = modifier
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}