package com.example.quazz.app.presentation.profile.updateProfile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quazz.R
import com.example.quazz.app.presentation.auth.login.TextError
import com.example.quazz.core.components.QuazzTopAppBar
import com.example.quazz.core.components.outlinedTextField.OutlinedTextFieldEmail
import com.example.quazz.core.components.outlinedTextField.OutlinedTextFieldPassword
import com.example.quazz.ui.theme.AppTheme
import com.example.quazz.ui.theme.QuazzTheme

@Composable
fun UpdateProfileScreen(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    viewModel: UpdateProfileViewModel = hiltViewModel(),
    model: UpdateProfile,
) {
    val state by viewModel.uiState.collectAsState()
    UpdateProfileContent(
        onBackClick = onBackClick,
        onSaveClick = onSaveClick,
        model = model,
        onEvent = viewModel::onEvent,
        state = state)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProfileContent(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    model: UpdateProfile,
    onEvent: (UpdateProfileEvent) -> Unit,
    state: UpdateProfileState,
) {
    val title = when(model) {
        is UpdateProfile.UpdateEmail -> stringResource(id = R.string.edit_email)
        is UpdateProfile.UpdatePassword -> stringResource(id = R.string.edit_password)
    }
    AppTheme {
        Scaffold(
            topBar = {
                QuazzTopAppBar(
                    title = title,
                    canNavigateBack = true,
                    navigateUp = onBackClick,
                ) {
                    ActionButton {
                        onEvent(
                            UpdateProfileEvent.Save(model, onSaveClick)
                        )
                    }
                }
            },
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = QuazzTheme.dimension.paddingM),
                verticalArrangement = Arrangement.spacedBy(QuazzTheme.dimension.paddingM)
            ) {
                Spacer(modifier = Modifier.padding(QuazzTheme.dimension.paddingM))
                TextError(state.errorMessage.asString())
                when(model) {
                    is UpdateProfile.UpdateEmail -> {
                        OutlinedTextFieldEmail(
                            onFocus = { onEvent(UpdateProfileEvent.ClearErrorMessage) },
                            value = state.email,
                            onValueChange = { onEvent(UpdateProfileEvent.UpdateEmail(it)) },
                            onIconButtonClick = { onEvent(UpdateProfileEvent.UpdateEmail("")) })
                        OutlinedTextFieldPassword(
                            onFocus = { onEvent(UpdateProfileEvent.ClearErrorMessage) },
                            value = state.currentPassword,
                            onValueChange = { onEvent(UpdateProfileEvent.UpdateCurrentPassword(it)) },
                            label = stringResource(id = R.string.password),
                            placeholder = stringResource(id = R.string.password),
                        )
                    }
                    is UpdateProfile.UpdatePassword -> {
                        OutlinedTextFieldPassword(
                            onFocus = { onEvent(UpdateProfileEvent.ClearErrorMessage) },
                            value = state.currentPassword,
                            onValueChange = { onEvent(UpdateProfileEvent.UpdateCurrentPassword(it)) },
                            label = stringResource(id = R.string.current_password),
                            placeholder = stringResource(id = R.string.placeholder_current_password),
                        )
                        OutlinedTextFieldPassword(
                            onFocus = { onEvent(UpdateProfileEvent.ClearErrorMessage) },
                            value = state.newPassword,
                            onValueChange = { onEvent(UpdateProfileEvent.UpdateNewPassword(it)) },
                            label = stringResource(id = R.string.new_password),
                            placeholder = stringResource(id = R.string.placeholder_new_password),
                        )
                        OutlinedTextFieldPassword(
                            onFocus = { onEvent(UpdateProfileEvent.ClearErrorMessage) },
                            value = state.confirmPassword,
                            onValueChange = { onEvent(UpdateProfileEvent.UpdateConfirmPassword(it)) },
                            label = stringResource(id = R.string.confirm_password),
                            placeholder = stringResource(id = R.string.placeholder_confirm_password),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.ActionButton(onClick: () -> Unit) {
    IconButton(
        colors = IconButtonColors(
            disabledContainerColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.inversePrimary,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.background),
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier,
            painter = painterResource(id = R.drawable.save_24),
            contentDescription = stringResource(id = R.string.save)
        )
    }
}

@PreviewLightDark
@Composable
fun UpdateProfileContentPreview() {
    AppTheme {
        UpdateProfileContent(
            onBackClick = {},
            onSaveClick = {},
            model = UpdateProfile.UpdateEmail,
            onEvent = {},
            state = UpdateProfileState()
        )
    }
}