package com.hippoddung.ribbit.ui.screens.commuscreens

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
import com.hippoddung.ribbit.network.bodys.User
import com.hippoddung.ribbit.ui.viewmodel.CommuViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState")
@Composable
fun SearchedUserGrid(
    sortedUsers: List<User>,
    commuViewModel: CommuViewModel,
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
                SearchedUserCardInCommu(
                    user = it,
                    commuViewModel = commuViewModel,
                    modifier = modifier
                )
            }
//            sortedUsers.forEach { user ->
//                SearchedUserCardInCommu(
//                    user = user,
//                    commuViewModel = commuViewModel,
//                    modifier = modifier
//                )
//            }
        }
    }
}