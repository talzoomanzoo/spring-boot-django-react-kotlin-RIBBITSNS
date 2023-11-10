package com.hippoddung.ribbit.ui.screens.listscreens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.network.bodys.RibbitListItem
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.screens.RibbitTopAppBar
import com.hippoddung.ribbit.ui.screens.statescreens.ErrorScreen
import com.hippoddung.ribbit.ui.screens.statescreens.LoadingScreen
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.ListClassificationUiState
import com.hippoddung.ribbit.ui.viewmodel.ListUiState
import com.hippoddung.ribbit.ui.viewmodel.ListViewModel
import com.hippoddung.ribbit.ui.viewmodel.TokenViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel
import kotlinx.coroutines.runBlocking

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListScreen(
    navController: NavHostController,
    getCardViewModel: GetCardViewModel,
    tokenViewModel: TokenViewModel,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    listViewModel: ListViewModel,
    modifier: Modifier = Modifier
) {
    when (listViewModel.listUiState) {
        is ListUiState.Loading -> {
            Log.d("HippoLog, ListScreen", "Loading")
            LoadingScreen(modifier = modifier)
        }
        is ListUiState.Success -> {
            Log.d("HippoLog, ListScreen", "Success")
            val listItems = (listViewModel.listUiState as ListUiState.Success).listItems
            ListSuccessScreen(
                getCardViewModel = getCardViewModel,
                authViewModel = authViewModel,
                tokenViewModel = tokenViewModel,
                userViewModel = userViewModel,
                listViewModel = listViewModel,
                navController = navController,
                listItems = listItems,
                modifier = modifier
            )
        }
        is ListUiState.Error -> {
            Log.d("HippoLog, ListScreen", "Error")
            ErrorScreen(modifier = modifier)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListSuccessScreen(
    getCardViewModel: GetCardViewModel,
    tokenViewModel: TokenViewModel,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    listViewModel: ListViewModel,
    navController: NavHostController,
    listItems: List<RibbitListItem>,
    modifier: Modifier
) {
    var filteredListItems by remember { mutableStateOf(listOf<RibbitListItem>()) }
    filteredListItems = when(listViewModel.listClassificationUiState){
        is ListClassificationUiState.PublicList ->  // listClassificationUiState 가 PublicList 인 경우
            listItems.filter { it.privateMode == false}   // RibbitListItem.privateMode 가 false 인 경우만 필터
        is ListClassificationUiState.PrivateList ->  // listClassificationUiState 가 PrivateList 인 경우
            listItems.filter { it.privateMode == true}  // RibbitListItem.privateMode 가 true 인 경우만 필터
    }
    val myId by remember { mutableStateOf(userViewModel.myProfile.value?.id!!) }    // 로그인시 저장한 정보기 때문에 반드시 값이 존재함.
    Scaffold(
        modifier = modifier,
        topBar = {
            RibbitTopAppBar(
                getCardViewModel = getCardViewModel,
                tokenViewModel = tokenViewModel,
                authViewModel = authViewModel,
                userViewModel = userViewModel,
                listViewModel = listViewModel,
                navController = navController,
                modifier = modifier
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(RibbitScreen.CreatingListScreen.name) },
                modifier = modifier
                    .padding(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Floating action button.",
                    modifier = modifier
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Box(modifier = modifier) {
                ListGrid(
                    myId = myId,
                    filteredListItems = filteredListItems,
                    getCardViewModel = getCardViewModel,
                    listViewModel = listViewModel,
                    navController = navController,
                    modifier = modifier
                )
            }
        }
    }
    if (listViewModel.deleteListClickedUiState) {
        AlertDialog(
            onDismissRequest = { listViewModel.deleteListClickedUiState = false },
            confirmButton = {
                OutlinedButton(
                    onClick = {
                        listViewModel.deleteListClickedUiState = false
                        runBlocking {   // 정확한 삭제정보 표시를 위해 동기로 실행
                            Log.d("HippoLog, ListScreen", "Delete List")
                            listViewModel.deleteListIdState?.let { listViewModel.deleteListIdList(it) }
                        }
                        listViewModel.getLists()
                    },
                    content = {
                        Text(
                            text = "Delete List",
                            color = Color(0xFF006400),
                            fontSize = 14.sp,
                            modifier = modifier
                        )
                    },
                    modifier = modifier,
                )
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { listViewModel.deleteListClickedUiState = false },
                    content = {
                        Text(
                            text = "Cancel",
                            color = Color(0xFF006400),
                            fontSize = 14.sp,
                            modifier = modifier
                        )
                    },
                    modifier = modifier
                )
            },
            text = {
                Text(
                    text = "Are you really going to delete this list?",
                    modifier = modifier
                )
            },
            modifier = modifier
        )
    }
}