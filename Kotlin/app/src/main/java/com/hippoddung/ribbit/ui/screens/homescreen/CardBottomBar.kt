package com.hippoddung.ribbit.ui.screens.homescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Repeat
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
import com.hippoddung.ribbit.network.bodys.RibbitPost

@Composable
fun CardBottomBar(post: RibbitPost){
    var isRetwited by remember { mutableStateOf(false) }
    var isLiked by remember { mutableStateOf(false) }
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = { " /*TODO*/ " }, content = {
                Icon(
                    imageVector = Icons.Default.ChatBubbleOutline,
                    contentDescription = "totalLikes"
                )
            }
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconToggleButton(
                checked = isRetwited,
                onCheckedChange = { isRetwited = it },
                content = {
                    Icon(
                        imageVector = Icons.Default.Repeat,
                        contentDescription = "totalLikes"
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
                        contentDescription = "totalLikes"
                    )
                }
            )
            Text(text = "${post.totalLikes}")
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(imageVector = Icons.Default.RemoveRedEye, contentDescription = "viewCount")
            Text(text = "${post.viewCount}")
        }
    }
}