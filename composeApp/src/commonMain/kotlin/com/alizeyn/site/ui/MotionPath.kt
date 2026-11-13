package com.alizeyn.site.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.withFrameNanos
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
 * damped spring curve drawn across the hero and a ball on it. Move the cursor anywhere near
 * the curve and the ball springs to the closest point on it; it settles back to the start when
 * the cursor leaves. The ball is driven by a small per-frame spring simulation rather than a
 * restarted animation, so a stream of pointer moves never makes it stutter.
 */
@Composable
fun MotionPathBackground(modifier: Modifier = Modifier, subtle: Boolean = false, content: @Composable () -> Unit) {
    var target by remember { mutableFloatStateOf(0f) }
    var position by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(Unit) {
        var velocity = 0f
        var last = 0L
        // Damping ratio 0.6 (a little bounce), natural frequency about 1.4 Hz.
        val omega = 2f * PI.toFloat() * 1.4f
        val zeta = 0.6f
        while (true) {
            withFrameNanos { now ->
                val dt = if (last == 0L) 0f else ((now - last) / 1e9f).coerceAtMost(0.04f)
                last = now
                val accel = omega * omega * (target - position) - 2f * zeta * omega * velocity
                velocity += accel * dt
                position += velocity * dt
            }
        }
    }

    val scheme = MaterialTheme.colorScheme
    // Background weight only, in the warm tertiary so the hero is not all green.
    val gridColor = scheme.onSurface.copy(alpha = if (subtle) 0.05f else 0.08f)
    val pathColor = scheme.tertiary.copy(alpha = if (subtle) 0.18f else 0.3f)
    val ballColor = scheme.tertiary.copy(alpha = if (subtle) 0.5f else 0.7f)
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
                            target = nearestT(pos, size.width.toFloat(), size.height.toFloat())
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
            val ball = pathPoint(position.coerceIn(-0.02f, 1.02f), w, h)
            drawCircle(ballColor.copy(alpha = ballColor.alpha * (1f - ring) * 0.35f), radius = 16.dp.toPx() + ring * 22.dp.toPx(), center = ball)
            drawCircle(ballColor, radius = 16.dp.toPx(), center = ball)
        }
        content()
    }
}

private const val X0 = 0.04f
private const val X1 = 0.985f

/** The curve parameter of the point closest to [pos], by sampling. */
private fun nearestT(pos: Offset, w: Float, h: Float): Float {
    var best = 0f
    var bestDistance = Float.MAX_VALUE
    val samples = 120
    for (i in 0..samples) {
        val t = i / samples.toFloat()
        val p = pathPoint(t, w, h)
        val d = (p.x - pos.x) * (p.x - pos.x) + (p.y - pos.y) * (p.y - pos.y)
        if (d < bestDistance) { bestDistance = d; best = t }
    }
    return best
}

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
