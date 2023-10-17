package com.hippoddung.ribbit.ui.screens

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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults.shape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.network.bodys.TwitCreateRequest
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.viewmodel.TwitsCreateViewModel

@Composable
fun TwitCreateScreen(
    navController: NavHostController,
    twitsCreateViewModel: TwitsCreateViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    var image by remember { mutableStateOf("") }
    var video by remember { mutableStateOf("") }
    var twitCreateRequest = TwitCreateRequest(content = inputText, image = image, video = video)

    Column(
        modifier = modifier
            .padding(40.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.twit_create),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(alignment = Alignment.Start)
        )
        InputTextField(
            value = inputText,
            onValueChange = { inputText = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        Row(modifier) {
            Button(
                onClick = { navController.navigate(RibbitScreen.PickImageScreen.name) },
                modifier.padding(14.dp)
            ) {
                Icon(Icons.Filled.Image, "Pick Image button.")
            }
            Button(
                onClick = {
                    twitsCreateViewModel.twitCreate(
                        twitCreateRequest
                    )
                    navController.navigate(RibbitScreen.PickImageScreen.name)
                },
                modifier.padding(14.dp)
            ) {
                Text(text = stringResource(R.string.text))
            }
        }
        Row(modifier) {
            Button(
                onClick = { navController.navigate(RibbitScreen.HomeScreen.name) },
                modifier.padding(14.dp)
            ) {
                Text(text = stringResource(R.string.Cancel))
            }
            Button(
                onClick = {
                    twitsCreateViewModel.twitCreate(
                        twitCreateRequest
                    )
                    navController.navigate(RibbitScreen.HomeScreen.name)
                },
                modifier.padding(14.dp)
            ) {
                Text(text = stringResource(R.string.twit_create))
            }
        }
        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Composable
fun InputTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        singleLine = false,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
        label = { Text(stringResource(R.string.twit_create)) },
        value = value,
        onValueChange = onValueChange,
        minLines = 5,
        modifier = modifier,
    )
}