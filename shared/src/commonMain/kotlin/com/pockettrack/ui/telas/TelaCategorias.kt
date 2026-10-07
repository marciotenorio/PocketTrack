package com.pockettrack.ui.telas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pockettrack.modelo.Categoria
import com.pockettrack.modelo.categoriasIniciais
import com.pockettrack.modelo.validarNomeCategoria
import com.pockettrack.ui.componentes.BarraSuperior
import com.pockettrack.ui.componentes.CampoTexto
import com.pockettrack.ui.componentes.EstadoVazio
import com.pockettrack.ui.componentes.LARGURA_MAXIMA_CONTEUDO
import com.pockettrack.ui.tema.TemaPocketTrack

private val ALTURA_LINHA = 56.dp

// Lista de categorias com cadastro de nova. Categorias em uso por alguma transação não
// podem ser removidas, e o botão diz isso ao leitor de tela.
@Composable
fun TelaCategorias(
    categorias: List<Categoria>,
    emUso: Set<Int>,
    novoNome: String,
    onNovoNome: (String) -> Unit,
    onAdicionar: () -> Unit,
    onRemover: (Int) -> Unit,
) {
    val erro = validarNomeCategoria(novoNome, categorias)
    Scaffold(topBar = { BarraSuperior("Categorias") }) { preenchimento ->
        Box(Modifier.fillMaxSize().padding(preenchimento), contentAlignment = Alignment.TopCenter) {
            LazyColumn(
                Modifier.widthIn(max = LARGURA_MAXIMA_CONTEUDO).fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                item(key = "formulario") {
                    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        CampoTexto(
                            valor = novoNome,
                            onValorChange = onNovoNome,
                            rotulo = "Nova categoria",
                            erro = erro,
                            acaoIme = ImeAction.Done,
                            modifier = Modifier.weight(1f),
                        )
                        Button(
                            onClick = onAdicionar,
                            enabled = erro == null,
                            modifier = Modifier.padding(top = 8.dp),
                        ) { Text("Adicionar") }
                    }
                }
                if (categorias.isEmpty()) {
                    item(key = "vazio") { EstadoVazio("Nenhuma categoria cadastrada.") }
                }
                items(categorias, key = { it.id }) { categoria ->
                    LinhaCategoriaCadastro(
                        categoria = categoria,
                        emUso = categoria.id in emUso,
                        onRemover = { onRemover(categoria.id) },
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun LinhaCategoriaCadastro(
    categoria: Categoria,
    emUso: Boolean,
    onRemover: () -> Unit,
) {
    Row(
        Modifier.fillMaxWidth().heightIn(min = ALTURA_LINHA),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            categoria.nome,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
        if (emUso) {
            Text(
                "Em uso",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        IconButton(onClick = onRemover, enabled = !emUso) {
            Icon(
                Icons.Filled.Delete,
                contentDescription =
                    if (emUso) {
                        "Não é possível remover ${categoria.nome}: está em uso"
                    } else {
                        "Remover ${categoria.nome}"
                    },
            )
        }
    }
}

@Preview
@Composable
fun TelaCategoriasPreview() {
    TemaPocketTrack {
        TelaCategorias(categoriasIniciais, emUso = setOf(1, 2), novoNome = "", onNovoNome = {
        }, onAdicionar = {}, onRemover = {})
    }
}

@Preview
@Composable
fun TelaCategoriasVaziaPreview() {
    TemaPocketTrack(escuro = true) {
        TelaCategorias(
            emptyList(),
            emUso = emptySet(),
            novoNome = "",
            onNovoNome = {},
            onAdicionar = {},
            onRemover = {},
        )
    }
}
