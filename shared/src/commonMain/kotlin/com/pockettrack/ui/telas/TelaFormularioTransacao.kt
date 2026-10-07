package com.pockettrack.ui.telas

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.pockettrack.modelo.Categoria
import com.pockettrack.modelo.Data
import com.pockettrack.modelo.FormularioTransacao
import com.pockettrack.modelo.MILIS_POR_DIA
import com.pockettrack.modelo.TipoTransacao
import com.pockettrack.modelo.categoriasIniciais
import com.pockettrack.modelo.validarTransacao
import com.pockettrack.ui.componentes.BarraSuperior
import com.pockettrack.ui.componentes.CampoData
import com.pockettrack.ui.componentes.CampoTexto
import com.pockettrack.ui.componentes.ColunaFormulario
import com.pockettrack.ui.componentes.GrupoOpcoes
import com.pockettrack.ui.componentes.SeletorCategoria
import com.pockettrack.ui.tema.TemaPocketTrack

// Formulário de nova transação e de edição (o mesmo formulário, com títulos diferentes).
// Os erros são derivados do formulário por uma função pura; "Salvar" só habilita quando
// tudo é válido, e cada campo inválido diz o que corrigir.
@Composable
fun TelaFormularioTransacao(
    titulo: String,
    formulario: FormularioTransacao,
    categorias: List<Categoria>,
    onAlterar: (FormularioTransacao) -> Unit,
    onSalvar: () -> Unit,
    onVoltar: () -> Unit,
) {
    val erros = validarTransacao(formulario)
    var escolhendoData by rememberSaveable { mutableStateOf(false) }

    Scaffold(topBar = { BarraSuperior(titulo, onVoltar = onVoltar) }) { preenchimento ->
        ColunaFormulario(preenchimento) {
            GrupoOpcoes(
                titulo = "Tipo",
                opcoes = TipoTransacao.entries,
                selecionada = formulario.tipo,
                rotulo = { it.rotulo },
                onSelecionar = { onAlterar(formulario.copy(tipo = it)) },
            )
            CampoTexto(
                valor = formulario.descricao,
                onValorChange = { onAlterar(formulario.copy(descricao = it)) },
                rotulo = "Descrição",
                erro = erros.descricao,
            )
            CampoTexto(
                valor = formulario.valor,
                onValorChange = { onAlterar(formulario.copy(valor = it)) },
                rotulo = "Valor (R$)",
                erro = erros.valor,
                teclado = KeyboardType.Decimal,
                acaoIme = ImeAction.Done,
            )
            CampoData(
                dataFormatada = formulario.data.formatada(),
                rotulo = "Data",
                onEscolher = { escolhendoData = true },
            )
            SeletorCategoria(
                categorias = categorias,
                selecionadaId = formulario.categoriaId,
                onSelecionar = { onAlterar(formulario.copy(categoriaId = it)) },
                erro = erros.categoria,
            )
            Button(onClick = onSalvar, enabled = erros.valido, modifier = Modifier.fillMaxWidth()) {
                Text("Salvar")
            }
        }
    }

    if (escolhendoData) {
        DialogoData(
            data = formulario.data,
            onEscolher = {
                onAlterar(formulario.copy(data = it))
                escolhendoData = false
            },
            onFechar = { escolhendoData = false },
        )
    }
}

// O DatePicker trabalha com milissegundos em UTC desde 1970; a conversão é exata em dias.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DialogoData(
    data: Data,
    onEscolher: (Data) -> Unit,
    onFechar: () -> Unit,
) {
    val estado = rememberDatePickerState(initialSelectedDateMillis = data.emDiasEpoca() * MILIS_POR_DIA)
    DatePickerDialog(
        onDismissRequest = onFechar,
        confirmButton = {
            TextButton(
                onClick = {
                    estado.selectedDateMillis?.let { onEscolher(Data.deDiasEpoca(it / MILIS_POR_DIA)) } ?: onFechar()
                },
            ) { Text("OK") }
        },
        dismissButton = { TextButton(onClick = onFechar) { Text("Cancelar") } },
    ) {
        DatePicker(state = estado)
    }
}

@Preview
@Composable
fun TelaFormularioTransacaoPreview() {
    TemaPocketTrack {
        TelaFormularioTransacao(
            titulo = "Nova transação",
            formulario = FormularioTransacao(descricao = "Mercado", valor = "12,", data = Data(2026, 10, 7)),
            categorias = categoriasIniciais,
            onAlterar = {},
            onSalvar = {},
            onVoltar = {},
        )
    }
}
