package com.hippoddung.ribbit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.Observer
import com.hippoddung.ribbit.network.ApiResponse
import com.hippoddung.ribbit.network.bodys.requestbody.AuthRequest
import com.hippoddung.ribbit.network.bodys.responsebody.AuthResponse
import com.hippoddung.ribbit.ui.RibbitApp
import com.hippoddung.ribbit.ui.theme.RibbitTheme
import com.hippoddung.ribbit.ui.viewmodel.AuthUiState
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.CoroutinesErrorHandler
import com.hippoddung.ribbit.ui.viewmodel.EmailUiState
import com.hippoddung.ribbit.ui.viewmodel.HomeUiState
import com.hippoddung.ribbit.ui.viewmodel.HomeViewModel
import com.hippoddung.ribbit.ui.viewmodel.PWUiState
import com.hippoddung.ribbit.ui.viewmodel.TokenUiState
import com.hippoddung.ribbit.ui.viewmodel.TokenViewModel
import com.hippoddung.ribbit.ui.viewmodel.TwitsCreateViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor() : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val tokenViewModel: TokenViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val twitsCreateViewModel: TwitsCreateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            RibbitTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RibbitApp(homeViewModel, authViewModel, tokenViewModel, twitsCreateViewModel)
                }
            }
        }
        val tokenObserver = Observer<String?> { token ->
            when (token) {
                null -> {
                    tokenViewModel.tokenUiState = TokenUiState.Lack
                    authViewModel.authUiState = AuthUiState.Logout
                    if((authViewModel.emailUiState != EmailUiState.Lack) and (authViewModel.pWUiState != PWUiState.Lack)){
                        val email = (authViewModel.emailUiState as EmailUiState.Exist).email
                        val pW = (authViewModel.pWUiState as PWUiState.Exist).pW
                        authViewModel.login(AuthRequest(email, pW),
                            object : CoroutinesErrorHandler { override fun onError(message: String) { "Error! $message" } })
                    }else{}
                }
                else -> {
                    if(homeViewModel.homeUiState == HomeUiState.Error("500")){
                        if((authViewModel.emailUiState != EmailUiState.Lack) and (authViewModel.pWUiState != PWUiState.Lack)){
                            val email = (authViewModel.emailUiState as EmailUiState.Exist).email
                            val pW = (authViewModel.pWUiState as PWUiState.Exist).pW
                            authViewModel.login(AuthRequest(email, pW),
                                object : CoroutinesErrorHandler { override fun onError(message: String) { "Error! $message" } })
                        }else{}
                    }else{
                        authViewModel.authUiState = AuthUiState.Login
                        homeViewModel.getRibbitPosts(
//                        object : CoroutinesErrorHandler {
//                            override fun onError(message: String) {
//                            }
//                        }
                        )
                    }
                }
            }
        }
        tokenViewModel.token.observe(this, tokenObserver)

        val authObserver = Observer<ApiResponse<AuthResponse>> { response ->
            // Update the UI, in this case, a TextView.
            when (response) {
                is ApiResponse.Failure -> {}
                is ApiResponse.Loading -> {}
                is ApiResponse.Success -> {
                    tokenViewModel.saveToken(response.data.jwt)
                }
            }
        }
        authViewModel.authResponse.observe(this, authObserver)

//        val homeObserver = Observer<ApiResponse<List<RibbitPost>>> { response ->
//            // Update the UI, in this case, a TextView.
//            when (response) {
//                is ApiResponse.Failure -> {
//                    if (response.code == 500) {
//                        tokenViewModel.deleteToken()
//                    }
//                    homeViewModel.homeUiState = HomeUiState.Error
//                }
//                is ApiResponse.Loading -> {
//                    homeViewModel.homeUiState = HomeUiState.Loading
//                }
//                is ApiResponse.Success -> {
//                    homeViewModel.homeUiState = HomeUiState.Success(response.data)
//                    Log.d("HippoLog, MainActivity","${homeViewModel.homeUiState}")
//                }
//            }
//        }
//        homeViewModel.homeResponse.observe(this, homeObserver)
    }
}