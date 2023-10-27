package com.hippoddung.ribbit.ui.screens.screenitems

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    homeViewModel: HomeViewModel,
    authViewModel: AuthViewModel,
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Log.d("HippoLog, HomeTopAppBar", "HomeTopAppBar")
    Column {
        CenterAlignedTopAppBar(
            scrollBehavior = scrollBehavior,
            title = {
                Box {
                    TextButton(onClick = {
                        navController.navigate(RibbitScreen.HomeScreen.name)
                        homeViewModel.getRibbitPosts()
                    }) {
                        Text(
                            text = stringResource(R.string.app_name),
                            color = Color(0xFF006400),
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                }
            },
            navigationIcon = { MainDropDownMenu(navController) },
            actions = { ProfileDropDownMenu(navController) },
            modifier = modifier
        )
//        AdBanner() // 불러오는 중 TimeOut이 자주 발생
    }
}

@Composable
fun MainDropDownMenu(navController: NavHostController) {
    var isDropDownMenuExpanded by remember { mutableStateOf(false) }

    OutlinedButton(
        onClick = { isDropDownMenuExpanded = true }
    ) {
        Text(text = "Menu",
            color = Color(0xFF006400))
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
                    color = Color(0xFF006400),
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.labelSmall
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
                    color = Color(0xFF006400),
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        )
    }
}

@Composable
fun ProfileDropDownMenu(navController: NavHostController) {
    var isDropDownMenuExpanded by remember { mutableStateOf(false) }

    OutlinedButton(
        onClick = { isDropDownMenuExpanded = true }
    ) {
        Text(text = "Profile",
            color = Color(0xFF006400))
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
                    color = Color(0xFF006400),
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.labelSmall
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
                    color = Color(0xFF006400),
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        )
    }
}

//@Composable
//fun AdBanner(modifier: Modifier = Modifier) {
//    AndroidView(
//        modifier = modifier,
//        factory = { context ->
//            AdView(context).apply {
//                setAdSize(AdSize.FULL_BANNER)
//                // Use test ad unit ID
//                adUnitId = "ca-app-pub-3940256099942544/6300978111"
//            }
//        },
//        update = { adView ->
//            adView.loadAd(AdRequest.Builder().build())
//        }
//    )
//}