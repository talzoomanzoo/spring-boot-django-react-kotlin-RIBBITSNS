package com.hippoddung.ribbit.ui.screens.searchitems

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.network.bodys.User
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState")
@Composable
fun SearchedGrid(
    sortedUsers: List<User>,
    sortedPosts: List<RibbitPost>,
    getCardViewModel: GetCardViewModel,
    userViewModel: UserViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
//            .verticalScroll(rememberScrollState())    // dropdown menu 에서 스크롤을 하는 것은 layout 구조상 불가능하다는 것 같음.
    ) {
        Column(modifier = modifier) {
            sortedUsers.forEach { user ->
                SearchedUserCard(
                    user = user,
                    getCardViewModel = getCardViewModel,
                    navController = navController,
                    userViewModel = userViewModel,
                    modifier = modifier
//                        .verticalScroll(rememberScrollState())
                )
            }
        }
        Column(modifier = modifier) {
            sortedPosts.forEach { post ->
                SearchedPostCard(
                    post = post,
                    getCardViewModel = getCardViewModel,
                    navController = navController,
                    modifier = modifier
//                        .verticalScroll(rememberScrollState())
                )
            }
        }
    }
}