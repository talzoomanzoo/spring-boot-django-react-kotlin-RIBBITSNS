@file:OptIn(ExperimentalMaterial3Api::class)

package com.hippoddung.ribbit.ui

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.ui.screens.HomeScreen
import com.hippoddung.ribbit.ui.screens.ProfileScreen
import com.hippoddung.ribbit.ui.screens.TwitCreateScreen
import com.hippoddung.ribbit.ui.screens.authscreens.LoginScreen
import com.hippoddung.ribbit.ui.screens.authscreens.LogoutScreen
import com.hippoddung.ribbit.ui.screens.authscreens.SignUpScreen
import com.hippoddung.ribbit.ui.viewmodel.AuthUiState
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.HomeViewModel

enum class RibbitScreen(@StringRes val title: Int) {
    HomeScreen(title = R.string.home_screen),
    LoginScreen(title = R.string.login_screen),
    LogoutScreen(title = R.string.logout_screen),
    ProfileScreen(title = R.string.profile_screen),
    SignUpScreen(title = R.string.sign_up_screen),
    TwitCreateScreen(title = R.string.twit_create_screen),
    PickImageScreen(title = R.string.pick_image_screen),
}

@Composable
fun RibbitApp(homeViewModel: HomeViewModel) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val navController: NavHostController = rememberNavController()

    when (authViewModel.authUiState) {
        is AuthUiState.Login -> {
            RibbitScreen(navController, homeViewModel)
        }
        is AuthUiState.Logout -> {
            AuthScreen(navController, authViewModel)
            Log.d("HippoLog, RibbitApp", "Fail")
        }
    }
}

@Composable
fun RibbitScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel
) {
//    val backStackEntry by navController.currentBackStackEntryAsState()
//    val currentScreen = RibbitScreen.valueOf(backStackEntry?.destination?.route ?: RibbitScreen.SignUpScreen.name)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { HippoTopAppBar(scrollBehavior = scrollBehavior, navController = navController) }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            NavHost(
                navController = navController,
                startDestination = RibbitScreen.HomeScreen.name,
                modifier = Modifier
            ) {
                composable(route = RibbitScreen.HomeScreen.name) {
                    HomeScreen(
                        navController = navController,
                        homeViewModel = homeViewModel
                    )
                }
                composable(route = RibbitScreen.ProfileScreen.name) {
                    ProfileScreen()
                }
                composable(route = RibbitScreen.TwitCreateScreen.name) {
                    TwitCreateScreen(navController)
                }
                composable(route = RibbitScreen.LogoutScreen.name) {
                    LogoutScreen()
                }
            }
        }
    }
}

@Composable
fun AuthScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            NavHost(
                navController = navController,
                startDestination = RibbitScreen.LoginScreen.name,
                modifier = Modifier
            ) {
                composable(route = RibbitScreen.LoginScreen.name) {
                    LoginScreen(navController, authViewModel)
                }
                composable(route = RibbitScreen.SignUpScreen.name) {
                    SignUpScreen()
                }
            }
        }
    }
}

@Composable
fun HippoTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Column {
        CenterAlignedTopAppBar(
            scrollBehavior = scrollBehavior,
            title = {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineSmall,
                )
            },
            navigationIcon = { MainDropDownMenu(navController) },
            actions = { ProfileDropDownMenu(navController) },
            modifier = modifier
        )
        AdBanner()
    }
}

@Composable
fun MainDropDownMenu(navController: NavHostController, modifier: Modifier = Modifier) {
    var isDropDownMenuExpanded by remember { mutableStateOf(false) }

    Button(
        onClick = { isDropDownMenuExpanded = true }
    ) {
        Text(text = "Menu")
    }

    DropdownMenu(
        expanded = isDropDownMenuExpanded,
        onDismissRequest = { isDropDownMenuExpanded = false },
        modifier = Modifier
            .wrapContentSize()
            .padding(4.dp)
    ) {
        DropdownMenuItem(
            onClick = {
                navController.navigate(RibbitScreen.HomeScreen.name)
                isDropDownMenuExpanded = false
            },
            text = {
                Text(
                    text = "Home",
                    color = Color.Blue,
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    style = TextStyle(shadow = Shadow(Color.Black))
                )
            }
        )
        DropdownMenuItem(
            onClick = {
                println("Hello 5")
                isDropDownMenuExpanded = false
            },
            text = {
                Text(
                    text = "Print Hello 5",
                    color = Color.Blue,
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(3f, 3f),
                            blurRadius = 3f
                        )
                    )
                )
            }
        )
    }
}

@Composable
fun ProfileDropDownMenu(navController: NavHostController, modifier: Modifier = Modifier) {
    var isDropDownMenuExpanded by remember { mutableStateOf(false) }

    Button(
        onClick = { isDropDownMenuExpanded = true }
    ) {
        Text(text = "Profile")
    }

    DropdownMenu(
        expanded = isDropDownMenuExpanded,
        onDismissRequest = { isDropDownMenuExpanded = false },
        modifier = Modifier
            .wrapContentSize()
            .padding(4.dp)
    ) {
        DropdownMenuItem(
            onClick = {
                navController.navigate(RibbitScreen.ProfileScreen.name)
                isDropDownMenuExpanded = false
            },
            text = {
                Text(
                    text = "My Profile",
                    color = Color.Blue,
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    style = TextStyle(shadow = Shadow(Color.Black))
                )
            }
        )
        DropdownMenuItem(
            onClick = {
                navController.navigate(RibbitScreen.LogoutScreen.name)
                isDropDownMenuExpanded = false
            },
            text = {
                Text(
                    text = "Log out",
                    color = Color.Blue,
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    style = TextStyle(shadow = Shadow(Color.Black))
                )
            }
        )
    }
}

@Composable
fun AdBanner(modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.FULL_BANNER)
                // Use test ad unit ID
                adUnitId = "ca-app-pub-3940256099942544/6300978111"
            }
        },
        update = { adView ->
            adView.loadAd(AdRequest.Builder().build())
        }
    )
}