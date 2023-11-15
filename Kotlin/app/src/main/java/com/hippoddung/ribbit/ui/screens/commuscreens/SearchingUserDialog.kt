package com.hippoddung.ribbit.ui.screens.commuscreens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hippoddung.ribbit.network.bodys.User
import com.hippoddung.ribbit.ui.viewmodel.CommuViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchingUserDialog(
    userViewModel: UserViewModel,
    commuViewModel: CommuViewModel,
    modifier: Modifier
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue()) }
    var isSearched by remember { mutableStateOf(false) }
    val usersData by userViewModel.usersSearchData.collectAsState()

    Column(
        modifier = modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
            .fillMaxSize()
    ) {
        val userComparator = compareByDescending<User> { it.id }
        val sortedUsers = remember(usersData, userComparator) {
            usersData.sortedWith(userComparator)
        }   // LazyColumn items 에 Commu 를 바로 주는 것이 아니라 Comparator 로 정렬하여 remember 로 기억시켜서 recomposition 을 방지하여 성능을 올린다.
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                if (it.text.isNotEmpty()) {
                    // 사용자가 검색어를 입력하면 여기에서 작업을 수행할 수 있음
                    // 예: 자동완성 결과 업데이트, 네트워크 요청 등
                    userViewModel.getUsersSearch(it.text)
                    isSearched = true
                }
            },
            label = {
                Text(
                    text = "Search User",
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
                    IconButton(
                        onClick = {
                            commuViewModel.searchingUserClickedUiState = false
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
                SearchedUserGrid(
                    sortedUsers = sortedUsers,
                    commuViewModel = commuViewModel,
                    modifier = modifier
                )
            }
        }
    }
}