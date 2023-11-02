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
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.ui.screens.screenitems.HomeTopAppBar
import com.hippoddung.ribbit.ui.screens.screenitems.ProfilePostsGrid
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
fun ProfileScreen(
//    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    cardViewModel: CardViewModel,
    tokenViewModel: TokenViewModel,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    userId: Int,
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
            ProfileSuccessScreen(
                cardViewModel = cardViewModel,
                authViewModel = authViewModel,
                tokenViewModel = tokenViewModel,
                userViewModel = userViewModel,
//                scrollBehavior = scrollBehavior,
                navController = navController,
                userId = userId,
                modifier = modifier
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSuccessScreen(
    cardViewModel: CardViewModel,
    tokenViewModel: TokenViewModel,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
//    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    userId: Int,
    modifier: Modifier
) {
    Log.d("HippoLog, ProfileScreen", "ProfileSuccessScreen")
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
            if(cardViewModel.getUserIdPostsUiState is GetUserIdPostsUiState.Success) {  // 원래 state에 따라 넘어오기 때문에 확인할 필요가 없으나 state에 무관하게 내려오는 문제가 있어 여기서 재확인
                ProfilePostsGrid(
                    posts = (cardViewModel.getUserIdPostsUiState as GetUserIdPostsUiState.Success).posts,
                    cardViewModel = cardViewModel,
                    userViewModel = userViewModel,
                    userId = userId,
                    navController = navController,
                    modifier = modifier
                )
            }
        }
    }
}