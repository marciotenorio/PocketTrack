package com.pockettrack.ui.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pockettrack.modelo.Categoria

private val ALTURA_MINIMA_LINHA = 56.dp

// Grupo de opções exclusivas (botões de rádio). Cada linha inteira é um único elemento
// selecionável, com o papel de RadioButton para o leitor de tela.
@Composable
fun <T> GrupoOpcoes(
    titulo: String,
    opcoes: List<T>,
    selecionada: T,
    rotulo: (T) -> String,
    onSelecionar: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxWidth()) {
        Text(
            titulo,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.semantics { heading() },
        )
        Column(Modifier.selectableGroup()) {
            opcoes.forEach { opcao ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .heightIn(min = ALTURA_MINIMA_LINHA)
                        .selectable(
                            selected = opcao == selecionada,
                            role = Role.RadioButton,
                            onClick = { onSelecionar(opcao) },
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(selected = opcao == selecionada, onClick = null)
                    Text(rotulo(opcao), Modifier.padding(start = 12.dp))
                }
            }
        }
    }
}

// Escolha de categoria em chips que quebram linha, sem vazar em telas estreitas.
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SeletorCategoria(
    categorias: List<Categoria>,
    selecionadaId: Int?,
    onSelecionar: (Int) -> Unit,
    modifier: Modifier = Modifier,
    erro: String? = null,
) {
    Column(modifier.fillMaxWidth()) {
        Text(
            "Categoria",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.semantics { heading() },
        )
        FlowRow(
            Modifier.fillMaxWidth().selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            categorias.forEach { categoria ->
                FilterChip(
                    selected = categoria.id == selecionadaId,
                    onClick = { onSelecionar(categoria.id) },
                    label = { Text(categoria.nome) },
                )
            }
        }
        if (erro != null) {
            Text(
                erro,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

// Linha que liga e desliga algo. A linha inteira é o alvo de toque e um único elemento
// para o leitor de tela (papel de Switch, com o texto e o estado).
@Composable
fun LinhaAlternavel(
    titulo: String,
    descricao: String,
    marcado: Boolean,
    onAlternar: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .fillMaxWidth()
            .heightIn(min = ALTURA_MINIMA_LINHA)
            .toggleable(value = marcado, role = Role.Switch, onValueChange = onAlternar)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(titulo, style = MaterialTheme.typography.bodyLarge)
            Text(
                descricao,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Switch(checked = marcado, onCheckedChange = null, modifier = Modifier.padding(start = 12.dp))
    }
}

// Mês exibido com botões de anterior e próximo. O nome do mês é uma live region:
// o leitor de tela anuncia o novo mês ao trocar.
@Composable
fun SeletorMes(
    nomeMes: String,
    onAnterior: () -> Unit,
    onProximo: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onAnterior) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Mês anterior")
        }
        Text(
            nomeMes.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .weight(1f)
                    .semantics { liveRegion = LiveRegionMode.Polite },
        )
        IconButton(onClick = onProximo) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Próximo mês")
        }
    }
}
