package com.hippoddung.ribbit.ui.screens.listscreens

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
import androidx.compose.runtime.collectAsState
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
import com.hippoddung.ribbit.ui.viewmodel.CommuViewModel
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.GetListIdPostsUiState
import com.hippoddung.ribbit.ui.viewmodel.GetUserIdPostsUiState
import com.hippoddung.ribbit.ui.viewmodel.ListIdUiState
import com.hippoddung.ribbit.ui.viewmodel.ListViewModel
import com.hippoddung.ribbit.ui.viewmodel.PostingViewModel
import com.hippoddung.ribbit.ui.viewmodel.TokenViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListIdScreen(
//    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    getCardViewModel: GetCardViewModel,
    postingViewModel: PostingViewModel,
    tokenViewModel: TokenViewModel,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    listViewModel: ListViewModel,
    commuViewModel: CommuViewModel,
    myId: Int,
    modifier: Modifier
) {
//    비동기처리를 통해 listViewModel 에서 listIdUiState 를 업데이트하고 getCardViewModel 의 getListIdPosts 함수를 통해 getListIdPostsUiState 를 업데이트하도록 설계한다.
    when (listViewModel.listIdUiState) {

        is ListIdUiState.Loading -> {
            Log.d("HippoLog, ListIdScreen", "listIdUiState Loading")
            LoadingScreen(modifier = modifier)
        }

        is ListIdUiState.Error -> {
            Log.d("HippoLog, ListIdScreen", "listIdUiState Error")
            ErrorScreen(modifier = modifier)
        }

        is ListIdUiState.Success -> {
            Log.d("HippoLog, ListIdScreen", "listIdUiState Success")
            when(getCardViewModel.getListIdPostsUiState){
                is GetListIdPostsUiState.Loading -> {
                    Log.d("HippoLog, ListIdScreen", "getListIdPostsUiState Loading")
                    LoadingScreen(modifier = modifier)
                }

                is GetListIdPostsUiState.Error -> {
                    Log.d("HippoLog, ListIdScreen", "getListIdPostsUiState Error")
                    ErrorScreen(modifier = modifier)
                }

                is GetListIdPostsUiState.Success -> {
                    Log.d("HippoLog, ListIdScreen", "getListIdPostsUiState Success")
                    ListIdSuccessScreen(
                        getCardViewModel = getCardViewModel,
                        postingViewModel = postingViewModel,
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
fun ListIdSuccessScreen(
    getCardViewModel: GetCardViewModel,
    postingViewModel: PostingViewModel,
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
    if (getCardViewModel.getListIdPostsUiState is GetListIdPostsUiState.Success) {   // 원래 state 에 따라 넘어오기 때문에 확인할 필요가 없으나 state 에 무관하게 내려오는 문제가 있어 여기서 재확인
        posts = (getCardViewModel.getListIdPostsUiState as GetListIdPostsUiState.Success).posts
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            RibbitTopAppBar(
                getCardViewModel = getCardViewModel,
                tokenViewModel = tokenViewModel,
                authViewModel = authViewModel,
                userViewModel = userViewModel,
                navController = navController,
                listViewModel = listViewModel,
                commuViewModel = commuViewModel,
                modifier = modifier
            )
        },
        floatingActionButton = {
            if ((listViewModel.listIdUiState as ListIdUiState.Success).listItem.user?.id == myId) {
                FloatingActionButton(
                    onClick = { listViewModel.searchingUserClickedUiState = true },
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
            if((listViewModel.listIdUiState as ListIdUiState.Success).listItem.followingsl.isNullOrEmpty()){
                Text(
                    text ="There is no following user at this list",
                    modifier = modifier
                )
            }else {
                ListIdPostsGrid(
                    posts = posts,
                    getCardViewModel = getCardViewModel,
                    postingViewModel = postingViewModel,
                    userViewModel = userViewModel,
                    myId = myId,
                    navController = navController,
                    modifier = modifier
                )
            }
        }
        if (listViewModel.searchingUserClickedUiState) {
            Dialog(
                onDismissRequest = {
                    listViewModel.searchingUserClickedUiState = false
                },
                content = {
                    SearchingUserDialog(
                        userViewModel = userViewModel,
                        listViewModel = listViewModel,
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