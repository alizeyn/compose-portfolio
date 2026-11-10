package com.alizeyn.site.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Material 3 shape scale: https://m3.material.io/styles/shape/shape-scale-tokens
private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(28.dp),
)

@Composable
fun SiteTheme(darkTheme: Boolean, content: @Composable () -> Unit) {
    // Material 3 Expressive: spring-based motion for every component transition.
    MaterialExpressiveTheme(
        colorScheme = (if (darkTheme) DarkColors else LightColors).animated(),
        motionScheme = MotionScheme.expressive(),
        shapes = AppShapes,
        typography = SiteTypography,
        content = content,
    )
}

/** Cross-fades every role when the scheme changes, so toggling the theme feels like one motion. */
@Composable
private fun ColorScheme.animated(): ColorScheme {
    @Composable
    fun c(target: Color): Color = animateColorAsState(target, tween(durationMillis = 450), label = "scheme").value
    return copy(
        primary = c(primary), onPrimary = c(onPrimary),
        primaryContainer = c(primaryContainer), onPrimaryContainer = c(onPrimaryContainer),
        secondary = c(secondary), onSecondary = c(onSecondary),
        secondaryContainer = c(secondaryContainer), onSecondaryContainer = c(onSecondaryContainer),
        tertiary = c(tertiary), onTertiary = c(onTertiary),
        tertiaryContainer = c(tertiaryContainer), onTertiaryContainer = c(onTertiaryContainer),
        error = c(error), onError = c(onError),
        errorContainer = c(errorContainer), onErrorContainer = c(onErrorContainer),
        background = c(background), onBackground = c(onBackground),
        surface = c(surface), onSurface = c(onSurface),
        surfaceVariant = c(surfaceVariant), onSurfaceVariant = c(onSurfaceVariant),
        outline = c(outline), outlineVariant = c(outlineVariant),
        inverseSurface = c(inverseSurface), inverseOnSurface = c(inverseOnSurface), inversePrimary = c(inversePrimary),
        surfaceTint = c(surfaceTint),
        surfaceContainerLowest = c(surfaceContainerLowest), surfaceContainerLow = c(surfaceContainerLow),
        surfaceContainer = c(surfaceContainer), surfaceContainerHigh = c(surfaceContainerHigh),
        surfaceContainerHighest = c(surfaceContainerHighest),
        surfaceDim = c(surfaceDim), surfaceBright = c(surfaceBright),
    )
}
