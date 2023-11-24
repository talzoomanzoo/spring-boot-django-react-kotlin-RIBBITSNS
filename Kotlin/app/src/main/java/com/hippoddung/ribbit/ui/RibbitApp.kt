package com.hippoddung.ribbit.ui

import ChatScreen
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.network.bodys.User
import com.hippoddung.ribbit.ui.screens.CreatingPostScreen
import com.hippoddung.ribbit.ui.screens.EditingPostScreen
import com.hippoddung.ribbit.ui.screens.RibbitTopAppBar
import com.hippoddung.ribbit.ui.screens.authscreens.LoginScreen
import com.hippoddung.ribbit.ui.screens.authscreens.SignUpScreen
import com.hippoddung.ribbit.ui.screens.chatscreens.ChatRoomListScreen
import com.hippoddung.ribbit.ui.screens.chatscreens.CreateChatRoomScreen
import com.hippoddung.ribbit.ui.screens.commuscreens.CommuIdScreen
import com.hippoddung.ribbit.ui.screens.commuscreens.CommuScreen
import com.hippoddung.ribbit.ui.screens.commuscreens.CreatingCommuScreen
import com.hippoddung.ribbit.ui.screens.commuscreens.EditingCommuScreen
import com.hippoddung.ribbit.ui.screens.commuscreens.ManageCommuScreen
import com.hippoddung.ribbit.ui.screens.homescreens.HomeScreen
import com.hippoddung.ribbit.ui.screens.listscreens.CreatingListScreen
import com.hippoddung.ribbit.ui.screens.listscreens.EditingListScreen
import com.hippoddung.ribbit.ui.screens.listscreens.ListIdScreen
import com.hippoddung.ribbit.ui.screens.listscreens.ListScreen
import com.hippoddung.ribbit.ui.screens.postidscreen.PostIdScreen
import com.hippoddung.ribbit.ui.screens.profilescreens.EditProfileScreen
import com.hippoddung.ribbit.ui.screens.profilescreens.ProfileScreen
import com.hippoddung.ribbit.ui.screens.statescreens.ErrorScreen
import com.hippoddung.ribbit.ui.screens.statescreens.LoadingScreen
import com.hippoddung.ribbit.ui.viewmodel.AuthUiState
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.ChatViewModel
import com.hippoddung.ribbit.ui.viewmodel.CommuViewModel
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.ListViewModel
import com.hippoddung.ribbit.ui.viewmodel.MyProfileUiState
import com.hippoddung.ribbit.ui.viewmodel.PostingViewModel
import com.hippoddung.ribbit.ui.viewmodel.TokenViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

