package com.pockettrack.ui.componentes

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

// Campo de texto com rótulo, teclado certo e mensagem de erro.
// O erro só aparece depois que a pessoa começou a digitar (`mostrarErro`), para não
// receber um formulário em vermelho logo ao abrir.
@Composable
fun CampoTexto(
    valor: String,
    onValorChange: (String) -> Unit,
    rotulo: String,
    modifier: Modifier = Modifier,
    erro: String? = null,
    mostrarErro: Boolean = valor.isNotEmpty(),
    teclado: KeyboardType = KeyboardType.Text,
    acaoIme: ImeAction = ImeAction.Next,
) {
    val erroVisivel = erro?.takeIf { mostrarErro }
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(rotulo) },
        isError = erroVisivel != null,
        supportingText = erroVisivel?.let { { Text(it) } },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = teclado, imeAction = acaoIme),
        modifier =
            modifier
                .fillMaxWidth()
                .semantics { if (erroVisivel != null) error(erroVisivel) },
    )
}

// Campo de senha com botão para mostrar ou ocultar o texto.
@Composable
fun CampoSenha(
    valor: String,
    onValorChange: (String) -> Unit,
    rotulo: String,
    visivel: Boolean,
    onAlternarVisibilidade: () -> Unit,
    modifier: Modifier = Modifier,
    erro: String? = null,
    acaoIme: ImeAction = ImeAction.Next,
) {
    val erroVisivel = erro?.takeIf { valor.isNotEmpty() }
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(rotulo) },
        isError = erroVisivel != null,
        supportingText = erroVisivel?.let { { Text(it) } },
        singleLine = true,
        visualTransformation = if (visivel) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = acaoIme),
        trailingIcon = {
            IconButton(onClick = onAlternarVisibilidade) {
                Icon(
                    Icons.Filled.Lock,
                    contentDescription = if (visivel) "Ocultar senha" else "Mostrar senha",
                )
            }
        },
        modifier =
            modifier
                .fillMaxWidth()
                .semantics { if (erroVisivel != null) error(erroVisivel) },
    )
}

// Campo somente leitura que mostra a data escolhida e abre o seletor de data.
@Composable
fun CampoData(
    dataFormatada: String,
    rotulo: String,
    onEscolher: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = dataFormatada,
        onValueChange = {},
        readOnly = true,
        label = { Text(rotulo) },
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = onEscolher) {
                Icon(Icons.Filled.DateRange, contentDescription = "Escolher data")
            }
        },
        modifier = modifier.fillMaxWidth(),
    )
}
