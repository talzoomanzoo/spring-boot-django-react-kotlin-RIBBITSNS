package com.hippoddung.ribbit.ui.screens.commuscreens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.network.bodys.RibbitCommuItem
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.screens.RibbitTopAppBar
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.CommuViewModel
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.ListViewModel
import com.hippoddung.ribbit.ui.viewmodel.ManageCommuUiState
import com.hippoddung.ribbit.ui.viewmodel.TokenViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ManageCommuScreen(
    userViewModel: UserViewModel,
    commuViewModel: CommuViewModel,
    navController: NavHostController,
    modifier: Modifier
) {
    var commuItem by remember { mutableStateOf(RibbitCommuItem())}
    if(commuViewModel.manageCommuUiState is ManageCommuUiState.ReadyManage) { // navigation으로 이동시 backStack으로 보내면서 실행되어 에러를 발생시키는 것 방지.
        commuItem =  (commuViewModel.manageCommuUiState as ManageCommuUiState.ReadyManage).commuItem     // 현재 페이지 접근시 저장한 정보기 때문에 반드시 값이 존재함.
    }
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { commuViewModel.searchingUserClickedUiState = true },
                modifier = modifier
                    .padding(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add User Button.",
                    modifier = modifier
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize()
        ) {
            Box(modifier = modifier) {
                ManageCommuGrid(
                    commuItem = commuItem,
                    commuViewModel = commuViewModel,
                    navController = navController,
                    modifier = modifier
                )
            }
        }
        if (commuViewModel.searchingUserClickedUiState) {
            Dialog(
                onDismissRequest = {
                    commuViewModel.searchingUserClickedUiState = false
                },
                content = {
                    SearchingUserDialog(
                        userViewModel = userViewModel,
                        commuViewModel = commuViewModel,
                        modifier = modifier
                    )
                }
            )
            // Dialog 호출 시에 현재 페이지를 recomposition 하면서 현재 카드 정보가 아닌 최근에 composition 된 카드의 정보가 넘어가는 문제가 있음.
            // Dialog 자체의 문제로 추정되므로 가급적 쓰지 않는 것이 좋을 것으로 보이나 우선은 사용하기로 하고
            // replyClickedUiState 에 카드의 정보를 담아서 사용하는 방식을 선택함.
        }
    }
}