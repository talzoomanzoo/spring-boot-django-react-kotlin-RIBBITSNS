package com.hippoddung.ribbit.ui.screens.textfielditems

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.hippoddung.ribbit.R

@Composable
fun InputPWCheckField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var passwordCheckVisible by rememberSaveable { mutableStateOf(false) }
    TextField(
        singleLine = true,
        visualTransformation = if (passwordCheckVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        label = {
            Text(
                text = stringResource(R.string.pW_check)
            )
        },
        value = value,
        onValueChange = onValueChange,
        trailingIcon = {
            val image = if (passwordCheckVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff
            val description = if (passwordCheckVisible) "Hide password" else "Show password"
            IconButton(
                onClick = { passwordCheckVisible = !passwordCheckVisible },
                modifier = modifier
            ) {
                Icon(
                    imageVector = image,
                    contentDescription = description,
                    modifier = modifier
                )
            }
        },
        modifier = modifier
            .padding(bottom = 32.dp)
            .fillMaxWidth(),
    )
}