enum class RibbitScreen(@StringRes val title: Int) {
    HomeScreen(title = R.string.home_screen),
    PostIdScreen(title = R.string.post_id_screen),
    LoginScreen(title = R.string.login_screen),
    LogoutScreen(title = R.string.logout_screen),
    ProfileScreen(title = R.string.profile_screen),
    EditProfileScreen(title = R.string.edit_profile_screen),
    SignUpScreen(title = R.string.sign_up_screen),
    CreatingPostScreen(title = R.string.creating_post_screen),
    EditingPostScreen(title = R.string.editing_post_screen),
    ListScreen(title = R.string.list_screen),
    ListIdScreen(title = R.string.list_id_screen),
    CreatingListScreen(title = R.string.creating_list_screen),
    EditingListScreen(title = R.string.editing_list_screen),
    CommuScreen(title = R.string.commu_screen),
    CommuIdScreen(title = R.string.commu_id_screen),
    CreatingCommuScreen(title = R.string.creating_commu_screen),
    ManageCommuScreen(title = R.string.manage_commu_screen),
    EditingCommuScreen(title = R.string.editing_commu_screen),
    ChatRoomListScreen(title = R.string.chat_room_list_screen),
    CreateChatRoomScreen(title = R.string.create_chat_room_screen),
    ChatScreen(title = R.string.chat_screen),
    LoadingScreen(title = R.string.loading_screen),
    ErrorScreen(title = R.string.error_screen)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RibbitApp(
    getCardViewModel: GetCardViewModel,
    authViewModel: AuthViewModel,
    tokenViewModel: TokenViewModel,
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {
    when (authViewModel.authUiState) {
        is AuthUiState.Login -> {
            Log.d("HippoLog, RibbitApp", "Login")
            RibbitScreen(
                getCardViewModel = getCardViewModel,
                authViewModel = authViewModel,
                tokenViewModel = tokenViewModel,
                userViewModel = userViewModel,
                modifier = modifier
            )
        }

        is AuthUiState.Logout -> {
            Log.d("HippoLog, RibbitApp", "Logout")
            AuthScreen(
                authViewModel = authViewModel,
                modifier = modifier
            )
        }

        is AuthUiState.LoginLoading -> {
            Log.d("HippoLog, RibbitApp", "Loading")
            AuthLoadingScreen(modifier)
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RibbitScreen(
    getCardViewModel: GetCardViewModel,
    authViewModel: AuthViewModel,
    tokenViewModel: TokenViewModel,
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val listViewModel: ListViewModel = hiltViewModel()
    val commuViewModel: CommuViewModel = hiltViewModel()
    val postingViewModel: PostingViewModel = hiltViewModel()
    val chatViewModel: ChatViewModel = hiltViewModel()
    var myId by remember { mutableStateOf(0) }
    if (userViewModel.myProfileUiState is MyProfileUiState.Exist) {   // 앱 시작시 casting 이 문제되는 경우가 있어 state check 를 넣어줌.
        myId = (userViewModel.myProfileUiState as MyProfileUiState.Exist).myProfile.id!!
    }
    var myProfile by remember { mutableStateOf(User()) }
    if (userViewModel.myProfileUiState is MyProfileUiState.Exist) {   // 앱 시작시 casting 이 문제되는 경우가 있어 state check 를 넣어줌.
        myProfile = (userViewModel.myProfileUiState as MyProfileUiState.Exist).myProfile
    }
    // myId 는 다양한 페이지에서 쓰이므로 여기서 composable 에 기억시킨다.
    // myId 정보를 불러오지 못한 경우 화면 전환을 막았으므로 현재 반드시 있는 상황이다.
//    val backStackEntry by navController.currentBackStackEntryAsState()
//    val currentScreen = RibbitScreen.valueOf(backStackEntry?.destination?.route ?: RibbitScreen.HomeScreen.name)
//    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Log.d("HippoLog, RibbitApp", "RibbitScreen")
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
//        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        // scrollBehavior 에 따라 리컴포지션이 트리거되는 것으로 추측, 해결할 방법을 찾아야 함.
        // navigation 위(RibbitApp)에 있던 scrollBehavior 을 navigation 하위에 있는 HomeScreen 으로 옮겨서 해결.
        // scrollBehavior 을 삭제하고 고정시키는 것으로 결정
        topBar = {
            RibbitTopAppBar(
                drawerState = drawerState,
                scope = scope,
                getCardViewModel = getCardViewModel,
                userViewModel = userViewModel,
//                scrollBehavior = scrollBehavior,
                navController = navController,
                modifier = modifier
            )
        }
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            modifier = modifier
                .wrapContentSize()
                .padding(it),
            drawerContent = {
                ModalDrawerSheet(
                    modifier = modifier
                        .width(200.dp),
                ) {
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                    ) {
                        TextButton(
                            onClick = {
                                navController.navigate(RibbitScreen.HomeScreen.name)
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            },
                            content = {
                                Row(modifier = modifier) {
                                    Icon(
                                        imageVector = Icons.Default.Home,
                                        contentDescription = "Commu",
                                        tint = Color(0xFF006400),
                                        modifier = modifier
                                    )
                                    Spacer(modifier = modifier.padding(horizontal = 8.dp))
                                    Text(
                                        text = "Home",
                                        color = Color(0xFF006400),
                                        fontSize = 20.sp,
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = modifier
                                            .fillMaxWidth()
                                    )
                                }
                            },
                            modifier = modifier
                        )
                        TextButton(
                            onClick = {
                                navController.navigate(RibbitScreen.ChatRoomListScreen.name)
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            },
                            content = {
                                Row(modifier = modifier) {
                                    Icon(
                                        imageVector = Icons.Default.ChatBubble,
                                        contentDescription = "Chat",
                                        tint = Color(0xFF006400),
                                        modifier = modifier
                                    )
                                    Spacer(modifier = modifier.padding(horizontal = 8.dp))
                                    Text(
                                        text = "Chat",
                                        color = Color(0xFF006400),
                                        fontSize = 20.sp,
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = modifier
                                            .fillMaxWidth()
                                    )
                                }
                            },
                            modifier = modifier
                        )
                        TextButton(
                            onClick = {
                                navController.navigate(RibbitScreen.ListScreen.name)
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            },
                            content = {
                                Icon(
                                    imageVector = Icons.Default.FormatListBulleted,
                                    contentDescription = "List",
                                    tint = Color(0xFF006400),
                                    modifier = modifier
                                )
                                Spacer(modifier = modifier.padding(horizontal = 8.dp))
                                Text(
                                    text = "List",
                                    color = Color(0xFF006400),
                                    fontSize = 20.sp,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = modifier
                                        .fillMaxWidth()
                                )
                            },
                            modifier = modifier
                        )
                        TextButton(
                            onClick = {
                                navController.navigate(RibbitScreen.CommuScreen.name)
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            },
                            content = {
                                Row(modifier = modifier) {
                                    Icon(
                                        imageVector = Icons.Default.SupervisorAccount,
                                        contentDescription = "List",
                                        tint = Color(0xFF006400),
                                        modifier = modifier
                                    )
                                    Spacer(modifier = modifier.padding(horizontal = 8.dp))
                                    Text(
                                        text = "Community",
                                        color = Color(0xFF006400),
                                        fontSize = 20.sp,
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = modifier
                                            .fillMaxWidth()
                                    )
                                }
                            },
                            modifier = modifier
                        )
                        TextButton(
                            onClick = {
                                userViewModel.myProfile.value?.id?.let {
                                    getCardViewModel.getUserIdPosts(userId = it)
                                    userViewModel.getProfile(userId = it)
                                }   // userViewModel 의 user 가 없는 경우 접근 자체가 불가능
                                navController.navigate(RibbitScreen.ProfileScreen.name)
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            },
                            content = {
                                Row(modifier = modifier) {
                                    Icon(
                                        imageVector = Icons.Default.AccountCircle,
                                        contentDescription = "List",
                                        tint = Color(0xFF006400),
                                        modifier = modifier
                                    )
                                    Spacer(modifier = modifier.padding(horizontal = 8.dp))
                                    Text(
                                        text = "My Profile",
                                        color = Color(0xFF006400),
                                        fontSize = 20.sp,
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = modifier
                                            .fillMaxWidth()
                                    )
                                }
                            },
                            modifier = modifier
                        )
                        TextButton(
                            onClick = {
                                runBlocking {
                                    Log.d("HippoLog, HomeTopAppBar", "LogOut")
                                    userViewModel.resetMyProfile()   // 유저 정보 리셋
                                    authViewModel.deleteLoginInfo() // 로그인 정보 삭제
                                    tokenViewModel.deleteToken()    // 토큰 정보 삭제. token 을 먼저 지우면 다시 로그인 됨
                                }
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            },
                            content = {
                                Row(modifier = modifier) {
                                    Icon(
                                        imageVector = Icons.Default.Logout,
                                        contentDescription = "List",
                                        tint = Color(0xFF006400),
                                        modifier = modifier
                                    )
                                    Spacer(modifier = modifier.padding(horizontal = 8.dp))
                                    Text(
                                        text = "Log out",
                                        color = Color(0xFF006400),
                                        fontSize = 20.sp,
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = modifier
                                            .fillMaxWidth()
                                    )
                                }
                            },
                            modifier = modifier
                        )
                    }
                }
            }
        ) {
            NavHost(
                navController = navController,
                startDestination = RibbitScreen.HomeScreen.name,
                modifier = modifier
            ) {
                composable(route = RibbitScreen.HomeScreen.name) {
//            homeViewModel.getRibbitPosts() // recomposition 시 계속 실행됨. 여기 함수를 두면 안 됨. (수정: 반복 recomposition 을 해결하여 상관 없음.) NavHostController 호출시 항상 실행되는 문제
//                // navigate 메소드 호출시마다 backstack 으로 보내면서 다시 실행하므로 여기 함수를 두면 안됨.
                    Log.d("HippoLog, RibbitApp, NavHost", "RibbitScreen -> HomeScreen")
                    HomeScreen(
//                scrollBehavior = scrollBehavior,
                        navController = navController,
                        getCardViewModel = getCardViewModel,
                        postingViewModel = postingViewModel,
                        userViewModel = userViewModel,
                        myId = myId,   // myProfile 정보를 불러오지 못한 경우 화면 전환을 막았으므로 현재 반드시 있는 것으로 가정한다.
                        modifier = modifier
                    )
                }

                composable(route = RibbitScreen.PostIdScreen.name) {
                    Log.d("HippoLog, RibbitApp, NavHost", "RibbitScreen -> PostIdScreen")
                    PostIdScreen(
                        navController = navController,
                        getCardViewModel = getCardViewModel,
                        postingViewModel = postingViewModel,
                        userViewModel = userViewModel,
                        myId = myId,   // 유저 정보를 불러오지 못한 경우 화면 전환을 막았으므로 현재 반드시 있는 것으로 가정한다.
                        modifier = modifier
                    )
                }

                composable(route = RibbitScreen.ProfileScreen.name) {
                    Log.d("HippoLog, RibbitApp, NavHost", "RibbitScreen -> ProfileScreen")
                    ProfileScreen(
                        navController = navController,
                        getCardViewModel = getCardViewModel,
                        postingViewModel = postingViewModel,
                        tokenViewModel = tokenViewModel,
                        authViewModel = authViewModel,
                        userViewModel = userViewModel,
                        myId = myId,
                        modifier = modifier
                    )
                }
                composable(route = RibbitScreen.EditProfileScreen.name) {
                    Log.d("HippoLog, RibbitApp, NavHost", "RibbitScreen -> EditProfileScreen")
                    EditProfileScreen(
                        navController = navController,
                        userViewModel = userViewModel,
                        myId = myId,
                        modifier = modifier
                    )
                }

                composable(route = RibbitScreen.CreatingPostScreen.name) {
                    Log.d("HippoLog, RibbitApp, NavHost", "RibbitScreen -> CreatingPostScreen")
                    CreatingPostScreen(
                        getCardViewModel = getCardViewModel,
                        postingViewModel = postingViewModel,
                        commuViewModel = commuViewModel,
                        navController = navController,
                        modifier = modifier
                    )
                }
                composable(route = RibbitScreen.EditingPostScreen.name) {
                    Log.d("HippoLog, RibbitApp, NavHost", "RibbitScreen -> EditingPostScreen")
                    EditingPostScreen(
                        getCardViewModel = getCardViewModel,
                        postingViewModel = postingViewModel,
                        navController = navController,
                        modifier = modifier
                    )
                }

                composable(route = RibbitScreen.ListScreen.name) {
                    Log.d("HippoLog, RibbitApp, NavHost", "RibbitScreen -> ListScreen")
                    ListScreen(
                        navController = navController,
                        getCardViewModel = getCardViewModel,
                        userViewModel = userViewModel,
                        listViewModel = listViewModel,
                        modifier = modifier
                    )
                }
                composable(route = RibbitScreen.ListIdScreen.name) {
                    Log.d("HippoLog, RibbitApp, NavHost", "RibbitScreen -> ListIdScreen")
                    ListIdScreen(
                        navController = navController,
                        getCardViewModel = getCardViewModel,
                        postingViewModel = postingViewModel,
                        userViewModel = userViewModel,
                        listViewModel = listViewModel,
                        myId = myId,   // 유저 정보를 불러오지 못한 경우 화면 전환을 막았으므로 현재 반드시 있는 것으로 가정한다.
                        modifier = modifier
                    )
                }
                composable(route = RibbitScreen.CreatingListScreen.name) {
                    Log.d("HippoLog, RibbitApp, NavHost", "RibbitScreen -> CreatingListScreen")
                    CreatingListScreen(
                        navController = navController,
                        userViewModel = userViewModel,
                        listViewModel = listViewModel,
                        modifier = modifier
                    )
                }
                composable(route = RibbitScreen.EditingListScreen.name) {
                    Log.d("HippoLog, RibbitApp, NavHost", "RibbitScreen -> EditingListScreen")
                    EditingListScreen(
                        navController = navController,
                        listViewModel = listViewModel,
                        modifier = modifier
                    )
                }

                composable(route = RibbitScreen.CommuScreen.name) {
                    Log.d("HippoLog, RibbitApp, NavHost", "RibbitScreen -> CommuScreen")
                    CommuScreen(
                        navController = navController,
                        getCardViewModel = getCardViewModel,
                        postingViewModel = postingViewModel,
                        userViewModel = userViewModel,
                        commuViewModel = commuViewModel,
                        modifier = modifier
                    )
                }
                composable(route = RibbitScreen.CommuIdScreen.name) {
                    Log.d("HippoLog, RibbitApp, NavHost", "RibbitScreen -> CommuIdScreen")
                    CommuIdScreen(
                        navController = navController,
                        getCardViewModel = getCardViewModel,
                        userViewModel = userViewModel,
                        commuViewModel = commuViewModel,
                        postingViewModel = postingViewModel,
                        myId = myId,   // 유저 정보를 불러오지 못한 경우 화면 전환을 막았으므로 현재 반드시 있는 것으로 가정한다.
                        modifier = modifier
                    )
                }
                composable(route = RibbitScreen.CreatingCommuScreen.name) {
                    Log.d("HippoLog, RibbitApp, NavHost", "RibbitScreen -> CreatingCommuScreen")
                    CreatingCommuScreen(
                        navController = navController,
                        userViewModel = userViewModel,
                        commuViewModel = commuViewModel,
                        modifier = modifier
                    )
                }
                composable(route = RibbitScreen.ManageCommuScreen.name) {
                    Log.d("HippoLog, RibbitApp, NavHost", "RibbitScreen -> ManageCommuScreen")
                    ManageCommuScreen(
                        navController = navController,
                        userViewModel = userViewModel,
                        commuViewModel = commuViewModel,
                        modifier = modifier
                    )
                }
                composable(route = RibbitScreen.EditingCommuScreen.name) {
                    Log.d("HippoLog, RibbitApp, NavHost", "RibbitScreen -> EditingCommuScreen")
                    EditingCommuScreen(
                        navController = navController,
                        commuViewModel = commuViewModel,
                        modifier = modifier
                    )
                }

                composable(route = RibbitScreen.ChatRoomListScreen.name) {
                    Log.d("HippoLog, RibbitApp, NavHost", "RibbitScreen -> ChatRoomListScreen")
                    ChatRoomListScreen(
                        navController = navController,
                        chatViewModel = chatViewModel,
                        modifier = modifier
                    )
                }
                composable(route = RibbitScreen.CreateChatRoomScreen.name) {
                    Log.d("HippoLog, RibbitApp, NavHost", "RibbitScreen -> CreateChatRoomScreen")
                    CreateChatRoomScreen(
                        navController = navController,
                        chatViewModel = chatViewModel,
                        myProfile = myProfile,
                        modifier = modifier
                    )
                }
                composable(route = RibbitScreen.ChatScreen.name) {
                    Log.d("HippoLog, RibbitApp, NavHost", "RibbitScreen -> ChatScreen")
                    ChatScreen(
                        modifier = modifier,
                        chatViewModel = chatViewModel,
                        userViewModel = userViewModel,
                        myProfile = myProfile,
                    )
                }

                composable(route = RibbitScreen.LoadingScreen.name) {
                    Log.d("HippoLog, RibbitApp, NavHost", "RibbitScreen -> LoadingScreen")
                    LoadingScreen(
                        modifier = modifier
                    )
                }
                composable(route = RibbitScreen.ErrorScreen.name) {
                    Log.d("HippoLog, RibbitApp, NavHost", "RibbitScreen -> ErrorScreen")
                    ErrorScreen(
                        modifier = modifier
                    )
                }
            }
        }
    }
}

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
//    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
//
//    Scaffold(
//        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
//    ) {
//        Surface(
//            modifier = modifier
//                .fillMaxSize()
//                .padding(it)
//        ) {
    NavHost(
        navController = navController,
        startDestination = RibbitScreen.LoginScreen.name,
        modifier = modifier
    ) {
        composable(route = RibbitScreen.LoginScreen.name) {
            Log.d("HippoLog, RibbitApp, NavHost", "AuthScreen -> LoginScreen")
            LoginScreen(
                navController = navController,
                authViewModel = authViewModel,
                modifier = modifier
            )
        }
        composable(route = RibbitScreen.SignUpScreen.name) {
            Log.d("HippoLog, RibbitApp, NavHost", "AuthScreen -> SignUpScreen")
            SignUpScreen(
                authViewModel = authViewModel,
                modifier = modifier
            )
        }
    }
//        }
//    }
}

@Composable
fun AuthLoadingScreen(modifier: Modifier = Modifier) {
    Log.d("HippoLog, LoadingScreen", "로딩스크린")
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(id = R.string.loading)
    )
}