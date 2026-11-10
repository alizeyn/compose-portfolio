package com.alizeyn.site.ui.lab

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale

/**
 * The one dot every Zone5 exercise draws its target circles, floor pucks and placement cues with.
 * Ring stroke, shadow and halo are derived from the drawn radius, so the proportions hold on any
 * device or export size. Ported from Zone5; the shadow is a gradient here rather than a blur.
 */
object DotMark {
    const val STROKE_TO_RADIUS = 0.16f
    const val SHADOW_BLUR_TO_RADIUS = 0.64f
    const val SHADOW_OFFSET_Y_TO_RADIUS = 0.13f
    const val GLOW_SCALE = 1.7f
    const val GLOW_OPACITY = 0.18f
    const val SHADOW_OPACITY = 0.8f
}

enum class DotPlane(val label: String, val verticalScale: Float) {
    /** Upright, facing the camera. */
    Body("Body", 1f),

    /** Flattened onto the floor so it reads as a marker lying down. */
    Floor("Floor", 0.4f),
}

enum class DotFill(val label: String) { Filled("Filled"), Outline("Outline") }

/** Draws one mark: halo, drop shadow, disc and white ring, then projects it onto [plane]. */
fun DrawScope.drawExerciseDot(
    center: Offset,
    radiusPx: Float,
    tint: Color,
    plane: DotPlane = DotPlane.Body,
    fill: DotFill = DotFill.Filled,
    ringColor: Color = Color.White,
    glowScale: Float = DotMark.GLOW_SCALE,
    opacity: Float = 1f,
) {
    if (radiusPx <= 0f || opacity <= 0f) return
    val strokePx = radiusPx * DotMark.STROKE_TO_RADIUS
    val shadowBlurPx = radiusPx * DotMark.SHADOW_BLUR_TO_RADIUS
    val shadowOffsetYPx = radiusPx * DotMark.SHADOW_OFFSET_Y_TO_RADIUS
    val glowRadiusPx = radiusPx * glowScale

    // Drawn round and then squashed, so on the floor the ring and shadow foreshorten with the disc.
    scale(scaleX = 1f, scaleY = plane.verticalScale, pivot = center) {
        if (glowScale > 1f) {
            drawCircle(
                brush = Brush.radialGradient(
                    colorStops = arrayOf(
                        radiusPx / glowRadiusPx to tint.fade(DotMark.GLOW_OPACITY * opacity),
                        1f to tint.fade(0f),
                    ),
                    center = center,
                    radius = glowRadiusPx,
                ),
                radius = glowRadiusPx,
                center = center,
            )
        }
        val shadowCenter = center + Offset(0f, shadowOffsetYPx)
        val shadowRadius = radiusPx + shadowBlurPx
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    (radiusPx * 0.6f) / shadowRadius to tint.fade(DotMark.SHADOW_OPACITY * 0.5f * opacity),
                    1f to tint.fade(0f),
                ),
                center = shadowCenter,
                radius = shadowRadius,
            ),
            radius = shadowRadius,
            center = shadowCenter,
        )
        if (fill == DotFill.Filled) drawCircle(color = tint.fade(opacity), radius = radiusPx, center = center)
        drawCircle(color = ringColor.fade(opacity), radius = radiusPx, center = center, style = Stroke(width = strokePx))
    }
}

private fun Color.fade(factor: Float): Color = copy(alpha = alpha * factor)
