package com.pockettrack

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.window.core.layout.WindowSizeClass
import com.pockettrack.modelo.DadosApp
import com.pockettrack.navegacao.AreaPrincipal
import com.pockettrack.navegacao.ClasseLargura
import com.pockettrack.navegacao.FluxoAutenticacao
import com.pockettrack.ui.tema.TemaPocketTrack
import com.pockettrack.ui.tema.usarTemaEscuro

// Raiz do app, compartilhada por Android, iOS e desktop.
@Composable
fun App() {
    // Estado elevado acima da navegação. Em memória nesta sprint; persistência na Sprint 2.
    var dados by remember { mutableStateOf(DadosApp()) }
    val largura = classeLarguraAtual()

    TemaPocketTrack(escuro = usarTemaEscuro(dados.preferencias.tema)) {
        // Surface na raiz: fundo e cor de texto vêm do tema, claro ou escuro.
        Surface(Modifier.fillMaxSize()) {
            val usuario = dados.usuario
            if (usuario == null) {
                FluxoAutenticacao(onAutenticado = { dados = dados.copy(usuario = it) })
            } else {
                AreaPrincipal(dados = dados, onAlterarDados = { dados = it }, largura = largura)
            }
        }
    }
}

// O layout é decidido pela largura da janela, não pela orientação nem pelo tipo de aparelho.
@Composable
private fun classeLarguraAtual(): ClasseLargura {
    val classe = currentWindowAdaptiveInfo().windowSizeClass
    return when {
        classe.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) -> ClasseLargura.EXPANDIDA
        classe.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) -> ClasseLargura.MEDIA
        else -> ClasseLargura.COMPACTA
    }
}

@Preview
@Composable
fun AppPreview() {
    App()
}
