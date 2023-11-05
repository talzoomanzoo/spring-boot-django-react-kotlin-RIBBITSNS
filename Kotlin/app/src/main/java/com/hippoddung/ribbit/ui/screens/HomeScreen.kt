package com.hippoddung.ribbit.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.ui.screens.screenitems.HomeTopAppBar
import com.hippoddung.ribbit.ui.screens.screenitems.PostsGrid
import com.hippoddung.ribbit.ui.screens.statescreens.ErrorScreen
import com.hippoddung.ribbit.ui.screens.statescreens.LoadingScreen
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.CardViewModel
import com.hippoddung.ribbit.ui.viewmodel.CreatingPostViewModel
import com.hippoddung.ribbit.ui.viewmodel.HomeUiState
import com.hippoddung.ribbit.ui.viewmodel.TokenViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
//    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    cardViewModel: CardViewModel,
    tokenViewModel: TokenViewModel,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    creatingPostViewModel: CreatingPostViewModel,
    myId: Int,
    onNavigateToCreatingPostScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (cardViewModel.homeUiState) {

        is HomeUiState.Loading -> {
            Log.d("HippoLog, HomeScreen", "Loading")
            LoadingScreen(modifier = modifier)
        }

        is HomeUiState.Success -> {
            Log.d("HippoLog, HomeScreen", "Success")
            val ribbitPosts = (cardViewModel.homeUiState as HomeUiState.Success).posts
            HomeSuccessScreen(
                cardViewModel = cardViewModel,
                authViewModel = authViewModel,
                tokenViewModel = tokenViewModel,
                userViewModel = userViewModel,
                creatingPostViewModel = creatingPostViewModel,
//                scrollBehavior = scrollBehavior,
                navController = navController,
                ribbitPosts = ribbitPosts,
                myId = myId,
                onNavigateToCreatingPostScreen = onNavigateToCreatingPostScreen,
                modifier = modifier
            )
        }

        is HomeUiState.Error -> {
            Log.d("HippoLog, HomeScreen", "Error")
            ErrorScreen(modifier = modifier)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSuccessScreen(
    cardViewModel: CardViewModel,
    tokenViewModel: TokenViewModel,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    creatingPostViewModel: CreatingPostViewModel,
//    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    ribbitPosts: List<RibbitPost>,
    myId: Int,
    onNavigateToCreatingPostScreen: () -> Unit,
    modifier: Modifier
) {
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
            Box(modifier = modifier) {
                PostsGrid(
                    posts = ribbitPosts,
                    cardViewModel = cardViewModel,
                    userViewModel = userViewModel,
                    myId = myId,
                    navController = navController,
                    modifier = modifier
                )
                FloatingActionButton(
                    onClick = onNavigateToCreatingPostScreen,
                    modifier = modifier
                        .align(Alignment.BottomEnd)
                        .padding(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Floating action button.",
                        modifier = modifier
                    )
                }
            }
//            Box(modifier = modifier) {
//
//            }
        }
    }
}

