package com.hippoddung.ribbit.ui.viewmodel

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Build
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.hippoddung.ribbit.data.network.RibbitRepository
import com.hippoddung.ribbit.network.bodys.RibbitPost
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

sealed interface DeletePostUiState {
    object Ready : DeletePostUiState
    object Success : DeletePostUiState
    data class Error(val errorCode: String) : DeletePostUiState
    object Loading : DeletePostUiState
}

sealed interface HomeUiState {
    data class Success(val posts: List<RibbitPost>) : HomeUiState
    data class Error(val errorCode: String) : HomeUiState
    object Loading : HomeUiState
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val ribbitRepository: RibbitRepository
) : BaseViewModel() {
    var homeUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
    var deletePostUiState: DeletePostUiState by mutableStateOf(DeletePostUiState.Ready)
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
    fun getRibbitPosts() {
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

    fun deleteRibbitPost(postId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
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
    }

    //suspend fun retrieveThumbnailFromVideo(videoUrl: String?): Bitmap? {
    fun retrieveThumbnailFromVideo(videoUrl: String?): Future<Bitmap> {
        val executor = Executors.newFixedThreadPool(2)
        val future: Future<Bitmap> = executor.submit(Callable<Bitmap> {
            var retrievalCount = 0
            val maxRetrievalCount = 2 // Adjust this value as needed
            if (retrievalCount >= maxRetrievalCount) {
                // You've reached the limit, so return null or handle it as needed
                return@Callable null
            }
            var bitmap: Bitmap? = null
            var mediaMetadataRetriever: MediaMetadataRetriever? = null
            mediaMetadataRetriever = MediaMetadataRetriever()
            if (Build.VERSION.SDK_INT >= 14) {
                mediaMetadataRetriever.setDataSource(videoUrl, HashMap())
            } else {
                mediaMetadataRetriever.setDataSource(videoUrl)
            }
            try {
                Log.d("HippoLog, HomeScreen, retrieve", "Thread - ${Thread.currentThread().name}")
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
//    return bitmap
            return@Callable bitmap
        }
        )
        return future
    }
}