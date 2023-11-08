package com.hippoddung.ribbit.ui.screens.carditems

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.PostingViewModel
import com.hippoddung.ribbit.ui.viewmodel.ProfileUiState
import com.hippoddung.ribbit.ui.viewmodel.UserIdClassificationUiState
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
@Composable
fun RibbitCard(
    post: RibbitPost,
    getCardViewModel: GetCardViewModel,
    userViewModel: UserViewModel,
    myId: Int,
    navController: NavHostController,
    modifier: Modifier
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen =
        RibbitScreen.valueOf(backStackEntry?.destination?.route ?: RibbitScreen.HomeScreen.name)
    Log.d("HippoLog, RibbitCard", "RibbitCard")
    Card(
        onClick = {
            Log.d("HippoLog, RibbitCard", "Card: ${post.id}")
            getCardViewModel.getPostIdPost(post.id)    // 뷰 카운트 + 호출을 getPostIdPost메소드에서 실행하도록 함.
            navController.navigate(RibbitScreen.PostIdScreen.name)
        },
        shape = MaterialTheme.shapes.extraSmall,
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.background
//        ),
//        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
    ) {
        Column(modifier = modifier) {
            if (currentScreen == RibbitScreen.ProfileScreen) {
                if(getCardViewModel.userIdClassificationUiState is UserIdClassificationUiState.Ribbit){
                if (userViewModel.profileUiState is ProfileUiState.Exist) {
                    if ((userViewModel.profileUiState as ProfileUiState.Exist).user.id != post.user?.id) {
                        Row(
                            modifier = modifier,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Repeat,
                                contentDescription = "Rebbit 표시",
                                modifier = modifier.size(12.dp),
                                tint = Color.Red
                            )
                            Text(
                                text = "Rebbit from ${(userViewModel.profileUiState as ProfileUiState.Exist).user.email}",
                                modifier = modifier,
                                color = Color.Red,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
            }
            CardTopBar(
                post = post,
                getCardViewModel = getCardViewModel,
                userViewModel = userViewModel,
                myId = myId,
                navController = navController,
                modifier = modifier
            )
            Text(
                text = post.content,
                fontSize = 14.sp,
                modifier = modifier.padding(16.dp),
                style = MaterialTheme.typography.headlineSmall
            )
            if (post.image != null) {
                RibbitImage(
                    image = post.image!!,
                    modifier = modifier
                )
            }

            if (post.video != null) {
                Log.d("HippoLog, RibbitCard", "RibbitVideo")
                RibbitVideo(
                    videoUrl = post.video!!,
                    getCardViewModel = getCardViewModel,
                    modifier = modifier
                )
            }
            CardBottomBar(
                myId = myId,
                post = post,
                getCardViewModel = getCardViewModel
            )
            Canvas(
                modifier = modifier,
                onDraw = {
                    drawLine(
                        color = Color(0xFF4c6c4a),
                        start = Offset(0.dp.toPx(), 0.dp.toPx()),
                        end = Offset(400.dp.toPx(), 0.dp.toPx()),
                        strokeWidth = 1.dp.toPx(),
//                        cap = StrokeCap.Round
                    )
                }
            )
        }
    }
}


