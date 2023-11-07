package com.hippoddung.ribbit.ui.screens.screenitems

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.network.bodys.User
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState")
@Composable
fun SearchedUsersGrid(
    sortedUsers: List<User>,
    getCardViewModel: GetCardViewModel,
    userViewModel: UserViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        sortedUsers.forEach { user ->
            UserCard(
                user = user,
                getCardViewModel = getCardViewModel,
                navController = navController,
                userViewModel = userViewModel,
                modifier = modifier
            )
        }
    }
}