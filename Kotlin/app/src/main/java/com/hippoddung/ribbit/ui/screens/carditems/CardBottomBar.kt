package com.hippoddung.ribbit.ui.screens.carditems

import android.util.Log
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = "postId: ${post.id}")
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    homeViewModel.replyPostIdUiState = post.id
                    homeViewModel.replyClickedUiState = ReplyClickedUiState.Clicked
                    Log.d("HippLog, CardBottomBar", "replyButton")
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = "replyButton"
                    )
                }
            )
            Text(text = "${post.totalReplies}")
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            var retwited by remember { mutableStateOf(post.retwit) }
            if (!retwited) {
                IconButton(
                    onClick = {
                        homeViewModel.putPostIdRepost(post.id)
                        retwited = true
                    },
                    content = {
                        Icon(
                            imageVector = Icons.Default.Repeat,
                            contentDescription = "Repost Button",
                            tint = Color.Black
                        )
                    }
                )
            } else {
                IconButton(
                    onClick = {
                        homeViewModel.putPostIdRepost(post.id)
                        retwited = false
                    },
                    content = {
                        Icon(
                            imageVector = Icons.Default.Repeat,
                            contentDescription = "Cancel Repost",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                )
            }
            if (retwited == post.retwit) {
                Text(text = "${post.totalRetweets}")
            } else {
                if (post.retwit and (retwited != post.retwit)) {
                    Text(text = "${post.totalRetweets - 1}")
                } else {
                    if (!post.retwit and (retwited != post.retwit)) {
                        Text(text = "${post.totalRetweets + 1}")
                    }
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            var isLiked by remember { mutableStateOf(post.liked) }
            if (!isLiked) {
                IconButton(
                    onClick = {
                        homeViewModel.postPostIdLike(post.id)
                        isLiked = true
                    },
                    content = {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "LikeButton",
                            tint = Color.Black
                        )
                    }
                )
            } else {
                IconButton(
                    onClick = {
                        homeViewModel.postPostIdLike(post.id)
                        isLiked = false
                    },
                    content = {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "DislikeButton",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                )
            }
            if (isLiked == post.liked) {
                Text(text = "${post.totalLikes}")
            } else {
                if (post.liked and (isLiked != post.liked)) {
                    Text(text = "${post.totalLikes - 1}")
                } else {
                    if (!post.liked and (isLiked != post.liked)) {
                        Text(text = "${post.totalLikes + 1}")
                    }
                }
            }
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
    if (homeViewModel.replyClickedUiState is ReplyClickedUiState.Clicked) {
        Dialog(
            onDismissRequest = {
                homeViewModel.replyClickedUiState = ReplyClickedUiState.NotClicked
            },
            content = { ReplyScreen(post = post, homeViewModel = homeViewModel) }
        )
    }
}