package com.hippoddung.ribbit.ui.screens.profilescreens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.network.bodys.User
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.screens.carditems.RibbitCard
import com.hippoddung.ribbit.ui.screens.searchitems.SearchedUserCard
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.PostingViewModel
import com.hippoddung.ribbit.ui.viewmodel.ProfileUiState
import com.hippoddung.ribbit.ui.viewmodel.TokenViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserIdClassificationUiState
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel
import kotlinx.coroutines.runBlocking

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState")
@Composable
fun ProfilePostsGrid(
    posts: List<RibbitPost>,
    getCardViewModel: GetCardViewModel,
    postingViewModel: PostingViewModel,
    userViewModel: UserViewModel,
    authViewModel: AuthViewModel,
    tokenViewModel: TokenViewModel,
    myId: Int,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val comparator by remember { mutableStateOf(compareByDescending<RibbitPost> { it.id }) }
    val sortedRibbitPost = remember(posts, comparator) {
        posts.sortedWith(comparator)
    }   // LazyColumn items 에 List 를 바로 주는 것이 아니라 Comparator 로 정렬하여 remember 로 기억시켜서 recomposition 을 방지하여 성능을 올린다.
    val profileUser by remember {
        if (userViewModel.profileUiState is ProfileUiState.Exist) {
            // navigation 중 backstack 으로 보내면서 재실행, state casting 이 정상적으로 되지 않아 fatal error 가 발생
            // state check 를 넣음
            mutableStateOf((userViewModel.profileUiState as ProfileUiState.Exist).user)
        } else {
            mutableStateOf(User())
        }
    }
    var withdrawalIsClicked by remember { mutableStateOf(false) }
    var followingsIsClicked by remember { mutableStateOf(false) }
    var followersIsClicked by remember { mutableStateOf(false) }
    LazyColumn(modifier = modifier) {
        item {
            Column(modifier = modifier) {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(150.dp)
                ) {
                    // profile background image
                    if (profileUser.backgroundImage == null) {
                        Image(
                            painter = painterResource(id = R.drawable.pond),
                            contentDescription = "default profile background image",
                            contentScale = ContentScale.Crop,
                            modifier = modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            alignment = Alignment.TopStart,
                        )
                    } else {
                        AsyncImage(
                            model = ImageRequest.Builder(context = LocalContext.current).data(
                                profileUser.backgroundImage
//                                ?: "https://res.heraldm.com/content/image/2015/06/15/20150615000967_0.jpg"

                            )
                                .crossfade(true).build(),
                            contentDescription = stringResource(R.string.user_image),
                            modifier = modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            placeholder = painterResource(R.drawable.loading_img),
                            error = painterResource(R.drawable.ic_broken_image),
                            alignment = Alignment.TopStart,
                            contentScale = ContentScale.Crop
                        )
                    }
                    // withdrawal button
                    if (profileUser.id == userViewModel.myProfile.value?.id) {
                        OutlinedButton(
                            onClick = {
                                withdrawalIsClicked = true
                            },
                            modifier = modifier.align(Alignment.TopEnd)
                        ) {
                            Text(
                                text = "Withdrawal",
                                color = Color(0xFF006400),
                                fontSize = 14.sp,
                                modifier = modifier
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
                        if (profileUser.image == null) {
                            Image(
                                painter = painterResource(id = R.drawable.frog_8341850_1280),
                                contentDescription = "default image",
                                contentScale = ContentScale.Crop,
                                modifier = modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                            )
                        } else {
                            AsyncImage(
                                model = ImageRequest.Builder(context = LocalContext.current).data(
                                    profileUser.image
//                                        ?: "https://img.animalplanet.co.kr/news/2020/01/13/700/sfu2275cc174s39hi89k.jpg"

                                )
                                    .crossfade(true).build(),
                                contentDescription = stringResource(R.string.user_image),
                                modifier = modifier
                                    .size(100.dp)
                                    .clip(CircleShape),
                                placeholder = painterResource(R.drawable.loading_img),
                                error = painterResource(R.drawable.ic_broken_image),
                                contentScale = ContentScale.Crop,
                            )
                        }
                        Box(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(start = 4.dp, bottom = 0.dp)
                        ) {
                            Column(
                                modifier = modifier
                                    .align(Alignment.BottomStart)
                                    .wrapContentSize()
                                    .padding(8.dp),
                            ) {
                                Log.d("HippoLog, ProfilePostGrid", "${profileUser.joinedAt}")
                                Text(
                                    text = "${
                                        profileUser.joinedAt?.substring(0, 10)
                                    } 에 가입",
                                    modifier = modifier
                                )
                            }
                            // 접근 Id와 프로필 Id가 일치할 경우 프로필수정버튼 활성화
                            if (profileUser.id == userViewModel.myProfile.value?.id) {
                                OutlinedButton(
                                    onClick = {
                                        navController.navigate(RibbitScreen.EditProfileScreen.name)
                                    },
                                    modifier = modifier.align(Alignment.BottomEnd)
                                ) {
                                    Text(
                                        text = "Edit Profile",
                                        color = Color(0xFF006400),
                                        fontSize = 14.sp,
                                        modifier = modifier
                                    )
                                }
                            } else {    // 접근 Id와 프로필 Id가 다를 경우 follow, unFollow 버튼 활성화
                                val isFollowed by remember { mutableStateOf(profileUser.followed) }
                                var followed by remember { mutableStateOf(isFollowed) }
                                if (!followed) {
                                    OutlinedButton(
                                        onClick = {
                                            profileUser.id?.let { userViewModel.putUserIdFollow(it) }
                                            followed = true
                                        },
                                        modifier = modifier.align(Alignment.BottomEnd)
                                    ) {
                                        Text(
                                            text = "Follow",
                                            color = Color(0xFF006400),
                                            fontSize = 14.sp,
                                            modifier = modifier
                                        )
                                    }
                                } else {
                                    OutlinedButton(
                                        onClick = {
                                            profileUser.id?.let { userViewModel.putUserIdFollow(it) }
                                            followed = false
                                        },
                                        modifier = modifier.align(Alignment.BottomEnd)
                                    ) {
                                        Text(
                                            text = "Unfollow",
                                            color = Color(0xFF006400),
                                            fontSize = 14.sp,
                                            modifier = modifier
                                        )
                                    }
                                }

                            }
                        }

                    }
                }
                Row(modifier = modifier) {
                    Text(
                        text = profileUser.fullName,
                        modifier = modifier
                            .padding(horizontal = 12.dp)
                    )
                    Text(
                        text = profileUser.email,
                        modifier = modifier
                            .padding(horizontal = 12.dp)
                    )
                }
                Row(
                    modifier = modifier
                        .padding(12.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "${profileUser.followings?.size ?: 0} followings",
                        modifier = modifier
                            .padding(start = 12.dp, end = 12.dp)
                            .clickable {
                                followingsIsClicked = true
                            }
                    )
                    Text(
                        text = "${profileUser.followers?.size ?: 0} followers",
                        modifier = modifier
                            .padding(start = 12.dp, end = 12.dp)
                            .clickable {
                                followersIsClicked = true
                            }
                    )
                }
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            userViewModel.myProfile.value?.id?.let {
                                getCardViewModel.getUserIdPosts(
                                    userId = it
                                )
                            }   // userViewModel 의 user 가 없는 경우 접근 자체가 불가능
                        },
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = if (getCardViewModel.userIdClassificationUiState is UserIdClassificationUiState.Ribbit) {
                                Color(0xFF006400)
                            } else Color.White,
                            contentColor = if (getCardViewModel.userIdClassificationUiState is UserIdClassificationUiState.Ribbit) {
                                Color.White
                            } else Color(0xFF006400),
                        ),
                        modifier = modifier
                    ) {
                        Text(
                            text = "Ribbit",
                            fontSize = 20.sp,
                            modifier = modifier
                        )
                    }
                    TextButton(
                        onClick = {
                            userViewModel.myProfile.value?.id?.let {
                                getCardViewModel.getUserIdReplies(
                                    userId = it
                                )
                            }   // userViewModel 의 user 가 없는 경우 접근 자체가 불가능
                        },
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = if (getCardViewModel.userIdClassificationUiState is UserIdClassificationUiState.Replies) {
                                Color(0xFF006400)
                            } else Color.White,
                            contentColor = if (getCardViewModel.userIdClassificationUiState is UserIdClassificationUiState.Replies) {
                                Color.White
                            } else Color(0xFF006400),
                        ),
                        modifier = modifier
                    ) {
                        Text(
                            text = "Replies",
                            fontSize = 20.sp,
                            modifier = modifier
                        )
                    }
                    TextButton(
                        onClick = {
                            userViewModel.myProfile.value?.id?.let {
                                getCardViewModel.getUserIdMedias(
                                    userId = it
                                )
                            }
                            // Media 의 경우 서버에서 해당 컨트롤러가 없어서 안드로이드 자체 구현
                            // getCardViewModel.getUserIdPosts 함수에 자체적으로 UiState 를 업데이트하는 함수가 포함되어 있어서
                            // state 관리가 어려운 점이 있어 같은 통신을 쓰지만 uiState 만 다르게 한 getCardViewModel.getUserIdMedias 함수를 만들어서 사용
                        },
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = if (getCardViewModel.userIdClassificationUiState is UserIdClassificationUiState.Media) {
                                Color(0xFF006400)
                            } else Color.White,
                            contentColor = if (getCardViewModel.userIdClassificationUiState is UserIdClassificationUiState.Media) {
                                Color.White
                            } else Color(0xFF006400),
                        ),
                        modifier = modifier
                    ) {
                        Text(
                            text = "Media",
                            fontSize = 20.sp,
                            modifier = modifier
                        )
                    }
                    TextButton(
                        onClick = {
                            userViewModel.myProfile.value?.id?.let {
                                getCardViewModel.getUserIdLikes(
                                    userId = it
                                )
                            }   // userViewModel 의 user 가 없는 경우 접근 자체가 불가능
                        },
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = if (getCardViewModel.userIdClassificationUiState is UserIdClassificationUiState.Likes) {
                                Color(0xFF006400)
                            } else Color.White,
                            contentColor = if (getCardViewModel.userIdClassificationUiState is UserIdClassificationUiState.Likes) {
                                Color.White
                            } else Color(0xFF006400),
                        ),
                        modifier = modifier
                    ) {
                        Text(
                            text = "Likes",
                            fontSize = 20.sp,
                            modifier = modifier
                        )
                    }
                }
                Canvas(
                    modifier = modifier,
                    onDraw = {
                        drawLine(
                            color = Color(0xFF4c6c4a),
                            start = Offset(0.dp.toPx(), 0.dp.toPx()),
                            end = Offset(500.dp.toPx(), 0.dp.toPx()),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                )
            }
        }
        items(items = sortedRibbitPost, key = { post -> post.id }) {
            RibbitCard(
                post = it,
                getCardViewModel = getCardViewModel,
                postingViewModel = postingViewModel,
                myId = myId,
                navController = navController,
                userViewModel = userViewModel,
                modifier = modifier
            )
        }
    }
    if (withdrawalIsClicked) {
        AlertDialog(
            onDismissRequest = { withdrawalIsClicked = false },
            confirmButton = {
                OutlinedButton(
                    onClick = {
                        withdrawalIsClicked = false
                        runBlocking {
                            Log.d("HippoLog, HomeTopAppBar", "LogOut")
                            userViewModel.postWithdrawal()  // 서버에 탈퇴 요청
                            userViewModel.resetMyProfile()   // 유저 정보 리셋
                            authViewModel.deleteLoginInfo() // 로그인 정보 삭제
                            tokenViewModel.deleteToken()    // 토큰 정보 삭제. token 을 먼저 지우면 다시 로그인 됨
                        }
                    },
                    content = {
                        Text(
                            text = "Withdraw",
                            color = Color(0xFF006400),
                            fontSize = 14.sp,
                            modifier = modifier
                        )
                    },
                    modifier = modifier,
                )
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { withdrawalIsClicked = false },
                    content = {
                        Text(
                            text = "Cancel",
                            color = Color(0xFF006400),
                            fontSize = 14.sp,
                            modifier = modifier
                        )
                    },
                    modifier = modifier
                )
            },
            text = {
                Text(
                    text = "Are you really going to withdraw your account?",
                    modifier = modifier
                )
            },
            modifier = modifier
        )
    }
    if (followingsIsClicked) {
        Dialog(
            onDismissRequest = {
                followingsIsClicked = false
            }
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!profileUser.followings.isNullOrEmpty()) {
                    profileUser.followings?.forEach { user ->
                        SearchedUserCard(
                            isExpanded = mutableStateOf(true),
                            user = user!!,  // 위에서 null check 함
                            getCardViewModel = getCardViewModel,
                            navController = navController,
                            userViewModel = userViewModel,
                            modifier = modifier
                        )
                    }
                } else {
                    Text(text = "There is no followings", modifier = modifier)
                }
            }
        }
    }
    if (followersIsClicked) {
        Dialog(
            onDismissRequest = {
                followersIsClicked = false
            }
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!profileUser.followers.isNullOrEmpty()) {
                    profileUser.followers?.forEach { user ->
                        SearchedUserCard(
                            isExpanded = mutableStateOf(true),
                            user = user!!,
                            getCardViewModel = getCardViewModel,
                            navController = navController,
                            userViewModel = userViewModel,
                            modifier = modifier
                        )
                    }
                } else {
                    Text(text = "There is no followers", modifier = modifier)
                }
            }
        }
    }
}