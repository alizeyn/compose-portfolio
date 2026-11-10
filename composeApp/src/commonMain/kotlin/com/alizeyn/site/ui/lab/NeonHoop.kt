package com.alizeyn.site.ui.lab

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.withSaveLayer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/**
 * Zone5's hula-hoop ring: an ellipse split into a far half and a near half, each stroked in
 * neon passes (wide and faint, the ring, a white screen-blended core). The far half is drawn
 * into its own layer and the athlete's torso is punched out of it, so the hoop reads as going
 * behind the body. Ported from Zone5; the ring geometry here is a plain ellipse.
 */
private val GLOW_PASS_SCALES = floatArrayOf(3.2f, 1.9f)
private val GLOW_OPACITIES = floatArrayOf(0.15f, 0.4f)
private const val CORE_SCALE = 0.45f
private const val CORE_OPACITY = 0.8f
private const val SEGMENTS = 48

fun DrawScope.drawNeonHoop(
    center: Offset,
    radiusX: Float,
    radiusY: Float,
    strokeWidth: Float,
    color: Color,
    torsoStart: Float,
    torsoEnd: Float,
) {
    if (radiusX <= 0f || strokeWidth <= 0f) return
    val back = ellipseArc(center, radiusX, radiusY, from = PI, to = 2 * PI)
    val front = ellipseArc(center, radiusX, radiusY, from = 0.0, to = PI)
    val pad = strokeWidth * GLOW_PASS_SCALES.max() / 2f
    val bounds = Rect(center.x - radiusX - pad, center.y - radiusY - pad, center.x + radiusX + pad, center.y + pad)
    drawContext.canvas.withSaveLayer(bounds, Paint()) {
        strokeNeon(back, strokeWidth, color)
        punchTorso(center.x - radiusX, center.x + radiusX, torsoStart, torsoEnd)
    }
    strokeNeon(front, strokeWidth, color)
}

private fun ellipseArc(center: Offset, rx: Float, ry: Float, from: Double, to: Double): Path = Path().apply {
    for (i in 0..SEGMENTS) {
        val a = from + (to - from) * i / SEGMENTS
        val p = Offset(center.x + (cos(a) * rx).toFloat(), center.y + (sin(a) * ry).toFloat())
        if (i == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y)
    }
}

private fun DrawScope.strokeNeon(path: Path, width: Float, color: Color) {
    GLOW_PASS_SCALES.forEachIndexed { pass, scale ->
        drawPath(path, color.copy(alpha = GLOW_OPACITIES[pass]), style = hoopStroke(width * scale))
    }
    drawPath(path, color, style = hoopStroke(width))
    drawPath(path, Color.White.copy(alpha = CORE_OPACITY), style = hoopStroke(width * CORE_SCALE), blendMode = BlendMode.Screen)
}

private fun hoopStroke(width: Float) = Stroke(width = width, cap = StrokeCap.Round)

/** A full-height band across the ring's extent: opaque between the two stops, fading at either end. */
private fun DrawScope.punchTorso(fromX: Float, toX: Float, start: Float, end: Float) {
    val width = toX - fromX
    if (width <= 0f) return
    drawRect(
        brush = Brush.linearGradient(
            0f to Color.Transparent,
            max(0f, start - 0.08f) to Color.Transparent,
            start to Color.Black,
            end to Color.Black,
            min(1f, end + 0.08f) to Color.Transparent,
            1f to Color.Transparent,
            start = Offset(fromX, 0f),
            end = Offset(toX, 0f),
        ),
        topLeft = Offset(fromX, 0f),
        size = Size(width, size.height),
        blendMode = BlendMode.DstOut,
    )
}
