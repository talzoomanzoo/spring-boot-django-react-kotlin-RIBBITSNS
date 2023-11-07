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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.network.bodys.RibbitListItem
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.screens.RibbitTopAppBar
import com.hippoddung.ribbit.ui.screens.statescreens.ErrorScreen
import com.hippoddung.ribbit.ui.screens.statescreens.LoadingScreen
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.ListUiState
import com.hippoddung.ribbit.ui.viewmodel.ListViewModel
import com.hippoddung.ribbit.ui.viewmodel.TokenViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel

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
        }
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Box(modifier = modifier) {
                ListGrid(
                    listItems = listItems,
                    getCardViewModel = getCardViewModel,
                    listViewModel = listViewModel,
                    navController = navController,
                    modifier = modifier
                )
                FloatingActionButton(
                    onClick = { navController.navigate(RibbitScreen.CreatingListScreen.name) },
                    modifier = modifier
                        .align(Alignment.BottomEnd)
                        .padding(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Floating action button.",
                        modifier = modifier
                    )
                }
            }
        }
    }
}