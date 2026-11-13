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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.hypot
import kotlin.math.sin

/**
 * The motion-physics illustration from the M3 Expressive motion blog, live: a damped spring
 * curve drawn across the hero with a ball at one end. Hover or tap the ball and it travels
 * the path on an expressive spring, then waits at the far end for the next nudge.
 */
@Composable
fun MotionPathBackground(modifier: Modifier = Modifier, subtle: Boolean = false, content: @Composable () -> Unit) {
    val progress = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val scheme = MaterialTheme.colorScheme
    // On phones the curve runs behind all the copy, so it steps back.
    val pathColor = scheme.primaryContainer.copy(alpha = if (subtle) 0.45f else 1f)
    val axisColor = scheme.outlineVariant.copy(alpha = if (subtle) 0.5f else 1f)
    val strokeWidth = if (subtle) 4.dp else 6.dp
    val ballColor = scheme.primary
    val pulse = rememberInfiniteTransition(label = "ballPulse")
    val ring by pulse.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2200, easing = LinearEasing), RepeatMode.Restart),
        label = "ring",
    )
    // Toggle the ball's end of the path on hover or tap.
    fun nudge() {
        val target = if (progress.targetValue < 0.5f) 1f else 0f
        scope.launch { progress.animateTo(target, spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessVeryLow)) }
    }

    Box(
        modifier.fillMaxWidth().pointerInput(Unit) {
            val radius = 18.dp.toPx() * 1.8f
            var inside = false
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    val pos = event.changes.firstOrNull()?.position ?: continue
                    val ball = pathPoint(progress.value, size.width.toFloat(), size.height.toFloat())
                    val near = hypot(pos.x - ball.x, pos.y - ball.y) < radius
                    when (event.type) {
                        PointerEventType.Move, PointerEventType.Enter -> {
                            if (near && !inside) nudge()
                            inside = near
                        }
                        PointerEventType.Press -> if (near) nudge()
                        PointerEventType.Exit -> inside = false
                        else -> Unit
                    }
                }
            }
        },
    ) {
        Canvas(Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height
            val start = pathPoint(0f, w, h)
            val end = pathPoint(1f, w, h)
            val thin = 1.dp.toPx()
            // Axis lines, as on the reference: one down from the start, one across at the start's height
            // and one at the resting height.
            drawLine(axisColor, Offset(start.x, start.y), Offset(start.x, h), thin)
            drawLine(axisColor, Offset(0f, start.y), Offset(w, start.y), thin)
            drawLine(axisColor, Offset(0f, end.y), Offset(w, end.y), thin)
            drawLine(axisColor, Offset(end.x, 0f), Offset(end.x, h), thin)
            val path = Path()
            val steps = 240
            for (i in 0..steps) {
                val p = pathPoint(i / steps.toFloat(), w, h)
                if (i == 0) path.moveTo(p.x, p.y) else path.lineTo(p.x, p.y)
            }
            drawPath(path, pathColor, style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
            val ball = pathPoint(progress.value, w, h)
            drawCircle(ballColor.copy(alpha = (1f - ring) * 0.35f), radius = 16.dp.toPx() + ring * 22.dp.toPx(), center = ball)
            drawCircle(ballColor, radius = 16.dp.toPx(), center = ball)
        }
        content()
    }
}

/** A point on the damped spring curve at [t] in 0..1, inside a [w] × [h] box. */
private fun pathPoint(t: Float, w: Float, h: Float): Offset {
    val x0 = w * 0.04f
    val x1 = w * 0.985f
    val yStart = h * 0.42f
    val yEnd = h * 0.88f
    val amplitude = h * 0.36f
    val x = x0 + (x1 - x0) * t
    val wobble = exp(-2.4f * t) * sin(2f * PI.toFloat() * 1.9f * t)
    val y = yStart + (yEnd - yStart) * t - amplitude * wobble
    return Offset(x, y)
}
