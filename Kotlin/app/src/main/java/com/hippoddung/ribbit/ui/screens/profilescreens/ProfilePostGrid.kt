package com.hippoddung.ribbit.ui.screens.profilescreens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.screens.carditems.RibbitCard
import com.hippoddung.ribbit.ui.theme.Shapes
import com.hippoddung.ribbit.ui.viewmodel.CardViewModel
import com.hippoddung.ribbit.ui.viewmodel.ProfileUiState
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState")
@Composable
fun ProfilePostsGrid(
    posts: List<RibbitPost>,
    cardViewModel: CardViewModel,
    userViewModel: UserViewModel,
    myId: Int,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val comparator = compareByDescending<RibbitPost> { it.id }
    var sortedRibbitPost = remember(posts, comparator) {
        posts.sortedWith(comparator)
    }   // LazyColumn items에 List를 바로 주는 것이 아니라 Comparator로 정렬하여 remember로 기억시켜서 recomposition을 방지하여 성능을 올린다.
    LazyColumn(modifier = modifier) {
        item {
            Column(modifier = modifier) {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(150.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current).data(
                            //                                userViewModel.user.value?.backgroundImage
                            "https://res.heraldm.com/content/image/2015/06/15/20150615000967_0.jpg"
                        )
                            .crossfade(true).build(),
                        contentDescription = stringResource(R.string.user_image),
                        modifier = modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        placeholder = painterResource(R.drawable.loading_img),
                        error = painterResource(R.drawable.ic_broken_image),
                        alignment = Alignment.TopStart,
                        contentScale = ContentScale.Crop,

                        )
                    Row(
                        modifier = modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .height(100.dp)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context = LocalContext.current).data(
                                //                                userViewModel.user.value?.image
                                "https://img.animalplanet.co.kr/news/2020/01/13/700/sfu2275cc174s39hi89k.jpg"
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
                        if (userViewModel.profileUiState is ProfileUiState.Exist) {
                            // navigation 중 backstack으로 보내면서 재실행, state casting이 정상적으로 되지 않아 fatal error가 발생
                            // state check를 넣음
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
                                if ((userViewModel.profileUiState as ProfileUiState.Exist).user.id == userViewModel.myProfile.value?.id) {
                                    OutlinedButton(
                                        onClick = {
                                            userViewModel.myProfile.value?.id?.let {
                                                cardViewModel.getUserIdPosts(
                                                    userId = it
                                                )
                                            }   // userViewModel의 user가 없는 경우 접근 자체가 불가능
                                            navController.navigate(RibbitScreen.ProfileScreen.name)
                                        },
                                        modifier = modifier.align(Alignment.BottomEnd),
                                    ) {
                                        Text(
                                            text = "Edit Profile",
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
                Spacer(modifier = modifier.height(20.dp))
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            userViewModel.myProfile.value?.id?.let {
                                cardViewModel.getUserIdPosts(
                                    userId = it
                                )
                            }   // userViewModel의 user가 없는 경우 접근 자체가 불가능
                            navController.navigate(RibbitScreen.ProfileScreen.name)
                        },
                        modifier = modifier
                    ) {
                        Text(
                            text = "Ribbit",
                            color = Color(0xFF006400),
                            fontSize = 20.sp,
                            modifier = modifier
                        )
                    }
                    TextButton(
                        onClick = {
                            userViewModel.myProfile.value?.id?.let {
                                cardViewModel.getUserIdReplies(
                                    userId = it
                                )
                            }   // userViewModel의 user가 없는 경우 접근 자체가 불가능
                            navController.navigate(RibbitScreen.ProfileRepliesScreen.name)
                        },
                        modifier = modifier
                    ) {
                        Text(
                            text = "Rebbit",
                            color = Color(0xFF006400),
                            fontSize = 20.sp,
                            modifier = modifier
                        )
                    }
                    TextButton(
                        onClick = {
                            // 서버에서 해당 컨트롤러가 없어서 안드로이드 자체 구현
                            navController.navigate(RibbitScreen.ProfileMediasScreen.name)
                        },
                        modifier = modifier
                    ) {
                        Text(
                            text = "Media",
                            color = Color(0xFF006400),
                            fontSize = 20.sp,
                            modifier = modifier
                        )
                    }
                    TextButton(
                        onClick = {
                            userViewModel.myProfile.value?.id?.let {
                                cardViewModel.getUserIdLikes(
                                    userId = it
                                )
                            }   // userViewModel의 user가 없는 경우 접근 자체가 불가능
                            navController.navigate(RibbitScreen.ProfileLikesScreen.name)
                        },
                        modifier = modifier
                    ) {
                        Text(
                            text = "Likes",
                            color = Color(0xFF006400),
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
        items(items = sortedRibbitPost, key = { post -> post.id }) { it ->
            RibbitCard(
                post = it,
                cardViewModel = cardViewModel,
                myId = myId,
                navController = navController,
                userViewModel = userViewModel,
                modifier = modifier
            )
        }
    }
}