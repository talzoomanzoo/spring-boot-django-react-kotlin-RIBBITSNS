package com.hippoddung.ribbit.ui.screens.carditems

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.ui.screens.ReplyScreen
import com.hippoddung.ribbit.ui.viewmodel.HomeViewModel
import com.hippoddung.ribbit.ui.viewmodel.ReplyClickedUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardBottomBar(
    post: RibbitPost,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    var isRetwited by remember { mutableStateOf(false) }
    var isLiked by remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    homeViewModel.replyClickedUiState = ReplyClickedUiState.Clicked
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = "reply"
                    )
                }
            )
            Text(text = "${post.totalReplies}")
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconToggleButton(
                checked = isRetwited,
                onCheckedChange = { isRetwited = it },
                content = {
                    Icon(
                        imageVector = Icons.Default.Repeat,
                        contentDescription = "retwit"
                    )
                }
            )
            Text(text = "${post.totalRetweets}")
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconToggleButton(
                checked = isLiked,
                onCheckedChange = { isLiked = it },
                content = {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "like"
                    )
                }
            )
            Text(text = "${post.totalLikes}")
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.RemoveRedEye,
                contentDescription = "viewCount",
                modifier = Modifier.padding(4.dp)
            )
            Text(text = "${post.viewCount}", modifier = Modifier.padding(4.dp))
        }
    }
    if (homeViewModel.replyClickedUiState is ReplyClickedUiState.Clicked){
        Dialog(
            onDismissRequest = { homeViewModel.replyClickedUiState = ReplyClickedUiState.NotClicked }
        ) {
            ReplyScreen(post = post, homeViewModel = homeViewModel)
        }
    }
}