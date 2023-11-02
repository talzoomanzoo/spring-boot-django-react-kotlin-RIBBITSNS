package com.hippoddung.ribbit.ui.screens.carditems

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Build
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.ui.viewmodel.CardViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future

@SuppressLint("SetJavaScriptEnabled", "CoroutineCreationDuringComposition")
@Composable
fun RibbitVideo(
    videoUrl: String,
    cardViewModel: CardViewModel,
    modifier: Modifier
) {
    var isVideoPlayed by remember { mutableStateOf(false) }
    var videoThumbnail: Bitmap? by remember { mutableStateOf(null) }
    rememberCoroutineScope().launch(Dispatchers.IO) {
        try {
            videoThumbnail = cardViewModel.retrieveThumbnailFromVideo(videoUrl).get()
        } catch (e: SocketTimeoutException) {
            Log.d("HippoLog, RibbitVideo", "Exception: ${e.message}")
        } catch (e: Exception) {
            Log.d("HippoLog, RibbitVideo", "SocketTimeoutException: ${e.message}")
        }
    }

    if (isVideoPlayed) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize()
        ) {
            WebViewFullScreen(
                videoUrl = videoUrl,
                modifier = modifier
                )
        }
    } else {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize()
        ) {
            if (videoThumbnail == null) {
                Image(
                    painter = painterResource(R.drawable.loading_img),
                    contentDescription = "Loading",
                    modifier = modifier
                        .size(300.dp)
                        .padding(8.dp)
                )
            } else {
                Image(
                    bitmap = videoThumbnail!!.asImageBitmap(),
                    contentDescription = "Success",
                    modifier = modifier
                        .size(300.dp)
                        .padding(8.dp)
                )
            }
            IconButton(
                onClick = { isVideoPlayed = true },
                modifier = modifier
                    .size(300.dp)
                    .padding(8.dp)
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