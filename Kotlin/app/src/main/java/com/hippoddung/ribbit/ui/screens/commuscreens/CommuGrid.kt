package com.hippoddung.ribbit.ui.screens.commuscreens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.network.bodys.RibbitCommuItem
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.ui.screens.carditems.RibbitCard
import com.hippoddung.ribbit.ui.screens.statescreens.ErrorScreen
import com.hippoddung.ribbit.ui.screens.statescreens.LoadingScreen
import com.hippoddung.ribbit.ui.viewmodel.AnalyzingPostEthicUiState
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.CommuClassificationUiState
import com.hippoddung.ribbit.ui.viewmodel.CommuViewModel
import com.hippoddung.ribbit.ui.viewmodel.GetAllCommuPostsUiState
import com.hippoddung.ribbit.ui.viewmodel.PostingViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState", "MutableCollectionMutableState")
@Composable
fun CommuGrid(
    myId: Int,
    filteredCommuItems: List<RibbitCommuItem>,
    getCardViewModel: GetCardViewModel,
    postingViewModel: PostingViewModel,
    userViewModel: UserViewModel,
    commuViewModel: CommuViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var posts by remember { mutableStateOf(listOf<RibbitPost>()) }
    when (getCardViewModel.getAllCommuPostsUiState) {
        is GetAllCommuPostsUiState.Success -> {
            posts =
                (getCardViewModel.getAllCommuPostsUiState as GetAllCommuPostsUiState.Success).posts
            Log.d("HippoLog, CommuGrid", "posts: $posts")
        }

        is GetAllCommuPostsUiState.Loading -> {
            LoadingScreen(modifier = modifier)
        }

        is GetAllCommuPostsUiState.Error -> {
            ErrorScreen(modifier = modifier)
        }
    }
    val postsComparator = compareByDescending<RibbitPost> { it.id }

    var sortedRibbitPost by remember(posts) {
        mutableStateOf(posts.toMutableList().apply {
            sortedWith(postsComparator)
        })
    }   // LazyColumn items 에 List 를 바로 주는 것이 아니라 Comparator 로 정렬하여 remember 로 기억시켜서 recomposition 을 방지하여 성능을 올린다.
    LaunchedEffect(postingViewModel.analyzingPostEthicUiState, Unit) {
        if (postingViewModel.analyzingPostEthicUiState is AnalyzingPostEthicUiState.Success) {
            sortedRibbitPost = sortedRibbitPost.toMutableList().apply {
                if (this.size != 0) {
                    this[0] = this[0].copy(
                        ethiclabel = (postingViewModel.analyzingPostEthicUiState as AnalyzingPostEthicUiState.Success).post.ethiclabel,
                        ethicrateMAX = (postingViewModel.analyzingPostEthicUiState as AnalyzingPostEthicUiState.Success).post.ethicrateMAX
                    )
                }
            }
        }
    }

    val comparator = compareByDescending<RibbitCommuItem> { it.id }

    val sortedRibbitCommuItem = remember(filteredCommuItems, comparator) {
        filteredCommuItems.sortedWith(comparator)
    }   // LazyColumn items 에 Commu 를 바로 주는 것이 아니라 Comparator 로 정렬하여 remember 로 기억시켜서 recomposition 을 방지하여 성능을 올린다.
    LazyColumn(modifier = modifier) {
        item {
            Column(modifier = modifier) {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            commuViewModel.commuClassificationUiState =
                                CommuClassificationUiState.AllCommuPosts
                        },
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = if (commuViewModel.commuClassificationUiState is CommuClassificationUiState.AllCommuPosts) {
                                Color(0xFF006400)
                            } else Color.White,
                            contentColor = if (commuViewModel.commuClassificationUiState is CommuClassificationUiState.AllCommuPosts) {
                                Color.White
                            } else Color(0xFF006400),
                        ),
                        modifier = modifier
                    ) {
                        Text(
                            text = "All Commu Posts",
                            fontSize = 16.sp,
                            modifier = modifier
                        )
                    }
                    TextButton(
                        onClick = {
                            commuViewModel.commuClassificationUiState =
                                CommuClassificationUiState.PublicCommu
                        },
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = if (commuViewModel.commuClassificationUiState is CommuClassificationUiState.PublicCommu) {
                                Color(0xFF006400)
                            } else Color.White,
                            contentColor = if (commuViewModel.commuClassificationUiState is CommuClassificationUiState.PublicCommu) {
                                Color.White
                            } else Color(0xFF006400),
                        ),
                        modifier = modifier
                    ) {
                        Text(
                            text = "Public",
                            fontSize = 16.sp,
                            modifier = modifier
                        )
                    }
                    TextButton(
                        onClick = {
                            commuViewModel.commuClassificationUiState =
                                CommuClassificationUiState.PrivateCommu
                        },
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = if (commuViewModel.commuClassificationUiState is CommuClassificationUiState.PrivateCommu) {
                                Color(0xFF006400)
                            } else Color.White,
                            contentColor = if (commuViewModel.commuClassificationUiState is CommuClassificationUiState.PrivateCommu) {
                                Color.White
                            } else Color(0xFF006400),
                        ),
                        modifier = modifier
                    ) {
                        Text(
                            text = "Private",
                            fontSize = 16.sp,
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
        if (commuViewModel.commuClassificationUiState is CommuClassificationUiState.AllCommuPosts) {
            Log.d("HippoLog, CommuGrid", "AllCommuPosts: $sortedRibbitPost")
            itemsIndexed(items = sortedRibbitPost) { index, post ->
                RibbitCard(
                    post = post,
                    getCardViewModel = getCardViewModel,
                    postingViewModel = postingViewModel,
                    myId = myId,
                    navController = navController,
                    userViewModel = userViewModel,
                    onPostModified = { modifiedPost ->
                        sortedRibbitPost[index] = modifiedPost
                    },
                    modifier = modifier
                )
            }
        } else {
            items(
                items = sortedRibbitCommuItem,
                key = { commuItem -> commuItem.id!! }) {   // commuItem 을 받아오면 id 는 반드시 있음.
                CommuCard(
                    myId = myId,
                    commuItem = it,
                    commuViewModel = commuViewModel,
                    getCardViewModel = getCardViewModel,
                    navController = navController,
                    modifier = modifier
                )
            }
        }
    }
}