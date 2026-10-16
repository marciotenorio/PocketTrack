package com.pockettrack.ui.telas

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.pockettrack.modelo.HORARIOS_LEMBRETE
import com.pockettrack.modelo.PreferenciaTema
import com.pockettrack.modelo.Preferencias
import com.pockettrack.modelo.Usuario
import com.pockettrack.ui.componentes.BarraSuperior
import com.pockettrack.ui.componentes.ColunaFormulario
import com.pockettrack.ui.componentes.GrupoOpcoes
import com.pockettrack.ui.componentes.LinhaAlternavel
import com.pockettrack.ui.tema.TemaPocketTrack

// Ajustes: conta, tema do app e lembrete diário. O agendamento real da notificação do
// lembrete entra na Sprint 2; aqui fica a configuração.
@Composable
fun TelaAjustes(
    usuario: Usuario?,
    preferencias: Preferencias,
    onAlterar: (Preferencias) -> Unit,
    onSair: () -> Unit,
) {
    Scaffold(topBar = { BarraSuperior("Ajustes") }) { preenchimento ->
        ColunaFormulario(preenchimento) {
            if (usuario != null) {
                Text(
                    "Conta",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.semantics { heading() },
                )
                Text(usuario.nome, style = MaterialTheme.typography.bodyLarge)
                Text(
                    usuario.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                HorizontalDivider()
            }
            GrupoOpcoes(
                titulo = "Tema",
                opcoes = PreferenciaTema.entries,
                selecionada = preferencias.tema,
                rotulo = { it.rotulo },
                onSelecionar = { onAlterar(preferencias.copy(tema = it)) },
            )
            HorizontalDivider()
            LinhaAlternavel(
                titulo = "Lembrete diário",
                descricao = "Avisa para registrar os gastos do dia",
                marcado = preferencias.lembreteAtivo,
                onAlternar = { onAlterar(preferencias.copy(lembreteAtivo = it)) },
            )
            if (preferencias.lembreteAtivo) {
                GrupoOpcoes(
                    titulo = "Horário do lembrete",
                    opcoes = HORARIOS_LEMBRETE,
                    selecionada = preferencias.horarioLembrete,
                    rotulo = { it },
                    onSelecionar = { onAlterar(preferencias.copy(horarioLembrete = it)) },
                )
            }
            HorizontalDivider()
            OutlinedButton(onClick = onSair, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                Text("Sair")
            }
        }
    }
}

@Preview
@Composable
fun TelaAjustesPreview() {
    TemaPocketTrack {
        TelaAjustes(
            usuario = Usuario("ana", "ana@exemplo.com"),
            preferencias = Preferencias(lembreteAtivo = true),
            onAlterar = {},
            onSair = {},
        )
    }
}
