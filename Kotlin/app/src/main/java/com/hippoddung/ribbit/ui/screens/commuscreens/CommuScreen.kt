package com.hippoddung.ribbit.ui.screens.commuscreens

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
import com.hippoddung.ribbit.network.bodys.RibbitCommuItem
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.screens.RibbitTopAppBar
import com.hippoddung.ribbit.ui.screens.statescreens.ErrorScreen
import com.hippoddung.ribbit.ui.screens.statescreens.LoadingScreen
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.CommuClassificationUiState
import com.hippoddung.ribbit.ui.viewmodel.CommuUiState
import com.hippoddung.ribbit.ui.viewmodel.CommuViewModel
import com.hippoddung.ribbit.ui.viewmodel.ListViewModel
import com.hippoddung.ribbit.ui.viewmodel.TokenViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel
import kotlinx.coroutines.runBlocking

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CommuScreen(
    navController: NavHostController,
    getCardViewModel: GetCardViewModel,
    tokenViewModel: TokenViewModel,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    listViewModel: ListViewModel,
    commuViewModel: CommuViewModel,
    modifier: Modifier = Modifier
) {
    when (commuViewModel.commuUiState) {
        is CommuUiState.Loading -> {
            Log.d("HippoLog, CommuScreen", "Loading")
            LoadingScreen(modifier = modifier)
        }
        is CommuUiState.Success -> {
            Log.d("HippoLog, CommuScreen", "Success")
            val commuItems = (commuViewModel.commuUiState as CommuUiState.Success).commuItems
            CommuSuccessScreen(
                getCardViewModel = getCardViewModel,
                authViewModel = authViewModel,
                tokenViewModel = tokenViewModel,
                userViewModel = userViewModel,
                listViewModel = listViewModel,
                commuViewModel = commuViewModel,
                navController = navController,
                commuItems = commuItems,
                modifier = modifier
            )
        }
        is CommuUiState.Error -> {
            Log.d("HippoLog, CommuScreen", "Error")
            ErrorScreen(modifier = modifier)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CommuSuccessScreen(
    getCardViewModel: GetCardViewModel,
    tokenViewModel: TokenViewModel,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    listViewModel: ListViewModel,
    commuViewModel: CommuViewModel,
    navController: NavHostController,
    commuItems: List<RibbitCommuItem>,
    modifier: Modifier
) {
    var filteredCommuItems by remember { mutableStateOf(listOf<RibbitCommuItem>()) }
    filteredCommuItems = when(commuViewModel.commuClassificationUiState){
        is CommuClassificationUiState.PublicCommu ->  // commuClassificationUiState 가 PublicCommu 인 경우
            commuItems.filter { it.privateMode == false}   // RibbitCommuItem.privateMode 가 false 인 경우만 필터
        is CommuClassificationUiState.PrivateCommu ->  // commuClassificationUiState 가 PrivateCommu 인 경우
            commuItems.filter { it.privateMode == true}  // RibbitCommuItem.privateMode 가 true 인 경우만 필터
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
                commuViewModel = commuViewModel,
                navController = navController,
                modifier = modifier
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(RibbitScreen.CreatingCommuScreen.name) },
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
                CommuGrid(
                    myId = myId,
                    filteredCommuItems = filteredCommuItems,
                    getCardViewModel = getCardViewModel,
                    commuViewModel = commuViewModel,
                    navController = navController,
                    modifier = modifier
                )
            }
        }
    }
    if (commuViewModel.deleteCommuClickedUiState) {
        AlertDialog(
            onDismissRequest = { commuViewModel.deleteCommuClickedUiState = false },
            confirmButton = {
                OutlinedButton(
                    onClick = {
                        commuViewModel.deleteCommuClickedUiState = false
                        runBlocking {   // 정확한 삭제정보 표시를 위해 동기로 실행
                            Log.d("HippoLog, CommuScreen", "Delete Commu")
                            commuViewModel.deleteCommuIdState?.let { commuViewModel.deleteCommuIdCommu(it) }
                        }
                        commuViewModel.getCommus()
                    },
                    content = {
                        Text(
                            text = "Delete Commu",
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
                    onClick = { commuViewModel.deleteCommuClickedUiState = false },
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
                    text = "Are you really going to delete this commu?",
                    modifier = modifier
                )
            },
            modifier = modifier
        )
    }
}