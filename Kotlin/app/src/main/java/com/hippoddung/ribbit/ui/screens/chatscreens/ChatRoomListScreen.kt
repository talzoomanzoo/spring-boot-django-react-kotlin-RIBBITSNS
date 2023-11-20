package com.hippoddung.ribbit.ui.screens.chatscreens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.network.bodys.User
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.viewmodel.ChatViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatRoomListScreen(
    navController: NavHostController,
    chatViewModel: ChatViewModel,
    myProfile: User,
    modifier: Modifier
) {
    val chatRooms by chatViewModel.chatRooms.collectAsState()

    Scaffold(
        modifier = modifier,
//        topBar = {
//            RibbitTopAppBar(
//                getCardViewModel = getCardViewModel,
//                tokenViewModel = tokenViewModel,
//                authViewModel = authViewModel,
//                userViewModel = userViewModel,
//                listViewModel = listViewModel,
//                commuViewModel = commuViewModel,
//                navController = navController,
//                modifier = modifier
//            )
//        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(RibbitScreen.CreateChatRoomScreen.name) },
                modifier = modifier
                    .padding(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Floating action button.",
                    modifier = modifier
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LaunchedEffect(chatViewModel) {
                // Retrieve chat rooms when the screen is launched
                chatViewModel.getChatRooms()
            }

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text("Chat Rooms", style = MaterialTheme.typography.titleLarge, modifier = modifier)

                Spacer(modifier = modifier.height(16.dp))

                // Display the list of chat rooms
                LazyColumn(modifier = modifier) {
                    items(chatRooms) { chatRoom ->
                        ChatRoomItem(
                            navController = navController,
                            chatViewModel = chatViewModel,
                            chatRoom = chatRoom,
                            modifier = modifier
                        )
                    }
                }
            }
        }
    }
}