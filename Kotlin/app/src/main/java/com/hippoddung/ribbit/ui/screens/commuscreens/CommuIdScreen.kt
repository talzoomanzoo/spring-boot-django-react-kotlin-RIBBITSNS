package com.hippoddung.ribbit.ui.screens.commuscreens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
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
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.screens.RibbitTopAppBar
import com.hippoddung.ribbit.ui.screens.statescreens.ErrorScreen
import com.hippoddung.ribbit.ui.screens.statescreens.LoadingScreen
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.GetCommuIdPostsUiState
import com.hippoddung.ribbit.ui.viewmodel.CommuIdUiState
import com.hippoddung.ribbit.ui.viewmodel.CommuViewModel
import com.hippoddung.ribbit.ui.viewmodel.ListViewModel
import com.hippoddung.ribbit.ui.viewmodel.PostingViewModel
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
    postingViewModel: PostingViewModel,
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
            when (getCardViewModel.getCommuIdPostsUiState) {
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
                        postingViewModel = postingViewModel,
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
    postingViewModel: PostingViewModel,
    navController: NavHostController,
    myId: Int,
    modifier: Modifier
) {
    Log.d("HippoLog, ProfileScreen", "ProfileSuccessScreen")
    var posts by remember { mutableStateOf(listOf<RibbitPost>()) }
    if (getCardViewModel.getCommuIdPostsUiState is GetCommuIdPostsUiState.Success) {   // 원래 state 에 따라 넘어오기 때문에 확인할 필요가 없으나 state 에 무관하게 내려오는 문제가 있어 여기서 재확인
        posts = (getCardViewModel.getCommuIdPostsUiState as GetCommuIdPostsUiState.Success).posts
    }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen =
        RibbitScreen.valueOf(backStackEntry?.destination?.route ?: RibbitScreen.HomeScreen.name)
//    postingViewModel.setCurrentScreen(currentScreen)    // CommuIdScreen 진입 성공시 현재 screen 정보 저장.
//    getCardViewModel.setCurrentScreen(currentScreen)    // CommuIdScreen 진입 성공시 현재 screen 정보 저장.
//    navigation시 backStack으로 이동하면서 다른 경로를 저장하는 것을 확인 onClick으로 이동
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
            FloatingActionButton(
                onClick = {
                    postingViewModel.setCurrentScreen(currentScreen)    // CreatingPostScreen으로 이동시 현재 screen 정보 저장.
                    getCardViewModel.setCurrentScreen(currentScreen)    // CreatingPostScreen으로 이동시 현재 screen 정보 저장.
                    navController.navigate(RibbitScreen.CreatingPostScreen.name)
                },
                modifier = modifier
                    .padding(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Creating Commu Post.",
                    modifier = modifier
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if((commuViewModel.commuIdUiState as CommuIdUiState.Success).commuItem.followingsc.isEmpty()){
                Text(
                    text ="There is no following user at this commu",
                    modifier = modifier
                )
            }else {
                CommuIdPostsGrid(
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
    }
}