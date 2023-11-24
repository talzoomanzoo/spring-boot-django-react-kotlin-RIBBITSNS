package com.hippoddung.ribbit.ui.viewmodel

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hippoddung.ribbit.data.network.RibbitRepository
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.network.bodys.requestbody.ReplyRequest
import com.hippoddung.ribbit.ui.RibbitScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import javax.inject.Inject

sealed interface HomeUiState {
    data class Success(val posts: List<RibbitPost>) : HomeUiState
    data class Error(val errorCode: String) : HomeUiState
    object Loading : HomeUiState
}

sealed interface ClassificationUiState{
    object Recent: ClassificationUiState
    object Following: ClassificationUiState
    object TopViews: ClassificationUiState
    object TopLikes: ClassificationUiState
}

sealed interface DeletePostUiState {
    object Ready : DeletePostUiState
    object Success : DeletePostUiState
    data class Error(val errorCode: String) : DeletePostUiState
    object Loading : DeletePostUiState
}

sealed interface PostIdUiState {
    data class Success(val post: RibbitPost) : PostIdUiState
    data class Error(val errorCode: String) : PostIdUiState
    object Loading : PostIdUiState
}

sealed interface GetUserIdPostsUiState {
    data class Success( val posts: List<RibbitPost> ) : GetUserIdPostsUiState
    data class Error(val errorCode: String) : GetUserIdPostsUiState
    object Loading : GetUserIdPostsUiState
}

sealed interface UserIdClassificationUiState{
    object Ribbit: UserIdClassificationUiState
    object Replies: UserIdClassificationUiState
    object Media: UserIdClassificationUiState
    object Likes: UserIdClassificationUiState
}

sealed interface GetListIdPostsUiState {
    data class Success( val posts: List<RibbitPost> ) : GetListIdPostsUiState
    data class Error(val errorCode: String) : GetListIdPostsUiState
    object Loading : GetListIdPostsUiState
}
sealed interface GetAllCommuPostsUiState {
    data class Success( val posts: List<RibbitPost> ) : GetAllCommuPostsUiState
    data class Error(val errorCode: String) : GetAllCommuPostsUiState
    object Loading : GetAllCommuPostsUiState
}

sealed interface GetCommuIdPostsUiState {
    data class Success( val posts: List<RibbitPost> ) : GetCommuIdPostsUiState
    data class Error(val errorCode: String) : GetCommuIdPostsUiState
    object Loading : GetCommuIdPostsUiState
}

sealed interface PostReplyUiState {
    object Ready : PostReplyUiState
    object Loading : PostReplyUiState
    object Success : PostReplyUiState
    object Error : PostReplyUiState
}

sealed interface ReplyClickedUiState {
    object Clicked : ReplyClickedUiState
    object NotClicked : ReplyClickedUiState
}

@HiltViewModel
class GetCardViewModel @Inject constructor(    // 원래 HomeViewModel 이었으나 ViewModel 의 기능을 적절히 설명하기 위해 이름을 변경
    private val ribbitRepository: RibbitRepository
) : ViewModel() {
    var homeUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)

    var classificationUiState: ClassificationUiState by mutableStateOf(ClassificationUiState.Recent)
    var deletePostUiState: DeletePostUiState by mutableStateOf(DeletePostUiState.Ready)
    var postIdUiState: PostIdUiState by mutableStateOf(PostIdUiState.Loading)

    var getUserIdPostsUiState: GetUserIdPostsUiState by mutableStateOf(GetUserIdPostsUiState.Loading)
    var userIdClassificationUiState: UserIdClassificationUiState by mutableStateOf(UserIdClassificationUiState.Ribbit)

    var getListIdPostsUiState: GetListIdPostsUiState by mutableStateOf(GetListIdPostsUiState.Loading)

    var getCommuIdPostsUiState: GetCommuIdPostsUiState by mutableStateOf(GetCommuIdPostsUiState.Loading)
    var getAllCommuPostsUiState: GetAllCommuPostsUiState by mutableStateOf(GetAllCommuPostsUiState.Loading)

    var replyPostIdUiState: Int? by mutableStateOf(null)
    var postReplyUiState: PostReplyUiState by mutableStateOf(PostReplyUiState.Ready)
    var replyClickedUiState: ReplyClickedUiState by mutableStateOf(ReplyClickedUiState.NotClicked)

    private var _postsSearchData = MutableStateFlow<List<RibbitPost>>(listOf())
    val postsSearchData: StateFlow<List<RibbitPost>> = _postsSearchData.asStateFlow()

