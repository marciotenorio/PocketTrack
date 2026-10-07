package com.pockettrack.navegacao

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.pockettrack.modelo.FormularioCadastro
import com.pockettrack.modelo.FormularioEntrada
import com.pockettrack.modelo.Usuario
import com.pockettrack.modelo.usuarioDoEmail
import com.pockettrack.modelo.validarCadastro
import com.pockettrack.modelo.validarEntrada
import com.pockettrack.ui.telas.TelaCadastro
import com.pockettrack.ui.telas.TelaEntrar

// Fluxo de entrada e cadastro, num grafo aninhado. Ao entrar, o app troca para a área
// principal; um deep link recebido antes do login é tratado pela área principal logo depois.
@Composable
fun FluxoAutenticacao(
    onAutenticado: (Usuario) -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    var entrada by remember { mutableStateOf(FormularioEntrada()) }
    var cadastro by remember { mutableStateOf(FormularioCadastro()) }
    var senhaVisivel by rememberSaveable { mutableStateOf(false) }

    NavHost(navController, startDestination = Rotas.Autenticacao, modifier = modifier) {
        navigation<Rotas.Autenticacao>(startDestination = Rotas.Entrar) {
            composable<Rotas.Entrar> {
                TelaEntrar(
                    formulario = entrada,
                    senhaVisivel = senhaVisivel,
                    onAlterar = { entrada = it },
                    onAlternarSenha = { senhaVisivel = !senhaVisivel },
                    onEntrar = {
                        if (validarEntrada(entrada).valido) onAutenticado(usuarioDoEmail(entrada.email))
                    },
                    onCriarConta = { navController.navigate(Rotas.Cadastro) },
                )
            }
            composable<Rotas.Cadastro> {
                TelaCadastro(
                    formulario = cadastro,
                    senhaVisivel = senhaVisivel,
                    onAlterar = { cadastro = it },
                    onAlternarSenha = { senhaVisivel = !senhaVisivel },
                    onCadastrar = {
                        if (validarCadastro(cadastro).valido) {
                            onAutenticado(Usuario(cadastro.nome.trim(), cadastro.email.trim()))
                        }
                    },
                    onVoltar = { navController.popBackStack() },
                )
            }
        }
    }
}
