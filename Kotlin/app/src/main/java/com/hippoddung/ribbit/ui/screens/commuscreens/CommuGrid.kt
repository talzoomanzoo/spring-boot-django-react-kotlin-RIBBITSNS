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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.network.bodys.RibbitCommuItem
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.CommuClassificationUiState
import com.hippoddung.ribbit.ui.viewmodel.CommuViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState")
@Composable
fun CommuGrid(
    myId: Int,
    filteredCommuItems: List<RibbitCommuItem>,
    getCardViewModel: GetCardViewModel,
    commuViewModel: CommuViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
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
//                    TextButton(
//                        onClick = {
//                            commuViewModel.getTopViewsRibbitPosts()
//                        },
//                        colors = ButtonDefaults.textButtonColors(
//                            containerColor = if(commuViewModel.commuClassificationUiState is ClassificationUiState.TopViews){
//                                Color(0xFF006400)
//                            } else Color.White,
//                            contentColor = if(commuViewModel.commuClassificationUiState is ClassificationUiState.TopViews){
//                                Color.White
//                            } else Color(0xFF006400),
//                        ),
//                        modifier = modifier
//                    ) {
//                        Text(
//                            text = "Top Views",
//                            fontSize = 16.sp,
//                            modifier = modifier
//                        )
//                    }
//                    TextButton(
//                        onClick = {
//                            commuViewModel.getTopLikesRibbitPosts()
//                        },
//                        colors = ButtonDefaults.textButtonColors(
//                            containerColor = if(commuViewModel.commuClassificationUiState is ClassificationUiState.TopLikes){
//                                Color(0xFF006400)
//                            } else Color.White,
//                            contentColor = if(commuViewModel.commuClassificationUiState is ClassificationUiState.TopLikes){
//                                Color.White
//                            } else Color(0xFF006400),
//                        ),
//                        modifier = modifier
//                    ) {
//                        Text(
//                            text = "Top Likes",
//                            fontSize = 16.sp,
//                            modifier = modifier
//                        )
//                    }
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
        items(items = sortedRibbitCommuItem, key = { commuItem -> commuItem.id!! }) {   // commuItem 을 받아오면 id 는 반드시 있음.
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