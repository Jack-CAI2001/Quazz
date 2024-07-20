package com.example.quazz.app.presentation.profile

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.quazz.R
import com.example.quazz.app.model.User
import com.example.quazz.app.presentation.profile.updateProfile.UpdateProfile
import com.example.quazz.app.presentation.profile.updateProfile.UpdateProfileActivity
import com.example.quazz.core.components.QuazzSnackbar
import com.example.quazz.core.components.ScaffoldBottomApp
import com.example.quazz.core.components.outlinedTextField.LoadingScreen
import com.example.quazz.core.components.outlinedTextField.QuazzOutlinedTextField
import com.example.quazz.navigation.Route
import com.example.quazz.ui.theme.AppTheme
import com.example.quazz.ui.theme.QuazzTheme

@Composable
fun ProfileScreen(
    paddingValues: PaddingValues,
    restartApp: ((String) -> Unit),
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    if (state.isUserConnected.not()) { restartApp(Route.SplashRoute.route) }
    val errorMessage = state.errorMessage.asString()
    if (state.isLoading) {
        LoadingScreen()
    } else {
    ProfileContent(modifier = Modifier.padding(paddingValues), state, viewModel::onEvent)
    }
    QuazzSnackbar(paddingValues = paddingValues, snackbarMessage = errorMessage, onDismissed = { viewModel.onEvent(ProfileEvent.ClearError) })
}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    state: ProfileState,
    onEvent: (ProfileEvent) -> Unit
) {
    var updateProfileResult by remember {
        mutableStateOf("")
    }
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            it.data?.getStringExtra(UpdateProfileActivity.TYPE)?.let { updateProfile ->
                updateProfileResult = updateProfile
            }
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = QuazzTheme.dimension.paddingS),
        horizontalAlignment = Alignment.End
    ) {
        val context = LocalContext.current
        Box(
            modifier = Modifier.fillMaxHeight(0.3f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_account_circle_24),
                modifier = Modifier
                    .fillMaxSize(),
                contentDescription = "test")
        }

        EditPseudoDialog(
            pseudo = state.user.pseudo,
            onDismissEvent = { onEvent(ProfileEvent.UpdatePseudo("")) },
            onConfirmClick = { onEvent(ProfileEvent.SavePseudo) },
            text = {
                QuazzOutlinedTextField(
                    placeholder = {
                        Text(text = stringResource(id = R.string.placeholder_pseudo))
                    },
                    value = state.pseudoText,
                    onValueChange = { onEvent(ProfileEvent.UpdatePseudo(it)) }) }
        )
        HorizontalDivider()
        TextIconRow(
            text = state.user.email,
            onClick = {
                val intent = UpdateProfileActivity.newIntent(context as Activity, data = UpdateProfile.UpdateEmail)
                launcher.launch(intent)
            }
        )
        HorizontalDivider()
        TextIconRow(
            text = stringResource(id = R.string.edit_password),
            onClick = {
                val intent = UpdateProfileActivity.newIntent(context as Activity, data = UpdateProfile.UpdatePassword)
                launcher.launch(intent)
            }
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.padding(QuazzTheme.dimension.paddingL))
        LogOutDialog(onConfirmClick = { onEvent(ProfileEvent.LogOut) })
    }
    QuazzSnackbar(paddingValues = PaddingValues(0.dp), snackbarMessage = updateProfileResult, onDismissed = { updateProfileResult = "" })
}

@Composable
fun EditPseudoDialog(
    modifier: Modifier = Modifier,
    pseudo: String,
    text: @Composable () -> Unit,
    onDismissEvent: () -> Unit,
    onConfirmClick: () -> Unit) {
    var openAlertDialog by remember { mutableStateOf(false) }
    when {
        openAlertDialog -> {
            AlertDialog(
                onDismissRequest = {
                    onDismissEvent()
                    openAlertDialog = false },
                confirmButton = {
                    Button(onClick = {
                        onConfirmClick()
                        openAlertDialog = false
                    }) {
                        Text(text = stringResource(id = R.string.save))
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        onDismissEvent()
                        openAlertDialog = false }) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                },
                title = { Text(text = stringResource(id = R.string.edit_pseudo)) },
                text = text
            )
        }
    }
    TextIconRow(
        text = pseudo,
        onClick = {
            openAlertDialog = true
        }
    )
}

@Composable
fun LogOutDialog(
    modifier: Modifier = Modifier,
    onConfirmClick: () -> Unit) {
    var openAlertDialog by remember { mutableStateOf(false) }
    when {
        openAlertDialog -> {
            AlertDialog(
                onDismissRequest = { openAlertDialog = false },
                confirmButton = {
                    Button(onClick = {
                        onConfirmClick()
                        openAlertDialog = false}) {
                        Text(text = stringResource(id = R.string.log_out_text))
                    }
                },
                dismissButton = {
                    Button(onClick = { openAlertDialog = false }) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                },
                title = { Text(text = stringResource(id = R.string.log_out_text)) },
                text = { Text(text = stringResource(id = R.string.log_out_text_description)) }
            )
        }
    }
    TextButton(onClick = { openAlertDialog = true }) {
        Text(text = "Logout")
    }
}

@Composable
fun TextIconRow(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(QuazzTheme.dimension.paddingM),
            text = text)
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = stringResource(id = R.string.icon_arrow_right)
        )
    }
}

@Composable
@PreviewLightDark
fun ProfileContentPreview() {
    AppTheme {
        ScaffoldBottomApp(navController = rememberNavController()) {
            ProfileContent(
                modifier = Modifier.padding(it),
                state = ProfileState(
                    user = User("uid", "Email", "pseudo")
                ), onEvent = {})
        }
    }
}