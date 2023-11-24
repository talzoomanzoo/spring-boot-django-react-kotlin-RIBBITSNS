package com.hippoddung.ribbit.ui.screens.homescreens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.screens.RibbitTopAppBar
import com.hippoddung.ribbit.ui.screens.statescreens.ErrorScreen
import com.hippoddung.ribbit.ui.screens.statescreens.LoadingScreen
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.CommuViewModel
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.HomeUiState
import com.hippoddung.ribbit.ui.viewmodel.ListViewModel
import com.hippoddung.ribbit.ui.viewmodel.PostingViewModel
import com.hippoddung.ribbit.ui.viewmodel.TokenViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
//    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    getCardViewModel: GetCardViewModel,
    postingViewModel: PostingViewModel,
    userViewModel: UserViewModel,
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
                postingViewModel = postingViewModel,
                userViewModel = userViewModel,
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
    postingViewModel: PostingViewModel,
    userViewModel: UserViewModel,
//    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    ribbitPosts: List<RibbitPost>,
    myId: Int,
    modifier: Modifier
) {

//    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//    val scope = rememberCoroutineScope()
//    ModalNavigationDrawer(
//        drawerState = drawerState,
//        modifier = modifier
//            .wrapContentSize()
//            .padding(4.dp),
//        drawerContent = {
//            ModalDrawerSheet {
//                Column(modifier = modifier) {
//                    TextButton(
//                        onClick = {
//                            navController.navigate(RibbitScreen.HomeScreen.name)
//                            scope.launch {
//                                drawerState.apply {
//                                    if (isClosed) open() else close()
//                                }
//                            }
//                        },
//                        content = {
//                            Text(
//                                text = "Home",
//                                color = Color(0xFF006400),
//                                fontSize = 14.sp,
//                                style = MaterialTheme.typography.labelSmall,
//                                modifier = modifier
//                            )
//                        },
//                        modifier = modifier
//                    )
//                    TextButton(
//                        onClick = {
//                            navController.navigate(RibbitScreen.ChatRoomListScreen.name)
//                            scope.launch {
//                                drawerState.apply {
//                                    if (isClosed) open() else close()
//                                }
//                            }
//                        },
//                        content = {
//                            Text(
//                                text = "Chat",
//                                color = Color(0xFF006400),
//                                fontSize = 14.sp,
//                                style = MaterialTheme.typography.labelSmall,
//                                modifier = modifier
//                            )
//                        },
//                        modifier = modifier
//                    )
//                    TextButton(
//                        onClick = {
//                            userViewModel.myProfile.value?.id?.let {
//                                getCardViewModel.getUserIdPosts(userId = it)
//                                userViewModel.getProfile(userId = it)
//                            }   // userViewModel 의 user 가 없는 경우 접근 자체가 불가능
//                            navController.navigate(RibbitScreen.ProfileScreen.name)
//                            scope.launch {
//                                drawerState.apply {
//                                    if (isClosed) open() else close()
//                                }
//                            }
//                        },
//                        content = {
//                            Text(
//                                text = "My Profile",
//                                color = Color(0xFF006400),
//                                fontSize = 14.sp,
//                                style = MaterialTheme.typography.labelSmall,
//                                modifier = modifier
//                            )
//                        },
//                        modifier = modifier
//                    )
//                    TextButton(
//                        onClick = {
//                            runBlocking {
//                                Log.d("HippoLog, HomeTopAppBar", "LogOut")
//                                userViewModel.resetMyProfile()   // 유저 정보 리셋
//                                authViewModel.deleteLoginInfo() // 로그인 정보 삭제
//                                tokenViewModel.deleteToken()    // 토큰 정보 삭제. token 을 먼저 지우면 다시 로그인 됨
//                            }
//                            scope.launch {
//                                drawerState.apply {
//                                    if (isClosed) open() else close()
//                                }
//                            }
//                        },
//                        content = {
//                            Text(
//                                text = "Log out",
//                                color = Color(0xFF006400),
//                                fontSize = 14.sp,
//                                style = MaterialTheme.typography.labelSmall,
//                                modifier = modifier
//                            )
//                        },
//                        modifier = modifier
//                    )
//                }
//            }
//        }
//    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentScreen =
            RibbitScreen.valueOf(backStackEntry?.destination?.route ?: RibbitScreen.HomeScreen.name)
        postingViewModel.setCurrentScreen(currentScreen)    // homeScreen 진입 성공시 현재 screen 정보 저장.
        getCardViewModel.setCurrentScreen(currentScreen)    // homeScreen 진입 성공시 현재 screen 정보 저장.
        Surface(
            modifier = modifier
                .fillMaxSize()
        ) {
            Box(modifier = modifier) {
                PostsGrid(
                    posts = ribbitPosts,
                    getCardViewModel = getCardViewModel,
                    postingViewModel = postingViewModel,
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
                        contentDescription = "Creating Post Screen.",
                        modifier = modifier
                    )
                }
            }
        }
//    }
}