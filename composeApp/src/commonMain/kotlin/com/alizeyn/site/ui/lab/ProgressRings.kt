package com.alizeyn.site.ui.lab

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * A progress ring that follows a rounded rectangle: the rounded-rect analogue of a circular
 * progress. The outline starts at the top centre and winds clockwise. Sized by its container,
 * so it overlays a rounded element such as a video. From Zone5.
 */
@Composable
fun RoundedRectangleProgressBorder(
    progress: Float,
    cornerRadius: Dp,
    activeColor: Color,
    inactiveColor: Color,
    modifier: Modifier = Modifier,
    lineWidth: Dp = 6.dp,
) {
    Canvas(modifier = modifier) {
        val stroke = lineWidth.toPx()
        val inset = stroke / 2f
        val rect = Rect(inset, inset, size.width - inset, size.height - inset)
        val radius = maxOf(0f, minOf(cornerRadius.toPx() - inset, minOf(rect.width, rect.height) / 2f))
        val path = roundedRectRingPath(rect, radius)
        drawPath(path = path, color = inactiveColor, style = Stroke(width = stroke))
        val fraction = progress.coerceIn(0f, 1f)
        if (fraction > 0f) {
            val measure = PathMeasure().apply { setPath(path, forceClosed = false) }
            val active = Path()
            measure.getSegment(0f, measure.length * fraction, active, startWithMoveTo = true)
            drawPath(path = active, color = activeColor, style = Stroke(width = stroke, cap = StrokeCap.Round, join = StrokeJoin.Round))
        }
    }
}

// Traces the rounded rectangle from the top-centre point, winding clockwise.
private fun roundedRectRingPath(rect: Rect, radius: Float): Path = Path().apply {
    val l = rect.left
    val t = rect.top
    val r = rect.right
    val b = rect.bottom
    val d = radius * 2f
    moveTo(rect.center.x, t)
    lineTo(r - radius, t)
    arcTo(Rect(r - d, t, r, t + d), -90f, 90f, forceMoveTo = false)
    lineTo(r, b - radius)
    arcTo(Rect(r - d, b - d, r, b), 0f, 90f, forceMoveTo = false)
    lineTo(l + radius, b)
    arcTo(Rect(l, b - d, l + d, b), 90f, 90f, forceMoveTo = false)
    lineTo(l, t + radius)
    arcTo(Rect(l, t, l + d, t + d), 180f, 90f, forceMoveTo = false)
    lineTo(rect.center.x, t)
}

/** A filled disc with a check mark and a ring that fills over [progressDurationMillis], then loops. From Zone5. */
@Composable
fun CircularCheckmarkProgressBar(modifier: Modifier = Modifier, progressDurationMillis: Int = 3000) {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        while (true) {
            progress.snapTo(0f)
            progress.animateTo(1f, tween(progressDurationMillis, easing = LinearEasing))
            delay(700)
        }
    }
    val disc = MaterialTheme.colorScheme.primary
    val ring = MaterialTheme.colorScheme.onPrimary
    Box(contentAlignment = Alignment.Center, modifier = modifier.size(64.dp)) {
        Canvas(Modifier.size(64.dp)) {
            val strokeWidth = 4.dp.toPx()
            val gap = 4.dp.toPx()
            val radius = (size.minDimension - strokeWidth) / 2f - gap
            drawCircle(color = disc, radius = size.minDimension / 2f)
            drawArc(
                color = ring.copy(alpha = 0.6f),
                startAngle = -90f,
                sweepAngle = 360f * progress.value,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                topLeft = Offset((size.width - radius * 2) / 2f, (size.height - radius * 2) / 2f),
                size = Size(radius * 2, radius * 2),
            )
        }
        Icon(Icons.Filled.Check, contentDescription = null, tint = ring, modifier = Modifier.size(28.dp))
    }
}

/** A thick round-capped progress arc whose duration and delay scale with the value. From Pushups. */
@Composable
fun ProgressCircle(
    progress: Float,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    progressColor: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Dp = 14.dp,
    startAngle: Float = -90f,
) {
    val animated = remember { Animatable(0f) }
    LaunchedEffect(progress) {
        animated.animateTo(progress, tween(durationMillis = 500 + (progress * 500).toInt()))
    }
    Canvas(modifier.aspectRatio(1f).padding(strokeWidth / 2)) {
        val stroke = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
        drawArc(backgroundColor, startAngle, 360f, useCenter = false, style = stroke)
        drawArc(progressColor, startAngle, 360f * animated.value, useCenter = false, style = stroke)
    }
}
