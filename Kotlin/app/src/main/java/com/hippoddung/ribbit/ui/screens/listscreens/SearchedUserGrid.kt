package com.hippoddung.ribbit.ui.screens.listscreens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.network.bodys.User
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.ListViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState")
@Composable
fun SearchedUserGrid(
    sortedUsers: List<User>,
    listViewModel: ListViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
//            .verticalScroll(rememberScrollState())    // dropdown menu 에서 스크롤을 하는 것은 layout 구조상 불가능하다는 것 같음.
        // 자동으로 스크롤이 됨
    ) {
        Log.d("HippoLog, SearchedUserGrid", "$sortedUsers")
        LazyColumn(
            modifier = modifier
                .background(color = Color.White)
        ) {
            items(items = sortedUsers, key = { user -> user.id!! }) {
                SearchedUserCardInList(
                    user = it,
                    listViewModel = listViewModel,
                    modifier = modifier
                )
            }
//            sortedUsers.forEach { user ->
//                SearchedUserCardInList(
//                    user = user,
//                    listViewModel = listViewModel,
//                    modifier = modifier
//                )
//            }
        }
    }
}