package com.hippoddung.ribbit.ui.screens.authscreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.network.bodys.requestbody.AuthRequest
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.screens.textfielditems.InputEmailField
import com.hippoddung.ribbit.ui.screens.textfielditems.InputPWField
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var pW by remember { mutableStateOf("") }
    var authRequest = AuthRequest(email = email, password = pW)

    Column(
        modifier = modifier
            .padding(40.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.login),
            style = MaterialTheme.typography.displaySmall,
            modifier = modifier
        )
        Text(
            text = stringResource(R.string.login),
            modifier = modifier
                .padding(bottom = 16.dp)
                .align(alignment = Alignment.Start)
        )
        InputEmailField(
            value = email,
            onValueChange = { email = it },
            modifier = modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        InputPWField(
            value = pW,
            onValueChange = { pW = it },
            modifier = modifier
        )
        Row(modifier = modifier) {
            Button(
                onClick = { /*TODO*/ },
                modifier.padding(14.dp)
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    modifier = modifier
                    )
            }
            Button(
                onClick = {
                    authViewModel.login(authRequest)
                    authViewModel.saveLoginInfo(email = email, pW = pW)
                },
                modifier.padding(14.dp)
            ) {
                Text(
                    text = stringResource(R.string.login),
                    modifier = modifier
                )
            }
        }
        Row(modifier = modifier) {
            Button(
                onClick = {
                    navController.navigate(RibbitScreen.SignUpScreen.name)
                },
                modifier.padding(14.dp)
            ) {
                Text(
                    text = stringResource(R.string.sign_up),
                    modifier = modifier)
            }
        }
        Spacer(modifier = modifier.height(150.dp))
    }
}