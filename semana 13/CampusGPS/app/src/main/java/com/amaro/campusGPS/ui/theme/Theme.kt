
package com.amaro.campusGPS.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF79C8FF),
    onPrimary = Color(0xFF003550),

    primaryContainer = Color(0xFF164B68),
    onPrimaryContainer = Color(0xFFD1ECFF),

    secondary = Color(0xFF9ED3F0),
    onSecondary = Color(0xFF00354B),

    tertiary = Color(0xFF85D5B2),
    onTertiary = Color(0xFF073C2A),

    background = Color(0xFF101820),
    onBackground = Color(0xFFE7EDF3),

    surface = Color(0xFF151F29),
    onSurface = Color(0xFFE7EDF3),

    surfaceVariant = Color(0xFF263747),
    onSurfaceVariant = Color(0xFFC5D1DC),

    outline = Color(0xFF8295A5)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF12699C),
    onPrimary = Color.White,

    primaryContainer = Color(0xFFDCEEF7),
    onPrimaryContainer = Color(0xFF103F5A),

    secondary = Color(0xFF356888),
    onSecondary = Color.White,

    tertiary = Color(0xFF238C59),
    onTertiary = Color.White,

    background = Color(0xFFF7FAFC),
    onBackground = Color(0xFF172A38),

    surface = Color.White,
    onSurface = Color(0xFF172A38),

    surfaceVariant = Color(0xFFEAF4F9),
    onSurfaceVariant = Color(0xFF526775),

    outline = Color(0xFF8498A6)
)

@Composable
fun CampusGPSTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {

    val colorScheme = when {

        dynamicColor &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {

            val context = LocalContext.current

            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }

        darkTheme -> DarkColorScheme

        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
