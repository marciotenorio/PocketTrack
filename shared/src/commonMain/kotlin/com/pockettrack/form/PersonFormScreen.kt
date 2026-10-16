package com.pockettrack.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.stringResource
import pockettrack.shared.generated.resources.Res
import pockettrack.shared.generated.resources.person_form_age_label
import pockettrack.shared.generated.resources.person_form_error_age_invalid
import pockettrack.shared.generated.resources.person_form_error_age_too_high
import pockettrack.shared.generated.resources.person_form_error_name_too_short
import pockettrack.shared.generated.resources.person_form_name_label
import pockettrack.shared.generated.resources.person_form_submit
import pockettrack.shared.generated.resources.person_form_success

@Composable
@Suppress("FunctionName", "ktlint:standard:function-naming")
fun PersonFormScreen(
    modifier: Modifier = Modifier,
    viewModel: PersonFormViewModel = viewModel { PersonFormViewModel() },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PersonFormContent(
        uiState = uiState,
        onNameChange = viewModel::onNameChange,
        onAgeChange = viewModel::onAgeChange,
        onSubmit = viewModel::onSubmit,
        modifier = modifier,
    )
}

@Composable
@Suppress("FunctionName", "ktlint:standard:function-naming")
fun PersonFormContent(
    uiState: PersonFormUiState,
    onNameChange: (String) -> Unit,
    onAgeChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .safeContentPadding()
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        OutlinedTextField(
            value = uiState.name,
            onValueChange = onNameChange,
            label = { Text(stringResource(Res.string.person_form_name_label)) },
            isError = uiState.nameError != null,
            supportingText = uiState.nameError?.let { { Text(it.message()) } },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = uiState.age,
            onValueChange = onAgeChange,
            label = { Text(stringResource(Res.string.person_form_age_label)) },
            isError = uiState.ageError != null,
            supportingText = uiState.ageError?.let { { Text(it.message()) } },
            singleLine = true,
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
            keyboardActions = KeyboardActions(onDone = { onSubmit() }),
            modifier = Modifier.fillMaxWidth(),
        )
        Button(
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(Res.string.person_form_submit))
        }
        if (uiState.isSubmitted) {
            Text(
                text = stringResource(Res.string.person_form_success, uiState.name.trim(), uiState.age.trim().toInt()),
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun NameError.message(): String =
    when (this) {
        NameError.TooShort ->
            stringResource(Res.string.person_form_error_name_too_short, PersonFormValidator.MIN_NAME_LENGTH)
    }

@Composable
private fun AgeError.message(): String =
    when (this) {
        AgeError.Invalid -> stringResource(Res.string.person_form_error_age_invalid)
        AgeError.TooHigh ->
            stringResource(Res.string.person_form_error_age_too_high, PersonFormValidator.MAX_AGE_EXCLUSIVE)
    }

@Composable
@Preview
@Suppress("FunctionName", "ktlint:standard:function-naming")
private fun PersonFormContentPreview() {
    MaterialTheme {
        PersonFormContent(
            uiState =
                PersonFormUiState(
                    name = "Al",
                    age = "120",
                    nameError = NameError.TooShort,
                    ageError = AgeError.TooHigh,
                    submitAttempted = true,
                ),
            onNameChange = {},
            onAgeChange = {},
            onSubmit = {},
        )
    }
}
