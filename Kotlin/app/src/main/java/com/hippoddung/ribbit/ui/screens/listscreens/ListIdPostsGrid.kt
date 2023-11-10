package com.hippoddung.ribbit.ui.screens.listscreens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.ui.screens.carditems.RibbitCard
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState")
@Composable
fun ListIdPostsGrid(
    posts: List<RibbitPost>,
    getCardViewModel: GetCardViewModel,
    userViewModel: UserViewModel,
    myId: Int,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    if (posts.isEmpty()) {
        Text(
            text = "There is no Ribbit from following users of this list",
            modifier = modifier
        )
    } else {
        val comparator = compareByDescending<RibbitPost> { it.id }

        val sortedRibbitPost = remember(posts, comparator) {
            posts.sortedWith(comparator)
        }   // LazyColumn items 에 List 를 바로 주는 것이 아니라 Comparator 로 정렬하여 remember 로 기억시켜서 recomposition 을 방지하여 성능을 올린다.
        LazyColumn(modifier = modifier) {
            items(items = sortedRibbitPost, key = { post -> post.id }) {
                RibbitCard(
                    post = it,
                    getCardViewModel = getCardViewModel,
                    myId = myId,
                    navController = navController,
                    userViewModel = userViewModel,
                    modifier = modifier
                )
            }
        }
    }
}