package com.hippoddung.ribbit.ui.screens.postidscreen

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.ui.screens.carditems.RibbitCard
import com.hippoddung.ribbit.ui.viewmodel.AnalyzingPostEthicUiState
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.PostingViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState", "MutableCollectionMutableState")
@Composable
fun PostIdPostsGrid(
    post: RibbitPost,
    posts: List<RibbitPost>,
    getCardViewModel: GetCardViewModel,
    postingViewModel: PostingViewModel,
    userViewModel: UserViewModel,
    myId: Int,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
//    val comparator = compareBy<RibbitPost> { it.id }    // id가 낮은 글이 위로
    val comparator = compareByDescending<RibbitPost> { it.id }  // id가 높은 글이 위로
    var sortedRibbitPost by remember(posts) {
        mutableStateOf(posts.toMutableList().apply {
            sortedWith(comparator)
        })
    }   // LazyColumn items 에 List 를 바로 주는 것이 아니라 Comparator 로 정렬하여 remember 로 기억시켜서 recomposition 을 방지하여 성능을 올린다.
    LaunchedEffect(postingViewModel.analyzingPostEthicUiState, Unit) {
        if (postingViewModel.analyzingPostEthicUiState is AnalyzingPostEthicUiState.Success) {
            sortedRibbitPost = sortedRibbitPost.toMutableList().apply {
                this[0] = this[0].copy(
                    ethiclabel = (postingViewModel.analyzingPostEthicUiState as AnalyzingPostEthicUiState.Success).post.ethiclabel,
                    ethicrateMAX = (postingViewModel.analyzingPostEthicUiState as AnalyzingPostEthicUiState.Success).post.ethicrateMAX
                )
            }
        }
    }
    LazyColumn(modifier = modifier) {
        item {
            RibbitCard(
                index = 0,
                post = post,
                getCardViewModel = getCardViewModel,
                postingViewModel = postingViewModel,
                myId = myId,
                navController = navController,
                userViewModel = userViewModel,
                modifier = modifier
            )
        }
        itemsIndexed(items = sortedRibbitPost) { index, post ->
            Row(modifier = modifier) {
                Spacer(modifier = modifier.width(28.dp))
                RibbitCard(
                    index = index,
                    post = post,
                    getCardViewModel = getCardViewModel,
                    postingViewModel = postingViewModel,
                    myId = myId,
                    navController = navController,
                    userViewModel = userViewModel,
                    onPostModified = { modifiedPost ->
                        sortedRibbitPost[index] = modifiedPost
                    },
                    modifier = modifier
                )
            }
        }
    }
}