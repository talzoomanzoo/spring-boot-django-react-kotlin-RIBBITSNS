package com.hippoddung.ribbit.ui.screens.carditems

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

@SuppressLint("SetJavaScriptEnabled", "CoroutineCreationDuringComposition")
@Composable
fun RibbitVideo(
    videoUrl: String,
    getCardViewModel: GetCardViewModel,
    modifier: Modifier
) {
    var isVideoPlayed by remember { mutableStateOf(false) }
    var videoThumbnail: Bitmap? by remember { mutableStateOf(null) }
    var isCodeExecuted by remember { mutableStateOf(false) }
    if (!isCodeExecuted) {
        // videoThumbnail을 불러오는 과정에서 recomposition이 발생하고 이에 따라 다시 videoThumbnail을 실행하는 무한루프 발견
        // 해결을 위한 1회 실행 코드 삽입
        rememberCoroutineScope().launch(Dispatchers.IO) {
            try {
                videoThumbnail = getCardViewModel.retrieveThumbnailFromVideo(videoUrl).get()
                Log.d("HippoLog, RibbitVideo", "cardViewModel.retrieveThumbnailFromVideo")
                isCodeExecuted = true   // 여러 recomposition 중 성공시 정지하게 함.
            } catch (e: SocketTimeoutException) {
                Log.d("HippoLog, RibbitVideo", "Exception: ${e.message}")
            } catch (e: Exception) {
                Log.d("HippoLog, RibbitVideo", "SocketTimeoutException: ${e.message}")
            }
        }
    }

    if (isVideoPlayed) {    // video가 플레이된 경우
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            WebViewFullScreen(
                videoUrl = videoUrl,
                modifier = modifier
                )
        }
    } else {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            if (videoThumbnail == null) {   // thumbnail을 불러오지 못한 경우
                Image(
                    painter = painterResource(R.drawable.loading_img),
                    contentDescription = "Loading",
                    contentScale = ContentScale.Fit,
                    modifier = modifier
                        .padding(8.dp)
                )
            } else {
                Image(
                    bitmap = videoThumbnail!!.asImageBitmap(),  // thumbnail을 불러온 경우
                    contentDescription = "Success",
                    contentScale = ContentScale.Fit,
                    modifier = modifier
                        .padding(8.dp)
                )
            }
            IconButton(
                onClick = { isVideoPlayed = true },
                modifier = modifier
                    .matchParentSize()
            ) {
                Icon(
                    Icons.Filled.PlayCircleOutline,
                    "Video play button.",
                    modifier = modifier.size(100.dp),
                    tint = Color.Magenta
                )
            }
        }
    }
}