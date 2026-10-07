package com.pockettrack.ui.tema

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.pockettrack.modelo.PreferenciaTema

// Esquema derivado de uma cor-semente verde (#386A20) no Material Theme Builder.
// As telas usam só os papéis (primary, error, surface…), nunca uma cor fixa.

private val esquemaClaro: ColorScheme =
    lightColorScheme(
        primary = Color(0xFF386A20),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFB7F397),
        onPrimaryContainer = Color(0xFF042100),
        secondary = Color(0xFF55624C),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFD9E7CB),
        onSecondaryContainer = Color(0xFF131F0D),
        tertiary = Color(0xFF386666),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFBBEBEC),
        onTertiaryContainer = Color(0xFF002020),
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFDAD6),
        onErrorContainer = Color(0xFF410002),
        background = Color(0xFFFDFDF6),
        onBackground = Color(0xFF1A1C18),
        surface = Color(0xFFFDFDF6),
        onSurface = Color(0xFF1A1C18),
        surfaceVariant = Color(0xFFDFE4D7),
        onSurfaceVariant = Color(0xFF43483F),
        outline = Color(0xFF73796E),
    )

private val esquemaEscuro: ColorScheme =
    darkColorScheme(
        primary = Color(0xFF9CD67D),
        onPrimary = Color(0xFF0B3900),
        primaryContainer = Color(0xFF20510A),
        onPrimaryContainer = Color(0xFFB7F397),
        secondary = Color(0xFFBDCBB0),
        onSecondary = Color(0xFF283421),
        secondaryContainer = Color(0xFF3E4A36),
        onSecondaryContainer = Color(0xFFD9E7CB),
        tertiary = Color(0xFFA0CFD0),
        onTertiary = Color(0xFF003738),
        tertiaryContainer = Color(0xFF1E4E4E),
        onTertiaryContainer = Color(0xFFBBEBEC),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        errorContainer = Color(0xFF93000A),
        onErrorContainer = Color(0xFFFFDAD6),
        background = Color(0xFF1A1C18),
        onBackground = Color(0xFFE3E3DC),
        surface = Color(0xFF1A1C18),
        onSurface = Color(0xFFE3E3DC),
        surfaceVariant = Color(0xFF43483F),
        onSurfaceVariant = Color(0xFFC3C8BC),
        outline = Color(0xFF8D9387),
    )

@Composable
fun usarTemaEscuro(preferencia: PreferenciaTema): Boolean =
    when (preferencia) {
        PreferenciaTema.SISTEMA -> isSystemInDarkTheme()
        PreferenciaTema.CLARO -> false
        PreferenciaTema.ESCURO -> true
    }

@Composable
fun TemaPocketTrack(
    escuro: Boolean = isSystemInDarkTheme(),
    conteudo: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (escuro) esquemaEscuro else esquemaClaro,
        content = conteudo,
    )
}
