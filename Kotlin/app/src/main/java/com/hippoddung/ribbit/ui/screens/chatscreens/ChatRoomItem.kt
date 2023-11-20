package com.hippoddung.ribbit.ui.screens.chatscreens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.network.bodys.User
import com.hippoddung.ribbit.network.bodys.chatbodys.ChatRoomDto
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.viewmodel.ChatViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomItem(
    navController: NavHostController,
    chatViewModel: ChatViewModel,
    chatRoom: ChatRoomDto,
    modifier: Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        onClick = {
            chatViewModel.selectedRoomIdState = chatRoom.roomId
            chatViewModel.subscribeRoom(chatRoom.roomId)
            navController.navigate(RibbitScreen.ChatScreen.name)
        }
    ) {
        Column(
            modifier = modifier
                .padding(16.dp)
        ) {
            Text(text = chatRoom.name, style = MaterialTheme.typography.titleMedium, modifier = modifier)
            Text(text = "Created by: ${chatRoom.creator}", style = MaterialTheme.typography.titleMedium, modifier = modifier)
        }
    }
}