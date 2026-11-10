package com.alizeyn.site.ui.lab

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alizeyn.site.theme.AndroidGreen
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random

// Two Zone5 workout effects: the expanding impact ring that lands at the athlete's feet, and the
// "+" particle that pops on every counted rep. Here a tap stands in for the pose detector.

private const val RING_DURATION_MS = 800
private const val PARTICLE_DURATION_MS = 800

private class Ring(val center: Offset) {
    val progress = Animatable(0f)
}

private class RepParticle(val id: Int, val origin: Offset, val driftAngle: Float, val driftDistance: Float) {
    val progress = Animatable(0f)
}

@Composable
fun ImpactBurstDemo(modifier: Modifier = Modifier) {
    val rings = remember { mutableStateListOf<Ring>() }
    val particles = remember { mutableStateListOf<RepParticle>() }
    var reps by remember { mutableIntStateOf(0) }
    var nextId by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()

    Box(
        modifier.pointerInput(Unit) {
            detectTapGestures { position ->
                reps++
                val ring = Ring(position)
                rings += ring
                val particle = RepParticle(nextId++, position, Random.nextFloat() * 360f, 60f + Random.nextFloat() * 50f)
                particles += particle
                scope.launch {
                    ring.progress.animateTo(1f, tween(RING_DURATION_MS, easing = LinearEasing))
                    rings -= ring
                }
                scope.launch {
                    particle.progress.animateTo(1f, tween(PARTICLE_DURATION_MS, easing = LinearEasing))
                    particles -= particle
                }
            }
        },
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val maxRadius = size.minDimension * 1.1f
            rings.forEach { ring ->
                drawImpactRing(ring.center, ring.progress.value, startRadius = 22.dp.toPx(), maxRadius = maxRadius, color = Color.White)
            }
        }
        particles.forEach { particle -> RepParticleEffect(particle) }
        Column(Modifier.align(Alignment.TopStart).padding(16.dp)) {
            Text("$reps", style = MaterialTheme.typography.displayMedium, color = AndroidGreen, fontWeight = FontWeight.Bold)
            Text("reps", style = MaterialTheme.typography.labelLarge, color = Color.White.copy(alpha = 0.7f))
        }
        if (reps == 0) {
            Text(
                "Tap anywhere to land a rep",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}

/** One expanding, foreshortened ring with the near edge opaque and the far edge transparent. */
private fun DrawScope.drawImpactRing(center: Offset, progress: Float, startRadius: Float, maxRadius: Float, color: Color) {
    if (progress <= 0f) return
    val r = startRadius + (maxRadius - startRadius) * progress
    val squash = 0.38f
    val thickness = (r * 0.16f * (1f - progress)).coerceAtLeast(2f)
    val ri = (r - thickness).coerceAtLeast(0f)
    val outer = Rect(center.x - r, center.y - r * squash, center.x + r, center.y + r * squash)
    val inner = Rect(center.x - ri, center.y - ri * squash, center.x + ri, center.y + ri * squash)
    val path = Path().apply {
        fillType = PathFillType.EvenOdd
        addOval(outer)
        addOval(inner)
    }
    drawPath(
        path,
        Brush.verticalGradient(
            colors = listOf(Color.Transparent, color.copy(alpha = 0.6f * (1f - progress * 0.6f))),
            startY = outer.top,
            endY = outer.bottom,
        ),
    )
}

@Composable
private fun BoxScope.RepParticleEffect(particle: RepParticle) {
    val p = particle.progress.value
    val angle = particle.driftAngle * PI.toFloat() / 180f
    val dx = cos(angle) * particle.driftDistance * p
    val dy = sin(angle) * particle.driftDistance * p - 30f * p
    // Pop in, settle, fade out.
    val scale = when {
        p < 0.15f -> 0.5f + (p / 0.15f) * 0.7f
        p < 0.3f -> 1.2f - ((p - 0.15f) / 0.15f) * 0.2f
        else -> 1.0f - ((p - 0.3f) / 0.7f) * 0.5f
    }
    val alpha = when {
        p < 0.15f -> p / 0.15f
        p < 0.5f -> 1f
        else -> 1f - (p - 0.5f) / 0.5f
    }
    Text(
        "+",
        fontSize = 72.sp,
        fontWeight = FontWeight.Black,
        color = AndroidGreen,
        modifier = Modifier
            .offset {
                IntOffset(
                    (particle.origin.x + dx.dp.toPx() - 22.dp.toPx()).roundToInt(),
                    (particle.origin.y + dy.dp.toPx() - 52.dp.toPx()).roundToInt(),
                )
            }
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
            },
    )
}
