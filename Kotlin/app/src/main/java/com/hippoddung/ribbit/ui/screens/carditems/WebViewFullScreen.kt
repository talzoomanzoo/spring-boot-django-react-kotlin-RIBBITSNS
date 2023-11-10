package com.hippoddung.ribbit.ui.screens.carditems

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebView.setWebContentsDebuggingEnabled
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewFullScreen(
    videoUrl: String,
    modifier: Modifier
) {
    val activity = LocalView.current.context as Activity
    val isFullScreen = remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            modifier = modifier.width(400.dp)
                .height(250.dp)
                .padding(8.dp),
            factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(1, 1)
                    settings.javaScriptEnabled = true
                    webViewClient = WebViewClient()
                    webChromeClient = object : WebChromeClient() {
                        var customView: View? = null
//                        @RequiresApi(Build.VERSION_CODES.R)
//                        val controller = windowInsetsController
                        @RequiresApi(Build.VERSION_CODES.R)
                        override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                            super.onShowCustomView(view, callback)
                            isFullScreen.value = true
                            if (this.customView != null) {
                                onHideCustomView()
                                return
                            }
                            this.customView = view
                            (activity.window.decorView as FrameLayout).addView(
                                this.customView
                            )
//                            if (controller !=null){
//                                controller.hide(WindowInsets.Type.statusBars() and WindowInsets.Type.navigationBars() and WindowInsets.Type.tappableElement())
//                                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//                            }
                        }
                        override fun onHideCustomView() {
                            super.onHideCustomView()
                            isFullScreen.value = false
                            (activity.window.decorView as FrameLayout).removeView(this.customView)
                            this.customView = null
                        }
                    }
                    setWebContentsDebuggingEnabled(true)
                    loadUrl(
                        videoUrl
                    )
                }
            }
        )
    }
    activity.requestedOrientation =
        if (isFullScreen.value) ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
}