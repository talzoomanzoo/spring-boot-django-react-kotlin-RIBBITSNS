package com.hippoddung.ribbit.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.ui.screens.screenitems.InputTextField
import com.hippoddung.ribbit.ui.screens.statescreens.ErrorScreen
import com.hippoddung.ribbit.ui.screens.statescreens.LoadingScreen
import com.hippoddung.ribbit.ui.viewmodel.HomeViewModel
import com.hippoddung.ribbit.ui.viewmodel.ReplyClickedUiState
import com.hippoddung.ribbit.ui.viewmodel.ReplyUiState

@Composable
fun ReplyScreen(
    post: RibbitPost,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier.wrapContentSize()
) {
    when (homeViewModel.replyUiState) {
        is ReplyUiState.Ready -> {
            Log.d("HippoLog, ReplyScreen", "Ready")
            InputReplyScreen(
                post = post,
                homeViewModel = homeViewModel,
                modifier = modifier
            )
        }

        is ReplyUiState.Success -> {
            Log.d("HippoLog, ReplyScreen", "Success")
            homeViewModel.replyUiState = ReplyUiState.Ready
        }

        is ReplyUiState.Loading -> {
            Log.d("HippoLog, ReplyScreen", "Loading")
            LoadingScreen()
        }

        is ReplyUiState.Error -> {
            Log.d("HippoLog, ReplyScreen", "Error")
            ErrorScreen(modifier = modifier.fillMaxSize())
        }

        else -> {}
    }
}

@Composable
fun InputReplyScreen(
    post: RibbitPost,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background, shape = RoundedCornerShape(12.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = (stringResource(R.string.reply) + " to ${post.user.email}"),
            fontSize = 16.sp,
            modifier = Modifier
                .padding(start = 28.dp, top = 28.dp, end = 0.dp, bottom = 0.dp)
                .align(alignment = Alignment.Start)
        )
        InputTextField(
            value = inputText,
            onValueChange = { inputText = it },
            modifier = Modifier
                .padding(28.dp)
                .fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    homeViewModel.replyClickedUiState = ReplyClickedUiState.NotClicked
                }
            ) {
                Text(text = stringResource(R.string.Cancel))
            }
            Button(
                onClick = {
                    homeViewModel.postReply(
                        inputText = inputText,
                        postId = post.id
                    )
                    homeViewModel.replyClickedUiState = ReplyClickedUiState.NotClicked
                }
            ) {
                Text(text = stringResource(R.string.reply))
            }
        }
    }
}