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
import androidx.compose.material3.MaterialTheme
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
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.ui.screens.ReplyScreen
import com.hippoddung.ribbit.ui.viewmodel.CardViewModel
import com.hippoddung.ribbit.ui.viewmodel.ReplyClickedUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardBottomBar(
    myId: Int,
    post: RibbitPost,
    cardViewModel: CardViewModel,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = {
                cardViewModel.replyPostIdUiState = post.id
                cardViewModel.replyClickedUiState = ReplyClickedUiState.Clicked
                Log.d("HippLog, CardBottomBar", "replyButton")
            },
            content = {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = "replyButton",
                        modifier = modifier
                    )
                    Text(
                        text = "${post.totalReplies}",
                        modifier = modifier
                    )
                }
            },
            modifier = modifier
                .fillMaxWidth(0.25f)
                .padding(horizontal = 12.dp)
        )

        val isRePosted by remember { mutableStateOf(post.retwitUsersId.contains(myId)) }
        var rePosted by remember { mutableStateOf(isRePosted) }
        if (post.user.id != myId) {
            if (!rePosted) {
                IconButton(
                    onClick = {
                        cardViewModel.putPostIdRepost(post.id)
                        rePosted = true
                    },
                    content = {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Repeat,
                                contentDescription = "Repost Button",
                                modifier = modifier
                            )
                            if (rePosted == isRePosted) {
                                Text(
                                    text = "${post.totalRetweets}",
                                    modifier = modifier
                                )
                            } else {
                                if (isRePosted) {
                                    Text(
                                        text = "${post.totalRetweets - 1}",
                                        modifier = modifier
                                    )
                                } else {
                                    Text(
                                        text = "${post.totalRetweets + 1}",
                                        modifier = modifier
                                    )
                                }
                            }
                        }
                    },
                    modifier = modifier
                        .fillMaxWidth(1/3f)
                        .padding(horizontal = 12.dp)
                )
            } else {
                IconButton(
                    onClick = {
                        cardViewModel.putPostIdRepost(post.id)
                        rePosted = false
                    },
                    content = {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Repeat,
                                contentDescription = "Cancel Repost",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = modifier
                            )
                            if (rePosted == isRePosted) {
                                Text(
                                    text = "${post.totalRetweets}",
                                    modifier = modifier
                                )
                            } else {
                                if (isRePosted) {
                                    Text(
                                        text = "${post.totalRetweets - 1}",
                                        modifier = modifier
                                    )
                                } else {
                                    Text(
                                        text = "${post.totalRetweets + 1}",
                                        modifier = modifier
                                    )
                                }
                            }
                        }
                    },
                    modifier = modifier
                        .fillMaxWidth(1/3f)
                        .padding(horizontal = 12.dp)
                )
            }
        } else {
            IconButton(
                onClick = {},   // 본인의 post인 경우 클릭 비활성화
                content = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Repeat,
                            contentDescription = "Cancel Repost",
                            modifier = modifier
                        )
                        if (rePosted == isRePosted) {
                            Text(
                                text = "${post.totalRetweets}",
                                modifier = modifier
                            )
                        } else {
                            if (isRePosted) {
                                Text(
                                    text = "${post.totalRetweets - 1}",
                                    modifier = modifier
                                )
                            } else {
                                Text(
                                    text = "${post.totalRetweets + 1}",
                                    modifier = modifier
                                )
                            }
                        }
                    }
                },
                modifier = modifier
                    .fillMaxWidth(1/3f)
                    .padding(horizontal = 12.dp)
            )
        }

        var isLiked by remember { mutableStateOf(post.liked) }
        if (!isLiked) {
            IconButton(
                onClick = {
                    cardViewModel.postPostIdLike(post.id)
                    isLiked = true
                },
                content = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "LikeButton",
                            modifier = modifier
                        )
                        if (isLiked == post.liked) {
                            Text(
                                text = "${post.totalLikes}",
                                modifier = modifier
                            )
                        } else {
                            if (post.liked) {
                                Text(
                                    text = "${post.totalLikes - 1}",
                                    modifier = modifier
                                )
                            } else {
                                Text(
                                    text = "${post.totalLikes + 1}",
                                    modifier = modifier
                                )
                            }
                        }
                    }
                },
                modifier = modifier
                    .fillMaxWidth(0.5f)
                    .padding(horizontal = 12.dp)
            )
        } else {
            IconButton(
                onClick = {
                    cardViewModel.postPostIdLike(post.id)
                    isLiked = false
                },
                content = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "DislikeButton",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = modifier
                        )
                        if (isLiked == post.liked) {
                            Text(
                                text = "${post.totalLikes}",
                                modifier = modifier
                            )
                        } else {
                            if (post.liked) {
                                Text(
                                    text = "${post.totalLikes - 1}",
                                    modifier = modifier
                                )
                            } else {
                                Text(
                                    text = "${post.totalLikes + 1}",
                                    modifier = modifier
                                )
                            }
                        }
                    }
                },
                modifier = modifier
                    .fillMaxWidth(0.5f)
                    .padding(horizontal = 12.dp)
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.RemoveRedEye,
                contentDescription = "viewCount",
                modifier = modifier
            )
            Text(
                text = "${post.viewCount}",
                modifier = modifier
            )
        }
    }
    if (cardViewModel.replyClickedUiState is ReplyClickedUiState.Clicked) {
        Dialog(
            onDismissRequest = {
                cardViewModel.replyClickedUiState = ReplyClickedUiState.NotClicked
            },
            content = {
                ReplyScreen(
                    post = post,
                    cardViewModel = cardViewModel,
                    modifier = modifier
                )
            }
        )
        // Dialog 호출 시에 현재 페이지를 recomposition하면서 현재 카드 정보가 아닌 최근에 composition된 카드의 정보가 넘어가는 문제가 있음.
        // Dialog 자체의 문제로 추정되므로 가급적 쓰지 않는 것이 좋을 것으로 보이나 우선은 사용하기로 하고
        // replyClickedUiState에 카드의 정보를 담아서 사용하는 방식을 선택함.
    }
}