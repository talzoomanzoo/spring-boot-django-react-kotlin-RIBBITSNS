package com.hippoddung.ribbit.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.screens.carditems.RibbitCard
import com.hippoddung.ribbit.ui.screens.screenitems.HomeTopAppBar
import com.hippoddung.ribbit.ui.screens.screenitems.PostsGrid
import com.hippoddung.ribbit.ui.screens.statescreens.ErrorScreen
import com.hippoddung.ribbit.ui.screens.statescreens.LoadingScreen
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.CardViewModel
import com.hippoddung.ribbit.ui.viewmodel.CreatingPostViewModel
import com.hippoddung.ribbit.ui.viewmodel.PostIdUiState
import com.hippoddung.ribbit.ui.viewmodel.TokenViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostIdScreen(
//    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    cardViewModel: CardViewModel,
    tokenViewModel: TokenViewModel,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    creatingPostViewModel: CreatingPostViewModel,
    userId: Int,
    modifier: Modifier = Modifier
) {
    when (cardViewModel.postIdUiState) {

        is PostIdUiState.Loading -> {
            Log.d("HippoLog, TwitIdScreen", "Loading")
            LoadingScreen(modifier = modifier)
        }

        is PostIdUiState.Success -> {
            Log.d("HippoLog, TwitIdScreen", "Success")
            val post = (cardViewModel.postIdUiState as PostIdUiState.Success).post
            PostIdSuccessScreen(
                cardViewModel = cardViewModel,
                tokenViewModel = tokenViewModel,
                authViewModel = authViewModel,
                userViewModel = userViewModel,
                creatingPostViewModel = creatingPostViewModel,
//                scrollBehavior = scrollBehavior,
                navController = navController,
                post = post,
                userId = userId,
                modifier = modifier
            )
        }

        is PostIdUiState.Error -> {
            Log.d("HippoLog, TwitIdScreen", "Error")
            ErrorScreen(modifier = modifier)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostIdSuccessScreen(
    cardViewModel: CardViewModel,
    tokenViewModel: TokenViewModel,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    creatingPostViewModel: CreatingPostViewModel,
//    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    post: RibbitPost,
    userId: Int,
    modifier: Modifier = Modifier
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
                userViewModel = userViewModel,
                authViewModel = authViewModel,
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
            Column(modifier = modifier) {
                RibbitCard(
                    post = post,
                    cardViewModel = cardViewModel,
                    userId = userId,
                    navController = navController,
                    modifier = modifier
                )
                if (post.replyTwits != null){
                    if(post.replyTwits.isNotEmpty()) {
                        Row(modifier = modifier) {
                            Spacer(modifier = modifier.width(28.dp))
                            PostsGrid(
                                posts = post.replyTwits as List<RibbitPost>,    // null, Empty check를 하였음에도 컴파일오류가 계속되어 강제 캐스팅함.
                                cardViewModel = cardViewModel,
                                userId = userId,
                                navController = navController,
                                modifier = modifier
                            )
                        }
                    }
                }
            }
            Box(modifier = modifier) {
                FloatingActionButton(
                    onClick = {
                        Log.d("HippoLog, HomeScreen", "onClick")
                        navController.navigate(RibbitScreen.CreatingPostScreen.name)
                    },
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