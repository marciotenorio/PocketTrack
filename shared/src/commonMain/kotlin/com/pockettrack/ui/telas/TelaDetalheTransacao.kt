package com.pockettrack.ui.telas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pockettrack.modelo.Transacao
import com.pockettrack.modelo.transacoesIniciais
import com.pockettrack.ui.componentes.BarraSuperior
import com.pockettrack.ui.componentes.ColunaFormulario
import com.pockettrack.ui.componentes.EstadoVazio
import com.pockettrack.ui.componentes.LinhaValor
import com.pockettrack.ui.componentes.corDoTipo
import com.pockettrack.ui.componentes.valorComSinal
import com.pockettrack.ui.tema.TemaPocketTrack

// Detalhe de uma transação, com editar e excluir.
// `transacao` nula: id inexistente (por exemplo, um deep link para uma transação apagada)
// ou nenhuma selecionada no painel lateral.
// `onVoltar` nulo: painel de detalhe ao lado da lista, sem para onde voltar.
@Composable
fun TelaDetalheTransacao(
    transacao: Transacao?,
    categoria: String,
    confirmandoExclusao: Boolean,
    acoes: AcoesDetalhe,
    modifier: Modifier = Modifier,
    onVoltar: (() -> Unit)? = null,
) {
    Scaffold(modifier = modifier, topBar = { BarraSuperior("Detalhes", onVoltar = onVoltar) }) { preenchimento ->
        if (transacao == null) {
            EstadoVazio(
                if (onVoltar == null) "Selecione uma transação na lista." else "Transação não encontrada.",
                Modifier.padding(preenchimento),
            )
            return@Scaffold
        }
        ColunaFormulario(preenchimento) {
            Text(
                transacao.descricao,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.semantics { heading() },
            )
            Text(
                valorComSinal(transacao),
                style = MaterialTheme.typography.displaySmall,
                color = corDoTipo(transacao.tipo),
            )
            LinhaValor("Tipo", transacao.tipo.rotulo)
            LinhaValor("Categoria", categoria)
            LinhaValor("Data", transacao.data.formatada())
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = acoes.onEditar, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Filled.Edit, contentDescription = null)
                    Text("Editar", Modifier.padding(start = 8.dp))
                }
                Button(
                    onClick = acoes.onPedirExclusao,
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError,
                        ),
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = null)
                    Text("Excluir", Modifier.padding(start = 8.dp))
                }
            }
        }
        if (confirmandoExclusao) {
            AlertDialog(
                onDismissRequest = acoes.onCancelarExclusao,
                title = { Text("Excluir transação?") },
                text = { Text("\"${transacao.descricao}\" será removida do histórico.") },
                confirmButton = { TextButton(onClick = acoes.onConfirmarExclusao) { Text("Excluir") } },
                dismissButton = { TextButton(onClick = acoes.onCancelarExclusao) { Text("Cancelar") } },
            )
        }
    }
}

// Eventos da tela de detalhe, agrupados para a assinatura da tela ficar curta.
data class AcoesDetalhe(
    val onEditar: () -> Unit = {},
    val onPedirExclusao: () -> Unit = {},
    val onCancelarExclusao: () -> Unit = {},
    val onConfirmarExclusao: () -> Unit = {},
)

@Preview
@Composable
fun TelaDetalheTransacaoPreview() {
    TemaPocketTrack {
        TelaDetalheTransacao(
            transacoesIniciais[1],
            "Moradia",
            confirmandoExclusao = false,
            AcoesDetalhe(),
            onVoltar = {},
        )
    }
}

@Preview
@Composable
fun TelaDetalheTransacaoVaziaPreview() {
    TemaPocketTrack(escuro = true) {
        TelaDetalheTransacao(null, "", confirmandoExclusao = false, AcoesDetalhe())
    }
}
