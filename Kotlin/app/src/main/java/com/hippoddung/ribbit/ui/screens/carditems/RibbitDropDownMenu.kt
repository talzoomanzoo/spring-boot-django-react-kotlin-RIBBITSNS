package com.hippoddung.ribbit.ui.screens.carditems

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.viewmodel.CardViewModel
import kotlinx.coroutines.runBlocking

@Composable
fun RibbitDropDownMenu(
    post: RibbitPost,
    cardViewModel: CardViewModel,
    userId: Int,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var isDropDownMenuExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = { isDropDownMenuExpanded = true },
            modifier = modifier.align(Alignment.CenterEnd)
                .padding(start = 4.dp, end = 4.dp),
        ) {
            Icon(
                imageVector = Icons.Default.MoreHoriz,
                contentDescription = "RibbitDropDownMenu icon",
                modifier = modifier.padding(start = 4.dp, end = 4.dp),
            )
        }

        Box(modifier = modifier.align(Alignment.BottomEnd)) {
            DropdownMenu(
                expanded = isDropDownMenuExpanded,
                onDismissRequest = { isDropDownMenuExpanded = false },
                modifier = modifier
                    .wrapContentSize()
                    .padding(start = 4.dp, end = 4.dp),
            ) {
                if (post.user.id == userId) {
                    DropdownMenuItem(
                        onClick = {
                            // 본인 계정이 아닌 경우 서버에서 삭제를 거부함. UI단계에서 타 계정의 접근을 막아야 함.
                            runBlocking { cardViewModel.deleteRibbitPost(post.id) }
                            Log.d("HippoLog, RibbitDropDownMenu", "${post.id}")
                            isDropDownMenuExpanded = false
                            cardViewModel.getRibbitPosts()
                        },
                        text = {
                            Text(
                                text = "Delete",
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
                                text = "Edit",
                                color = Color.Blue,
                                fontStyle = FontStyle.Italic,
                                fontSize = 14.sp,
                                style = TextStyle(shadow = Shadow(Color.Black))
                            )
                        }
                    )
                }
                DropdownMenuItem(
                    onClick = {
                        cardViewModel.getPostIdPost(post.id)    // 뷰 카운트 메소드 호출을 getPostIdPost메소드에서 실행하도록 함.
                        navController.navigate(RibbitScreen.PostIdScreen.name)
                        isDropDownMenuExpanded = false
                    },
                    text = {
                        Text(
                            text = "Details",
                            color = Color.Blue,
                            fontStyle = FontStyle.Italic,
                            fontSize = 14.sp,
                            style = TextStyle(shadow = Shadow(Color.Black))
                        )
                    }
                )
            }
        }
    }
}