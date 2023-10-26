package com.hippoddung.ribbit.ui.screens.homescreen

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.HomeViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
@Composable
fun RibbitCard(post: RibbitPost,
               homeViewModel: HomeViewModel,
               userId: Int,
               navcontroller: NavHostController,
               modifier: Modifier = Modifier
) {
    Log.d("HippoLog, RibbitCard", "RibbitCard")
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = modifier) {
            CardTopBar(post = post,
                homeViewModel = homeViewModel,
                userId = userId,
                navController = navcontroller)
            Text(
                text = post.content,
                fontSize = 14.sp,
                modifier = modifier.padding(4.dp),
                style = MaterialTheme.typography.headlineSmall
            )
            if (post.image != null) {
                RibbitImage(image = post.image)
            } else {
            }

            if (post.video != null) {
                Log.d("HippoLog, RibbitCard", "RibbitVideo")
                RibbitVideo(post.video, homeViewModel = homeViewModel)
            } else {
            }
            CardBottomBar(post = post)
        }
    }
}


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