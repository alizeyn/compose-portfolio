package com.alizeyn.site.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alizeyn.site.WidthClass
import com.alizeyn.site.resources.Res
import com.alizeyn.site.resources.ic_language
import com.alizeyn.site.resources.ic_phone_iphone
import com.alizeyn.site.theme.AndroidGreen
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

// "Ali ♥ [ iOS | Figma | Web ]": the bracket cycles through the three platforms and the whole
// stage restyles itself for each one. iOS gets glass over soft colour; Figma gets the dark canvas,
// dot grid and multiplayer cursors; Web keeps this site's own Material 3 Expressive look.

private const val DWELL_MS = 4500L

enum class Platform(val label: String, val headline: String, val body: String, val skills: List<String>) {
    Ios(
        label = "iOS",
        headline = "Two platforms, one product.",
        body = "SwiftUI and UIKit alongside the Android work, keeping screens, motion and copy in step across both apps.",
        skills = listOf("Swift", "SwiftUI", "UIKit", "Xcode", "Human Interface Guidelines", "Platform parity"),
    ),
    Figma(
        label = "Figma",
        headline = "Design files I can read and write.",
        body = "Components, variants and variables built the way engineers consume them, and prototypes that settle motion questions before code.",
        skills = listOf("Auto layout", "Components & variants", "Variables & tokens", "Prototyping", "Dev Mode", "Code Connect"),
    ),
    Web(
        label = "Web",
        headline = "This page, for instance.",
        body = "Compose Multiplatform compiled to WebAssembly, plus the plain HTML, CSS and TypeScript it took to build my previous site.",
        skills = listOf("Compose Multiplatform", "Kotlin/Wasm", "Material 3 Expressive", "Astro", "HTML & CSS", "TypeScript"),
    ),
}

private class Look(
    val bgTop: Color,
    val bgBottom: Color,
    val onBg: Color,
    val muted: Color,
    val accent: Color,
    val cardRadius: Dp,
    val cardFill: Color,
    val cardBorder: Color,
    val chipRadius: Dp,
    val chipFill: Color,
    val onChip: Color,
    val family: FontFamily,
)

@Composable
private fun lookFor(platform: Platform): Look {
    val scheme = MaterialTheme.colorScheme
    val dark = scheme.background.luminance() < 0.5f
    return when (platform) {
        Platform.Ios -> if (dark) Look(
            bgTop = Color(0xFF000000), bgBottom = Color(0xFF1C1C1E), onBg = Color(0xFFF2F2F7), muted = Color(0xFF98989F), accent = Color(0xFF0A84FF),
            cardRadius = 28.dp, cardFill = Color.White.copy(alpha = 0.10f), cardBorder = Color.White.copy(alpha = 0.22f),
            chipRadius = 20.dp, chipFill = Color.White.copy(alpha = 0.14f), onChip = Color(0xFFF2F2F7), family = FontFamily.SansSerif,
        ) else Look(
            bgTop = Color(0xFFF2F2F7), bgBottom = Color(0xFFE5E5EA), onBg = Color(0xFF1C1C1E), muted = Color(0xFF6E6E73), accent = Color(0xFF007AFF),
            cardRadius = 28.dp, cardFill = Color.White.copy(alpha = 0.55f), cardBorder = Color.White.copy(alpha = 0.85f),
            chipRadius = 20.dp, chipFill = Color.White.copy(alpha = 0.7f), onChip = Color(0xFF1C1C1E), family = FontFamily.SansSerif,
        )
        Platform.Figma -> if (dark) Look(
            bgTop = Color(0xFF1E1E1E), bgBottom = Color(0xFF2C2C2C), onBg = Color(0xFFFFFFFF), muted = Color(0xFFB3B3B3), accent = Color(0xFF0D99FF),
            cardRadius = 8.dp, cardFill = Color(0xFF2C2C2C), cardBorder = Color(0xFF444444),
            chipRadius = 4.dp, chipFill = Color(0xFF383838), onChip = Color(0xFFE6E6E6), family = FontFamily.Monospace,
        ) else Look(
            // Figma's light UI: the pale canvas, white panels, hairline borders.
            bgTop = Color(0xFFF5F5F5), bgBottom = Color(0xFFE9E9E9), onBg = Color(0xFF1E1E1E), muted = Color(0xFF6F6F6F), accent = Color(0xFF0D99FF),
            cardRadius = 8.dp, cardFill = Color(0xFFFFFFFF), cardBorder = Color(0xFFE0E0E0),
            chipRadius = 4.dp, chipFill = Color(0xFFF0F0F0), onChip = Color(0xFF2C2C2C), family = FontFamily.Monospace,
        )
        Platform.Web -> Look(
            bgTop = scheme.background, bgBottom = scheme.background, onBg = scheme.onBackground, muted = scheme.onSurfaceVariant, accent = scheme.primary,
            cardRadius = 28.dp, cardFill = scheme.surfaceContainerLowest, cardBorder = Color.Transparent,
            chipRadius = 8.dp, chipFill = scheme.secondaryContainer, onChip = scheme.onSecondaryContainer, family = FontFamily.Default,
        )
    }
}

