package com.hippoddung.ribbit.ui.screens.profilescreens

import android.graphics.Bitmap
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreenTopBar(
    userViewModel: UserViewModel,
    navController: NavHostController,
    inputFullName: String,
    inputBio: String,
    inputWebsite: String,
    inputEducation: String,
    inputBirthDate: String,
    profileImage: Bitmap?,
    profileBackgroundImage: Bitmap?,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = {
            Text(
                text = stringResource(R.string.edit_profile),
                color = Color(0xFF006400),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = modifier
            )
        },
        navigationIcon = {
            Button(
                onClick = { navController.navigate(RibbitScreen.ProfileScreen.name) },
                modifier = modifier.padding(14.dp)
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    modifier = modifier
                )
            }
        },
        actions = {
            Button(
                onClick = {
                    userViewModel.editProfile(
                        inputFullName = inputFullName,
                        inputBio = inputBio,
                        inputWebsite = inputWebsite,
                        inputEducation = inputEducation,
                        inputBirthDate = inputBirthDate,
                        profileImage = profileImage,
                        profileBackgroundImage = profileBackgroundImage
                    )
                },
                modifier = modifier.padding(14.dp)
            ) {
                Text(
                    text = stringResource(R.string.edit),
                    modifier = modifier
                )
            }
        }
    )
}