package com.alizeyn.site.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alizeyn.site.theme.AndroidGreen
import kotlinx.coroutines.delay

/**
 * Entrance motion: the content fades in and rises (or, with [pop], springs up from 85% scale)
 * the first time it scrolls into view. [delayMillis] staggers siblings.
 */
@Composable
fun Reveal(
    modifier: Modifier = Modifier,
    delayMillis: Int = 0,
    pop: Boolean = false,
    content: @Composable BoxScope.() -> Unit,
) {
    var revealed by remember { mutableStateOf(false) }
    val windowHeight = LocalWindowInfo.current.containerSize.height
    val progress = remember { Animatable(0f) }

    LaunchedEffect(revealed) {
        if (revealed) {
            delay(delayMillis.toLong())
            progress.animateTo(
                targetValue = 1f,
                animationSpec = if (pop) {
                    spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
                } else {
                    tween(durationMillis = 650, easing = FastOutSlowInEasing)
                },
            )
        }
    }

    Box(
        modifier
            .onGloballyPositioned { coordinates ->
                if (!revealed && (windowHeight == 0 || coordinates.positionInRoot().y < windowHeight * 0.92f)) revealed = true
            }
            .graphicsLayer {
                val p = progress.value
                alpha = p.coerceIn(0f, 1f)
                if (pop) {
                    scaleX = 0.85f + 0.15f * p
                    scaleY = scaleX
                } else {
                    translationY = (1f - p) * 28.dp.toPx()
                }
            },
        content = content,
    )
}

/** Pointer hover lifts the element slightly: a small scale and rise on a soft spring. */
@Composable
fun Modifier.hoverLift(scale: Float = 1.015f, lift: Dp = 3.dp): Modifier {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    val s by animateFloatAsState(if (hovered) scale else 1f, spring(stiffness = Spring.StiffnessMediumLow), label = "hoverScale")
    val y by animateDpAsState(if (hovered) -lift else 0.dp, spring(stiffness = Spring.StiffnessMediumLow), label = "hoverLift")
    return this
        .hoverable(interaction)
        .graphicsLayer {
            scaleX = s
            scaleY = s
            translationY = y.toPx()
        }
}

/** A soft highlight that sweeps across the content every few seconds. */
@Composable
fun Modifier.sheen(color: Color = Color.White, strength: Float = 0.28f): Modifier {
    val transition = rememberInfiniteTransition(label = "sheen")
    val x by transition.animateFloat(
        initialValue = -0.6f,
        targetValue = 1.6f,
        animationSpec = infiniteRepeatable(tween(durationMillis = 2600, delayMillis = 2400, easing = FastOutSlowInEasing)),
        label = "sheenX",
    )
    return drawWithContent {
        drawContent()
        val w = size.width
        drawRect(
            Brush.linearGradient(
                colors = listOf(Color.Transparent, color.copy(alpha = strength), Color.Transparent),
                start = Offset(x * w - w * 0.25f, 0f),
                end = Offset(x * w + w * 0.25f, size.height),
            ),
        )
    }
}

/** A slowly turning, blurred green-to-teal halo behind [content]. */
@Composable
fun Glow(modifier: Modifier = Modifier, cornerRadius: Dp = 28.dp, content: @Composable BoxScope.() -> Unit) {
    val transition = rememberInfiniteTransition(label = "glow")
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(durationMillis = 12_000, easing = LinearEasing)),
        label = "glowAngle",
    )
    val tertiary = MaterialTheme.colorScheme.tertiary
    val primary = MaterialTheme.colorScheme.primary
    Box(modifier, contentAlignment = Alignment.Center) {
        Box(
            Modifier
                .matchParentSize()
                .padding(6.dp)
                .blur(26.dp, BlurredEdgeTreatment.Unbounded)
                .drawBehind {
                    rotate(angle) {
                        drawRoundRect(
                            brush = Brush.sweepGradient(listOf(AndroidGreen, tertiary, primary, AndroidGreen)),
                            cornerRadius = CornerRadius(cornerRadius.toPx()),
                            alpha = 0.85f,
                        )
                    }
                },
        )
        content()
    }
}

/** A gradient brush that drifts back and forth, for display text that should catch the light. */
@Composable
fun shimmerBrush(): Brush {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val shift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(durationMillis = 4500, easing = LinearEasing), RepeatMode.Reverse),
        label = "shimmerShift",
    )
    val primary = MaterialTheme.colorScheme.primary
    val tertiary = MaterialTheme.colorScheme.tertiary
    val x = shift * 500f
    return Brush.linearGradient(
        colors = listOf(primary, AndroidGreen, tertiary, primary),
        start = Offset(x - 250f, 0f),
        end = Offset(x + 450f, 160f),
    )
}
