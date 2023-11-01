package com.hippoddung.ribbit.ui.screens.statescreens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.hippoddung.ribbit.R

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Log.d("HippoLog, LoadingScreen", "로딩스크린")
    Image(
        modifier = modifier.fillMaxSize(),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(id = R.string.loading)
    )
}