//    var whereReplyClickedUiState: WhereReplyClickedUiState by mutableStateOf( // 현재 viewModel 에 접근하는 스크린의 정보상태를 저장하려고 했으나 아래의 currentScreenState 로 대체
//        WhereReplyClickedUiState.HomeScreen
//    )
    private val currentScreenState = mutableStateOf(RibbitScreen.HomeScreen)    // 현재 viewModel 에 접근하는 스크린의 정보를 가져온다.
    fun setCurrentScreen(screen: RibbitScreen) {
        currentScreenState.value = screen
    }

    fun getRibbitPosts() {  // 모든 Post 를 불러오는 메소드
        viewModelScope.launch(Dispatchers.IO) {
            homeUiState = HomeUiState.Loading
            classificationUiState = ClassificationUiState.Recent
            Log.d("HippoLog, GetCardViewModel", "getRibbitPosts, $homeUiState")
            homeUiState = try {
                HomeUiState.Success(ribbitRepository.getRibbitPosts())
            } catch (e: IOException) {
                Log.d("HippoLog, GetCardViewModel", "getRibbitPosts, ${e.stackTrace}, ${e.message}")
                HomeUiState.Error(e.message.toString())
            } catch (e: ExceptionInInitializerError) {
                Log.d("HippoLog, GetCardViewModel", "getRibbitPosts, ${e.stackTrace}, ${e.message}")
                HomeUiState.Error(e.message.toString())
            } catch (e: HttpException) {
                Log.d("HippoLog, GetCardViewModel", "getRibbitPosts, ${e.stackTrace}, ${e.code()}, $e")
                if (e.code() == 500) {
                    HomeUiState.Error(e.code().toString())
                } else {
                    HomeUiState.Error(e.message.toString())
                }
            }
            Log.d("HippoLog, GetCardViewModel", "getRibbitPosts, $homeUiState")
        }
    }

    fun getFollowingPosts() {  // 모든 Post 를 불러오는 메소드
        viewModelScope.launch(Dispatchers.IO) {
            homeUiState = HomeUiState.Loading
            classificationUiState = ClassificationUiState.Following
            Log.d("HippoLog, GetCardViewModel", "getFollowingPosts, $homeUiState")
            homeUiState = try {
                HomeUiState.Success(ribbitRepository.getFollowingPosts())
            } catch (e: IOException) {
                Log.d("HippoLog, GetCardViewModel", "getFollowingPosts, ${e.stackTrace}, ${e.message}")
                HomeUiState.Error(e.message.toString())
            } catch (e: ExceptionInInitializerError) {
                Log.d("HippoLog, GetCardViewModel", "getFollowingPosts, ${e.stackTrace}, ${e.message}")
                HomeUiState.Error(e.message.toString())
            } catch (e: HttpException) {
                Log.d("HippoLog, GetCardViewModel", "getFollowingPosts, ${e.stackTrace}, ${e.code()}, $e")
                if (e.code() == 500) {
                    HomeUiState.Error(e.code().toString())
                } else {
                    HomeUiState.Error(e.message.toString())
                }
            }
            Log.d("HippoLog, GetCardViewModel", "getFollowingPosts, $homeUiState")
        }
    }

    fun getTopViewsRibbitPosts() {  // 모든 Post 를 불러오는 메소드
        viewModelScope.launch(Dispatchers.IO) {
            homeUiState = HomeUiState.Loading
            classificationUiState = ClassificationUiState.TopViews
            Log.d("HippoLog, GetCardViewModel", "getTopViewsRibbitPosts, $homeUiState")
            homeUiState = try {
                HomeUiState.Success(ribbitRepository.getTopViewsRibbitPosts())
            } catch (e: IOException) {
                Log.d("HippoLog, GetCardViewModel", "getTopViewsRibbitPosts, ${e.stackTrace}, ${e.message}")
                HomeUiState.Error(e.message.toString())
            } catch (e: ExceptionInInitializerError) {
                Log.d("HippoLog, GetCardViewModel", "getTopViewsRibbitPosts, ${e.stackTrace}, ${e.message}")
                HomeUiState.Error(e.message.toString())
            } catch (e: HttpException) {
                Log.d("HippoLog, GetCardViewModel", "getTopViewsRibbitPosts, ${e.stackTrace}, ${e.code()}, $e")
                if (e.code() == 500) {
                    HomeUiState.Error(e.code().toString())
                } else {
                    HomeUiState.Error(e.message.toString())
                }
            }
            Log.d("HippoLog, GetCardViewModel", "getTopViewsRibbitPosts, $homeUiState")
        }
    }

    fun getTopLikesRibbitPosts() {  // 모든 Post 를 불러오는 메소드
        viewModelScope.launch(Dispatchers.IO) {
            homeUiState = HomeUiState.Loading
            classificationUiState = ClassificationUiState.TopLikes
            Log.d("HippoLog, GetCardViewModel", "getTopLikesRibbitPosts, $homeUiState")
            homeUiState = try {
                HomeUiState.Success(ribbitRepository.getTopLikesRibbitPosts())
            } catch (e: IOException) {
                Log.d("HippoLog, GetCardViewModel", "getTopLikesRibbitPosts, ${e.stackTrace}, ${e.message}")
                HomeUiState.Error(e.message.toString())
            } catch (e: ExceptionInInitializerError) {
                Log.d("HippoLog, GetCardViewModel", "getTopLikesRibbitPosts, ${e.stackTrace}, ${e.message}")
                HomeUiState.Error(e.message.toString())
            } catch (e: HttpException) {
                Log.d("HippoLog, GetCardViewModel", "getTopLikesRibbitPosts, ${e.stackTrace}, ${e.code()}, $e")
                if (e.code() == 500) {
                    HomeUiState.Error(e.code().toString())
                } else {
                    HomeUiState.Error(e.message.toString())
                }
            }
            Log.d("HippoLog, GetCardViewModel", "getTopLikesRibbitPosts, $homeUiState")
        }
    }

    suspend fun deleteRibbitPost(postId: Int) { // Post 를 삭제하는 메소드, 정상적인 페이지 노출을 위해 동기함수로 구성
        Log.d("HippoLog, GetCardViewModel", "deleteRibbitPost, $postId")
        try {
            deletePostUiState = DeletePostUiState.Loading
            ribbitRepository.deletePost(postId)
        } catch (e: IOException) {
            Log.d("HippoLog, GetCardViewModel", "${e.stackTrace}, ${e.message}")
            DeletePostUiState.Error(e.message.toString())

        } catch (e: ExceptionInInitializerError) {
            Log.d("HippoLog, GetCardViewModel", "${e.stackTrace}, ${e.message}")
            DeletePostUiState.Error(e.message.toString())

        } catch (e: HttpException) {
            Log.d("HippoLog, GetCardViewModel", "${e.stackTrace}, ${e.code()}, $e")
            if (e.code() == 500) {
                DeletePostUiState.Error(e.code().toString())
            } else {
                DeletePostUiState.Error(e.message.toString())
            }
        }
        Log.d("HippoLog, GetCardViewModel", "deleteRibbitPost")
    }

    fun getPostIdPost(postId: Int) {    // PostDetail 을 불러오는 함수
        viewModelScope.launch(Dispatchers.IO) {
            postIdUiState = PostIdUiState.Loading
            Log.d("HippoLog, GetCardViewModel", "getPostIdPost, $postIdUiState")
            postPostIdCount(postId)
            postIdUiState = try {
                PostIdUiState.Success(ribbitRepository.getPostIdPost(postId))
            } catch (e: IOException) {
                Log.d("HippoLog, GetCardViewModel", "getPostIdPost: ${e.stackTrace}, ${e.message}")
                PostIdUiState.Error(e.message.toString())

            } catch (e: ExceptionInInitializerError) {
                Log.d("HippoLog, GetCardViewModel", "getPostIdPost: ${e.stackTrace}, ${e.message}")
                PostIdUiState.Error(e.message.toString())

            } catch (e: HttpException) {
                Log.d("HippoLog, GetCardViewModel", "getPostIdPost: ${e.stackTrace}, ${e.code()}, $e")
                if (e.code() == 500) {
                    PostIdUiState.Error(e.code().toString())
                } else {
                    PostIdUiState.Error(e.message.toString())
                }
            }
            Log.d("HippoLog, GetCardViewModel", "getPostIdPost, $postIdUiState")
        }
    }

    fun getUserIdPosts(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            getUserIdPostsUiState = GetUserIdPostsUiState.Loading
            userIdClassificationUiState = UserIdClassificationUiState.Ribbit
            Log.d("HippoLog, GetCardViewModel", "getUserIdPost, $getUserIdPostsUiState")
            getUserIdPostsUiState = try {
                GetUserIdPostsUiState.Success( posts = ribbitRepository.getUserIdPosts(userId) )
            } catch (e: IOException) {
                Log.d("HippoLog, GetCardViewModel", "getUserIdPost: ${e.stackTrace}, ${e.message}")
                GetUserIdPostsUiState.Error(e.message.toString())

            } catch (e: ExceptionInInitializerError) {
                Log.d("HippoLog, GetCardViewModel", "getUserIdPost: ${e.stackTrace}, ${e.message}")
                GetUserIdPostsUiState.Error(e.message.toString())

            } catch (e: HttpException) {
                Log.d("HippoLog, GetCardViewModel", "getUserIdPost: ${e.stackTrace}, ${e.code()}, $e")
                if (e.code() == 500) {
                    GetUserIdPostsUiState.Error(e.code().toString())
                } else {
                    GetUserIdPostsUiState.Error(e.message.toString())
                }
            }
            Log.d("HippoLog, GetCardViewModel", "getUserIdPosts, $getUserIdPostsUiState")
        }
    }

    fun getUserIdReplies(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            getUserIdPostsUiState = GetUserIdPostsUiState.Loading
            userIdClassificationUiState = UserIdClassificationUiState.Replies
            Log.d("HippoLog, GetCardViewModel", "getUserIdReplies, $getUserIdPostsUiState")
            getUserIdPostsUiState = try {
                GetUserIdPostsUiState.Success(ribbitRepository.getUserIdReplies(userId))
            } catch (e: IOException) {
                Log.d("HippoLog, GetCardViewModel", "getUserIdReplies: ${e.stackTrace}, ${e.message}")
                GetUserIdPostsUiState.Error(e.message.toString())

            } catch (e: ExceptionInInitializerError) {
                Log.d("HippoLog, GetCardViewModel", "getUserIdReplies: ${e.stackTrace}, ${e.message}")
                GetUserIdPostsUiState.Error(e.message.toString())

            } catch (e: HttpException) {
                Log.d("HippoLog, GetCardViewModel", "getUserIdReplies: ${e.stackTrace}, ${e.code()}, $e")
                if (e.code() == 500) {
                    GetUserIdPostsUiState.Error(e.code().toString())
                } else {
                    GetUserIdPostsUiState.Error(e.message.toString())
                }
            }
            Log.d("HippoLog, GetCardViewModel", "getUserIdReplies, $getUserIdPostsUiState")
        }
    }

    fun getUserIdMedias(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            getUserIdPostsUiState = GetUserIdPostsUiState.Loading
            userIdClassificationUiState = UserIdClassificationUiState.Media
            Log.d("HippoLog, GetCardViewModel", "getUserIdPost, $getUserIdPostsUiState")
            getUserIdPostsUiState = try {
                GetUserIdPostsUiState.Success( posts = ribbitRepository.getUserIdPosts(userId) )
            } catch (e: IOException) {
                Log.d("HippoLog, GetCardViewModel", "getUserIdPost: ${e.stackTrace}, ${e.message}")
                GetUserIdPostsUiState.Error(e.message.toString())

            } catch (e: ExceptionInInitializerError) {
                Log.d("HippoLog, GetCardViewModel", "getUserIdPost: ${e.stackTrace}, ${e.message}")
                GetUserIdPostsUiState.Error(e.message.toString())

            } catch (e: HttpException) {
                Log.d("HippoLog, GetCardViewModel", "getUserIdPost: ${e.stackTrace}, ${e.code()}, $e")
                if (e.code() == 500) {
                    GetUserIdPostsUiState.Error(e.code().toString())
                } else {
                    GetUserIdPostsUiState.Error(e.message.toString())
                }
            }
            Log.d("HippoLog, GetCardViewModel", "getUserIdPosts, $getUserIdPostsUiState")
        }
    }

    fun getUserIdLikes(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            getUserIdPostsUiState = GetUserIdPostsUiState.Loading
            userIdClassificationUiState = UserIdClassificationUiState.Likes
            Log.d("HippoLog, GetCardViewModel", "getUserIdLikes, $getUserIdPostsUiState")
            getUserIdPostsUiState = try {
                GetUserIdPostsUiState.Success(ribbitRepository.getUserIdLikes(userId))
            } catch (e: IOException) {
                Log.d("HippoLog, GetCardViewModel", "getUserIdLikes: ${e.stackTrace}, ${e.message}")
                GetUserIdPostsUiState.Error(e.message.toString())

            } catch (e: ExceptionInInitializerError) {
                Log.d("HippoLog, GetCardViewModel", "getUserIdLikes: ${e.stackTrace}, ${e.message}")
                GetUserIdPostsUiState.Error(e.message.toString())

            } catch (e: HttpException) {
                Log.d("HippoLog, GetCardViewModel", "getUserIdLikes: ${e.stackTrace}, ${e.code()}, $e")
                if (e.code() == 500) {
                    GetUserIdPostsUiState.Error(e.code().toString())
                } else {
                    GetUserIdPostsUiState.Error(e.message.toString())
                }
            }
            Log.d("HippoLog, GetCardViewModel", "getUserIdLikes, $getUserIdPostsUiState")
        }
    }

    private suspend fun postPostIdCount(postId: Int) {  // Detail 을 불러올 때 ViewCount 가 정확하지 않은 문제를 해결하기 위해 동기작업으로 실행
        try {
            ribbitRepository.postPostIdCount(postId)
        } catch (e: IOException) {
            Log.d("HippoLog, GetCardViewModel", "postPostIdCount: ${e.stackTrace}, ${e.message}")

        } catch (e: ExceptionInInitializerError) {
            Log.d("HippoLog, GetCardViewModel", "postPostIdCount: ${e.stackTrace}, ${e.message}")

        } catch (e: HttpException) {
            Log.d("HippoLog, GetCardViewModel", "postPostIdCount: ${e.stackTrace}, ${e.code()}, $e")
            if (e.code() == 500) {
            } else {
            }
        }
    }

    fun retrieveThumbnailFromVideo(videoUrl: String?): Future<Bitmap> {
        val executor = Executors.newFixedThreadPool(2)
        val future: Future<Bitmap> = executor.submit(
            Callable<Bitmap> {
                var bitmap: Bitmap? = null
                val mediaMetadataRetriever: MediaMetadataRetriever?
                mediaMetadataRetriever = MediaMetadataRetriever()
                mediaMetadataRetriever.setDataSource(videoUrl, HashMap())

                try {
                    Log.d(
                        "HippoLog, GetCardViewModel",
                        "retrieve Thread - ${Thread.currentThread().name}"
                    )
                    bitmap = mediaMetadataRetriever.getFrameAtTime(100000)
                } catch (e: SocketTimeoutException) {
                    Log.d(
                        "HippoLog, GetCardViewModel",
                        "Exception in retrieveThumbnailFromVideo: ${e.message}"
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d(
                        "HippoLog, GetCardViewModel",
                        "Exception in retrieveThumbnailFromVideo: ${e.message}"
                    )
                } finally {
                    mediaMetadataRetriever.release()
                }
                return@Callable bitmap
            }
        )
        return future
    }

    fun postReply(inputText: String, postId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            postReplyUiState = PostReplyUiState.Loading
            val replyRequest = ReplyRequest(content = inputText, twitId = postId)
            try {
                ribbitRepository.postReply(replyRequest)
                postReplyUiState = PostReplyUiState.Success
            } catch (e: IOException) {
                postReplyUiState = PostReplyUiState.Error
                println(e.stackTrace)
            } catch (e: ExceptionInInitializerError) {
                postReplyUiState = PostReplyUiState.Error
                println(e.stackTrace)
            }
            when (currentScreenState.value) {
                RibbitScreen.HomeScreen -> {
                    getRibbitPosts()    // 댓글수 정보를 정확하게 표시하기 위해 다시 request
                }
                RibbitScreen.PostIdScreen -> {
                    getPostIdPost((postIdUiState as PostIdUiState.Success).post.id) // 새로운 댓글을 표시하기 위해 다시 request
                }
                else -> {}
            }
        }
    }

    fun postPostIdLike(postId: Int) {    // 이 통신으로 like, deleteLike 를 다 함.
        viewModelScope.launch(Dispatchers.IO) {
            try {
                ribbitRepository.postPostIdLike(postId)
            } catch (e: IOException) {
                println(e.stackTrace)
            } catch (e: ExceptionInInitializerError) {
                println(e.stackTrace)
            }
        }
    }

