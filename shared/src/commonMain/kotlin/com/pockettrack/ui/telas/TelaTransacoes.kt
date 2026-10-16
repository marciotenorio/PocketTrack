package com.pockettrack.ui.telas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pockettrack.modelo.Resumo
import com.pockettrack.modelo.Transacao
import com.pockettrack.modelo.categoriasIniciais
import com.pockettrack.modelo.ordenadasPorData
import com.pockettrack.modelo.transacoesIniciais
import com.pockettrack.ui.componentes.BarraSuperior
import com.pockettrack.ui.componentes.CartaoResumo
import com.pockettrack.ui.componentes.CartaoTransacao
import com.pockettrack.ui.componentes.EstadoVazio
import com.pockettrack.ui.tema.TemaPocketTrack

// Espaço no fim da lista para o último cartão não ficar atrás do botão flutuante.
private val ESPACO_BOTAO_FLUTUANTE = 88.dp

// Histórico de transações, com o resumo do mês corrente no topo.
@Composable
fun TelaTransacoes(
    transacoes: List<Transacao>,
    resumoDoMes: Resumo,
    nomeCategoria: (Int) -> String,
    onAbrir: (Int) -> Unit,
    onNova: () -> Unit,
    modifier: Modifier = Modifier,
    selecionadaId: Int? = null,
) {
    Scaffold(
        modifier = modifier,
        topBar = { BarraSuperior("Transações") },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNova,
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text("Nova transação") },
            )
        },
    ) { preenchimento ->
        if (transacoes.isEmpty()) {
            EstadoVazio(
                "Nenhuma transação ainda.\nToque em \"Nova transação\" para registrar a primeira.",
                Modifier.padding(preenchimento),
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(preenchimento),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = ESPACO_BOTAO_FLUTUANTE),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item(key = "resumo") { CartaoResumo(resumoDoMes) }
                items(transacoes.ordenadasPorData(), key = { it.id }) { transacao ->
                    CartaoTransacao(
                        transacao = transacao,
                        categoria = nomeCategoria(transacao.categoriaId),
                        onAbrir = { onAbrir(transacao.id) },
                        selecionada = transacao.id == selecionadaId,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun TelaTransacoesPreview() {
    TemaPocketTrack {
        TelaTransacoes(
            transacoes = transacoesIniciais,
            resumoDoMes = Resumo(450_000, 161_040, emptyList()),
            nomeCategoria = { id -> categoriasIniciais.first { it.id == id }.nome },
            onAbrir = {},
            onNova = {},
        )
    }
}

@Preview
@Composable
fun TelaTransacoesVaziaPreview() {
    TemaPocketTrack(escuro = true) {
        TelaTransacoes(
            transacoes = emptyList(),
            resumoDoMes = Resumo(0, 0, emptyList()),
            nomeCategoria = { "" },
            onAbrir = {},
            onNova = {},
        )
    }
}
