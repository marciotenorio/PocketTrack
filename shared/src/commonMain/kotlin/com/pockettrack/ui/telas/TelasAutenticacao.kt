package com.pockettrack.ui.telas

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.pockettrack.modelo.FormularioCadastro
import com.pockettrack.modelo.FormularioEntrada
import com.pockettrack.modelo.validarCadastro
import com.pockettrack.modelo.validarEntrada
import com.pockettrack.ui.componentes.BarraSuperior
import com.pockettrack.ui.componentes.CampoSenha
import com.pockettrack.ui.componentes.CampoTexto
import com.pockettrack.ui.componentes.ColunaFormulario
import com.pockettrack.ui.tema.TemaPocketTrack

// Entrada no app. Até a Sprint 2 (Firebase Authentication) qualquer e-mail válido entra.
// A validade vem de uma função pura sobre o formulário, sem estado próprio na tela.
@Composable
fun TelaEntrar(
    formulario: FormularioEntrada,
    senhaVisivel: Boolean,
    onAlterar: (FormularioEntrada) -> Unit,
    onAlternarSenha: () -> Unit,
    onEntrar: () -> Unit,
    onCriarConta: () -> Unit,
) {
    val erros = validarEntrada(formulario)
    Scaffold { preenchimento ->
        ColunaFormulario(preenchimento) {
            Text(
                "PocketTrack",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.semantics { heading() },
            )
            Text(
                "Suas receitas e despesas no bolso.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            CampoTexto(
                valor = formulario.email,
                onValorChange = { onAlterar(formulario.copy(email = it)) },
                rotulo = "E-mail",
                erro = erros.email,
                teclado = KeyboardType.Email,
            )
            CampoSenha(
                valor = formulario.senha,
                onValorChange = { onAlterar(formulario.copy(senha = it)) },
                rotulo = "Senha",
                visivel = senhaVisivel,
                onAlternarVisibilidade = onAlternarSenha,
                erro = erros.senha,
                acaoIme = ImeAction.Done,
            )
            Button(onClick = onEntrar, enabled = erros.valido, modifier = Modifier.fillMaxWidth()) {
                Text("Entrar")
            }
            TextButton(onClick = onCriarConta, modifier = Modifier.fillMaxWidth()) {
                Text("Ainda não tem conta? Criar conta")
            }
        }
    }
}

@Composable
fun TelaCadastro(
    formulario: FormularioCadastro,
    senhaVisivel: Boolean,
    onAlterar: (FormularioCadastro) -> Unit,
    onAlternarSenha: () -> Unit,
    onCadastrar: () -> Unit,
    onVoltar: () -> Unit,
) {
    val erros = validarCadastro(formulario)
    Scaffold(topBar = { BarraSuperior("Criar conta", onVoltar = onVoltar) }) { preenchimento ->
        ColunaFormulario(preenchimento) {
            CampoTexto(
                valor = formulario.nome,
                onValorChange = { onAlterar(formulario.copy(nome = it)) },
                rotulo = "Nome",
                erro = erros.nome,
            )
            CampoTexto(
                valor = formulario.email,
                onValorChange = { onAlterar(formulario.copy(email = it)) },
                rotulo = "E-mail",
                erro = erros.email,
                teclado = KeyboardType.Email,
            )
            CampoSenha(
                valor = formulario.senha,
                onValorChange = { onAlterar(formulario.copy(senha = it)) },
                rotulo = "Senha",
                visivel = senhaVisivel,
                onAlternarVisibilidade = onAlternarSenha,
                erro = erros.senha,
            )
            CampoSenha(
                valor = formulario.confirmacao,
                onValorChange = { onAlterar(formulario.copy(confirmacao = it)) },
                rotulo = "Confirmar senha",
                visivel = senhaVisivel,
                onAlternarVisibilidade = onAlternarSenha,
                erro = erros.confirmacao,
                acaoIme = ImeAction.Done,
            )
            Button(onClick = onCadastrar, enabled = erros.valido, modifier = Modifier.fillMaxWidth()) {
                Text("Criar conta")
            }
        }
    }
}

@Preview
@Composable
fun TelaEntrarPreview() {
    TemaPocketTrack {
        TelaEntrar(FormularioEntrada(email = "ana@"), false, {}, {}, {}, {})
    }
}

@Preview
@Composable
fun TelaCadastroPreview() {
    TemaPocketTrack(escuro = true) {
        TelaCadastro(FormularioCadastro(nome = "Ana", senha = "123"), false, {}, {}, {}, {})
    }
}