@Composable
fun BeyondScreen(widthClass: WidthClass) {
    var index by remember { mutableIntStateOf(0) }
    var lastTap by remember { mutableLongStateOf(0L) }
    val progress = remember { Animatable(0f) }
    val platform = Platform.entries[index]

    // Auto-advance; a tap restarts the dwell for the chosen platform.
    LaunchedEffect(index, lastTap) {
        progress.snapTo(0f)
        progress.animateTo(1f, tween(DWELL_MS.toInt(), easing = LinearEasing))
        index = (index + 1) % Platform.entries.size
    }

    val look = lookFor(platform)
    val spec = tween<Color>(700)
    val bgTop by animateColorAsState(look.bgTop, spec, label = "bgTop")
    val bgBottom by animateColorAsState(look.bgBottom, spec, label = "bgBottom")
    val onBg by animateColorAsState(look.onBg, spec, label = "onBg")
    val accent by animateColorAsState(look.accent, spec, label = "accent")

    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(bgTop, bgBottom)))
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                index = (index + 1) % Platform.entries.size
                lastTap++
            },
    ) {
        Decor(platform, compact = widthClass == WidthClass.Compact, Modifier.fillMaxSize())
        // Fixed slots keep "Ali ♥ [" and the pills exactly where they are while the content changes.
        val compact = widthClass == WidthClass.Compact
        Column(
            Modifier.fillMaxSize().padding(horizontal = widthClass.margin(), vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Headline(platform, onBg, compact = compact)
            Spacer(Modifier.height(28.dp))
            Box(Modifier.widthIn(max = 560.dp).fillMaxWidth().height(if (compact) 390.dp else 290.dp), contentAlignment = Alignment.TopCenter) {
                PlatformCard(platform, look, Modifier.fillMaxWidth())
            }
            Spacer(Modifier.height(24.dp))
            Selector(index, progress.value, accent, onBg) { i -> index = i; lastTap++ }
        }
    }
}

private val HeartRed = Color(0xFFE5393E)

@Composable
private fun Headline(platform: Platform, onBg: Color, compact: Boolean) {
    val style = if (compact) MaterialTheme.typography.displayMedium else MaterialTheme.typography.displayLarge
    val iconSize = if (compact) 30.dp else 44.dp
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(if (compact) 8.dp else 14.dp)) {
        Text("Ali", style = style, fontWeight = FontWeight.SemiBold, color = onBg)
        Icon(
            Icons.Filled.Favorite, contentDescription = "loves", tint = HeartRed,
            modifier = Modifier.size(iconSize),
        )
        Text("[", style = style, color = onBg.copy(alpha = 0.45f))
        // The slot is as wide as the longest platform word, so the brackets never move.
        Box(Modifier.width(if (compact) 172.dp else 232.dp), contentAlignment = Alignment.Center) {
            AnimatedContent(
                targetState = platform,
                transitionSpec = {
                    (slideInVertically { it / 2 } + fadeIn()) togetherWith (slideOutVertically { -it / 2 } + fadeOut())
                },
                label = "platform",
            ) { p ->
                val accent = lookFor(p).accent
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    PlatformIcon(p, accent, size = iconSize)
                    Text(p.label, style = style, fontWeight = FontWeight.Bold, color = accent)
                }
            }
        }
        Text("]", style = style, color = onBg.copy(alpha = 0.45f))
    }
}

@Composable
private fun PlatformIcon(platform: Platform, tint: Color, size: Dp) {
    when (platform) {
        Platform.Ios -> Icon(painterResource(Res.drawable.ic_phone_iphone), contentDescription = null, tint = tint, modifier = Modifier.size(size))
        Platform.Web -> Icon(painterResource(Res.drawable.ic_language), contentDescription = null, tint = tint, modifier = Modifier.size(size))
        Platform.Figma -> Canvas(Modifier.size(size * 0.75f, size)) { drawFigmaMark() }
    }
}

