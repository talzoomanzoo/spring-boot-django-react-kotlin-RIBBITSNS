package com.hippoddung.ribbit.ui.screens.homescreens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
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
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.screens.RibbitTopAppBar
import com.hippoddung.ribbit.ui.screens.statescreens.ErrorScreen
import com.hippoddung.ribbit.ui.screens.statescreens.LoadingScreen
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.HomeUiState
import com.hippoddung.ribbit.ui.viewmodel.ListViewModel
import com.hippoddung.ribbit.ui.viewmodel.TokenViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
//    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    getCardViewModel: GetCardViewModel,
    tokenViewModel: TokenViewModel,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    listViewModel: ListViewModel,
    myId: Int,
    modifier: Modifier = Modifier
) {
    when (getCardViewModel.homeUiState) {

        is HomeUiState.Loading -> {
            Log.d("HippoLog, HomeScreen", "Loading")
            LoadingScreen(modifier = modifier)
        }

        is HomeUiState.Success -> {
            Log.d("HippoLog, HomeScreen", "Success")
            val ribbitPosts = (getCardViewModel.homeUiState as HomeUiState.Success).posts
            HomeSuccessScreen(
                getCardViewModel = getCardViewModel,
                authViewModel = authViewModel,
                tokenViewModel = tokenViewModel,
                userViewModel = userViewModel,
                listViewModel = listViewModel,
//                scrollBehavior = scrollBehavior,
                navController = navController,
                ribbitPosts = ribbitPosts,
                myId = myId,
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
@Composable
fun HomeSuccessScreen(
    getCardViewModel: GetCardViewModel,
    tokenViewModel: TokenViewModel,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    listViewModel: ListViewModel,
//    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    ribbitPosts: List<RibbitPost>,
    myId: Int,
    modifier: Modifier
) {
    Scaffold(
        modifier = modifier,
//        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        // scrollBehavior 에 따라 리컴포지션이 트리거되는 것으로 추측, 해결할 방법을 찾아야 함.
        // navigation 위(RibbitApp)에 있던 scrollBehavior 을 navigation 하위에 있는 HomeScreen 으로 옮겨서 해결.
        topBar = {
            RibbitTopAppBar(
                getCardViewModel = getCardViewModel,
                tokenViewModel = tokenViewModel,
                authViewModel = authViewModel,
                userViewModel = userViewModel,
                listViewModel = listViewModel,
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
                    getCardViewModel = getCardViewModel,
                    userViewModel = userViewModel,
                    myId = myId,
                    navController = navController,
                    modifier = modifier
                )
                FloatingActionButton(
                    onClick = { navController.navigate(RibbitScreen.CreatingPostScreen.name) },
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
        }
    }
}