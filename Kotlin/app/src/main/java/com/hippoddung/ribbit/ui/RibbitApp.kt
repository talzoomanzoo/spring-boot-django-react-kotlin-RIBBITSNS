@file:OptIn(ExperimentalMaterial3Api::class)

package com.hippoddung.ribbit.ui

import androidx.annotation.StringRes
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.data.local.LoginUiState
import com.hippoddung.ribbit.ui.screens.HomeScreen
import com.hippoddung.ribbit.ui.screens.LogInScreen
import com.hippoddung.ribbit.ui.screens.ProfileScreen
import com.hippoddung.ribbit.ui.screens.SignUpScreen
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.HomeViewModel

enum class HippoScreen(@StringRes val title: Int) {
    HomeScreen(title = R.string.Home_screen),
    SignUpScreen(title = R.string.Sign_up_screen),
    LogInScreen(title = R.string.Login_screen),
    ProfileScreen(title = R.string.Profile_screen)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RibbitApp(
    navController: NavHostController = rememberNavController()
) {
    val navController = navController
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = HippoScreen.valueOf(
        backStackEntry?.destination?.route ?: HippoScreen.SignUpScreen.name
    )
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val loginUiState = LoginUiState()
    val isLogin: Boolean = loginUiState.isLogin
    val jwt: String = loginUiState.jwt

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
                startDestination =
                if (isLogin) HippoScreen.HomeScreen.name else HippoScreen.LogInScreen.name,
                modifier = Modifier
            ) {
                composable(route = HippoScreen.HomeScreen.name) {
                    val homeViewModel: HomeViewModel = viewModel()
                    HomeScreen(
                        homeUiState = homeViewModel.homeUiState
                    )
                }
                composable(route = HippoScreen.LogInScreen.name) {
                    val authViewModel: AuthViewModel = viewModel()
                    LogInScreen(
                        authViewModel
                    )
                }
                composable(route = HippoScreen.ProfileScreen.name) {
                    ProfileScreen()
                }
                composable(route = HippoScreen.SignUpScreen.name) {
                    SignUpScreen()
                }
            }
        }
    }
}

@Composable
fun HippoTopAppBar(scrollBehavior: TopAppBarScrollBehavior,navController: NavHostController, modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineSmall,
            )
        },
        navigationIcon = { ButtonWithDropDownMenu(navController) },
        modifier = modifier
    )
}

@Composable
fun ButtonWithDropDownMenu(navController: NavHostController, modifier: Modifier = Modifier) {
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
                navController.navigate(HippoScreen.HomeScreen.name)
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
                navController.navigate(HippoScreen.LogInScreen.name)
                isDropDownMenuExpanded = false
                      },
            text = {
                Text(
                    text = "LogIn",
                    color = Color.Blue,
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    style = TextStyle(shadow = Shadow(Color.Black))
                )
            }
        )
        DropdownMenuItem(
            onClick = {
                navController.navigate(HippoScreen.SignUpScreen.name)
                isDropDownMenuExpanded = false
                      },
            text = {
                Text(
                    text = "SignUp",
                    color = Color.Blue,
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    style = TextStyle(shadow = Shadow(Color.Black))
                )
            }
        )
        DropdownMenuItem(
            onClick = {
                navController.navigate(HippoScreen.ProfileScreen.name)
                isDropDownMenuExpanded = false
                      },
            text = {
                Text(
                    text = "Profile",
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




