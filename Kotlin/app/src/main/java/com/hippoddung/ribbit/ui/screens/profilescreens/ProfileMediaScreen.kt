package com.hippoddung.ribbit.ui.screens.profilescreens

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
import com.hippoddung.ribbit.ui.screens.screenitems.HomeTopAppBar
import com.hippoddung.ribbit.ui.screens.statescreens.ErrorScreen
import com.hippoddung.ribbit.ui.screens.statescreens.LoadingScreen
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.CardViewModel
import com.hippoddung.ribbit.ui.viewmodel.GetUserIdPostsUiState
import com.hippoddung.ribbit.ui.viewmodel.TokenViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileMediasScreen(
//    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    cardViewModel: CardViewModel,
    tokenViewModel: TokenViewModel,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    myId: Int,
    modifier: Modifier
) {
    when (cardViewModel.getUserIdPostsUiState) {

        is GetUserIdPostsUiState.Loading -> {
            Log.d("HippoLog, ProfileScreen", "Loading")
            LoadingScreen(modifier = modifier)
        }

        is GetUserIdPostsUiState.Error -> {
            Log.d("HippoLog, ProfileScreen", "Error")
            ErrorScreen(modifier = modifier)
        }

        is GetUserIdPostsUiState.Success -> {
            Log.d("HippoLog, ProfileScreen", "Success")
            ProfileMediasSuccessScreen(
                cardViewModel = cardViewModel,
                authViewModel = authViewModel,
                tokenViewModel = tokenViewModel,
                userViewModel = userViewModel,
//                scrollBehavior = scrollBehavior,
                navController = navController,
                myId = myId,
                modifier = modifier
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileMediasSuccessScreen(
    cardViewModel: CardViewModel,
    tokenViewModel: TokenViewModel,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
//    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    myId: Int,
    modifier: Modifier
) {
    Log.d("HippoLog, ProfileLikesScreen", "ProfileLikesSuccessScreen")
    Scaffold(
        modifier = modifier,
//        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        // scrollBehavior에 따라 리컴포지션이 트리거되는 것으로 추측, 해결할 방법을 찾아야 함.
        // navigation 위(RibbitApp)에 있던 scrollBehavior을 navigation 하위에 있는 HomeScreen으로 옮겨서 해결.
        topBar = {
            HomeTopAppBar(
                cardViewModel = cardViewModel,
                tokenViewModel = tokenViewModel,
                authViewModel = authViewModel,
                userViewModel = userViewModel,
//                scrollBehavior = scrollBehavior,
                navController = navController,
                modifier = modifier
            )
        }
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        ) {
            var posts by remember { mutableStateOf(listOf<RibbitPost>()) }
            if (cardViewModel.getUserIdPostsUiState is GetUserIdPostsUiState.Success) {
                posts = (cardViewModel.getUserIdPostsUiState as GetUserIdPostsUiState.Success).posts
                // navigation으로 ProfileScreen main으로 이동시 getUserIdPostsUiState 를 공유하는 현재 페이지를 백스택으로 넣으면서 문제가 발생.
                // state check 를 추가.
            }
            val userIdMedias by remember {
                mutableStateOf(
                    posts.filter { (it.image != null) or (it.video != null) }   // image나 video가 null이 아닌 경우만 뽑아서 리스트로 만든다.
                )
            }
            ProfilePostsGrid(
                posts = userIdMedias,
                cardViewModel = cardViewModel,
                userViewModel = userViewModel,
                myId = myId,
                navController = navController,
                modifier = modifier
            )
        }
    }
}