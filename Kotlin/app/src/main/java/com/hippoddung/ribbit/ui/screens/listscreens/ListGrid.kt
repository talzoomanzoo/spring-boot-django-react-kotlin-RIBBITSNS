package com.hippoddung.ribbit.ui.screens.listscreens

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
import com.hippoddung.ribbit.network.bodys.RibbitListItem
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.ListClassificationUiState
import com.hippoddung.ribbit.ui.viewmodel.ListViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState")
@Composable
fun ListGrid(
    listItems: List<RibbitListItem>,
    getCardViewModel: GetCardViewModel,
    listViewModel: ListViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val comparator =
        when(listViewModel.listClassificationUiState){
            is ListClassificationUiState.ListHome -> compareByDescending<RibbitListItem> { it.id }
            is ListClassificationUiState.Following -> compareByDescending { it.id }
            is ListClassificationUiState.TopViews -> compareByDescending { it.id }
            is ListClassificationUiState.TopLikes -> compareByDescending { it.id }
        }
    val sortedRibbitListItem = remember(listItems, comparator) {
        listItems.sortedWith(comparator)
    }   // LazyColumn items에 List를 바로 주는 것이 아니라 Comparator로 정렬하여 remember로 기억시켜서 recomposition을 방지하여 성능을 올린다.
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
                            listViewModel.getLists()
                        },
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = if(listViewModel.listClassificationUiState is ListClassificationUiState.ListHome){
                                Color(0xFF006400)
                            } else Color.White,
                            contentColor = if(listViewModel.listClassificationUiState is ListClassificationUiState.ListHome){
                                Color.White
                            } else Color(0xFF006400),
                        ),
                        modifier = modifier
                    ) {
                        Text(
                            text = "List Home",
                            fontSize = 16.sp,
                            modifier = modifier
                        )
                    }
//                    TextButton(
//                        onClick = {
//                            listViewModel.getFollowingPosts()
//                        },
//                        colors = ButtonDefaults.textButtonColors(
//                            containerColor = if(listViewModel.listClassificationUiState is ClassificationUiState.Following){
//                                Color(0xFF006400)
//                            } else Color.White,
//                            contentColor = if(listViewModel.listClassificationUiState is ClassificationUiState.Following){
//                                Color.White
//                            } else Color(0xFF006400),
//                        ),
//                        modifier = modifier
//                    ) {
//                        Text(
//                            text = "Following",
//                            fontSize = 16.sp,
//                            modifier = modifier
//                        )
//                    }
//                    TextButton(
//                        onClick = {
//                            listViewModel.getTopViewsRibbitPosts()
//                        },
//                        colors = ButtonDefaults.textButtonColors(
//                            containerColor = if(listViewModel.listClassificationUiState is ClassificationUiState.TopViews){
//                                Color(0xFF006400)
//                            } else Color.White,
//                            contentColor = if(listViewModel.listClassificationUiState is ClassificationUiState.TopViews){
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
//                            listViewModel.getTopLikesRibbitPosts()
//                        },
//                        colors = ButtonDefaults.textButtonColors(
//                            containerColor = if(listViewModel.listClassificationUiState is ClassificationUiState.TopLikes){
//                                Color(0xFF006400)
//                            } else Color.White,
//                            contentColor = if(listViewModel.listClassificationUiState is ClassificationUiState.TopLikes){
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
        items(items = sortedRibbitListItem, key = { listItem -> listItem.id }) {
            ListCard(
                listItem = it,
                listViewModel = listViewModel,
                getCardViewModel = getCardViewModel,
                navController = navController,
                modifier = modifier
            )
        }
    }
}