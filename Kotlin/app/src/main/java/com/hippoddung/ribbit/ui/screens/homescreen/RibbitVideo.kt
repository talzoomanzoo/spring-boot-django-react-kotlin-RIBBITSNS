package com.hippoddung.ribbit.ui.screens.homescreen

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
import com.hippoddung.ribbit.ui.screens.WebViewFullScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future

@SuppressLint("SetJavaScriptEnabled", "CoroutineCreationDuringComposition")
@Composable
fun RibbitVideo(videoUrl: String) {
    var isVideoPlayed by remember { mutableStateOf(false) }
    var videoThumbnail: Bitmap? by remember { mutableStateOf(null) }
    rememberCoroutineScope().launch(Dispatchers.IO) {
        try {
            videoThumbnail = retrieveThumbnailFromVideo(videoUrl).get()
        }catch (e: Exception){
            Log.d("HippoLog, HomeScreen","Exception: ${e.message}")
        }catch (e: SocketTimeoutException){
            Log.d("HippoLog, HomeScreen","SocketTimeoutException: ${e.message}")
        }
    }

    if (isVideoPlayed) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            WebViewFullScreen(videoUrl)
        }
    } else {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            if (videoThumbnail == null) {
                Image(
                    painter = painterResource(R.drawable.loading_img),
                    contentDescription = "Loading",
                    modifier = Modifier
                        .size(300.dp)
                        .padding(8.dp)
                )
            } else {
                Image(
                    bitmap = videoThumbnail!!.asImageBitmap(),
                    contentDescription = "Success",
                    modifier = Modifier
                        .size(300.dp)
                        .padding(8.dp)
                )
            }
            IconButton(
                onClick = { isVideoPlayed = true },
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
            ) {
                Icon(
                    Icons.Filled.PlayCircleOutline,
                    "Video play button.",
                    modifier = Modifier.fillMaxSize(),
                    tint = Color.Magenta
                )
            }
        }
    }
}

fun retrieveThumbnailFromVideo(videoUrl: String?): Future<Bitmap> {
    val executor = Executors.newFixedThreadPool(10)
    val future: Future<Bitmap> = executor.submit(Callable<Bitmap> {
        var bitmap: Bitmap? = null
        var mediaMetadataRetriever: MediaMetadataRetriever? = null
        try {
            Log.d("HippoLog, HomeScreen, retrieve", "Thread - ${Thread.currentThread().name}")
            mediaMetadataRetriever = MediaMetadataRetriever()
            if (Build.VERSION.SDK_INT >= 14) {
                mediaMetadataRetriever.setDataSource(videoUrl, HashMap())
            } else {
                mediaMetadataRetriever.setDataSource(videoUrl)
            }
            bitmap = mediaMetadataRetriever.getFrameAtTime(1000000)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(
                "HippoLog, HomeScreen, retrieve, Exception",
                "Exception in retrieveThumbnailFromVideo: ${e.message}"
            )
        } finally {
            mediaMetadataRetriever?.release()
        }
        return@Callable bitmap
    })
    return future
}