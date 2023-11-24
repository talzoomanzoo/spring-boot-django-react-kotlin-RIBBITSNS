package com.hippoddung.ribbit.ui.screens.searchitems

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchedPostCard(
    isExpanded: MutableState<Boolean>,
    post: RibbitPost,
    getCardViewModel: GetCardViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 20.dp, top = 0.dp, end = 20.dp, bottom = 0.dp)
            .clickable {
                Log.d("HippoLog, UserCard", "UserCardClick")
                post.id.let {
                    getCardViewModel.getPostIdPost(postId = it)
                }
                navController.navigate(RibbitScreen.PostIdScreen.name)
                isExpanded.value = !isExpanded.value
            }
    ) {
        Icon(imageVector = Icons.Default.Comment, contentDescription = "RibbitPost", modifier = modifier)
        Row(
            modifier = modifier.padding(start = 12.dp,bottom = 4.dp)
        ) {
            Text(
                text = post.content,
                fontSize = 14.sp,
                modifier = modifier.padding(start = 4.dp, end = 4.dp),
                style = MaterialTheme.typography.headlineSmall
            )
            Log.d("HippoLog, UserCard", "$post")
            post.user?.fullName?.let {
                Text(
                    text = "@$it",
                    fontSize = 14.sp,
                    modifier = modifier.padding(start = 4.dp, end = 4.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}