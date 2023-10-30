package com.hippoddung.ribbit.ui.screens.carditems

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.ui.viewmodel.HomeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
@Composable
fun RibbitCard(post: RibbitPost,
               homeViewModel: HomeViewModel,
               userId: Int,
               navController: NavHostController,
               modifier: Modifier = Modifier
) {
    Log.d("HippoLog, RibbitCard", "RibbitCard")
    Card(
        shape = MaterialTheme.shapes.extraSmall,
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.background
//        ),
//        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = modifier) {
            CardTopBar(post = post,
                homeViewModel = homeViewModel,
                userId = userId,
                navController = navController)
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
            CardBottomBar(
                post = post,
                homeViewModel = homeViewModel)
            Canvas(
                modifier = modifier,
                onDraw = {
                    drawLine(
                        color = Color.Black,
                        start = Offset(0.dp.toPx(), 0.dp.toPx()),
                        end = Offset(400.dp.toPx(), 0.dp.toPx()),
                        strokeWidth = 1.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            )
        }
    }
}


