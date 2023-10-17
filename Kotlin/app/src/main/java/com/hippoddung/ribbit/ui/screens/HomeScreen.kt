package com.hippoddung.ribbit.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.viewmodel.CoroutinesErrorHandler
import com.hippoddung.ribbit.ui.viewmodel.HomeUiState
import com.hippoddung.ribbit.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    when (homeViewModel.homeUiState) {
        is HomeUiState.Loading -> LoadingScreen(
            modifier = modifier.fillMaxSize()
        )

        is HomeUiState.Success -> SuccessScreen(
            navController = navController,
            homeViewModel = homeViewModel,
            modifier = modifier.fillMaxSize()
        )

        is HomeUiState.Error -> ErrorScreen(
            modifier = modifier.fillMaxSize()
        )
    }
}

@Composable
fun LoadingScreen(modifier: Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(id = R.string.loading)
    )
}

@Composable
fun ErrorScreen(modifier: Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = modifier.size(200.dp),
            painter = painterResource(R.drawable.ic_connection_error),
            contentDescription = ""
        )
        Text(
            text = stringResource(id = R.string.loading_failed),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun SuccessScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    modifier: Modifier
) {
    homeViewModel.getRibbitPosts(
        object : CoroutinesErrorHandler {
            override fun onError(message: String) {
            }
        }
    )
    Column {
        (homeViewModel.homeUiState as HomeUiState.Success).posts?.let {
            PostsGridScreen(
                it, modifier
            )
        }
    }

    Box(modifier = modifier) {
        FloatingActionButton(
            onClick = { navController.navigate(RibbitScreen.TwitCreateScreen.name) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(14.dp)
        ) {
            Icon(Icons.Filled.Edit, "Floating action button.")
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun PostsGridScreen(posts: List<RibbitPost>, modifier: Modifier) {
    LazyColumn(modifier = modifier) {
        items(posts) { post ->
            TextCard(
                post = post,
                modifier = modifier.padding(8.dp)
            )
        }
    }
}

@SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
@Composable
fun TextCard(post: RibbitPost, modifier: Modifier) {
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row {
                Text(
                    text = "No." + post.id.toString(),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "@" + post.user.email,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            Text(
                text = post.content,
                fontSize = 14.sp,
                modifier = Modifier.padding(4.dp),
                style = MaterialTheme.typography.headlineSmall
            )
            if (post.image.isNotEmpty()) {
                Log.d("HippoLog, HomeScreen, Image","${post.image}")
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current).data(post.image)
                        .crossfade(true).build(),
                    error = painterResource(R.drawable.ic_broken_image),
                    placeholder = painterResource(R.drawable.loading_img),
                    contentDescription = stringResource(R.string.user_image),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (post.video.isNotEmpty()) {
                RibbitVideo(post.video)
            }
        }
    }
}

@Composable
fun RibbitVideo(video: String) {
    val jsCode =
        "javascript: function() { let videos = document.getElementsByTagName('video'); for(let i=0;i<videos.length;i++){ videos[i].autoplay='false'; }}()};" +
                "javascript: const x = { document.getElementByName(\"media\").removeAttribute(\"autoplay\"); };"
//    val video = "https://scontent.cdninstagram.com/v/t50.16885-16/10000000_295215923058216_2424649624308768222_n.mp4?_nc_ht=scontent.cdninstagram.com&_nc_cat=100&_nc_ohc=CPqU_m6gRZYAX-frll-&edm=APs17CUBAAAA&ccb=7-5&oh=00_AfD9-2tj5Zv4wLVnOJyPi1MUBhkfI1NgHoLsYpYdRNhpfg&oe=652EA093&_nc_sid=10d13b"
    AndroidView(
        factory = {
            val maxWidth: Int = 1000
            WebView(it)
                .apply {
                    layoutParams = ViewGroup.LayoutParams(
                        maxWidth,
                        maxWidth
                    )
                    webViewClient = WebViewClient()
                    settings.mediaPlaybackRequiresUserGesture = true
                    settings.useWideViewPort = true
                    settings.javaScriptEnabled = true
                    loadUrl(jsCode)
                    evaluateJavascript(jsCode, null)
                }
        },
        update = { webView ->
            webView.loadUrl(video)
        },
        modifier = Modifier
    )
}