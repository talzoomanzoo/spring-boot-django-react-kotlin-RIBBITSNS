package com.hippoddung.ribbit.ui.screens.screenitems

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.ui.screens.carditems.RibbitCard
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.PostingViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState")
@Composable
fun PostsGrid(
    posts: List<RibbitPost>,
    getCardViewModel: GetCardViewModel,
    userViewModel: UserViewModel,
    postingViewModel: PostingViewModel,
    myId: Int,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val comparator = compareByDescending<RibbitPost> { it.id }
    val sortedRibbitPost = remember(posts, comparator) {
        posts.sortedWith(comparator)
    }   // LazyColumn items에 List를 바로 주는 것이 아니라 Comparator로 정렬하여 remember로 기억시켜서 recomposition을 방지하여 성능을 올린다.
    LazyColumn(modifier = modifier) {
        items(items = sortedRibbitPost, key = { post -> post.id }) { it ->
            RibbitCard(
                post = it,
                getCardViewModel = getCardViewModel,
                myId = myId,
                navController = navController,
                userViewModel = userViewModel,
                postingViewModel = postingViewModel,
                modifier = modifier
            )
        }
    }
}