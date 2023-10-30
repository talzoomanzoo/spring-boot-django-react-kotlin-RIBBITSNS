package com.hippoddung.ribbit.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.screens.carditems.RibbitCard
import com.hippoddung.ribbit.ui.screens.screenitems.HomeTopAppBar
import com.hippoddung.ribbit.ui.screens.statescreens.ErrorScreen
import com.hippoddung.ribbit.ui.screens.statescreens.LoadingScreen
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.HomeUiState
import com.hippoddung.ribbit.ui.viewmodel.HomeViewModel
import com.hippoddung.ribbit.ui.viewmodel.TokenViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    tokenViewModel: TokenViewModel,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    userId: Int,
    modifier: Modifier = Modifier.fillMaxSize()
) {
    when (homeViewModel.homeUiState) {

        is HomeUiState.Loading -> {
            Log.d("HippoLog, HomeScreen", "Loading")
            LoadingScreen(modifier = modifier)
        }

        is HomeUiState.Success -> {
            Log.d("HippoLog, HomeScreen", "Success")
            val ribbitPosts = (homeViewModel.homeUiState as HomeUiState.Success).posts
            HomeSuccessScreen(
                homeViewModel = homeViewModel,
                authViewModel = authViewModel,
                tokenViewModel =  tokenViewModel,
                userViewModel = userViewModel,
                scrollBehavior = scrollBehavior,
                navController = navController,
                ribbitPosts = ribbitPosts,
                userId = userId,
                modifier = modifier
            )
        }

        is HomeUiState.Error -> {
            Log.d("HippoLog, HomeScreen", "Error")
            ErrorScreen(modifier = modifier)
        }

        else -> {}
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSuccessScreen(
    homeViewModel: HomeViewModel,
    tokenViewModel: TokenViewModel,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    ribbitPosts: List<RibbitPost>,
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
                tokenViewModel = tokenViewModel,
                authViewModel = authViewModel,
                userViewModel = userViewModel,
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
                PostsGridScreen(
                    posts = ribbitPosts,
                    homeViewModel = homeViewModel,
                    userId = userId,
                    navController = navController,
                    modifier = modifier
                )
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
@SuppressLint("UnrememberedMutableState")
@Composable
fun PostsGridScreen(posts: List<RibbitPost>,
                    homeViewModel: HomeViewModel,
                    userId: Int,
                    navController: NavHostController,
                    modifier: Modifier
) {
    val comparator = compareByDescending<RibbitPost> { it.id }
    val sortedRibbitPost = remember(posts, comparator) {
        posts.sortedWith(comparator)
    }   // LazyColumn items에 List를 바로 주는 것이 아니라 Comparator로 정렬하여 remember로 기억시켜서 recomposition을 방지하여 성능을 올린다.
    LazyColumn(modifier = modifier) {
        items(items = sortedRibbitPost, key = { post -> post.id }) {
            RibbitCard(
                post = it,
                homeViewModel = homeViewModel,
                userId = userId,
                navController = navController,
                modifier = modifier.padding(8.dp)
            )

        }
    }
}