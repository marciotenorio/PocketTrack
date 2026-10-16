package com.pockettrack.ui.telas

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.pockettrack.modelo.Mes
import com.pockettrack.modelo.Resumo
import com.pockettrack.modelo.TotalCategoria
import com.pockettrack.modelo.categoriasIniciais
import com.pockettrack.ui.componentes.BarraSuperior
import com.pockettrack.ui.componentes.CartaoResumo
import com.pockettrack.ui.componentes.ColunaFormulario
import com.pockettrack.ui.componentes.LinhaCategoria
import com.pockettrack.ui.componentes.SeletorMes
import com.pockettrack.ui.tema.TemaPocketTrack

// Resumo mensal: receitas, despesas, saldo e para onde foram as despesas.
@Composable
fun TelaResumo(
    mes: Mes,
    resumo: Resumo,
    onMesAnterior: () -> Unit,
    onProximoMes: () -> Unit,
) {
    Scaffold(topBar = { BarraSuperior("Resumo mensal") }) { preenchimento ->
        ColunaFormulario(preenchimento) {
            SeletorMes(mes.nome(), onAnterior = onMesAnterior, onProximo = onProximoMes)
            CartaoResumo(resumo)
            Text(
                "Despesas por categoria",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.semantics { heading() },
            )
            if (resumo.despesasPorCategoria.isEmpty()) {
                Text(
                    "Nenhuma despesa neste mês.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            resumo.despesasPorCategoria.forEach { LinhaCategoria(it) }
        }
    }
}

@Preview
@Composable
fun TelaResumoPreview() {
    TemaPocketTrack {
        TelaResumo(
            mes = Mes(2026, 10),
            resumo =
                Resumo(
                    receitasCentavos = 450_000,
                    despesasCentavos = 161_040,
                    despesasPorCategoria =
                        listOf(
                            TotalCategoria(categoriasIniciais[2], 120_000, 0.75f),
                            TotalCategoria(categoriasIniciais[0], 38_790, 0.24f),
                            TotalCategoria(categoriasIniciais[1], 2_250, 0.01f),
                        ),
                ),
            onMesAnterior = {},
            onProximoMes = {},
        )
    }
}

@Preview
@Composable
fun TelaResumoVaziaPreview() {
    TemaPocketTrack(escuro = true) {
        TelaResumo(Mes(2026, 11), Resumo(0, 0, emptyList()), onMesAnterior = {}, onProximoMes = {})
    }
}
