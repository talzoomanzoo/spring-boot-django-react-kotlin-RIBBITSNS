package com.hippoddung.ribbit.ui.screens.carditems

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.ribbitmethod.calculationTime
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.PostingViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CardTopBar(
    index: Int,
    post: RibbitPost,
    getCardViewModel: GetCardViewModel,
    postingViewModel: PostingViewModel,
    userViewModel: UserViewModel,
    myId: Int,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.Bottom
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .size(40.dp)
        ) {
            if(post.user?.image == null){
                Image(
                    painter = painterResource(id = R.drawable.frog_8341850_1280),
                    contentDescription = "default image",
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .clickable {    // 해당 Composable function 을 click 하여 "() -> unit" 을 받을 수 있는 형태로 만들어 줌.
                            Log.d("HippoLog, CardTopBar", "profileImageClick")
                            post.user?.id?.let {
                                getCardViewModel.getUserIdPosts(userId = it)
                                userViewModel.getProfile(userId = it)
                            }
                            navController.navigate(RibbitScreen.ProfileScreen.name)
                        }
                )
            }else {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(
                            post.user.image
//                                ?: "https://img.animalplanet.co.kr/news/2020/01/13/700/sfu2275cc174s39hi89k.jpg"
                        )
                        .crossfade(true).build(),
                    error = painterResource(R.drawable.frog_8341850_1280),
                    placeholder = painterResource(R.drawable.loading_img),
                    contentDescription = stringResource(R.string.user_image),
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .clickable {    // 해당 Composable function 을 click 하여 "() -> unit" 을 받을 수 있는 형태로 만들어 줌.
                            Log.d("HippoLog, CardTopBar", "profileImageClick")
                            post.user.id?.let {
                                getCardViewModel.getUserIdPosts(userId = it)
                                userViewModel.getProfile(userId = it)
                            }
                            navController.navigate(RibbitScreen.ProfileScreen.name)
                        }
                )
            }
        }
        Row(
            modifier = modifier.padding(bottom = 4.dp)
        ) {
//            Text(
//                text = "No." + post.id.toString(),
//                fontSize = 14.sp,
//                modifier = modifier.padding(start = 4.dp, end = 4.dp),
//                style = MaterialTheme.typography.headlineSmall
//            )
            post.user?.let {
                Text(
                    text = it.email,
                    fontSize = 14.sp,
                    modifier = modifier.padding(start = 4.dp, end = 4.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            Text(
                text = calculationTime(targetDateTimeStr = post.createdAt),
                fontSize = 14.sp,
                modifier = modifier.padding(start = 4.dp, end = 4.dp),
                style = MaterialTheme.typography.headlineSmall
            )
            if (post.edited) {
                Log.d("HippoLog, CardTopBar", "${post.editedAt}")
                Text(
                    text = post.editedAt?.let { calculationTime(targetDateTimeStr = it) } + " 수정",
                    fontSize = 14.sp,
                    modifier = modifier.padding(start = 4.dp, end = 4.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
        RibbitDropDownMenu(
            index = index,
            post = post,
            getCardViewModel = getCardViewModel,
            postingViewModel = postingViewModel,
            myId = myId,
            navController = navController,
            modifier = modifier
        )
    }
}