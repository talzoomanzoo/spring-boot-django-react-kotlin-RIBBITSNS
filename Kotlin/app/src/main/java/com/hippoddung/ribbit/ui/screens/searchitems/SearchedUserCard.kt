package com.hippoddung.ribbit.ui.screens.searchitems

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.network.bodys.User
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchedUserCard(
    user: User,
    getCardViewModel: GetCardViewModel,
    userViewModel: UserViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(start = 20.dp, top = 12.dp, end = 20.dp, bottom = 12.dp)
            .clickable {
                Log.d("HippoLog, UserCard", "UserCardClick")
                user.id?.let {
                    getCardViewModel.getUserIdPosts(userId = it)
                    userViewModel.getProfile(userId = it)
                }
                navController.navigate(RibbitScreen.ProfileScreen.name)
            }
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
                    .clickable {    // 해당 Composable function 을 click 하여 "() -> unit" 을 받을 수 있는 형태로 만들어 줌.
                        Log.d("HippoLog, CardTopBar", "profileImageClick")
                        user.id?.let {
                            getCardViewModel.getUserIdPosts(userId = it)
                            userViewModel.getProfile(userId = it)
                        }
                        navController.navigate(RibbitScreen.ProfileScreen.name)
                    }
            )
        }
        Row(
            modifier = modifier.padding(start = 12.dp,bottom = 4.dp)
        ) {
            Text(
                text = "${user.fullName}",
                fontSize = 14.sp,
                modifier = modifier.padding(start = 4.dp, end = 4.dp),
                style = MaterialTheme.typography.headlineSmall
            )
            Log.d("HippoLog, UserCard", "$user")
            Text(
                text = user.email,
                fontSize = 14.sp,
                modifier = modifier.padding(start = 4.dp, end = 4.dp),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}