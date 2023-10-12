package com.hippoddung.ribbit.ui.screens.authscreens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.hippoddung.ribbit.ui.viewmodel.TokenViewModel

@Composable
fun LogoutScreen(
    tokenViewModel: TokenViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
){
    val logout = tokenViewModel.deleteToken()
}