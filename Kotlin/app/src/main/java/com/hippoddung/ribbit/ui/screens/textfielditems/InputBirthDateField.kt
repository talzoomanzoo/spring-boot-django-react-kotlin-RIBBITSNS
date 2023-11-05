package com.hippoddung.ribbit.ui.screens.textfielditems

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.hippoddung.ribbit.R

@Composable
fun InputBirthDateField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = {
            Text(
                text = stringResource(R.string.birth_date)
            )
        },
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
    )
}