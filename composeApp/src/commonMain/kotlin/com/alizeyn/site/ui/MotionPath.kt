package com.alizeyn.site.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

/**
 * The motion-physics illustration from the M3 Expressive motion blog, live: a faint grid, a
 * damped spring curve drawn across the hero and a ball on it. The ball follows the cursor along
 * the curve on an expressive spring and settles back to the start when the cursor leaves.
 */
@Composable
fun MotionPathBackground(modifier: Modifier = Modifier, subtle: Boolean = false, content: @Composable () -> Unit) {
    val progress = remember { Animatable(0f) }
    var target by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(target) { progress.animateTo(target, spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessLow)) }

    val scheme = MaterialTheme.colorScheme
    // Background weight only: the copy sits on top of all of this.
    val gridColor = scheme.onSurface.copy(alpha = if (subtle) 0.05f else 0.08f)
    val pathColor = scheme.primary.copy(alpha = if (subtle) 0.14f else 0.22f)
    val ballColor = scheme.primary.copy(alpha = if (subtle) 0.55f else 0.85f)
    val strokeWidth = if (subtle) 4.dp else 6.dp
    val pulse = rememberInfiniteTransition(label = "ballPulse")
    val ring by pulse.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2400, easing = LinearEasing), RepeatMode.Restart),
        label = "ring",
    )

    Box(
        modifier.fillMaxWidth().pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    val pos = event.changes.firstOrNull()?.position ?: continue
                    when (event.type) {
                        PointerEventType.Move, PointerEventType.Enter, PointerEventType.Press -> {
                            target = pathT(pos.x, size.width.toFloat())
                        }
                        PointerEventType.Exit -> target = 0f
                        else -> Unit
                    }
                }
            }
        },
    ) {
        Canvas(Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height
            val thin = 1.dp.toPx()
            // Grid, 48dp cells, anchored to the curve's start so a line runs through the ball's rest point.
            val cell = 48.dp.toPx()
            val start = pathPoint(0f, w, h)
            var x = start.x % cell
            while (x < w) { drawLine(gridColor, Offset(x, 0f), Offset(x, h), thin); x += cell }
            var y = start.y % cell
            while (y < h) { drawLine(gridColor, Offset(0f, y), Offset(w, y), thin); y += cell }
            val path = Path()
            val steps = 240
            for (i in 0..steps) {
                val p = pathPoint(i / steps.toFloat(), w, h)
                if (i == 0) path.moveTo(p.x, p.y) else path.lineTo(p.x, p.y)
            }
            drawPath(path, pathColor, style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
            val ball = pathPoint(progress.value, w, h)
            drawCircle(ballColor.copy(alpha = ballColor.alpha * (1f - ring) * 0.35f), radius = 16.dp.toPx() + ring * 22.dp.toPx(), center = ball)
            drawCircle(ballColor, radius = 16.dp.toPx(), center = ball)
        }
        content()
    }
}

private const val X0 = 0.04f
private const val X1 = 0.985f

/** The curve parameter whose point sits under [x], clamped to the curve. */
private fun pathT(x: Float, w: Float): Float = ((x / w - X0) / (X1 - X0)).coerceIn(0f, 1f)

/** A point on the damped spring curve at [t] in 0..1, inside a [w] × [h] box. */
private fun pathPoint(t: Float, w: Float, h: Float): Offset {
    val yStart = h * 0.42f
    val yEnd = h * 0.88f
    val amplitude = h * 0.36f
    val x = w * (X0 + (X1 - X0) * t)
    val wobble = exp(-2.4f * t) * sin(2f * PI.toFloat() * 1.9f * t)
    val y = yStart + (yEnd - yStart) * t - amplitude * wobble
    return Offset(x, y)
}
