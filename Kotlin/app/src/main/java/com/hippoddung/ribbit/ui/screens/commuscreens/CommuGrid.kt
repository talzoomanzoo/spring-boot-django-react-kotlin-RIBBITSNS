package com.hippoddung.ribbit.ui.screens.commuscreens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.CommuClassificationUiState
import com.hippoddung.ribbit.ui.viewmodel.CommuViewModel
import com.hippoddung.ribbit.ui.viewmodel.GetAllCommuPostsUiState
import com.hippoddung.ribbit.ui.viewmodel.GetCommuIdPostsUiState
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState")
@Composable
fun CommuGrid(
    myId: Int,
    filteredCommuItems: List<RibbitCommuItem>,
    getCardViewModel: GetCardViewModel,
    userViewModel: UserViewModel,
    commuViewModel: CommuViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var posts by remember { mutableStateOf(listOf<RibbitPost>()) }
    if (getCardViewModel.getAllCommuPostsUiState is GetAllCommuPostsUiState.Success) {   // 원래 state 에 따라 넘어오기 때문에 확인할 필요가 없으나 state 에 무관하게 내려오는 문제가 있어 여기서 재확인
        posts = (getCardViewModel.getAllCommuPostsUiState as GetAllCommuPostsUiState.Success).posts
    }
    val postsComparator = compareByDescending<RibbitPost> { it.id }

    val sortedRibbitPost = remember(posts, postsComparator) {
        posts.sortedWith(postsComparator)
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
                            commuViewModel.commuClassificationUiState = CommuClassificationUiState.AllCommuPosts
                        },
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = if(commuViewModel.commuClassificationUiState is CommuClassificationUiState.AllCommuPosts){
                                Color(0xFF006400)
                            } else Color.White,
                            contentColor = if(commuViewModel.commuClassificationUiState is CommuClassificationUiState.AllCommuPosts){
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
                            commuViewModel.commuClassificationUiState = CommuClassificationUiState.PublicCommu
                        },
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = if(commuViewModel.commuClassificationUiState is CommuClassificationUiState.PublicCommu){
                                Color(0xFF006400)
                            } else Color.White,
                            contentColor = if(commuViewModel.commuClassificationUiState is CommuClassificationUiState.PublicCommu){
                                Color.White
                            } else Color(0xFF006400),
                        ),
                        modifier = modifier
                    ) {
                        Text(
                            text = "Public Commu",
                            fontSize = 16.sp,
                            modifier = modifier
                        )
                    }
                    TextButton(
                        onClick = {
                            commuViewModel.commuClassificationUiState = CommuClassificationUiState.PrivateCommu
                        },
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = if(commuViewModel.commuClassificationUiState is CommuClassificationUiState.PrivateCommu){
                                Color(0xFF006400)
                            } else Color.White,
                            contentColor = if(commuViewModel.commuClassificationUiState is CommuClassificationUiState.PrivateCommu){
                                Color.White
                            } else Color(0xFF006400),
                        ),
                        modifier = modifier
                    ) {
                        Text(
                            text = "Private Commu",
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
        if(commuViewModel.commuClassificationUiState is CommuClassificationUiState.AllCommuPosts){
            items(items = sortedRibbitPost, key = { post -> post.id }) {
                RibbitCard(
                    post = it,
                    getCardViewModel = getCardViewModel,
                    myId = myId,
                    navController = navController,
                    userViewModel = userViewModel,
                    modifier = modifier
                )
            }
        }else {
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