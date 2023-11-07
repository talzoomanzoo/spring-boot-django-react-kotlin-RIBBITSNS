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
import com.hippoddung.ribbit.ui.screens.statescreens.ErrorScreen
import com.hippoddung.ribbit.ui.screens.statescreens.LoadingScreen
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.GetUserIdRepliesUiState
import com.hippoddung.ribbit.ui.viewmodel.PostingViewModel
import com.hippoddung.ribbit.ui.viewmodel.TokenViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileRepliesScreen(
//    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    getCardViewModel: GetCardViewModel,
    tokenViewModel: TokenViewModel,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    postingViewModel: PostingViewModel,
    myId: Int,
    modifier: Modifier
) {
    when (getCardViewModel.getUserIdRepliesUiState) {

        is GetUserIdRepliesUiState.Loading -> {
            Log.d("HippoLog, ProfileRepliesScreen", "Loading")
            LoadingScreen(modifier = modifier)
        }

        is GetUserIdRepliesUiState.Error -> {
            Log.d("HippoLog, ProfileRepliesScreen", "Error")
            ErrorScreen(modifier = modifier)
        }

        is GetUserIdRepliesUiState.Success -> {
            Log.d("HippoLog, ProfileRepliesScreen", "Success")
            ProfileRepliesSuccessScreen(
                getCardViewModel = getCardViewModel,
                authViewModel = authViewModel,
                tokenViewModel = tokenViewModel,
                userViewModel = userViewModel,
                postingViewModel = postingViewModel,
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
fun ProfileRepliesSuccessScreen(
    getCardViewModel: GetCardViewModel,
    tokenViewModel: TokenViewModel,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    postingViewModel: PostingViewModel,
//    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    myId: Int,
    modifier: Modifier
) {
    Log.d("HippoLog, ProfileRepliesScreen", "ProfileRepliesSuccessScreen")
    Scaffold(
        modifier = modifier,
//        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        // scrollBehavior에 따라 리컴포지션이 트리거되는 것으로 추측, 해결할 방법을 찾아야 함.
        // navigation 위(RibbitApp)에 있던 scrollBehavior을 navigation 하위에 있는 HomeScreen으로 옮겨서 해결.
        topBar = {
            HomeTopAppBar(
                getCardViewModel = getCardViewModel,
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
            Log.d("HippoLog, ProfileRepliesScreen", "posts: ${(getCardViewModel.getUserIdRepliesUiState as GetUserIdRepliesUiState.Success).posts}")
            ProfilePostsGrid(
                posts = (getCardViewModel.getUserIdRepliesUiState as GetUserIdRepliesUiState.Success).posts,
                getCardViewModel = getCardViewModel,
                userViewModel = userViewModel,
                postingViewModel = postingViewModel,
                authViewModel = authViewModel,
                tokenViewModel = tokenViewModel,
                myId = myId,
                navController = navController,
                modifier = modifier
            )
        }
    }
}