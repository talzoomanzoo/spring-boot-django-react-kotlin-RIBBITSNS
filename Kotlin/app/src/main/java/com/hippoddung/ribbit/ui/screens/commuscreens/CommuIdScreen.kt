package com.hippoddung.ribbit.ui.screens.commuscreens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.ui.screens.RibbitTopAppBar
import com.hippoddung.ribbit.ui.screens.statescreens.ErrorScreen
import com.hippoddung.ribbit.ui.screens.statescreens.LoadingScreen
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.GetCommuIdPostsUiState
import com.hippoddung.ribbit.ui.viewmodel.CommuIdUiState
import com.hippoddung.ribbit.ui.viewmodel.CommuViewModel
import com.hippoddung.ribbit.ui.viewmodel.ListViewModel
import com.hippoddung.ribbit.ui.viewmodel.TokenViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CommuIdScreen(
//    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    getCardViewModel: GetCardViewModel,
    tokenViewModel: TokenViewModel,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    listViewModel: ListViewModel,
    commuViewModel: CommuViewModel,
    myId: Int,
    modifier: Modifier
) {

//    비동기처리를 통해 commuViewModel 에서 commuIdUiState 를 업데이트하고 getCardViewModel 의 getCommuIdPosts 함수를 통해 getCommuIdPostsUiState 를 업데이트하도록 설계한다.
    when (commuViewModel.commuIdUiState) {

        is CommuIdUiState.Loading -> {
            Log.d("HippoLog, CommuIdScreen", "commuIdUiState Loading")
            LoadingScreen(modifier = modifier)
        }

        is CommuIdUiState.Error -> {
            Log.d("HippoLog, CommuIdScreen", "commuIdUiState Error")
            ErrorScreen(modifier = modifier)
        }

        is CommuIdUiState.Success -> {
            Log.d("HippoLog, CommuIdScreen", "commuIdUiState Success")
            when(getCardViewModel.getCommuIdPostsUiState){
                is GetCommuIdPostsUiState.Loading -> {
                    Log.d("HippoLog, CommuIdScreen", "getCommuIdPostsUiState Loading")
                    LoadingScreen(modifier = modifier)
                }

                is GetCommuIdPostsUiState.Error -> {
                    Log.d("HippoLog, CommuIdScreen", "getCommuIdPostsUiState Error")
                    ErrorScreen(modifier = modifier)
                }

                is GetCommuIdPostsUiState.Success -> {
                    Log.d("HippoLog, CommuIdScreen", "getCommuIdPostsUiState Success")
                    CommuIdSuccessScreen(
                        getCardViewModel = getCardViewModel,
                        authViewModel = authViewModel,
                        tokenViewModel = tokenViewModel,
                        userViewModel = userViewModel,
                        listViewModel = listViewModel,
                        commuViewModel = commuViewModel,
                        navController = navController,
                        myId = myId,
                        modifier = modifier
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CommuIdSuccessScreen(
    getCardViewModel: GetCardViewModel,
    tokenViewModel: TokenViewModel,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    listViewModel: ListViewModel,
    commuViewModel: CommuViewModel,
    navController: NavHostController,
    myId: Int,
    modifier: Modifier
) {
    Log.d("HippoLog, ProfileScreen", "ProfileSuccessScreen")
    var posts by remember { mutableStateOf(listOf<RibbitPost>()) }
    if (getCardViewModel.getCommuIdPostsUiState is GetCommuIdPostsUiState.Success) {   // 원래 state 에 따라 넘어오기 때문에 확인할 필요가 없으나 state 에 무관하게 내려오는 문제가 있어 여기서 재확인
        posts = (getCardViewModel.getCommuIdPostsUiState as GetCommuIdPostsUiState.Success).posts
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            RibbitTopAppBar(
                getCardViewModel = getCardViewModel,
                tokenViewModel = tokenViewModel,
                authViewModel = authViewModel,
                userViewModel = userViewModel,
                listViewModel = listViewModel,
                commuViewModel = commuViewModel,
                navController = navController,
                modifier = modifier
            )
        },
        floatingActionButton = {
            if ((commuViewModel.commuIdUiState as CommuIdUiState.Success).commuItem.user?.id == myId) {
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
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        ) {
            // 231110 1120 이따가 처리해야 함
//            if((commuViewModel.commuIdUiState as CommuIdUiState.Success).commuItem.followingsl.isNullOrEmpty()){
//                Text(
//                    text ="There is no following user at this commu",
//                    modifier = modifier
//                )
//            }else {
//                CommuIdPostsGrid(
//                    posts = posts,
//                    getCardViewModel = getCardViewModel,
//                    userViewModel = userViewModel,
//                    myId = myId,
//                    navController = navController,
//                    modifier = modifier
//                )
//            }
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