package com.hippoddung.ribbit.ui.screens.postidscreen

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.screens.RibbitTopAppBar
import com.hippoddung.ribbit.ui.screens.carditems.RibbitCard
import com.hippoddung.ribbit.ui.screens.statescreens.ErrorScreen
import com.hippoddung.ribbit.ui.screens.statescreens.LoadingScreen
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.CommuViewModel
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.ListViewModel
import com.hippoddung.ribbit.ui.viewmodel.PostIdUiState
import com.hippoddung.ribbit.ui.viewmodel.PostingViewModel
import com.hippoddung.ribbit.ui.viewmodel.TokenViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostIdScreen(
//    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    getCardViewModel: GetCardViewModel,
    postingViewModel: PostingViewModel,
    userViewModel: UserViewModel,
    myId: Int,
    modifier: Modifier = Modifier
) {
    when (getCardViewModel.postIdUiState) {

        is PostIdUiState.Loading -> {
            Log.d("HippoLog, PostIdScreen", "Loading")
            LoadingScreen(modifier = modifier)
        }

        is PostIdUiState.Success -> {
            Log.d("HippoLog, PostIdScreen", "Success")
            val post = (getCardViewModel.postIdUiState as PostIdUiState.Success).post
            PostIdSuccessScreen(
                getCardViewModel = getCardViewModel,
                postingViewModel = postingViewModel,
                userViewModel = userViewModel,
//                scrollBehavior = scrollBehavior,
                navController = navController,
                post = post,
                myId = myId,
                modifier = modifier
            )
        }

        is PostIdUiState.Error -> {
            Log.d("HippoLog, PostIdScreen", "Error")
            ErrorScreen(modifier = modifier)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "RememberReturnType")
@Composable
fun PostIdSuccessScreen(
    getCardViewModel: GetCardViewModel,
    postingViewModel: PostingViewModel,
    userViewModel: UserViewModel,
//    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    post: RibbitPost,
    myId: Int,
    modifier: Modifier = Modifier
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen =
        RibbitScreen.valueOf(backStackEntry?.destination?.route ?: RibbitScreen.HomeScreen.name)
    getCardViewModel.setCurrentScreen(currentScreen)    // homeScreen 진입 성공시 현재 screen 정보 저장.
    Surface(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(modifier = modifier) {
            if (!post.replyTwits.isNullOrEmpty()) { // 본문 post 가 너무 큰 경우 댓글 lazyColumn 이 너무 작아지는 문제가 있어 댓글이 있는 경우 본문을 lazyColumn 내로 같이 보내는 방식 채택
                PostIdPostsGrid(
                    post = post,
                    posts = post.replyTwits as List<RibbitPost>,    // Null or Empty check 를 하였음에도 컴파일오류가 계속되어 강제 캐스팅함.
                    getCardViewModel = getCardViewModel,
                    postingViewModel = postingViewModel,
                    userViewModel = userViewModel,
                    myId = myId,
                    navController = navController,
                    modifier = modifier
                )
            } else {    // lazyColumn 이 차지하는 리소스를 줄이기 위해 댓글이 없는 경우 바로 보여주는 방식 채택
                RibbitCard(
                    index = 0,
                    post = post,
                    getCardViewModel = getCardViewModel,
                    postingViewModel = postingViewModel,
                    userViewModel = userViewModel,
                    myId = myId,
                    navController = navController,
                    modifier = modifier
                )
            }
        }
        Box(modifier = modifier) {
            FloatingActionButton(
                onClick = {
                    Log.d("HippoLog, PostIdScreen", "onClick")
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