/** The five-piece Figma mark in brand colours, laid out on a 2 × 3 grid. */
private fun DrawScope.drawFigmaMark() {
    val u = size.width / 2f
    val r = CornerRadius(u / 2f)
    fun piece(col: Int, row: Int, color: Color, tl: Boolean, tr: Boolean, br: Boolean, bl: Boolean) {
        val rect = Rect(col * u, row * u, (col + 1) * u, (row + 1) * u)
        drawPath(
            Path().apply {
                addRoundRect(
                    RoundRect(
                        rect,
                        topLeft = if (tl) r else CornerRadius.Zero, topRight = if (tr) r else CornerRadius.Zero,
                        bottomRight = if (br) r else CornerRadius.Zero, bottomLeft = if (bl) r else CornerRadius.Zero,
                    ),
                )
            },
            color,
        )
    }
    piece(0, 0, Color(0xFFF24E1E), tl = true, tr = false, br = false, bl = true)
    piece(1, 0, Color(0xFFFF7262), tl = false, tr = true, br = true, bl = false)
    piece(0, 1, Color(0xFFA259FF), tl = true, tr = false, br = false, bl = true)
    piece(1, 1, Color(0xFF1ABCFE), tl = true, tr = true, br = true, bl = true)
    piece(0, 2, Color(0xFF0ACF83), tl = true, tr = false, br = true, bl = true)
}

