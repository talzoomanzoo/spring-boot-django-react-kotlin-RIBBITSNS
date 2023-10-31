package com.hippoddung.ribbit.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.ui.screens.statescreens.ErrorScreen
import com.hippoddung.ribbit.ui.screens.statescreens.LoadingScreen
import com.hippoddung.ribbit.ui.viewmodel.CardViewModel
import com.hippoddung.ribbit.ui.viewmodel.PostReplyUiState
import com.hippoddung.ribbit.ui.viewmodel.ReplyClickedUiState

@Composable
fun ReplyScreen(
    post: RibbitPost,
    cardViewModel: CardViewModel,
    modifier: Modifier = Modifier
) {
    when (cardViewModel.postReplyUiState) {
        is PostReplyUiState.Ready -> {
            Log.d("HippoLog, ReplyScreen", "Ready")
            InputReplyScreen(
                post = post,
                cardViewModel = cardViewModel,
                modifier = modifier
            )
        }

        is PostReplyUiState.Success -> {
            Log.d("HippoLog, ReplyScreen", "Success")
            cardViewModel.postReplyUiState = PostReplyUiState.Ready
        }

        is PostReplyUiState.Loading -> {
            Log.d("HippoLog, ReplyScreen", "Loading")
            LoadingScreen(
                modifier = modifier
            )
        }

        is PostReplyUiState.Error -> {
            Log.d("HippoLog, ReplyScreen", "Error")
            ErrorScreen(
                modifier = modifier
            )
        }
    }
}

@Composable
fun InputReplyScreen(
    post: RibbitPost,
    cardViewModel: CardViewModel,
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(12.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = (stringResource(R.string.reply) + " to No.${cardViewModel.replyPostIdUiState}, ${post.user.email}"),
            fontSize = 16.sp,
            modifier = modifier
                .padding(start = 28.dp, top = 28.dp, end = 0.dp, bottom = 0.dp)
                .align(alignment = Alignment.Start)
        )
        BasicTextField(
            value = inputText,
            onValueChange = {
                inputText = it
            },
            modifier = modifier
                .padding(start = 28.dp, top = 14.dp, end = 28.dp, bottom = 0.dp)
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.White),
            textStyle = TextStyle(
                fontSize = 14.sp,
                color = Color.Black // Change the text color here
            ),
            visualTransformation = VisualTransformation.None, // Optional
            decorationBox = { innerTextField ->
                Column(
                    modifier = modifier.drawWithContent {
                        drawContent()
                        drawLine(
                            color = Color(0xFFEFEFEF),
                            start = Offset(
                                x = 0f,
                                y = size.height - 1.dp.toPx(),
                            ),
                            end = Offset(
                                x = size.width,
                                y = size.height - 1.dp.toPx(),
                            ),
                            strokeWidth = 1.dp.toPx(),
                        )
                    },
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = modifier
                    ) {
                        Icon(
                            modifier = modifier.size(24.dp),
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color(0xFFA8A8A8),
                        )
                        innerTextField()
                    }
                    Spacer(modifier = modifier.height(8.dp))
                }
            },
        )
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    cardViewModel.replyClickedUiState = ReplyClickedUiState.NotClicked
                },
                modifier = modifier
            ) {
                Text(
                    text = stringResource(R.string.Cancel),
                    modifier = modifier
                )
            }
            Button(
                onClick = {
                    cardViewModel.replyPostIdUiState?.let {
                        cardViewModel.postReply(
                            inputText = inputText,
                            postId = it
                        )
                    }
                    cardViewModel.replyClickedUiState = ReplyClickedUiState.NotClicked
                },
                modifier = modifier
            ) {
                Text(
                    text = stringResource(R.string.reply),
                    modifier = modifier
                )
            }
        }
    }
}