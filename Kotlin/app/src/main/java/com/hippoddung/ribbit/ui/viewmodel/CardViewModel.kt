package com.hippoddung.ribbit.ui.viewmodel

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.hippoddung.ribbit.data.network.RibbitRepository
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.network.bodys.requestbody.ReplyRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

sealed interface WhereReplyClickedUiState {
    object HomeScreen : WhereReplyClickedUiState
    object TwitIdScreen : WhereReplyClickedUiState
}

@HiltViewModel
class CardViewModel @Inject constructor(    // 원래 HomeViewModel 이었으나 ViewModel의 기능을 적절히 설명하기 위해 이름을 변경
    private val ribbitRepository: RibbitRepository
) : BaseViewModel() {
    var homeUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
    var deletePostUiState: DeletePostUiState by mutableStateOf(DeletePostUiState.Ready)
    var postIdUiState: PostIdUiState by mutableStateOf(PostIdUiState.Loading)

    var replyPostIdUiState: Int? by mutableStateOf(null)
    var postReplyUiState: PostReplyUiState by mutableStateOf(PostReplyUiState.Ready)
    var replyClickedUiState: ReplyClickedUiState by mutableStateOf(ReplyClickedUiState.NotClicked)
    var whereReplyClickedUiState: WhereReplyClickedUiState by mutableStateOf(
        WhereReplyClickedUiState.HomeScreen
    )
//    val homeResponse: MutableLiveData<ApiResponse<List<RibbitPost>>> by lazy {
//        MutableLiveData<ApiResponse<List<RibbitPost>>>()
//    }

//    init {
//        getRibbitPosts(
////            object : CoroutinesErrorHandler {
////                override fun onError(message: String) {
////                    Log.d("HippoLog, HomeViewModel", "Error! $message")
////                }
////            }
//        )
//    }

    //    fun getRibbitPosts(coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
//        homeResponse, coroutinesErrorHandler
//    ) {
//        Log.d("HippoLog, HomeViewModel", "getRibbitPosts")
//        ribbitRepository.getPosts()
//    }
    fun getRibbitPosts() {  // 모든 Post를 불러오는 메소드
        viewModelScope.launch(Dispatchers.IO) {
            homeUiState = HomeUiState.Loading
            Log.d("HippoLog, HomeViewModel", "getRibbitPosts, $homeUiState")
            homeUiState = try {
                HomeUiState.Success(ribbitRepository.getPosts())
            } catch (e: IOException) {
                Log.d("HippoLog, HomeViewModel", "${e.stackTrace}, ${e.message}")
                HomeUiState.Error(e.message.toString())

            } catch (e: ExceptionInInitializerError) {
                Log.d("HippoLog, HomeViewModel", "${e.stackTrace}, ${e.message}")
                HomeUiState.Error(e.message.toString())
            } catch (e: HttpException) {
                Log.d("HippoLog, HomeViewModel", "${e.stackTrace}, ${e.code()}, $e")
                if (e.code() == 500) {
                    HomeUiState.Error(e.code().toString())
                } else {
                    HomeUiState.Error(e.message.toString())
                }
            }
            Log.d("HippoLog, HomeViewModel", "getRibbitPosts, $homeUiState")
        }
    }

    suspend fun deleteRibbitPost(postId: Int) { // Post를 삭제하는 메소드, 정상적인 페이지 노출을 위해 동기함수로 구성
        Log.d("HippoLog, HomeViewModel", "deleteRibbitPost, $postId")
        try {
            deletePostUiState = DeletePostUiState.Loading
            ribbitRepository.deletePost(postId)
        } catch (e: IOException) {
            Log.d("HippoLog, HomeViewModel", "${e.stackTrace}, ${e.message}")
            DeletePostUiState.Error(e.message.toString())

        } catch (e: ExceptionInInitializerError) {
            Log.d("HippoLog, HomeViewModel", "${e.stackTrace}, ${e.message}")
            DeletePostUiState.Error(e.message.toString())

        } catch (e: HttpException) {
            Log.d("HippoLog, HomeViewModel", "${e.stackTrace}, ${e.code()}, $e")
            if (e.code() == 500) {
                DeletePostUiState.Error(e.code().toString())
            } else {
                DeletePostUiState.Error(e.message.toString())
            }
        }
        Log.d("HippoLog, HomeViewModel", "deleteRibbitPost")
    }

    fun getPostIdPost(postId: Int) {    // PostDetail을 불러오는 함수
        viewModelScope.launch(Dispatchers.IO) {
            postIdUiState = PostIdUiState.Loading
            Log.d("HippoLog, HomeViewModel", "getTwitIdPosts, $postIdUiState")
            postPostIdCount(postId)
            postIdUiState = try {
                PostIdUiState.Success(ribbitRepository.getPostIdPost(postId))
            } catch (e: IOException) {
                Log.d("HippoLog, HomeViewModel", "getPostIdPost: ${e.stackTrace}, ${e.message}")
                PostIdUiState.Error(e.message.toString())

            } catch (e: ExceptionInInitializerError) {
                Log.d("HippoLog, HomeViewModel", "getPostIdPost: ${e.stackTrace}, ${e.message}")
                PostIdUiState.Error(e.message.toString())

            } catch (e: HttpException) {
                Log.d("HippoLog, HomeViewModel", "getPostIdPost: ${e.stackTrace}, ${e.code()}, $e")
                if (e.code() == 500) {
                    PostIdUiState.Error(e.code().toString())
                } else {
                    PostIdUiState.Error(e.message.toString())
                }
            }
            Log.d("HippoLog, HomeViewModel", "getTwitIdPosts, $postIdUiState")
        }
    }

    suspend fun postPostIdCount(postId: Int) {  // Detail을 불러올 때 Count가 정확하지 않은 문제를 해결하기 위해 동기작업으로 실행
        try {
            ribbitRepository.postPostIdCount(postId)
        } catch (e: IOException) {
            Log.d("HippoLog, HomeViewModel", "postPostIdCount: ${e.stackTrace}, ${e.message}")

        } catch (e: ExceptionInInitializerError) {
            Log.d("HippoLog, HomeViewModel", "postPostIdCount: ${e.stackTrace}, ${e.message}")

        } catch (e: HttpException) {
            Log.d("HippoLog, HomeViewModel", "postPostIdCount: ${e.stackTrace}, ${e.code()}, $e")
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
                var mediaMetadataRetriever: MediaMetadataRetriever?
                mediaMetadataRetriever = MediaMetadataRetriever()
                mediaMetadataRetriever.setDataSource(videoUrl, HashMap())
                var retrievalCount = 0
                val maxRetrievalCount = 2 // Adjust this value as needed
                if (retrievalCount >= maxRetrievalCount) {
                    // You've reached the limit, so return null or handle it as needed
                    return@Callable null
                }
                try {
                    Log.d(
                        "HippoLog, HomeScreen, retrieve",
                        "Thread - ${Thread.currentThread().name}"
                    )
                    bitmap = mediaMetadataRetriever.getFrameAtTime(100000)
                } catch (e: SocketTimeoutException) {
                    Log.d(
                        "HippoLog, HomeScreen, retrieve, Exception",
                        "Exception in retrieveThumbnailFromVideo: ${e.message}"
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d(
                        "HippoLog, HomeScreen, retrieve, Exception",
                        "Exception in retrieveThumbnailFromVideo: ${e.message}"
                    )
                } finally {
                    mediaMetadataRetriever.release()
                }
                retrievalCount++
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
            when (whereReplyClickedUiState) {
                is WhereReplyClickedUiState.HomeScreen -> {
                    getRibbitPosts()
                }

                is WhereReplyClickedUiState.TwitIdScreen -> {
                    getPostIdPost((postIdUiState as PostIdUiState.Success).post.id)
                    whereReplyClickedUiState =
                        WhereReplyClickedUiState.HomeScreen  // 기본값으로 HomeScreen으로 한다.
                }
            }
        }
    }

    fun postPostIdLike(postId: Int) {    // 이 통신으로 like, dislike를 다 함.
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

    fun deletePostIdLike(postId: Int) {  // 서버 컨트롤러에 있지만 쓰지 않음.
        viewModelScope.launch(Dispatchers.IO) {
            try {
                ribbitRepository.deletePostIdLike(postId)
            } catch (e: IOException) {
                println(e.stackTrace)
            } catch (e: ExceptionInInitializerError) {
                println(e.stackTrace)
            }
        }
    }

    fun putPostIdRepost(postId: Int) {   // 얘도 이것만으로 repost와 cancel을 다 함.
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

}