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
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.pockettrack.modelo.DadosApp
import com.pockettrack.modelo.FormularioTransacao
import com.pockettrack.modelo.Transacao
import com.pockettrack.modelo.categoriasEmUso
import com.pockettrack.modelo.comNova
import com.pockettrack.modelo.hoje
import com.pockettrack.modelo.paraFormulario
import com.pockettrack.modelo.paraTransacao
import com.pockettrack.modelo.proximoId
import com.pockettrack.modelo.removendo
import com.pockettrack.modelo.resumoDoMes
import com.pockettrack.modelo.salvando
import com.pockettrack.modelo.semCategoria
import com.pockettrack.modelo.validarNomeCategoria
import com.pockettrack.ui.telas.AcoesDetalhe
import com.pockettrack.ui.telas.TelaAjustes
import com.pockettrack.ui.telas.TelaCategorias
import com.pockettrack.ui.telas.TelaDetalheTransacao
import com.pockettrack.ui.telas.TelaFormularioTransacao
import com.pockettrack.ui.telas.TelaResumo
import com.pockettrack.ui.telas.TelaTransacoes

// O grafo da área principal. As telas não recebem o NavController: recebem lambdas, e quem
// navega é este NavHost. Os dados vêm do estado elevado (`dados`), então uma tela que volta
// ao topo da pilha já aparece atualizada.
@Composable
fun GrafoPrincipal(
    dados: DadosApp,
    onAlterarDados: (DadosApp) -> Unit,
    listaEDetalhe: Boolean,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    // Estado de UI compartilhado entre destinos, elevado acima do NavHost.
    var selecionadaId by rememberSaveable { mutableStateOf<Int?>(null) }
    var confirmandoExclusao by rememberSaveable { mutableStateOf(false) }
    var mesResumo by remember { mutableStateOf(hoje().mesDoAno) }
    var novaCategoria by rememberSaveable { mutableStateOf("") }

    val salvar = { transacao: Transacao ->
        onAlterarDados(
            dados.copy(transacoes = dados.transacoes.salvando(transacao)),
        )
    }
    val excluir = { id: Int ->
        confirmandoExclusao = false
        if (selecionadaId == id) selecionadaId = null
        onAlterarDados(dados.copy(transacoes = dados.transacoes.removendo(id)))
    }
    val acoesDetalhe = { id: Int, depoisDeExcluir: () -> Unit ->
        AcoesDetalhe(
            onEditar = { navController.navigate(Rotas.EditarTransacao(id)) },
            onPedirExclusao = { confirmandoExclusao = true },
            onCancelarExclusao = { confirmandoExclusao = false },
            onConfirmarExclusao = {
                excluir(id)
                depoisDeExcluir()
            },
        )
    }

    NavHost(navController, startDestination = Rotas.Transacoes, modifier = modifier) {
        composable<Rotas.Transacoes> {
            val lista: @Composable (Modifier) -> Unit = { modificador ->
                TelaTransacoes(
                    transacoes = dados.transacoes,
                    resumoDoMes = resumoDoMes(dados.transacoes, dados.categorias, hoje().mesDoAno),
                    nomeCategoria = dados::nomeCategoria,
                    onAbrir = { id ->
                        if (listaEDetalhe) selecionadaId = id else navController.navigate(Rotas.DetalheTransacao(id))
                    },
                    onNova = { navController.navigate(Rotas.NovaTransacao) },
                    selecionadaId = selecionadaId.takeIf { listaEDetalhe },
                    modifier = modificador,
                )
            }
            if (listaEDetalhe) {
                ListaEDetalhe(
                    lista = lista,
                    detalhe = { modificador ->
                        val transacao = selecionadaId?.let(dados::transacao)
                        TelaDetalheTransacao(
                            transacao = transacao,
                            categoria = transacao?.let { dados.nomeCategoria(it.categoriaId) }.orEmpty(),
                            confirmandoExclusao = confirmandoExclusao,
                            acoes = acoesDetalhe(transacao?.id ?: 0) {},
                            modifier = modificador,
                        )
                    },
                )
            } else {
                lista(Modifier)
            }
        }

        // pockettrack://transacao/{id} abre direto este destino, com o argumento já tipado.
        composable<Rotas.DetalheTransacao>(
            deepLinks = listOf(navDeepLink<Rotas.DetalheTransacao>(basePath = BASE_DEEP_LINK)),
        ) { entrada ->
            val id = entrada.toRoute<Rotas.DetalheTransacao>().id
            val transacao = dados.transacao(id)
            TelaDetalheTransacao(
                transacao = transacao,
                categoria = transacao?.let { dados.nomeCategoria(it.categoriaId) }.orEmpty(),
                confirmandoExclusao = confirmandoExclusao,
                acoes = acoesDetalhe(id) { navController.popBackStack() },
                onVoltar = { navController.popBackStack() },
            )
        }

        composable<Rotas.NovaTransacao> {
            var formulario by remember { mutableStateOf(FormularioTransacao(data = hoje())) }
            TelaFormularioTransacao(
                titulo = "Nova transação",
                formulario = formulario,
                categorias = dados.categorias,
                onAlterar = { formulario = it },
                onSalvar = {
                    formulario.paraTransacao(dados.transacoes.proximoId())?.let {
                        salvar(it)
                        navController.popBackStack()
                    }
                },
                onVoltar = { navController.popBackStack() },
            )
        }

        composable<Rotas.EditarTransacao> { entrada ->
            val id = entrada.toRoute<Rotas.EditarTransacao>().id
            var formulario by remember(id) {
                mutableStateOf(dados.transacao(id)?.paraFormulario() ?: FormularioTransacao(data = hoje()))
            }
            TelaFormularioTransacao(
                titulo = "Editar transação",
                formulario = formulario,
                categorias = dados.categorias,
                onAlterar = { formulario = it },
                onSalvar = {
                    formulario.paraTransacao(id)?.let {
                        salvar(it)
                        navController.popBackStack()
                    }
                },
                onVoltar = { navController.popBackStack() },
            )
        }

        composable<Rotas.ResumoMensal> {
            TelaResumo(
                mes = mesResumo,
                resumo = resumoDoMes(dados.transacoes, dados.categorias, mesResumo),
                onMesAnterior = { mesResumo = mesResumo.anterior() },
                onProximoMes = { mesResumo = mesResumo.proximo() },
            )
        }

        composable<Rotas.Categorias> {
            TelaCategorias(
                categorias = dados.categorias,
                emUso = categoriasEmUso(dados.transacoes),
                novoNome = novaCategoria,
                onNovoNome = { novaCategoria = it },
                onAdicionar = {
                    if (validarNomeCategoria(novaCategoria, dados.categorias) == null) {
                        onAlterarDados(dados.copy(categorias = dados.categorias.comNova(novaCategoria)))
                        novaCategoria = ""
                    }
                },
                onRemover = { id -> onAlterarDados(dados.copy(categorias = dados.categorias.semCategoria(id))) },
            )
        }

        composable<Rotas.Ajustes> {
            TelaAjustes(
                usuario = dados.usuario,
                preferencias = dados.preferencias,
                onAlterar = { onAlterarDados(dados.copy(preferencias = it)) },
                onSair = { onAlterarDados(dados.copy(usuario = null)) },
            )
        }
    }
}
