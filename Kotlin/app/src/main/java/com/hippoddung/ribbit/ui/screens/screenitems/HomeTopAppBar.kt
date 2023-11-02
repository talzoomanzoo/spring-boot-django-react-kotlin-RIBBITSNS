package com.hippoddung.ribbit.ui.screens.screenitems

import android.util.Log
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.CardViewModel
import com.hippoddung.ribbit.ui.viewmodel.TokenViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    cardViewModel: CardViewModel,
    authViewModel: AuthViewModel,
    tokenViewModel: TokenViewModel,
    userViewModel: UserViewModel,
//    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Log.d("HippoLog, HomeTopAppBar", "HomeTopAppBar")
    Column(modifier = modifier) {
        CenterAlignedTopAppBar(
//            scrollBehavior = scrollBehavior,    // scroll에 따라 TopAppBar가 보여지거나 숨겨지게 해주는 기능이나 recompostion을 과도하게 발생시키는 문제가 있어 잠그기로 함.
            title = {
                Box(modifier = modifier) {
                    TextButton(
                        onClick = {
                            navController.navigate(RibbitScreen.HomeScreen.name)
                            cardViewModel.getRibbitPosts()
                        },
                        modifier = modifier
                    ) {
                        Text(
                            text = stringResource(R.string.app_name),
                            color = Color(0xFF006400),
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = modifier
                        )
                    }
                }
            },
            navigationIcon = { MainDropDownMenu(navController, modifier) },
            actions = {
                ProfileDropDownMenu(
                    navController,
                    tokenViewModel,
                    authViewModel,
                    cardViewModel,
                    userViewModel,
                    modifier = modifier
                )
            },
            modifier = modifier
        )
//        AdBanner() // 불러오는 중 TimeOut이 자주 발생
        Canvas(
            modifier = modifier,
            onDraw = {
                drawLine(
                    color = Color(0xFF4c6c4a),
                    start = Offset(0.dp.toPx(), 0.dp.toPx()),
                    end = Offset(500.dp.toPx(), 0.dp.toPx()),
                    strokeWidth = 1.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        )
    }
}

@Composable
fun MainDropDownMenu(
    navController: NavHostController,
    modifier: Modifier
) {
    var isDropDownMenuExpanded by remember { mutableStateOf(false) }

    OutlinedButton(
        onClick = { isDropDownMenuExpanded = true },
        modifier = modifier
    ) {
        Text(
            text = "Menu",
            color = Color(0xFF006400),
            modifier = modifier
        )
    }

    DropdownMenu(
        expanded = isDropDownMenuExpanded,
        onDismissRequest = { isDropDownMenuExpanded = false },
        modifier = modifier
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
                    style = MaterialTheme.typography.labelSmall,
                    modifier = modifier
                )
            },
            modifier = modifier
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
                    style = MaterialTheme.typography.labelSmall,
                    modifier = modifier
                )
            },
            modifier = modifier
        )
    }
}

@Composable
fun ProfileDropDownMenu(
    navController: NavHostController,
    tokenViewModel: TokenViewModel,
    authViewModel: AuthViewModel,
    cardViewModel: CardViewModel,
    userViewModel: UserViewModel,
    modifier: Modifier
) {
    var isDropDownMenuExpanded by remember { mutableStateOf(false) }

    OutlinedButton(
        onClick = { isDropDownMenuExpanded = true },
        modifier = modifier
    ) {
        Text(
            text = "Profile",
            color = Color(0xFF006400),
            modifier = modifier
        )
    }

    DropdownMenu(
        expanded = isDropDownMenuExpanded,
        onDismissRequest = { isDropDownMenuExpanded = false },
        modifier = modifier
            .wrapContentSize()
            .padding(4.dp)
    ) {
        DropdownMenuItem(
            onClick = {
                userViewModel.myProfile.value?.id?.let {
                    cardViewModel.getUserIdPosts(userId = it)
                    userViewModel.getProfile(userId = it)
                }   // userViewModel의 user가 없는 경우 접근 자체가 불가능
                navController.navigate(RibbitScreen.ProfileScreen.name)
                isDropDownMenuExpanded = false
            },
            text = {
                Text(
                    text = "My Profile",
                    color = Color(0xFF006400),
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = modifier
                )
            },
            modifier = modifier
        )
        DropdownMenuItem(
            onClick = {
                runBlocking {
                    Log.d("HippoLog, HomeTopAppBar", "LogOut")
                    launch {
                        userViewModel.resetMyProfile()   // 유저 정보 리셋
                        authViewModel.deleteLoginInfo() // 로그인 정보 삭제
                        tokenViewModel.deleteToken()    // 토큰 정보 삭제. token을 먼저 지우면 다시 로그인 됨
                    }
                }
                isDropDownMenuExpanded = false
            },
            text = {
                Text(
                    text = "Log out",
                    color = Color(0xFF006400),
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = modifier
                )
            },
            modifier = modifier
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