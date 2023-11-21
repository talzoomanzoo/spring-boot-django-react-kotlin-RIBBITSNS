package com.hippoddung.ribbit.ui.screens.commuscreens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.network.bodys.RibbitCommuItem
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.viewmodel.CommuViewModel
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.ManageCommuUiState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CommuCard(
    myId: Int,
    commuItem: RibbitCommuItem,
    commuViewModel: CommuViewModel,
    getCardViewModel: GetCardViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(start = 20.dp, top = 0.dp, end = 20.dp, bottom = 0.dp)
            .clickable {
                Log.d("HippoLog, UserCard", "CommuCardClick")
                commuViewModel.getCommuIdCommu(commuId = commuItem.id!!)    // commuItem을 받아오면 id는 반드시 있음.
                getCardViewModel.getCommuIdPosts(commuId = commuItem.id)
                navController.navigate(RibbitScreen.CommuIdScreen.name)
            }
    ) {
        Row(
            modifier = modifier
                .weight(2f)
        ) {
            Icon(
                imageVector = Icons.Default.List,
                contentDescription = "RibbitCommu",
                modifier = modifier
            )
            Row(
                modifier = modifier.padding(start = 12.dp, bottom = 4.dp)
            ) {
                Log.d("HippoLog, CommuCard", "$commuItem")
                commuItem.comName?.let {
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        modifier = modifier.padding(start = 4.dp, end = 4.dp),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                commuItem.user?.fullName?.let {
                    Text(
                        text = "@$it",
                        fontSize = 14.sp,
                        modifier = modifier.padding(start = 4.dp, end = 4.dp),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        }
        if (commuItem.user?.id == myId) {    // 관리자 계정일 경우
            OutlinedButton(
                onClick = {
                    commuViewModel.manageCommuUiState = ManageCommuUiState.ReadyManage(commuItem)
                    navController.navigate(RibbitScreen.ManageCommuScreen.name)
                },
                modifier = modifier
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.manage),
                    color = Color(0xFF006400),
                    fontSize = 14.sp,
                    modifier = modifier
                )
            }
//            OutlinedButton(
//                onClick = {
//                    commuViewModel.editingCommuUiState = EditingCommuUiState.Ready(commuItem)
//                    navController.navigate(RibbitScreen.EditingCommuScreen.name)
//                },
//                modifier = modifier
//            ) {
//                Text(
//                    text = stringResource(R.string.manage),
//                    color = Color(0xFF006400),
//                    fontSize = 14.sp,
//                    modifier = modifier
//                )
//            }
        } else {  // 관리자 계정 아닐 경우
            if(commuItem.followingsc.any{ it?.id == myId}){
                OutlinedButton(
                    onClick = {
                        commuViewModel.signoutCommuIdState = commuItem.id
                        commuViewModel.signoutCommuClickedUiState = true
                    },
                    modifier = modifier
                        .weight(1f)
                ) {
                    Text(
                        text = "Signout",
                        color = Color(0xFF006400),
                        fontSize = 14.sp,
                        modifier = modifier
                    )
                }
            }else {
                OutlinedButton(
                    onClick = {
                        commuViewModel.signupCommu(commuItem)
                        commuViewModel.signupCommuClickedUiState = true
                    },
                    modifier = modifier
                        .weight(1f)
                ) {
                    Text(
                        text = "Signup",
                        color = Color(0xFF006400),
                        fontSize = 14.sp,
                        modifier = modifier
                    )
                }
            }
        }
    }
}