//    fun deletePostIdLike(postId: Int) {  // 서버 컨트롤러에 있지만 서버에서 구현되지 않은 기능.
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                ribbitRepository.deletePostIdLike(postId)
//            } catch (e: IOException) {
//                println(e.stackTrace)
//            } catch (e: ExceptionInInitializerError) {
//                println(e.stackTrace)
//            }
//        }
//    }

    fun putPostIdRepost(postId: Int) {   // 얘도 이것 만으로 repost 와 deleteRepost 를 다 함.
        viewModelScope.launch(Dispatchers.IO) {
            try {
                ribbitRepository.putPostIdRepost(postId)
            } catch (e: IOException) {
                println(e.stackTrace)
            } catch (e: ExceptionInInitializerError) {
                println(e.stackTrace)
            }
        }
    }

    fun getPostsSearch(searchQuery: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("HippoLog, UserViewModel", "getUserSearch")
                _postsSearchData.value = (ribbitRepository.getPostsSearch(searchQuery))
            } catch (e: IOException) {
                Log.d("HippoLog, UserViewModel", "getUserSearch error: ${e.message}")
                SearchingUserUiState.Error
            } catch (e: ExceptionInInitializerError) {
                Log.d("HippoLog, UserViewModel", "getUserSearch error: ${e.message}")
                SearchingUserUiState.Error
            }
        }
    }

    fun getListIdPosts(listId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            getListIdPostsUiState = GetListIdPostsUiState.Loading
            userIdClassificationUiState = UserIdClassificationUiState.Ribbit
            Log.d("HippoLog, GetCardViewModel", "getListIdPosts, $getListIdPostsUiState")
            getListIdPostsUiState = try {
                GetListIdPostsUiState.Success( posts = ribbitRepository.getListIdPosts(listId) )
            } catch (e: IOException) {
                Log.d("HippoLog, GetCardViewModel", "getListIdPosts: ${e.stackTrace}, ${e.message}")
                GetListIdPostsUiState.Error(e.message.toString())

            } catch (e: ExceptionInInitializerError) {
                Log.d("HippoLog, GetCardViewModel", "getListIdPosts: ${e.stackTrace}, ${e.message}")
                GetListIdPostsUiState.Error(e.message.toString())

            } catch (e: HttpException) {
                Log.d("HippoLog, GetCardViewModel", "getListIdPosts: ${e.stackTrace}, ${e.code()}, $e")
                if (e.code() == 500) {
                    GetListIdPostsUiState.Error(e.code().toString())
                } else {
                    GetListIdPostsUiState.Error(e.message.toString())
                }
            }
            Log.d("HippoLog, GetCardViewModel", "getListIdPosts, $getListIdPostsUiState")
        }
    }

    fun getAllCommuPosts() {  // 모든 Post 를 불러오는 메소드
        viewModelScope.launch(Dispatchers.IO) {
            getAllCommuPostsUiState = GetAllCommuPostsUiState.Loading
            Log.d("HippoLog, GetCardViewModel", "getAllCommuPosts, $getAllCommuPostsUiState")
            getAllCommuPostsUiState = try {
                GetAllCommuPostsUiState.Success(ribbitRepository.getAllCommuPosts())
            } catch (e: IOException) {
                Log.d("HippoLog, GetCardViewModel", "getAllCommuPosts, ${e.stackTrace}, ${e.message}")
                GetAllCommuPostsUiState.Error(e.message.toString())
            } catch (e: ExceptionInInitializerError) {
                Log.d("HippoLog, GetCardViewModel", "getAllCommuPosts, ${e.stackTrace}, ${e.message}")
                GetAllCommuPostsUiState.Error(e.message.toString())
            } catch (e: HttpException) {
                Log.d("HippoLog, GetCardViewModel", "getAllCommuPosts, ${e.stackTrace}, ${e.code()}, $e")
                if (e.code() == 500) {
                    GetAllCommuPostsUiState.Error(e.code().toString())
                } else {
                    GetAllCommuPostsUiState.Error(e.message.toString())
                }
            }
            Log.d("HippoLog, GetCardViewModel", "getAllCommuPosts, $getAllCommuPostsUiState")
        }
    }

    fun getCommuIdPosts(commuId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            getCommuIdPostsUiState = GetCommuIdPostsUiState.Loading
            userIdClassificationUiState = UserIdClassificationUiState.Ribbit
            Log.d("HippoLog, GetCardViewModel", "getCommuIdPosts, $getCommuIdPostsUiState")
            getCommuIdPostsUiState = try {
                GetCommuIdPostsUiState.Success( posts = ribbitRepository.getCommuIdPosts(commuId) )
            } catch (e: IOException) {
                Log.d("HippoLog, GetCardViewModel", "getCommuIdPosts: ${e.stackTrace}, ${e.message}")
                GetCommuIdPostsUiState.Error(e.message.toString())

            } catch (e: ExceptionInInitializerError) {
                Log.d("HippoLog, GetCardViewModel", "getCommuIdPosts: ${e.stackTrace}, ${e.message}")
                GetCommuIdPostsUiState.Error(e.message.toString())

            } catch (e: HttpException) {
                Log.d("HippoLog, GetCardViewModel", "getCommuIdPosts: ${e.stackTrace}, ${e.code()}, $e")
                if (e.code() == 500) {
                    GetCommuIdPostsUiState.Error(e.code().toString())
                } else {
                    GetCommuIdPostsUiState.Error(e.message.toString())
                }
            }
            Log.d("HippoLog, GetCardViewModel", "getCommuIdPosts, $getCommuIdPostsUiState")
        }
    }
}