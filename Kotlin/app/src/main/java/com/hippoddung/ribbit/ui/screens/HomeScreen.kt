package com.hippoddung.ribbit.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Build
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.screens.appbars.HippoTopAppBar
import com.hippoddung.ribbit.ui.screens.statescreens.ErrorScreen
import com.hippoddung.ribbit.ui.screens.statescreens.LoadingScreen
import com.hippoddung.ribbit.ui.viewmodel.HomeUiState
import com.hippoddung.ribbit.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier.fillMaxSize()
) {
    when (homeViewModel.homeUiState) {

        is HomeUiState.Loading -> {
            Log.d("HippoLog, HomeScreen", "Loading")
            LoadingScreen(modifier = modifier)
        }

        is HomeUiState.Success -> {
            Log.d("HippoLog, HomeScreen", "Success")
            val ribbitPosts = (homeViewModel.homeUiState as HomeUiState.Success).posts
            HomeSuccessScreen(
                homeViewModel = homeViewModel,
                scrollBehavior = scrollBehavior,
                navController = navController,
                ribbitPosts = ribbitPosts,
                modifier = modifier
            )
        }

        is HomeUiState.Error -> {
            Log.d("HippoLog, HomeScreen", "Error")
            ErrorScreen(modifier = modifier)
        }

        else -> {}
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSuccessScreen(
    homeViewModel: HomeViewModel,
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    ribbitPosts: List<RibbitPost>,
    modifier: Modifier
) {
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        // scrollBehavior에 따라 리컴포지션이 트리거되는 것으로 추측, 해결할 방법을 찾아야 함.
        // navigation 위(RibbitApp)에 있던 scrollBehavior을 navigation 하위에 있는 HomeScreen으로 옮겨서 해결.
        topBar = {
            HippoTopAppBar(
                homeViewModel = homeViewModel,
                scrollBehavior = scrollBehavior,
                navController = navController
            )
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Box(modifier = modifier) {
                PostsGridScreen(
                    posts = ribbitPosts, modifier
                )
            }
            Box(modifier = modifier) {
                FloatingActionButton(
                    onClick = {
                        Log.d("HippoLog, HomeScreen", "onClick")
                        navController.navigate(RibbitScreen.TwitCreateScreen.name)
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(14.dp)
                ) {
                    Icon(Icons.Filled.Edit, "Floating action button.")
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun PostsGridScreen(posts: List<RibbitPost>, modifier: Modifier) {
    val comparator = compareByDescending<RibbitPost> { it.id }
    val sortedRibbitPost = remember(posts, comparator) {
        posts.sortedWith(comparator)
    }   // LazyColumn items에 List를 바로 주는 것이 아니라 Comparator로 정렬하여 remember로 기억시켜서 recomposition을 방지하여 성능을 올린다.
    LazyColumn(modifier = modifier) {
        items(items = sortedRibbitPost, key = { post -> post.id }) {
            RibbitCard(
                post = it,
                modifier = modifier.padding(8.dp)
            )
        }
    }
}

@SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
@Composable
fun RibbitCard(post: RibbitPost, modifier: Modifier) {
    var isRetwited by remember { mutableStateOf(false) }
    var isLiked by remember { mutableStateOf(false) }

    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = modifier) {
            Row {
                Text(
                    text = "No." + post.id.toString(),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = post.user.email,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
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
                RibbitVideo(post.video)
            } else {
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = { " /*TODO*/ " }, content = {
                        Icon(
                            imageVector = Icons.Default.ChatBubbleOutline,
                            contentDescription = "totalLikes"
                        )
                    }
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconToggleButton(
                        checked = isRetwited,
                        onCheckedChange = { isRetwited = it },
                        content = {
                            Icon(
                                imageVector = Icons.Default.Repeat,
                                contentDescription = "totalLikes"
                            )
                        }
                    )
                    Text(text = "${post.totalRetweets}")
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconToggleButton(
                        checked = isLiked,
                        onCheckedChange = { isLiked = it },
                        content = {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "totalLikes"
                            )
                        }
                    )
                    Text(text = "${post.totalLikes}")
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(imageVector = Icons.Default.RemoveRedEye, contentDescription = "viewCount")
                    Text(text = "${post.viewCount}")
                }
            }
        }
    }
}

@Composable
fun RibbitDropDownMenu(navController: NavHostController) {
    var isDropDownMenuExpanded by remember { mutableStateOf(false) }

    Button(
        onClick = { isDropDownMenuExpanded = true }
    ) {
        Text(text = "Menu")
    }

    DropdownMenu(
        expanded = isDropDownMenuExpanded,
        onDismissRequest = { isDropDownMenuExpanded = false },
        modifier = Modifier
            .wrapContentSize()
            .padding(4.dp)
    ) {
        DropdownMenuItem(
            onClick = {
                navController.navigate(RibbitScreen.HomeScreen.name)
                isDropDownMenuExpanded = false
            },
            text = {
                Text(
                    text = "Edit",
                    color = Color.Blue,
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    style = TextStyle(shadow = Shadow(Color.Black))
                )
            }
        )
        DropdownMenuItem(
            onClick = {
                println("Hello 5")
                isDropDownMenuExpanded = false
            },
            text = {
                Text(
                    text = "Delete",
                    color = Color.Blue,
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(3f, 3f),
                            blurRadius = 3f
                        )
                    )
                )
            }
        )
    }
}

@Composable
fun RibbitImage(image: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current).data(image)
                .crossfade(true).build(),
            error = painterResource(R.drawable.ic_broken_image),
            placeholder = painterResource(R.drawable.loading_img),
            contentDescription = stringResource(R.string.user_image),
            contentScale = ContentScale.Inside,
            modifier = Modifier
                .size(300.dp)
                .padding(8.dp)
        )
    }
}

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