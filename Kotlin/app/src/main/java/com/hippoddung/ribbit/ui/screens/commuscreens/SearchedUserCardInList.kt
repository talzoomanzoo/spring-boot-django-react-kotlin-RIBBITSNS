package com.hippoddung.ribbit.ui.screens.commuscreens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.network.bodys.User
import com.hippoddung.ribbit.ui.viewmodel.CommuViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchedUserCardInCommu(
    user: User,
    commuViewModel: CommuViewModel,
    modifier: Modifier = Modifier
) {
    Log.d("HippoLog, SearchedUserCardInCommu", "$user")
    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(start = 20.dp, top = 12.dp, end = 20.dp, bottom = 12.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .size(40.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(
                        user.image
                            ?: "https://img.animalplanet.co.kr/news/2020/01/13/700/sfu2275cc174s39hi89k.jpg"
                    )
                    .crossfade(true).build(),
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = stringResource(R.string.user_image),
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        }
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 12.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = modifier) {
                Text(
                    text = "${user.fullName}",
                    fontSize = 14.sp,
                    modifier = modifier.padding(start = 4.dp, end = 4.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = user.email,
                    fontSize = 14.sp,
                    modifier = modifier.padding(start = 4.dp, end = 4.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            IconButton(
                onClick = {
                    Log.d("HippoLog, SearchedUserCardInCommu", "UserCardInCommuClick")
                    user.id?.let {
                        commuViewModel.postAddUser(it)
                    }
                },
                content = {Icon(imageVector = Icons.Default.Add, contentDescription = "Add user to commu button")},
                modifier = modifier
            )
        }
    }
}