@Composable
private fun PlatformCard(platform: Platform, look: Look, modifier: Modifier = Modifier) {
    AnimatedContent(
        targetState = platform,
        transitionSpec = { (fadeIn(tween(450)) + scaleIn(initialScale = 0.96f)) togetherWith fadeOut(tween(250)) },
        label = "card",
        modifier = modifier,
    ) { p ->
        val l = lookFor(p)
        when (p) {
            Platform.Web -> ElevatedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(p.headline, style = MaterialTheme.typography.headlineSmallEmphasized, color = MaterialTheme.colorScheme.onSurface)
                    Text(p.body, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        p.skills.forEach { SuggestionChip(onClick = {}, label = { Text(it) }) }
                    }
                }
            }
            else -> {
                val shape = RoundedCornerShape(l.cardRadius)
                val figma = p == Platform.Figma
                Box(
                    Modifier
                        .fillMaxWidth()
                        .then(if (figma) Modifier.selectionHandles(l.accent) else Modifier)
                        .clip(shape)
                        .background(l.cardFill)
                        .border(1.dp, l.cardBorder, shape)
                        .padding(24.dp),
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            p.headline,
                            style = MaterialTheme.typography.headlineSmall.copy(fontFamily = l.family, fontWeight = FontWeight.SemiBold, letterSpacing = if (figma) 0.sp else (-0.3).sp),
                            color = l.onBg,
                        )
                        Text(p.body, style = MaterialTheme.typography.bodyLarge.copy(fontFamily = l.family), color = l.muted)
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            p.skills.forEach { skill ->
                                Text(
                                    if (figma) "◇ $skill" else skill,
                                    style = MaterialTheme.typography.labelLarge.copy(fontFamily = l.family),
                                    color = l.onChip,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(l.chipRadius))
                                        .background(l.chipFill)
                                        .padding(horizontal = 12.dp, vertical = 7.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/** Figma's selection chrome: a 1px accent outline with square handles at the corners, drawn outside the clip. */
private fun Modifier.selectionHandles(color: Color): Modifier = drawBehind {
    val inset = 1.dp.toPx()
    drawRect(color, topLeft = Offset(-inset, -inset), size = Size(size.width + 2 * inset, size.height + 2 * inset), style = Stroke(1.5.dp.toPx()))
    val h = 7.dp.toPx()
    listOf(Offset(0f, 0f), Offset(size.width, 0f), Offset(0f, size.height), Offset(size.width, size.height)).forEach { c ->
        drawRect(Color.White, topLeft = Offset(c.x - h / 2, c.y - h / 2), size = Size(h, h))
        drawRect(color, topLeft = Offset(c.x - h / 2, c.y - h / 2), size = Size(h, h), style = Stroke(1.5.dp.toPx()))
    }
}

@Composable
private fun Selector(index: Int, progress: Float, accent: Color, onBg: Color, onSelect: (Int) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Platform.entries.forEachIndexed { i, p ->
            val active = i == index
            val width by animateDpAsState(if (active) 44.dp else 10.dp, spring(stiffness = Spring.StiffnessMediumLow), label = "pill")
            Box(
                Modifier
                    .width(width)
                    .height(10.dp)
                    .clip(CircleShape)
                    .background(onBg.copy(alpha = 0.18f))
                    .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onSelect(i) },
            ) {
                if (active) Box(Modifier.fillMaxSize().clip(CircleShape).background(accent).graphicsLayer { scaleX = progress; transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 0.5f) })
            }
        }
    }
}

/** Per-platform background decoration, cross-faded as the platform changes. */
@Composable
private fun Decor(platform: Platform, compact: Boolean, modifier: Modifier) {
    AnimatedContent(platform, transitionSpec = { fadeIn(tween(900)) togetherWith fadeOut(tween(600)) }, label = "decor", modifier = modifier) { p ->
        Box(Modifier.fillMaxSize()) {
            when (p) {
                Platform.Ios -> GlassBlobs()
                Platform.Figma -> FigmaCanvas()
                Platform.Web -> MaterialFloaters(compact)
            }
        }
    }
}

@Composable
private fun BoxScope.GlassBlobs() {
    val t by rememberInfiniteTransition(label = "blobs").animateFloat(0f, 1f, infiniteRepeatable(tween(14000, easing = LinearEasing)), label = "t")
    val a = t * 2f * 3.1416f
    val blobs = listOf(
        Triple(Color(0xFF5AC8FA), Alignment.TopStart, Offset(cos(a), sin(a))),
        Triple(Color(0xFFAF52DE), Alignment.CenterEnd, Offset(sin(a * 0.7f), cos(a * 0.9f))),
        Triple(Color(0xFFFF9500), Alignment.BottomStart, Offset(cos(a * 0.6f + 1f), sin(a * 0.8f))),
    )
    blobs.forEach { (color, align, drift) ->
        Box(
            Modifier
                .align(align)
                .offset { IntOffset((drift.x * 40.dp.toPx()).roundToInt(), (drift.y * 40.dp.toPx()).roundToInt()) }
                .size(320.dp)
                .blur(80.dp, BlurredEdgeTreatment.Unbounded)
                .background(color.copy(alpha = 0.55f), CircleShape),
        )
    }
}

@Composable
private fun BoxScope.FigmaCanvas() {
    val dark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val dot = if (dark) Color.White.copy(alpha = 0.10f) else Color.Black.copy(alpha = 0.10f)
    Canvas(Modifier.fillMaxSize()) {
        val step = 24.dp.toPx()
        var y = step / 2
        while (y < size.height) {
            var x = step / 2
            while (x < size.width) { drawCircle(dot, radius = 1.2.dp.toPx(), center = Offset(x, y)); x += step }
            y += step
        }
    }
    val t by rememberInfiniteTransition(label = "cursors").animateFloat(0f, 1f, infiniteRepeatable(tween(9000, easing = LinearEasing)), label = "t")
    val a = t * 2f * 3.1416f
    // Kept to the corners so they never sit on the headline or the pills.
    Cursor("Ali", Color(0xFF0ACF83), Alignment.TopEnd, Offset(-96f + 28f * cos(a), 40f + 22f * sin(a * 1.3f)))
    Cursor("Designer", Color(0xFFA259FF), Alignment.BottomStart, Offset(40f + 26f * sin(a), -72f + 18f * cos(a * 0.8f)))
}

@Composable
private fun BoxScope.Cursor(name: String, color: Color, align: Alignment, offset: Offset) {
    Row(
        Modifier.align(align).offset { IntOffset(offset.x.dp.roundToPx(), offset.y.dp.roundToPx()) },
        verticalAlignment = Alignment.Top,
    ) {
        Canvas(Modifier.size(20.dp)) {
            // The classic pointer: tip at the top-left, barb to the lower right.
            val u = size.width / 24f
            val p = Path().apply {
                moveTo(4f * u, 2f * u); lineTo(4f * u, 20f * u); lineTo(8.6f * u, 15.8f * u); lineTo(12.2f * u, 22f * u)
                lineTo(15f * u, 20.6f * u); lineTo(11.5f * u, 14.6f * u); lineTo(18f * u, 14.6f * u); close()
            }
            drawPath(p, color); drawPath(p, Color.White, style = Stroke(1.5f * u))
        }
        Text(
            name,
            style = MaterialTheme.typography.labelMedium.copy(fontFamily = FontFamily.Monospace),
            color = Color.White,
            modifier = Modifier.offset(x = 6.dp, y = 18.dp).clip(RoundedCornerShape(4.dp)).background(color).padding(horizontal = 8.dp, vertical = 3.dp),
        )
    }
}

@Composable
private fun BoxScope.MaterialFloaters(compact: Boolean) {
    val spin by rememberInfiniteTransition(label = "floaters").animateFloat(0f, 360f, infiniteRepeatable(tween(40000, easing = LinearEasing)), label = "spin")
    val shapes = listOf(MaterialShapes.Sunny, MaterialShapes.Cookie9Sided, MaterialShapes.Clover8Leaf).map { it.toShape() }
    val tints = listOf(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.tertiaryContainer, AndroidGreen.copy(alpha = 0.5f))
    val aligns = listOf(Alignment.TopEnd, Alignment.BottomStart, Alignment.CenterStart)
    shapes.forEachIndexed { i, shape ->
        Box(
            Modifier
                .align(aligns[i])
                .padding(if (compact) 12.dp else 32.dp)
                .size(if (compact) (if (i == 0) 96.dp else 72.dp) else (if (i == 0) 180.dp else 120.dp))
                .graphicsLayer { rotationZ = spin * (if (i % 2 == 0) 1f else -1f); alpha = if (compact) 0.5f else 0.7f }
                .clip(shape)
                .background(tints[i]),
        )
    }
}
