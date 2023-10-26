package com.hippoddung.ribbit.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.screens.homescreen.HomeTopAppBar
import com.hippoddung.ribbit.ui.screens.homescreen.RibbitCard
import com.hippoddung.ribbit.ui.screens.statescreens.ErrorScreen
import com.hippoddung.ribbit.ui.screens.statescreens.LoadingScreen
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.HomeViewModel
import com.hippoddung.ribbit.ui.viewmodel.TwitIdUiState

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwitIdScreen(
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    authViewModel: AuthViewModel,
    userId: Int,
    modifier: Modifier = Modifier.fillMaxSize()
) {
    when (homeViewModel.twitIdUiState) {

        is TwitIdUiState.Loading -> {
            Log.d("HippoLog, TwitIdScreen", "Loading")
            LoadingScreen(modifier = modifier)
        }

        is TwitIdUiState.Success -> {
            Log.d("HippoLog, TwitIdScreen", "Success")
            val post = (homeViewModel.twitIdUiState as TwitIdUiState.Success).post
            TwitIdSuccessScreen(
                homeViewModel = homeViewModel,
                authViewModel = authViewModel,
                scrollBehavior = scrollBehavior,
                navController = navController,
                post = post,
                userId = userId,
                modifier = modifier
            )
        }

        is TwitIdUiState.Error -> {
            Log.d("HippoLog, TwitIdScreen", "Error")
            ErrorScreen(modifier = modifier)
        }

        else -> {}
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwitIdSuccessScreen(
    homeViewModel: HomeViewModel,
    authViewModel: AuthViewModel,
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    post: RibbitPost,
    userId: Int,
    modifier: Modifier
) {
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        // scrollBehavior에 따라 리컴포지션이 트리거되는 것으로 추측, 해결할 방법을 찾아야 함.
        // navigation 위(RibbitApp)에 있던 scrollBehavior을 navigation 하위에 있는 HomeScreen으로 옮겨서 해결.
        topBar = {
            HomeTopAppBar(
                homeViewModel = homeViewModel,
                authViewModel = authViewModel,
                scrollBehavior = scrollBehavior,
                navController = navController
            )
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Box(modifier = modifier) {
                Box(modifier = modifier) {
                    RibbitCard(
                        post = post,
                        homeViewModel = homeViewModel,
                        userId = userId,
                        navcontroller = navController,
                        modifier = Modifier.padding(8.dp)
                            .wrapContentHeight()
                            .fillMaxWidth()
                    )
                }
            }
            Box(modifier = modifier) {
                FloatingActionButton(
                    onClick = {
                        Log.d("HippoLog, HomeScreen", "onClick")
                        navController.navigate(RibbitScreen.TwitCreateScreen.name)
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(14.dp)
                ) {
                    Icon(Icons.Filled.Edit, "Floating action button.")
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState", "RememberReturnType")
@Composable
fun PostsGridScreen(
    post: RibbitPost,
    homeViewModel: HomeViewModel,
    userId: Int,
    navController: NavHostController,
    modifier: Modifier
) {

}