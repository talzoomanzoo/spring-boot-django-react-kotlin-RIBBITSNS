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
import com.hippoddung.ribbit.network.bodys.LoginResponse
import com.hippoddung.ribbit.ui.RibbitApp
import com.hippoddung.ribbit.ui.theme.RibbitTheme
import com.hippoddung.ribbit.ui.viewmodel.AuthUiState
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.TokenViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor(): ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val tokenViewModel: TokenViewModel by viewModels()

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

                    RibbitApp()
                }
            }
        }
        val tokenObserver = Observer<String?>{token ->
            when(token){
                null -> {
                    authViewModel.authUiState = AuthUiState.Logout
                }
                else ->{
                    authViewModel.authUiState = AuthUiState.Login
                }
            }
        }
        tokenViewModel.token.observe(this, tokenObserver)

        val authObserver = Observer<ApiResponse<LoginResponse>> { response ->
            // Update the UI, in this case, a TextView.
            when(response){
                is ApiResponse.Failure -> {}
                is ApiResponse.Loading -> {}
                is ApiResponse.Success -> {
                    tokenViewModel.saveToken(response.data.jwt)
                }
            }
        }
        authViewModel.loginResponse.observe(this, authObserver)
    }
}