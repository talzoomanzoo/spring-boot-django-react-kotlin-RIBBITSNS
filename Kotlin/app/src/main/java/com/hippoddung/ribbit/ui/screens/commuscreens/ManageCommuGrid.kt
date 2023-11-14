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
import com.hippoddung.ribbit.network.bodys.User
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.CommuClassificationUiState
import com.hippoddung.ribbit.ui.viewmodel.CommuViewModel
import com.hippoddung.ribbit.ui.viewmodel.ManageCommuUiState

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState")
@Composable
fun ManageCommuGrid(
    commuItem: RibbitCommuItem,
    commuViewModel: CommuViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val comparator = compareByDescending<User> { it.id }
    val sortedCommuReadyUser = remember(commuItem.followingscReady, comparator) {
        commuItem.followingscReady.sortedWith(comparator)
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
                            commuViewModel.manageCommuUiState = ManageCommuUiState.ReadyManage(commuItem)
                        },
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = if(commuViewModel.manageCommuUiState is ManageCommuUiState.ReadyManage){
                                Color(0xFF006400)
                            } else Color.White,
                            contentColor = if(commuViewModel.manageCommuUiState is ManageCommuUiState.ReadyManage){
                                Color.White
                            } else Color(0xFF006400),
                        ),
                        modifier = modifier
                    ) {
                        Text(
                            text = "Manage",
                            fontSize = 16.sp,
                            modifier = modifier
                        )
                    }
                    TextButton(
                        onClick = {
                            commuViewModel.manageCommuUiState = ManageCommuUiState.ReadyEdit(commuItem)
                            navController.navigate(RibbitScreen.EditingCommuScreen.name)
                        },
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = if(commuViewModel.manageCommuUiState is ManageCommuUiState.ReadyEdit){
                                Color(0xFF006400)
                            } else Color.White,
                            contentColor = if(commuViewModel.manageCommuUiState is ManageCommuUiState.ReadyEdit){
                                Color.White
                            } else Color(0xFF006400),
                        ),
                        modifier = modifier
                    ) {
                        Text(
                            text = "Edit",
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
        items(items = sortedCommuReadyUser, key = { commuItem -> commuItem.id!! }) {   // commuItem 을 받아오면 id 는 반드시 있음.
            ManageCommuUserCard(
                user = it,
                commuViewModel = commuViewModel,
                modifier = modifier
            )
        }
    }
}