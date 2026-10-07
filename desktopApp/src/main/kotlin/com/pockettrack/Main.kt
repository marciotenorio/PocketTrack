package com.pockettrack

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

// Abre com tamanho de celular em pé (largura compacta). Redimensione a janela para ver o
// layout com trilho lateral (a partir de 600 dp) e com lista e detalhe (a partir de 840 dp).
fun main() =
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "PocketTrack",
            state = rememberWindowState(width = 420.dp, height = 860.dp),
        ) {
            App()
        }
    }
