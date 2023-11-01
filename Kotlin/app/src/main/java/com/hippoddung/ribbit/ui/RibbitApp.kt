@file:OptIn(ExperimentalMaterial3Api::class)

package com.hippoddung.ribbit.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.ui.screens.CreatingPostScreen
import com.hippoddung.ribbit.ui.screens.HomeScreen
import com.hippoddung.ribbit.ui.screens.PostIdScreen
import com.hippoddung.ribbit.ui.screens.ProfileScreen
import com.hippoddung.ribbit.ui.screens.authscreens.LoginScreen
import com.hippoddung.ribbit.ui.screens.authscreens.SignUpScreen
import com.hippoddung.ribbit.ui.screens.statescreens.ErrorScreen
import com.hippoddung.ribbit.ui.screens.statescreens.LoadingScreen
import com.hippoddung.ribbit.ui.viewmodel.AuthUiState
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.CardViewModel
import com.hippoddung.ribbit.ui.viewmodel.CreatingPostViewModel
import com.hippoddung.ribbit.ui.viewmodel.TokenViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserUiState
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel

enum class RibbitScreen(@StringRes val title: Int) {
    HomeScreen(title = R.string.home_screen),
    PostIdScreen(title = R.string.post_id_screen),
    LoginScreen(title = R.string.login_screen),
    LogoutScreen(title = R.string.logout_screen),
    ProfileScreen(title = R.string.profile_screen),
    SignUpScreen(title = R.string.sign_up_screen),
    CreatingPostScreen(title = R.string.creating_post_screen),
    LoadingScreen(title = R.string.loading_screen),
    ErrorScreen(title = R.string.error_screen)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RibbitApp(
    cardViewModel: CardViewModel,
    authViewModel: AuthViewModel,
    tokenViewModel: TokenViewModel,
    creatingPostViewModel: CreatingPostViewModel,
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {
    when (authViewModel.authUiState) {
        is AuthUiState.Login -> {
            Log.d("HippoLog, RibbitApp", "Login")
            RibbitScreen(cardViewModel, authViewModel, tokenViewModel, creatingPostViewModel, userViewModel, modifier)
        }

        is AuthUiState.Logout -> {
            Log.d("HippoLog, RibbitApp", "Logout")
            AuthScreen(authViewModel, modifier)
        }

        is AuthUiState.LoginLoading ->{
            Log.d("HippoLog, RibbitApp", "Loading")
            AuthLoadingScreen(modifier)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RibbitScreen(
    cardViewModel: CardViewModel,
    authViewModel: AuthViewModel,
    tokenViewModel: TokenViewModel,
    creatingPostViewModel: CreatingPostViewModel,
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {
//    val backStackEntry by navController.currentBackStackEntryAsState()
//    val currentScreen = RibbitScreen.valueOf(backStackEntry?.destination?.route ?: RibbitScreen.SignUpScreen.name)
    val navController: NavHostController = rememberNavController()
//    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    NavHost(
        navController = navController,
        startDestination = RibbitScreen.HomeScreen.name,
        modifier = modifier
    ) {
        composable(route = RibbitScreen.HomeScreen.name) {
//            homeViewModel.getRibbitPosts() // recompositon시 계속 실행됨. 여기 함수를 두면 안 됨. (수정: 반복 recomposition을 해결하여 상관 없음.) NavHostController 호출시 항상 실행되는 문제
            Log.d("HippoLog, RibbitApp, RibbitScreen", "HomeScreen")
            HomeScreen(
//                scrollBehavior = scrollBehavior,
                navController = navController,
                cardViewModel = cardViewModel,
                tokenViewModel = tokenViewModel,
                authViewModel = authViewModel,
                userViewModel = userViewModel,
                creatingPostViewModel = creatingPostViewModel,
                userId = ((userViewModel.userUiState as UserUiState.Exist).user.id)!!,   // 유저 정보를 불러오지 못한 경우 화면 전환을 막았으므로 현재 반드시 있는 것으로 가정한다.
                modifier = modifier
            )
        }
        composable(route = RibbitScreen.PostIdScreen.name) {
            Log.d("HippoLog, RibbitApp, RibbitScreen", "TwitIdScreen")
            PostIdScreen(
//                scrollBehavior = scrollBehavior,
                navController = navController,
                cardViewModel = cardViewModel,
                tokenViewModel = tokenViewModel,
                authViewModel = authViewModel,
                userViewModel = userViewModel,
                creatingPostViewModel = creatingPostViewModel,
                userId = ((userViewModel.userUiState as UserUiState.Exist).user.id)!!,   // 유저 정보를 불러오지 못한 경우 화면 전환을 막았으므로 현재 반드시 있는 것으로 가정한다.
                modifier = modifier
            )
        }
        composable(route = RibbitScreen.ProfileScreen.name) {
            Log.d("HippoLog, RibbitApp, RibbitScreen", "ProfileScreen")
            ProfileScreen(
//                scrollBehavior = scrollBehavior,
                navController = navController,
                cardViewModel = cardViewModel,
                tokenViewModel = tokenViewModel,
                authViewModel = authViewModel,
                userViewModel = userViewModel,
                userId = ((userViewModel.userUiState as UserUiState.Exist).user.id)!!,   // 유저 정보를 불러오지 못한 경우 화면 전환을 막았으므로 현재 반드시 있는 것으로 가정한다.
                modifier = modifier
            )
        }
        composable(route = RibbitScreen.CreatingPostScreen.name) {
            Log.d("HippoLog, RibbitApp, RibbitScreen", "TwitCreateScreen")
            CreatingPostScreen(
                creatingPostViewModel = creatingPostViewModel,
                cardViewModel = cardViewModel,
                navController = navController,
                modifier = modifier
            )
        }
        composable(route = RibbitScreen.LoadingScreen.name) {
            Log.d("HippoLog, RibbitApp, RibbitScreen", "LoadingScreen")
            LoadingScreen(
                modifier = modifier
            )
        }
        composable(route = RibbitScreen.ErrorScreen.name) {
            Log.d("HippoLog, RibbitApp, RibbitScreen", "ErrorScreen")
            ErrorScreen(
                modifier = modifier
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val navController: NavHostController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        ) {
            NavHost(
                navController = navController,
                startDestination = RibbitScreen.LoginScreen.name,
                modifier = modifier
            ) {
                composable(route = RibbitScreen.LoginScreen.name) {
                    Log.d("HippoLog, RibbitApp, AuthScreen", "LoginScreen")
                    LoginScreen(
                        navController = navController,
                        authViewModel = authViewModel,
                        modifier = modifier
                    )
                }
                composable(route = RibbitScreen.SignUpScreen.name) {
                    Log.d("HippoLog, RibbitApp, AuthScreen", "SignUpScreen")
                    SignUpScreen(
                        authViewModel = authViewModel,
                        modifier = modifier
                    )
                }
            }
        }
    }
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