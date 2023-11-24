import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.widget.addTextChangedListener
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.network.bodys.User
import com.hippoddung.ribbit.ribbitmethod.calculationTime
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.screens.RibbitTopAppBar
import com.hippoddung.ribbit.ui.screens.chatscreens.SearchedUserAtChatCard
import com.hippoddung.ribbit.ui.screens.searchitems.SearchedGrid
import com.hippoddung.ribbit.ui.screens.searchitems.SearchedUserCard
import com.hippoddung.ribbit.ui.theme.Shapes
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalComposeUiApi
@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel,
    userViewModel: UserViewModel,
    myProfile: User
) {
    val chatHistory by chatViewModel.chatHistory.collectAsState()
    var message by remember { mutableStateOf("") }
    val lazyColumnState = rememberLazyListState()

    DisposableEffect(chatHistory) {

        CoroutineScope(Dispatchers.Main).launch {
            // Scroll to the bottom whenever the chat history changes
            lazyColumnState.scrollToItem(chatHistory.size)
        }

        onDispose { /* cleanup code if needed */ }
    }

    LaunchedEffect(chatViewModel) {
        // Retrieve chat history when the screen is launched
        chatViewModel.selectedRoomIdState.let { chatViewModel.getChatHistory(it) }
    }

    var isAddUserButtonClicked by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = chatViewModel.selectedRoomNameState)
                TextButton(onClick = { isAddUserButtonClicked = true }) {
                    Text(text = "Add User")
                }
            }
        }
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            color = MaterialTheme.colorScheme.tertiary
        ) {
            Box(modifier = modifier) {

                DropdownMenu(
                    expanded = isAddUserButtonClicked,
                    onDismissRequest = { isAddUserButtonClicked = false },
                    modifier = modifier
                        .animateContentSize(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioNoBouncy,
                                stiffness = Spring.StiffnessMedium
                            )
                        )
                        .fillMaxWidth()
                ) {
                    var isSearched by remember { mutableStateOf(false) }
                    var searchQuery by remember { mutableStateOf(TextFieldValue()) }
                    val usersSearchData by userViewModel.usersSearchData.collectAsState()
                    val userComparator = compareByDescending<User> { it.id }
                    val sortedUsersSearch = remember(usersSearchData, userComparator) {
                        usersSearchData.sortedWith(userComparator)
                    }   // LazyColumn items 에 List 를 바로 주는 것이 아니라 Comparator 로 정렬하여 remember 로 기억시켜서 recomposition 을 방지하여 성능을 올린다.
                    TextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            if (it.text.isNotEmpty()) {
                                // 사용자가 검색어를 입력하면 여기에서 작업을 수행할 수 있음
                                // 예: 자동완성 결과 업데이트, 네트워크 요청 등
                                userViewModel.getUsersSearch(it.text)
                                isSearched = true
                            }
                        },
                        label = {
                            Text(
                                text = "Search User and Ribbit",
                                modifier = modifier
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color(0xFF006400),
                                modifier = modifier
                            )
                        },
                        trailingIcon = {
                            Row(modifier = modifier) {
//                    OutlinedButton(
//                        onClick = {
//                            isExpanded = !isExpanded
//                        },
//                        modifier = modifier
//                    ) {
//                        Text(
//                            text = "Search",
//                            modifier = modifier
//                        )
//                    }
                                IconButton(
                                    onClick = {
                                        isAddUserButtonClicked = !isAddUserButtonClicked
                                    },
                                    modifier = modifier.padding(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Collapse",
                                        tint = Color(0xFF006400),
                                        modifier = modifier
                                    )
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Search
                        ),
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 16.sp),
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .background(Color.White)
                    )
                    if (isSearched) {
                        Box(
                            modifier = modifier
                                .sizeIn(maxWidth = Dp.Infinity, maxHeight = Dp.Infinity)
                        ) {
                            Column(modifier = modifier) {
                                sortedUsersSearch.forEach { user ->
                                    SearchedUserAtChatCard(
                                        user = user,
                                        userViewModel = userViewModel,
                                        modifier = modifier
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Box(modifier = modifier) {
                // Example: Display chat messages in a LazyColumn
                LazyColumn(
                    modifier = modifier
                        .padding(bottom = 60.dp),   // 화면이 꽉 찬 경우 채팅바가 겹치는 문제 해결을 위한 패딩
                    state = lazyColumnState
                ) {
                    items(chatHistory) { chatMessage ->
                        // Display each chat message
                        Log.d("HippoLog ChatScreen", "chatHistory: $chatMessage")
                        Column(modifier = modifier) {
                            if (chatMessage.email != myProfile.email) {
                                Row(
                                    modifier = modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.frog_8341850_1280),
                                        contentDescription = "default image",
                                        contentScale = ContentScale.Crop,
                                        modifier = modifier
                                            .size(40.dp)
                                    )
                                    Column(
                                        modifier = modifier,
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        Row(
                                            modifier = modifier
                                                .padding(
                                                    start = 8.dp,
                                                    end = 8.dp
                                                )
                                        ) {
                                            Text(
                                                text = "${chatMessage.sender}",
                                                modifier = modifier
                                            )
                                            Text(
                                                text = calculationTime(chatMessage.time!!),
                                                modifier = modifier
                                                    .padding(start = 16.dp, end = 16.dp)
                                            )
                                        }
                                        Card(
                                            shape = Shapes.small,
                                            modifier = modifier
                                                .padding(
                                                    start = 8.dp,
                                                    top = 8.dp,
                                                    end = 8.dp,
                                                    bottom = 8.dp
                                                )
                                        ) {
                                            Text(
                                                text = chatMessage.message,
                                                modifier = modifier
                                                    .padding(
                                                        start = 8.dp,
                                                        top = 8.dp,
                                                        end = 8.dp,
                                                        bottom = 8.dp
                                                    )
                                            )
                                        }
                                    }
                                }
                            } else {
                                Row(
                                    modifier = modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Column(
                                        modifier = modifier,
                                        horizontalAlignment = Alignment.End
                                    ) {
                                        Row(
                                            modifier = modifier
                                                .padding(
                                                    start = 8.dp,
                                                    end = 8.dp
                                                )
                                        ) {
                                            Text(
                                                text = calculationTime(chatMessage.time!!),
                                                modifier = modifier
                                                    .padding(start = 16.dp, end = 16.dp)
                                            )
                                            Text(
                                                text = "${chatMessage.sender}",
                                                modifier = modifier
                                            )
                                        }
                                        Card(
                                            shape = Shapes.small,
                                            modifier = modifier
                                                .padding(
                                                    start = 8.dp,
                                                    top = 8.dp,
                                                    end = 8.dp,
                                                    bottom = 8.dp
                                                )
                                        ) {
                                            Text(
                                                text = chatMessage.message,
                                                modifier = modifier
                                                    .padding(
                                                        start = 8.dp,
                                                        top = 8.dp,
                                                        end = 8.dp,
                                                        bottom = 8.dp
                                                    )
                                            )
                                        }
                                    }
                                }
                            }
//                            Canvas(
//                                modifier = modifier,
//                                onDraw = {
//                                    drawLine(
//                                        color = Color(0xFF4c6c4a),
//                                        start = Offset(0.dp.toPx(), 0.dp.toPx()),
//                                        end = Offset(400.dp.toPx(), 0.dp.toPx()),
//                                        strokeWidth = 1.dp.toPx(),
//                                    )
//                                }
//                            )
                        }
                    }
                }
                // 채팅 입력 row
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .align(Alignment.BottomCenter),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        shape = Shapes.small,
                        modifier = modifier
                            .weight(1f)
                            .padding(
                                start = 8.dp,
                                top = 8.dp,
                                end = 8.dp,
                                bottom = 8.dp
                            )
                    ) {
                        YourComposeContent(
                            chatViewModel = chatViewModel,
                            myProfile = myProfile,
                            initialMessage = message,
                            onMessageChange = { newMessage -> message = newMessage },
                            modifier = modifier
                        )
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

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun YourComposeContent(
    chatViewModel: ChatViewModel,
    myProfile: User,
    initialMessage: String,
    onMessageChange: (String) -> Unit,
    modifier: Modifier
) {
    var message by remember { mutableStateOf(initialMessage) }

    // Access the keyboard controller
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    val view = LocalView.current

    val editText = remember {
        EditText(context).apply {
            hint = "Type your message"
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    keyboardController?.show()
                }
            }
        }
    }

    Row(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = modifier
                .weight(1f)
                .fillMaxWidth()
                .height(60.dp)
        ) {
            AndroidView({ editText }, modifier = modifier.matchParentSize())
        }
        // Button to simulate sending the message
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

                // Set the text of the EditText to an empty string
                editText.setText("")

                // Hide the keyboard
                hideKeyboard(context, view)
            },
            modifier = modifier
                .size(48.dp)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send",
                tint = MaterialTheme.colorScheme.primary,
                modifier = modifier
            )
        }

        // Listen for text changes
        DisposableEffect(editText) {
            val textWatcher = object : android.text.TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    message = s.toString()
                    onMessageChange(message) // Notify the external code about the message change
                }

                override fun afterTextChanged(s: android.text.Editable?) {
                }
            }

            editText.addTextChangedListener(textWatcher)

            onDispose {
                editText.removeTextChangedListener(textWatcher)
            }
        }
    }
}