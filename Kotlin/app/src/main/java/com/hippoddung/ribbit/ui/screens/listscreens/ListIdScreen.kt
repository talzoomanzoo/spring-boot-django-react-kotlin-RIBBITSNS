package com.hippoddung.ribbit.ui.screens.listscreens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.ui.screens.RibbitTopAppBar
import com.hippoddung.ribbit.ui.screens.statescreens.ErrorScreen
import com.hippoddung.ribbit.ui.screens.statescreens.LoadingScreen
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.GetListIdPostsUiState
import com.hippoddung.ribbit.ui.viewmodel.GetUserIdPostsUiState
import com.hippoddung.ribbit.ui.viewmodel.ListIdUiState
import com.hippoddung.ribbit.ui.viewmodel.ListViewModel
import com.hippoddung.ribbit.ui.viewmodel.TokenViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListIdScreen(
//    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    getCardViewModel: GetCardViewModel,
    tokenViewModel: TokenViewModel,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    listViewModel: ListViewModel,
    myId: Int,
    modifier: Modifier
) {

//    비동기처리를 통해 listViewModel에서 listIdUiState를 업데이트하고 getCardViewModel의 getListIdPosts 함수를 통해 getListIdPostsUiState를 업데이트하도록 설계한다.
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
                        authViewModel = authViewModel,
                        tokenViewModel = tokenViewModel,
                        userViewModel = userViewModel,
                        listViewModel = listViewModel,
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListIdSuccessScreen(
    getCardViewModel: GetCardViewModel,
    tokenViewModel: TokenViewModel,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    listViewModel: ListViewModel,
    navController: NavHostController,
    myId: Int,
    modifier: Modifier
) {
    Log.d("HippoLog, ProfileScreen", "ProfileSuccessScreen")
    var posts by remember { mutableStateOf(listOf<RibbitPost>()) }
    if (getCardViewModel.getUserIdPostsUiState is GetUserIdPostsUiState.Success) {   // 원래 state에 따라 넘어오기 때문에 확인할 필요가 없으나 state에 무관하게 내려오는 문제가 있어 여기서 재확인
        posts = (getCardViewModel.getUserIdPostsUiState as GetUserIdPostsUiState.Success).posts
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
                modifier = modifier
            )
        }
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        ) {
            ListIdPostsGrid(
                posts = posts,
                getCardViewModel = getCardViewModel,
                userViewModel = userViewModel,
                myId = myId,
                navController = navController,
                modifier = modifier
            )
        }
    }
}