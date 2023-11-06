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
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.network.bodys.Verification
import com.hippoddung.ribbit.network.bodys.requestbody.SignUpRequest
import com.hippoddung.ribbit.ui.screens.textfielditems.InputBirthDateField
import com.hippoddung.ribbit.ui.screens.textfielditems.InputEmailField
import com.hippoddung.ribbit.ui.screens.textfielditems.InputFullNameField
import com.hippoddung.ribbit.ui.screens.textfielditems.InputPWCheckField
import com.hippoddung.ribbit.ui.screens.textfielditems.InputPWField
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.CoroutinesErrorHandler

@Composable
fun SignUpScreen(
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var pW by remember { mutableStateOf("") }
    var pWCheck by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var user = SignUpRequest(
        email = email,
        password = pW,
        fullName = fullName,
        birthDate = birthDate,
        verification = Verification()
    )

    Column(
        modifier = modifier
            .padding(40.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.sign_up),
            style = MaterialTheme.typography.displaySmall
        )
        Text(
            text = stringResource(R.string.sign_up),
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
        )
        InputPWCheckField(
            value = pWCheck,
            onValueChange = { pWCheck = it },
            modifier = Modifier
        )
        InputFullNameField(
            value = fullName,
            onValueChange = { fullName = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        InputBirthDateField(
            value = birthDate,
            onValueChange = { birthDate = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        Row(modifier) {
            Button(
                onClick = { /*TODO*/ },
                modifier.padding(14.dp)
            ) {
                Text(text = stringResource(R.string.cancel))
            }
            Button(
                onClick = {
                    authViewModel.signUp(
                        user,
                        object : CoroutinesErrorHandler { override fun onError(message: String) { "Error! $message" } }
                    )
                },
                modifier.padding(14.dp)
            ) {
                Text(text = stringResource(R.string.sign_up))
            }
        }
        Spacer(modifier = Modifier.height(150.dp))
    }
}