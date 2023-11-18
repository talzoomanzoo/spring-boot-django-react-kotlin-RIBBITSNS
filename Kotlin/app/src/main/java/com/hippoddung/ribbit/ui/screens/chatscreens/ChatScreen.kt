import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.network.bodys.User
import com.hippoddung.ribbit.ui.screens.RibbitTopAppBar
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.ChatViewModel
import com.hippoddung.ribbit.ui.viewmodel.CommuViewModel
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.ListViewModel
import com.hippoddung.ribbit.ui.viewmodel.TokenViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalComposeUiApi
@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel,
    getCardViewModel: GetCardViewModel,
    tokenViewModel: TokenViewModel,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    listViewModel: ListViewModel,
    commuViewModel: CommuViewModel,
    navController: NavHostController,
    myProfile: User
) {
    val chatHistory by chatViewModel.chatHistory.collectAsState()
    var message by remember { mutableStateOf("") }
    val lazyColumnState = rememberLazyListState()

    LaunchedEffect(chatViewModel) {
        // Retrieve chat history when the screen is launched
        chatViewModel.selectedRoomIdState.let { chatViewModel.getChatHistory(it) }
        lazyColumnState.scrollToItem(chatHistory.size)
    }


    Scaffold(
        modifier = modifier,
        topBar = {
            RibbitTopAppBar(
                getCardViewModel = getCardViewModel,
                tokenViewModel = tokenViewModel,
                authViewModel = authViewModel,
                userViewModel = userViewModel,
                listViewModel = listViewModel,
                commuViewModel = commuViewModel,
                navController = navController,
                modifier = modifier
            )
        }
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(modifier = modifier) {
                // Example: Display chat messages in a LazyColumn
                LazyColumn(
                    modifier = modifier,
                    state = lazyColumnState
                    ) {
                    items(chatHistory) { chatMessage ->
                        // Display each chat message
                        Card(modifier = modifier.fillMaxWidth()) {
                            Text(
                                text = "${chatMessage.sender}: ${chatMessage.message}",
                                modifier = modifier
                            )
                            Log.d("HippoLog ChatScreen", "chatHistory: $chatMessage")
                        }
                    }
                    item {
                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .padding(top = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val context = LocalContext.current
                            val view = LocalView.current
                            BasicTextField(
                                value = message,
                                onValueChange = { message = it },
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Send
                                ),
                                keyboardActions = KeyboardActions(
                                    onSend = {
                                        // Send the message to the server
                                        chatViewModel.sendMessage(
                                            email = myProfile.email,
                                            message = message,
                                            sender = myProfile.fullName
                                        )

                                        // Clear the input field
                                        message = ""

                                        // Hide the keyboard
                                        hideKeyboard(context, view)
                                    }
                                ),
                                modifier = modifier
                                    .weight(1f)
                                    .padding(end = 8.dp),
                            )

                            IconButton(
                                onClick = {
                                    // Send the message to the server
                                    chatViewModel.sendMessage(
                                        email = myProfile.email,
                                        message = message,
                                        sender = myProfile.fullName
                                    )

                                    // Clear the input field
                                    message = ""

                                    // Hide the keyboard
                                    hideKeyboard(context, view)
                                },
                                modifier = modifier
                                    .size(48.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "Send",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = modifier
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun hideKeyboard(context: Context, view: View) {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}