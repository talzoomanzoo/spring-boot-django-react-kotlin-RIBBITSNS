package com.hippoddung.ribbit.ui.screens.authscreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.network.bodys.Auth
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.CoroutinesErrorHandler

@Composable
fun LoginScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var pW by remember { mutableStateOf("") }
    var auth = Auth(email = email, password = pW)

    Column(
        modifier = modifier
            .padding(40.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.Login),
            style = MaterialTheme.typography.displaySmall
        )
        Text(
            text = stringResource(R.string.Login),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(alignment = Alignment.Start)
        )
        InputEmailField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        InputPWField(
            value = pW,
            onValueChange = { pW = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        Row(modifier) {
            Button(
                onClick = { /*TODO*/ },
                modifier.padding(14.dp)
            ) {
                Text(text = stringResource(R.string.Cancel))
            }
            Button(
                onClick = {
                    authViewModel.login(
                        auth,
                        object : CoroutinesErrorHandler { override fun onError(message: String) { "Error! $message" } }
                    )
                },
                modifier.padding(14.dp)
            ) {
                Text(text = stringResource(R.string.Login))
            }
        }
        Row(modifier) {
            Button(
                onClick = {
                    navController.navigate(RibbitScreen.SignUpScreen.name)
                },
                modifier.padding(14.dp)
            ) {
                Text(text = stringResource(R.string.Sign_up))
            }
        }
        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Composable
fun InputEmailField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        label = { Text(stringResource(R.string.Email)) },
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
    )
}

@Composable
fun InputPWField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    TextField(
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        label = { Text(stringResource(R.string.PW)) },
        value = value,
        onValueChange = onValueChange,
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            // Please provide localized description for accessibility services
            val description = if (passwordVisible) "Hide password" else "Show password"

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, description)
            }
        },
        modifier = modifier,
    )
}