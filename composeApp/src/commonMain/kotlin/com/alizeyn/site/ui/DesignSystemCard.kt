package com.alizeyn.site.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alizeyn.site.WidthClass
import com.alizeyn.site.theme.AndroidGreen

/**
 * A compact specimen of this page's design system: the six accent roles, the shape and type
 * scales, and a bugdroid you wave with the slider. Everything reads from [MaterialTheme], so it
 * cross-fades with the theme toggle.
 */
@Composable
fun DesignSystemCard(widthClass: WidthClass) {
    var wave by remember { mutableFloatStateOf(0.35f) }

    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        modifier = Modifier.fillMaxWidth(),
    ) {
        if (widthClass == WidthClass.Compact) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Eyebrow()
                Palette(showLabels = false)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        ShapeScale()
                        TypeRamp()
                    }
                    Bugdroid(wave = wave, size = 72.dp)
                }
                Slider(value = wave, onValueChange = { wave = it }, modifier = Modifier.fillMaxWidth())
            }
        } else {
            Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(20.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Eyebrow()
                    Palette(showLabels = widthClass == WidthClass.Expanded)
                    Row(horizontalArrangement = Arrangement.spacedBy(20.dp), verticalAlignment = Alignment.Bottom) {
                        ShapeScale()
                        TypeRamp()
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Bugdroid(wave = wave, size = 72.dp)
                    Slider(value = wave, onValueChange = { wave = it }, modifier = Modifier.width(140.dp))
                }
            }
        }
    }
}

@Composable
private fun Eyebrow() {
    Text("Design system · this page, live", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
}

@Composable
private fun Palette(showLabels: Boolean) {
    val c = MaterialTheme.colorScheme
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Swatch("primary", c.primary, c.onPrimary, showLabels, Modifier.weight(1f))
        Swatch("container", c.primaryContainer, c.onPrimaryContainer, showLabels, Modifier.weight(1f))
        Swatch("secondary", c.secondary, c.onSecondary, showLabels, Modifier.weight(1f))
        Swatch("container", c.secondaryContainer, c.onSecondaryContainer, showLabels, Modifier.weight(1f))
        Swatch("tertiary", c.tertiary, c.onTertiary, showLabels, Modifier.weight(1f))
        Swatch("container", c.tertiaryContainer, c.onTertiaryContainer, showLabels, Modifier.weight(1f))
    }
}

@Composable
private fun Swatch(role: String, color: Color, onColor: Color, showLabel: Boolean, modifier: Modifier) {
    BoxWithConstraints(modifier.height(30.dp).clip(MaterialTheme.shapes.small).background(color).padding(horizontal = 6.dp)) {
        if (showLabel && maxWidth >= 60.dp) {
            Text(role, style = MaterialTheme.typography.labelSmall, color = onColor, maxLines = 1, softWrap = false, overflow = TextOverflow.Clip, modifier = Modifier.align(Alignment.CenterStart))
        }
    }
}

@Composable
private fun ShapeScale() {
    val s = MaterialTheme.shapes
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.Bottom) {
        listOf(s.extraSmall, s.small, s.medium, s.large, s.extraLarge).forEach { shape ->
            Box(
                Modifier
                    .size(26.dp)
                    .clip(shape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .border(1.5.dp, MaterialTheme.colorScheme.primary, shape),
            )
        }
    }
}

@Composable
private fun TypeRamp() {
    val t = MaterialTheme.typography
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Bottom) {
        Text("Aa", style = t.headlineSmall, color = MaterialTheme.colorScheme.onSurface, softWrap = false)
        Text("Aa", style = t.titleMedium, color = MaterialTheme.colorScheme.onSurface, softWrap = false)
        Text("Aa", style = t.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, softWrap = false)
        Text("Aa", style = t.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, softWrap = false)
    }
}

/** The Android robot, drawn in a 100×100 unit box. [wave] raises the right arm from 0 (down) to 1 (up). */
@Composable
private fun Bugdroid(wave: Float, size: Dp) {
    val armAngle by animateFloatAsState(wave * 150f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow), label = "arm")
    val idle = rememberInfiniteTransition(label = "bob")
    val bob by idle.animateFloat(
        initialValue = -1.5f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(tween(2200), RepeatMode.Reverse),
        label = "bobY",
    )
    val eye = MaterialTheme.colorScheme.surfaceContainerLowest
    // A dark outline on every part keeps the waving arm legible when it crosses the body.
    val outline = if (MaterialTheme.colorScheme.background.luminance() < 0.5f) Color(0xFF071009) else Color(0xFF0E5E36)
    Canvas(Modifier.size(size)) {
        val u = this.size.minDimension / 100f
        translate(top = bob * u) {
            drawBugdroid(u, armAngle, eye, outline)
        }
    }
}

private fun DrawScope.drawBugdroid(u: Float, armAngle: Float, eye: Color, outline: Color) {
    val green = AndroidGreen
    val stroke = Stroke(width = 1.8f * u, join = StrokeJoin.Round)
    fun r(x: Float, y: Float, w: Float, h: Float) = Rect(x * u, y * u, (x + w) * u, (y + h) * u)
    fun part(path: Path) {
        drawPath(path, green)
        drawPath(path, outline, style = stroke)
    }
    fun capsule(topLeft: Offset, size: Size, radius: Float) {
        drawRoundRect(green, topLeft = topLeft, size = size, cornerRadius = CornerRadius(radius))
        drawRoundRect(outline, topLeft = topLeft, size = size, cornerRadius = CornerRadius(radius), style = stroke)
    }

    // Antennae: outline first, then the green core.
    for ((from, to) in listOf(Offset(37f, 19f) to Offset(30f, 9f), Offset(63f, 19f) to Offset(70f, 9f))) {
        drawLine(outline, from * u, to * u, strokeWidth = 3f * u + stroke.width * 2f, cap = StrokeCap.Round)
        drawLine(green, from * u, to * u, strokeWidth = 3f * u, cap = StrokeCap.Round)
    }
    // Head: a half disc.
    val head = Rect(26f * u, 14f * u, 74f * u, 62f * u)
    drawArc(green, startAngle = 180f, sweepAngle = 180f, useCenter = true, topLeft = head.topLeft, size = head.size)
    drawArc(outline, startAngle = 180f, sweepAngle = 180f, useCenter = true, topLeft = head.topLeft, size = head.size, style = stroke)
    // Eyes.
    drawCircle(eye, radius = 2.6f * u, center = Offset(41f * u, 29f * u))
    drawCircle(eye, radius = 2.6f * u, center = Offset(59f * u, 29f * u))
    // Body, rounded only at the bottom, then legs.
    part(bottomRounded(r(26f, 41f, 48f, 37f), 8f * u))
    part(bottomRounded(r(36f, 76f, 8f, 16f), 4f * u))
    part(bottomRounded(r(56f, 76f, 8f, 16f), 4f * u))
    // Left arm, resting.
    capsule(Offset(16f * u, 41f * u), Size(8f * u, 30f * u), 4f * u)
    // Right arm, pivoting at its shoulder, drawn last so it passes over the body.
    rotate(degrees = armAngle, pivot = Offset(80f * u, 45f * u)) {
        capsule(Offset(76f * u, 41f * u), Size(8f * u, 30f * u), 4f * u)
    }
}

private fun bottomRounded(rect: Rect, radius: Float): Path = Path().apply {
    addRoundRect(
        RoundRect(
            rect,
            topLeft = CornerRadius.Zero,
            topRight = CornerRadius.Zero,
            bottomRight = CornerRadius(radius),
            bottomLeft = CornerRadius(radius),
        ),
    )
}
