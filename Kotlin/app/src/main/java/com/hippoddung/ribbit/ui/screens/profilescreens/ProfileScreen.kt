package com.hippoddung.ribbit.ui.screens.profilescreens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.ui.screens.ReplyScreen
import com.hippoddung.ribbit.ui.screens.RibbitTopAppBar
import com.hippoddung.ribbit.ui.screens.statescreens.ErrorScreen
import com.hippoddung.ribbit.ui.screens.statescreens.LoadingScreen
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.CommuViewModel
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.GetUserIdPostsUiState
import com.hippoddung.ribbit.ui.viewmodel.ListViewModel
import com.hippoddung.ribbit.ui.viewmodel.PostingViewModel
import com.hippoddung.ribbit.ui.viewmodel.ReplyClickedUiState
import com.hippoddung.ribbit.ui.viewmodel.TokenViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserIdClassificationUiState
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(
//    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    getCardViewModel: GetCardViewModel,
    postingViewModel: PostingViewModel,
    tokenViewModel: TokenViewModel,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    listViewModel: ListViewModel,
    commuViewModel: CommuViewModel,
    myId: Int,
    modifier: Modifier
) {
    when (getCardViewModel.getUserIdPostsUiState) {

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
                getCardViewModel = getCardViewModel,
                postingViewModel = postingViewModel,
                authViewModel = authViewModel,
                tokenViewModel = tokenViewModel,
                userViewModel = userViewModel,
                listViewModel = listViewModel,
                commuViewModel = commuViewModel,
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
@Composable
fun ProfileSuccessScreen(
    getCardViewModel: GetCardViewModel,
    postingViewModel: PostingViewModel,
    tokenViewModel: TokenViewModel,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    listViewModel: ListViewModel,
    commuViewModel: CommuViewModel,
//    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    myId: Int,
    modifier: Modifier
) {
    Log.d("HippoLog, ProfileScreen", "ProfileSuccessScreen")
    var posts by remember { mutableStateOf(listOf<RibbitPost>()) }
    if (getCardViewModel.getUserIdPostsUiState is GetUserIdPostsUiState.Success) {   // 원래 state 에 따라 넘어오기 때문에 확인할 필요가 없으나 state 에 무관하게 내려오는 문제가 있어 여기서 재확인
        posts = (getCardViewModel.getUserIdPostsUiState as GetUserIdPostsUiState.Success).posts
    }
    if (getCardViewModel.userIdClassificationUiState is UserIdClassificationUiState.Media) {    // useridClassificationUiState 가 Media 인 경우 아래의 필터 적용
        posts = posts.filter { (it.image != null) or (it.video != null) }   // image 나 video 가 null 이 아닌 경우만 뽑아서 리스트로 만든다.
    }
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
                commuViewModel = commuViewModel,
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
            ProfilePostsGrid(
                posts = posts,
                getCardViewModel = getCardViewModel,
                postingViewModel = postingViewModel,
                userViewModel = userViewModel,
                authViewModel = authViewModel,
                tokenViewModel = tokenViewModel,
                myId = myId,
                navController = navController,
                modifier = modifier
            )
        }
    }
}