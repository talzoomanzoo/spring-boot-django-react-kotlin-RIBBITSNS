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
import com.hippoddung.ribbit.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future

@SuppressLint("SetJavaScriptEnabled", "CoroutineCreationDuringComposition")
@Composable
fun RibbitVideo(videoUrl: String, homeViewModel: HomeViewModel) {
    var isVideoPlayed by remember { mutableStateOf(false) }
    var videoThumbnail: Bitmap? by remember { mutableStateOf(null) }
    rememberCoroutineScope().launch(Dispatchers.IO) {
        try {
            videoThumbnail = homeViewModel.retrieveThumbnailFromVideo(videoUrl).get()
        } catch (e: SocketTimeoutException) {
            Log.d("HippoLog, RibbitVideo", "Exception: ${e.message}")
        } catch (e: Exception) {
            Log.d("HippoLog, RibbitVideo", "SocketTimeoutException: ${e.message}")
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

//suspend fun retrieveThumbnailFromVideo(videoUrl: String?): Bitmap? {
fun retrieveThumbnailFromVideo(videoUrl: String?): Future<Bitmap> {
    val executor = Executors.newFixedThreadPool(2)
    val future: Future<Bitmap> = executor.submit(Callable<Bitmap> {
        var retrievalCount = 0
        val maxRetrievalCount = 2 // Adjust this value as needed
        if (retrievalCount >= maxRetrievalCount) {
            // You've reached the limit, so return null or handle it as needed
            return@Callable null
        }
        var bitmap: Bitmap? = null
        var mediaMetadataRetriever: MediaMetadataRetriever? = null
        mediaMetadataRetriever = MediaMetadataRetriever()
        if (Build.VERSION.SDK_INT >= 14) {
            mediaMetadataRetriever.setDataSource(videoUrl, HashMap())
        } else {
            mediaMetadataRetriever.setDataSource(videoUrl)
        }
        try {
            Log.d("HippoLog, HomeScreen, retrieve", "Thread - ${Thread.currentThread().name}")
            bitmap = mediaMetadataRetriever.getFrameAtTime(100000)
        } catch (e: java.net.SocketTimeoutException) {
            Log.d(
                "HippoLog, HomeScreen, retrieve, Exception",
                "Exception in retrieveThumbnailFromVideo: ${e.message}"
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(
                "HippoLog, HomeScreen, retrieve, Exception",
                "Exception in retrieveThumbnailFromVideo: ${e.message}"
            )
        } finally {
            mediaMetadataRetriever?.release()
        }
        retrievalCount++
//    return bitmap
        return@Callable bitmap
    }
    )
    return future
}