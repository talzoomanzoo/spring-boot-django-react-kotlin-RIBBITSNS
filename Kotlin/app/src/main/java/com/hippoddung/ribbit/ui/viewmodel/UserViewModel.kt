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

sealed interface MyProfileUiState {
    data class Exist(val myProfile: User) : MyProfileUiState
    object Lack : MyProfileUiState
    object Loading : MyProfileUiState
    data class Error(val errorCode: String) : MyProfileUiState
}

sealed interface ProfileUiState {
    data class Exist(val user: User) : ProfileUiState
    object Loading : ProfileUiState
    data class Error(val errorCode: String) : ProfileUiState
}

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {
    var myProfileUiState: MyProfileUiState by mutableStateOf(MyProfileUiState.Lack)
        private set
    var myProfile = MutableLiveData<User?>()

    var profileUiState: ProfileUiState by mutableStateOf(ProfileUiState.Loading)
        private set

    fun getMyProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            myProfileUiState = MyProfileUiState.Loading
            Log.d("HippoLog, UserViewModel", "getMyProfile, $myProfileUiState")
            try {
                val responseUser = userRepository.getMyProfile()
                myProfileUiState = MyProfileUiState.Exist(responseUser)
                myProfile.postValue(responseUser)    // user Livedata 에 user값을 넣는다.
            } catch (e: IOException) {
                Log.d("HippoLog, UserViewModel", "${e.stackTrace}, ${e.message}")
                myProfileUiState = MyProfileUiState.Error(e.message.toString())

            } catch (e: ExceptionInInitializerError) {
                Log.d("HippoLog, UserViewModel", "${e.stackTrace}, ${e.message}")
                myProfileUiState = MyProfileUiState.Error(e.message.toString())

            } catch (e: HttpException) {
                Log.d("HippoLog, UserViewModel", "${e.stackTrace}, ${e.code()}, $e")
                myProfileUiState = if (e.code() == 500) {
                    MyProfileUiState.Error(e.code().toString())
                } else {
                    MyProfileUiState.Error(e.message.toString())
                }
            }
            Log.d("HippoLog, UserViewModel", "getMyProfile, $myProfileUiState")
        }
    }

    fun resetMyProfile() {
        myProfile.postValue(null)
        Log.d("HippoLog, UserViewModel", "유저정보 리셋")
    }

    fun getProfile(userId: Int) {   // 원하는 유저의 profile을 가져오는 메소드
        viewModelScope.launch(Dispatchers.IO) {
            profileUiState = ProfileUiState.Loading
            Log.d("HippoLog, UserViewModel", "getUserProfile, $profileUiState")
            try {
                val responseProfile = userRepository.getProfile(userId)
                profileUiState = ProfileUiState.Exist(responseProfile)
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
            Log.d("HippoLog, UserViewModel", "getProfile, $profileUiState")
        }
    }
}