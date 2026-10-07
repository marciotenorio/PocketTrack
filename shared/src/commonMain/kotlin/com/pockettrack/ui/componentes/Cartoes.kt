package com.pockettrack.ui.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pockettrack.modelo.Resumo
import com.pockettrack.modelo.TipoTransacao
import com.pockettrack.modelo.TotalCategoria
import com.pockettrack.modelo.Transacao
import com.pockettrack.modelo.categoriasIniciais
import com.pockettrack.modelo.formatarMoeda
import com.pockettrack.modelo.transacoesIniciais
import com.pockettrack.ui.tema.TemaPocketTrack

private const val PORCENTO = 100

// Cor do valor pelo papel do tema: receita em primary, despesa em error.
@Composable
fun corDoTipo(tipo: TipoTransacao): Color =
    when (tipo) {
        TipoTransacao.RECEITA -> MaterialTheme.colorScheme.primary
        TipoTransacao.DESPESA -> MaterialTheme.colorScheme.error
    }

fun valorComSinal(transacao: Transacao): String =
    when (transacao.tipo) {
        TipoTransacao.RECEITA -> "+" + formatarMoeda(transacao.valorCentavos)
        TipoTransacao.DESPESA -> "-" + formatarMoeda(transacao.valorCentavos)
    }

// Um item da lista de transações. O cartão inteiro é o alvo de toque, e o leitor de tela
// anuncia uma frase completa em vez de quatro textos soltos.
@Composable
fun CartaoTransacao(
    transacao: Transacao,
    categoria: String,
    onAbrir: () -> Unit,
    modifier: Modifier = Modifier,
    selecionada: Boolean = false,
) {
    val valor = formatarMoeda(transacao.valorCentavos)
    val descricaoFalada =
        "${transacao.tipo.rotulo}: ${transacao.descricao}, $valor, $categoria, ${transacao.data.formatada()}"
    Card(
        onClick = onAbrir,
        colors =
            if (selecionada) {
                CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            } else {
                CardDefaults.cardColors()
            },
        modifier =
            modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = descricaoFalada
                    selected = selecionada
                },
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    transacao.descricao,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    "$categoria · ${transacao.data.formatada()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text(
                valorComSinal(transacao),
                style = MaterialTheme.typography.titleMedium,
                color = corDoTipo(transacao.tipo),
                modifier = Modifier.padding(start = 12.dp),
            )
        }
    }
}

// Receitas, despesas e saldo de um mês.
@Composable
fun CartaoResumo(
    resumo: Resumo,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            LinhaValor("Receitas", formatarMoeda(resumo.receitasCentavos))
            LinhaValor("Despesas", formatarMoeda(resumo.despesasCentavos))
            HorizontalDivider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.3f))
            LinhaValor("Saldo", formatarMoeda(resumo.saldoCentavos), destaque = true)
        }
    }
}

// Rótulo à esquerda, valor à direita, lidos juntos pelo leitor de tela.
@Composable
fun LinhaValor(
    rotulo: String,
    valor: String,
    modifier: Modifier = Modifier,
    destaque: Boolean = false,
) {
    val estilo = if (destaque) MaterialTheme.typography.titleLarge else MaterialTheme.typography.bodyLarge
    Row(
        modifier.fillMaxWidth().semantics(mergeDescendants = true) {},
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(rotulo, style = estilo, modifier = Modifier.weight(1f))
        Text(
            valor,
            style = estilo,
            fontWeight = if (destaque) FontWeight.Bold else null,
        )
    }
}

// Uma categoria no resumo: nome, total e a fatia das despesas do mês.
@Composable
fun LinhaCategoria(
    total: TotalCategoria,
    modifier: Modifier = Modifier,
) {
    val porcentagem = (total.fracao * PORCENTO).toInt()
    Column(
        modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {},
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                total.categoria.nome,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            Text("$porcentagem%", style = MaterialTheme.typography.bodyMedium)
            Text(
                formatarMoeda(total.totalCentavos),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 12.dp),
                maxLines = 1,
            )
        }
        // Decorativa: a porcentagem já está no texto acima.
        LinearProgressIndicator(
            progress = { total.fracao },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
fun CartaoTransacaoPreview() {
    TemaPocketTrack {
        CartaoTransacao(transacoesIniciais[2], categoria = "Alimentação", onAbrir = {})
    }
}

@Preview
@Composable
fun CartaoResumoPreview() {
    TemaPocketTrack(escuro = true) {
        CartaoResumo(Resumo(450_000, 166_040, listOf(TotalCategoria(categoriasIniciais[0], 38_790, 0.23f))))
    }
}
