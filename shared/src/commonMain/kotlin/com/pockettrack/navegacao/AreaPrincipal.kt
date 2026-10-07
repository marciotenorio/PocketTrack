package com.pockettrack.navegacao

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pockettrack.modelo.DadosApp
import kotlin.reflect.KClass

private const val PESO_LISTA = 0.4f

// Classes de largura de janela do Material 3 (limites em 600 dp e 840 dp).
enum class ClasseLargura { COMPACTA, MEDIA, EXPANDIDA }

private data class DestinoPrincipal(
    val rota: Any,
    val rotulo: String,
    val icone: ImageVector,
)

private val destinosPrincipais =
    listOf(
        DestinoPrincipal(Rotas.Transacoes, "Transações", Icons.AutoMirrored.Filled.List),
        DestinoPrincipal(Rotas.ResumoMensal, "Resumo", Icons.Filled.DateRange),
        DestinoPrincipal(Rotas.Categorias, "Categorias", Icons.Filled.ShoppingCart),
        DestinoPrincipal(Rotas.Ajustes, "Ajustes", Icons.Filled.Settings),
    )

private fun NavDestination?.estaEm(rota: KClass<*>): Boolean = this?.hierarchy?.any { it.hasRoute(rota) } == true

// Área do app depois do login. O layout é decidido pela largura da janela:
// - compacta (celular em pé): barra de navegação inferior, uma tela por vez;
// - média (celular deitado, tablet pequeno): trilho lateral, uma tela por vez;
// - expandida (tablet deitado, desktop): trilho lateral e, em Transações, lista e detalhe
//   lado a lado.
// A estrutura (Scaffold > Row > grafo) é a mesma nas duas larguras, para o NavHost não ser
// recriado ao girar o aparelho ou redimensionar a janela.
@Composable
fun AreaPrincipal(
    dados: DadosApp,
    onAlterarDados: (DadosApp) -> Unit,
    largura: ClasseLargura,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val largo = largura != ClasseLargura.COMPACTA
    val entradaAtual by navController.currentBackStackEntryAsState()
    val destinoAtual = entradaAtual?.destination
    val naRaiz = destinosPrincipais.any { destinoAtual.estaEm(it.rota::class) }

    val irPara = { rota: Any ->
        navController.navigate(rota) {
            // Volta à raiz do grafo guardando o estado de cada aba, sem empilhar abas repetidas.
            popUpTo(Rotas.Transacoes) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            if (!largo && naRaiz) {
                NavigationBar {
                    destinosPrincipais.forEach { destino ->
                        NavigationBarItem(
                            selected = destinoAtual.estaEm(destino.rota::class),
                            onClick = { irPara(destino.rota) },
                            icon = { Icon(destino.icone, contentDescription = null) },
                            label = { Text(destino.rotulo) },
                        )
                    }
                }
            }
        },
    ) { preenchimento ->
        Row(
            Modifier
                .fillMaxSize()
                .padding(preenchimento)
                .consumeWindowInsets(preenchimento),
        ) {
            if (largo && naRaiz) {
                NavigationRail {
                    destinosPrincipais.forEach { destino ->
                        NavigationRailItem(
                            selected = destinoAtual.estaEm(destino.rota::class),
                            onClick = { irPara(destino.rota) },
                            icon = { Icon(destino.icone, contentDescription = null) },
                            label = { Text(destino.rotulo) },
                        )
                    }
                }
            }
            GrafoPrincipal(
                dados = dados,
                onAlterarDados = onAlterarDados,
                listaEDetalhe = largura == ClasseLargura.EXPANDIDA,
                navController = navController,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

// Janela larga em Transações: lista à esquerda, detalhe da selecionada à direita, sem navegar.
@Composable
internal fun ListaEDetalhe(
    lista: @Composable (Modifier) -> Unit,
    detalhe: @Composable (Modifier) -> Unit,
) {
    Row(Modifier.fillMaxSize()) {
        lista(Modifier.weight(PESO_LISTA).fillMaxHeight())
        VerticalDivider()
        detalhe(Modifier.weight(1f - PESO_LISTA).fillMaxHeight())
    }
}
