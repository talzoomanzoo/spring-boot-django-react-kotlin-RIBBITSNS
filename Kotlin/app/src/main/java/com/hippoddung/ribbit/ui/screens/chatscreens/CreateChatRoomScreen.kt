package com.hippoddung.ribbit.ui.screens.chatscreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.network.bodys.chatbodys.ChatRoomDto
import com.hippoddung.ribbit.network.bodys.User
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.screens.textfielditems.InputTextField
import com.hippoddung.ribbit.ui.viewmodel.ChatViewModel

@Composable
fun CreateChatRoomScreen(
    chatViewModel: ChatViewModel,
    navController: NavHostController,
    myProfile: User,
    modifier: Modifier
) {
    var chatRoomName by remember { mutableStateOf("") }

    Column(modifier = modifier) {

        Text(
            text = stringResource(R.string.create_chat_room),
            color = Color.Black,
            modifier = modifier
                .padding(bottom = 16.dp)
                .align(alignment = Alignment.Start)
        )
        InputTextField(
            value = chatRoomName,
            onValueChange = { chatRoomName = it },
            modifier = modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )

        Button(
            onClick = {
                // Create a new chat room
                val newChatRoom = ChatRoomDto(
                    name = chatRoomName,
                    creator = myProfile.fullName ?: "",
                    creatorEmail = myProfile.email
                )
                chatViewModel.createChatRoom(newChatRoom)
                navController.navigate(RibbitScreen.ChatRoomListScreen.name)
            },
            modifier = modifier
        ) {
            Text("Create Chat Room")
        }
    }
}