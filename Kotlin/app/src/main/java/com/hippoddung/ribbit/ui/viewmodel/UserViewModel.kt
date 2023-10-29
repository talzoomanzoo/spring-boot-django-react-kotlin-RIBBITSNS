package com.hippoddung.ribbit.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.hippoddung.ribbit.data.network.UserRepository
import com.hippoddung.ribbit.network.bodys.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed interface UserUiState {
    data class Exist(val user: User) : UserUiState
    object Lack : UserUiState
    data class Error(val errorCode: String) : UserUiState
}

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {
    var userUiState: UserUiState by mutableStateOf(UserUiState.Lack)
        private set

    fun getUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            userUiState = UserUiState.Lack
            Log.d("HippoLog, UserViewModel", "getUserProfile, $userUiState")
            userUiState = try {
                UserUiState.Exist(userRepository.getUserProfile())
            } catch (e: IOException) {
                Log.d("HippoLog, UserViewModel", "${e.stackTrace}, ${e.message}")
                UserUiState.Error(e.message.toString())

            } catch (e: ExceptionInInitializerError) {
                Log.d("HippoLog, UserViewModel", "${e.stackTrace}, ${e.message}")
                UserUiState.Error(e.message.toString())

            } catch (e: HttpException) {
                Log.d("HippoLog, UserViewModel", "${e.stackTrace}, ${e.code()}, $e")
                if (e.code() == 500) {
                    UserUiState.Error(e.code().toString())
                } else {
                    UserUiState.Error(e.message.toString())
                }
            }
            Log.d("HippoLog, UserViewModel", "getUserProfile, $userUiState")
        }
    }
}