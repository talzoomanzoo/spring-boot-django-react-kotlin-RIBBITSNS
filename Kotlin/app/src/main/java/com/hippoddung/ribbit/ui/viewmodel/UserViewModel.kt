package com.hippoddung.ribbit.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hippoddung.ribbit.data.network.UserRepository
import com.hippoddung.ribbit.network.bodys.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed interface UserUiState {
    data class Exist(val user: User) : UserUiState
    object Lack : UserUiState
    object Loading : UserUiState
    data class Error(val errorCode: String) : UserUiState
}

sealed interface ProfileUiState {
    data class Exist(val user: User) : ProfileUiState
    object Lack : ProfileUiState
    object Loading : ProfileUiState
    data class Error(val errorCode: String) : ProfileUiState
}

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {
    var userUiState: UserUiState by mutableStateOf(UserUiState.Lack)
        private set

    var user = MutableLiveData<User?>()

    var profileUiState: ProfileUiState by mutableStateOf(ProfileUiState.Lack)
        private set

    fun getUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            userUiState = UserUiState.Loading
            Log.d("HippoLog, UserViewModel", "getUserProfile, $userUiState")
            try {
                val responseUser = userRepository.getUserProfile()
                userUiState = UserUiState.Exist(responseUser)
                user.postValue(responseUser)    // user Livedata 에 user값을 넣는다.
                Log.d("HippoLog, TokenViewModel", "init DataStore 에서 토큰 보냄 ${user.value}")
            } catch (e: IOException) {
                Log.d("HippoLog, UserViewModel", "${e.stackTrace}, ${e.message}")
                userUiState = UserUiState.Error(e.message.toString())

            } catch (e: ExceptionInInitializerError) {
                Log.d("HippoLog, UserViewModel", "${e.stackTrace}, ${e.message}")
                userUiState = UserUiState.Error(e.message.toString())

            } catch (e: HttpException) {
                Log.d("HippoLog, UserViewModel", "${e.stackTrace}, ${e.code()}, $e")
                if (e.code() == 500) {
                    userUiState = UserUiState.Error(e.code().toString())
                } else {
                    userUiState = UserUiState.Error(e.message.toString())
                }
            }
            Log.d("HippoLog, UserViewModel", "getUserProfile, $userUiState")
        }
    }

    fun resetUser(){
        user.postValue(null)
        Log.d("HippoLog, UserViewModel", "유저정보 리셋")
    }

    fun getProfile(userId: Int) {   // 원하는 유저의 profile을 가져오는 메소드
        viewModelScope.launch(Dispatchers.IO) {
            profileUiState = ProfileUiState.Loading
            Log.d("HippoLog, UserViewModel", "getUserProfile, $profileUiState")
            try {
                val responseProfile = userRepository.getProfile(userId)
                profileUiState = ProfileUiState.Exist(responseProfile)
                user.postValue(responseProfile)    // user Livedata 에 user값을 넣는다.
                Log.d("HippoLog, TokenViewModel", "init DataStore 에서 토큰 보냄 ${user.value}")
            } catch (e: IOException) {
                Log.d("HippoLog, UserViewModel", "${e.stackTrace}, ${e.message}")
                profileUiState = ProfileUiState.Error(e.message.toString())

            } catch (e: ExceptionInInitializerError) {
                Log.d("HippoLog, UserViewModel", "${e.stackTrace}, ${e.message}")
                profileUiState = ProfileUiState.Error(e.message.toString())

            } catch (e: HttpException) {
                Log.d("HippoLog, UserViewModel", "${e.stackTrace}, ${e.code()}, $e")
                if (e.code() == 500) {
                    profileUiState = ProfileUiState.Error(e.code().toString())
                } else {
                    profileUiState = ProfileUiState.Error(e.message.toString())
                }
            }
            Log.d("HippoLog, UserViewModel", "getUserProfile, $profileUiState")
        }
    }
}