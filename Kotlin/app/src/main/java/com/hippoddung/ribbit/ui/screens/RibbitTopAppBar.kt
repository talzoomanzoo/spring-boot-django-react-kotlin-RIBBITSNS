package com.hippoddung.ribbit.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.network.bodys.User
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.screens.searchitems.SearchedGrid
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.CommuClassificationUiState
import com.hippoddung.ribbit.ui.viewmodel.CommuViewModel
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.ListViewModel
import com.hippoddung.ribbit.ui.viewmodel.MyProfileUiState
import com.hippoddung.ribbit.ui.viewmodel.TokenViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RibbitTopAppBar(
    drawerState: DrawerState,
    scope: CoroutineScope,
    getCardViewModel: GetCardViewModel,
    userViewModel: UserViewModel,
//    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Log.d("HippoLog, RibbitTopAppBar", "RibbitTopAppBar")
    Column(modifier = modifier) {
        CenterAlignedTopAppBar(
//            scrollBehavior = scrollBehavior,    // scroll 에 따라 TopAppBar 가 보여지거나 숨겨지게 해주는 기능이나 recomposition 을 과도하게 발생 시키는 문제가 있어 잠그기로 함.
            title = {
                Box(modifier = modifier) {
                    TextButton(
                        onClick = {
                            navController.navigate(RibbitScreen.HomeScreen.name)
                            getCardViewModel.getRibbitPosts()
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
            navigationIcon = {
                MainSideBarMenu(
                    drawerState = drawerState,
                    scope = scope,
                    navController = navController,
                    modifier = modifier
                )
            },
            actions = {
                RibbitSearchBar(
                    navController = navController,
                    userViewModel = userViewModel,
                    getCardViewModel = getCardViewModel,
                    modifier = modifier
                )
            },
            modifier = modifier
        )
//        AdBanner() // 불러오는 중 TimeOut 이 자주 발생
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
fun MainSideBarMenu(
    drawerState: DrawerState,
    scope: CoroutineScope,
    navController: NavHostController,
    modifier: Modifier
) {

//    var isDropDownMenuExpanded by remember {mutableStateOf(false)}
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        IconButton(
            onClick = {
                scope.launch(Dispatchers.IO) {
                    drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }
            },
            modifier = modifier.padding(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = Color(0xFF006400),
                modifier = modifier
            )
        }
    }

//    DropdownMenu(
//        expanded = isDropDownMenuExpanded,
//        onDismissRequest = { isDropDownMenuExpanded = false },
//        modifier = modifier
//            .wrapContentSize()
//            .padding(4.dp)
//    ) {
//        DropdownMenuItem(
//            onClick = {
//                navController.navigate(RibbitScreen.HomeScreen.name)
//                isDropDownMenuExpanded = false
//            },
//            text = {
//                Text(
//                    text = "Home",
//                    color = Color(0xFF006400),
//                    fontSize = 14.sp,
//                    style = MaterialTheme.typography.labelSmall,
//                    modifier = modifier
//                )
//            },
//            modifier = modifier
//        )
//        DropdownMenuItem(
//            onClick = {
//                userViewModel.myProfile.value?.id?.let {
//                    getCardViewModel.getUserIdPosts(userId = it)
//                    userViewModel.getProfile(userId = it)
//                }   // userViewModel 의 user 가 없는 경우 접근 자체가 불가능
//                navController.navigate(RibbitScreen.ProfileScreen.name)
//                isDropDownMenuExpanded = false
//            },
//            text = {
//                Text(
//                    text = "My Profile",
//                    color = Color(0xFF006400),
//                    fontSize = 14.sp,
//                    style = MaterialTheme.typography.labelSmall,
//                    modifier = modifier
//                )
//            },
//            modifier = modifier
//        )
//        DropdownMenuItem(
//            onClick = {
//                runBlocking {
//                    Log.d("HippoLog, HomeTopAppBar", "LogOut")
//                    userViewModel.resetMyProfile()   // 유저 정보 리셋
//                    authViewModel.deleteLoginInfo() // 로그인 정보 삭제
//                    tokenViewModel.deleteToken()    // 토큰 정보 삭제. token 을 먼저 지우면 다시 로그인 됨
//                }
//                isDropDownMenuExpanded = false
//            },
//            text = {
//                Text(
//                    text = "Log out",
//                    color = Color(0xFF006400),
//                    fontSize = 14.sp,
//                    style = MaterialTheme.typography.labelSmall,
//                    modifier = modifier
//                )
//            },
//            modifier = modifier
//        )
//        DropdownMenuItem(
//            onClick = {
//                navController.navigate(RibbitScreen.ChatRoomListScreen.name)
//                isDropDownMenuExpanded = false
//            },
//            text = {
//                Text(
//                    text = "Chat",
//                    color = Color(0xFF006400),
//                    fontSize = 14.sp,
//                    style = MaterialTheme.typography.labelSmall,
//                    modifier = modifier
//                )
//            },
//            modifier = modifier
//        )
//    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RibbitSearchBar(
    navController: NavHostController,
    userViewModel: UserViewModel,
    getCardViewModel: GetCardViewModel,
    modifier: Modifier
) {
    var isExpanded = remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf(TextFieldValue()) }
    var isSearched by remember { mutableStateOf(false) }
    val usersSearchData by userViewModel.usersSearchData.collectAsState()
    val postsSearchData by getCardViewModel.postsSearchData.collectAsState()

    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        IconButton(
            onClick = {
                isExpanded.value = !isExpanded.value
            },
            modifier = modifier.padding(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color(0xFF006400),
                modifier = modifier
            )
        }
    }

    DropdownMenu(
        expanded = isExpanded.value,
        onDismissRequest = { isExpanded.value = false },
//        offset = DpOffset(0.dp, 0.dp),
        modifier = modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
            .fillMaxWidth()
    ) {
        val userComparator = compareByDescending<User> { it.id }
        val sortedUsersSearch = remember(usersSearchData, userComparator) {
            usersSearchData.sortedWith(userComparator)
        }   // LazyColumn items 에 List 를 바로 주는 것이 아니라 Comparator 로 정렬하여 remember 로 기억시켜서 recomposition 을 방지하여 성능을 올린다.
        val postComparator = compareByDescending<RibbitPost> { it.id }
        val sortedPostsSearch = remember(postsSearchData, postComparator) {
            postsSearchData.sortedWith(postComparator)
        }   // LazyColumn items 에 List 를 바로 주는 것이 아니라 Comparator 로 정렬하여 remember 로 기억시켜서 recomposition 을 방지하여 성능을 올린다.
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                if (it.text.isNotEmpty()) {
                    // 사용자가 검색어를 입력하면 여기에서 작업을 수행할 수 있음
                    // 예: 자동완성 결과 업데이트, 네트워크 요청 등
                    userViewModel.getUsersSearch(it.text)
                    getCardViewModel.getPostsSearch(it.text)
                    isSearched = true
                }
            },
            label = {
                Text(
                    text = "Search User and Ribbit",
                    modifier = modifier
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color(0xFF006400),
                    modifier = modifier
                )
            },
            trailingIcon = {
                Row(modifier = modifier) {
//                    OutlinedButton(
//                        onClick = {
//                            isExpanded = !isExpanded
//                        },
//                        modifier = modifier
//                    ) {
//                        Text(
//                            text = "Search",
//                            modifier = modifier
//                        )
//                    }
                    IconButton(
                        onClick = {
                            isExpanded.value = !isExpanded.value
                        },
                        modifier = modifier.padding(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Collapse",
                            tint = Color(0xFF006400),
                            modifier = modifier
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            singleLine = true,
            textStyle = TextStyle(fontSize = 16.sp),
            modifier = modifier
                .fillMaxWidth()
                .padding(4.dp)
                .background(Color.White)
        )
        if (isSearched) {
            Box(
                modifier = modifier
                    .sizeIn(maxWidth = Dp.Infinity, maxHeight = Dp.Infinity)
            ) {
                SearchedGrid(
                    isExpanded = isExpanded,
                    sortedUsersSearch = sortedUsersSearch,
                    sortedPostsSearch = sortedPostsSearch,
                    getCardViewModel = getCardViewModel,
                    userViewModel = userViewModel,
                    navController = navController,
                    modifier = modifier
                )
            }
        }
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