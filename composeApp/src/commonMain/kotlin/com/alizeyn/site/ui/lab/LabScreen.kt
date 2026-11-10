package com.alizeyn.site.ui.lab

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alizeyn.site.WidthClass
import com.alizeyn.site.theme.AndroidGreen
import com.alizeyn.site.ui.Page
import com.alizeyn.site.ui.Reveal
import com.alizeyn.site.ui.SectionHeader
import com.alizeyn.site.ui.hoverLift
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private class LabDemo(
    val title: String,
    val app: String,
    val techniques: List<String>,
    val body: String,
    val content: @Composable () -> Unit,
)

private val demos = listOf(
    LabDemo(
        title = "Segmented control",
        app = "Pushups",
        techniques = listOf("Layout", "pointerInput", "animate*AsState", "semantics"),
        body = "An iOS-style control with a thumb that slides between segments. A custom Layout measures the " +
            "segments first and sizes the thumb from them; raw pointer input handles press, drag-to-switch and " +
            "cancel, so it never fights the scroll container.",
    ) { SegmentedDemo() },
    LabDemo(
        title = "Progress, three ways",
        app = "Zone5 · Pushups",
        techniques = listOf("PathMeasure", "Animatable", "drawArc"),
        body = "A rounded-rectangle progress border that trims a path with PathMeasure so it can sit on a video, " +
            "the thick arc from the Pushups stats, and the countdown disc that gates a workout step.",
    ) { ProgressDemo() },
    LabDemo(
        title = "The dot",
        app = "Zone5",
        techniques = listOf("DrawScope", "radialGradient", "scale"),
        body = "Every target circle, floor puck and placement cue in Zone5 draws through this one function. Ring, " +
            "shadow and halo are ratios of the radius, so the mark looks the same on a phone, a tablet and an " +
            "exported video. Drawn round, then squashed onto the floor plane.",
    ) { DotMarkDemo() },
    LabDemo(
        title = "Neon hoop",
        app = "Zone5",
        techniques = listOf("saveLayer", "BlendMode.DstOut", "BlendMode.Screen"),
        body = "The hula-hoop ring is stroked in neon passes, wide and faint to a white core. The far half is drawn " +
            "into its own layer and the torso is punched out of it, so the hoop reads as passing behind the body.",
    ) { HoopDemo() },
    LabDemo(
        title = "Impact and rep burst",
        app = "Zone5",
        techniques = listOf("detectTapGestures", "Animatable", "graphicsLayer"),
        body = "Landing spawns a foreshortened ring that races to the edge of the screen, and each counted rep pops " +
            "a \"+\" that drifts and fades. Fast jumping stacks several at once.",
    ) { ImpactDemo() },
    LabDemo(
        title = "Inline Markdown",
        app = "Pushups",
        techniques = listOf("AnnotatedString", "LinkAnnotation", "CommonMark flanking"),
        body = "A small pure-Kotlin parser for the Markdown subset our copy actually uses: bold, italic, links and " +
            "escapes. It follows the CommonMark flanking rules, so a footnote star stays a star.",
    ) { MarkdownDemo() },
)

@Composable
fun LabScreen(widthClass: WidthClass) {
    Page(widthClass) {
        Reveal { SectionHeader("Lab", "Components from the apps I ship") }
        Reveal(delayMillis = 80) {
            Text(
                "Pieces of the Zone5 and Pushups Android apps, ported to Compose Multiplatform and running live " +
                    "in this page. Every one is interactive.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        demos.forEachIndexed { i, demo -> Reveal(delayMillis = (i % 2) * 70) { LabCard(demo, widthClass) } }
    }
}

@Composable
private fun LabCard(demo: LabDemo, widthClass: WidthClass) {
    ElevatedCard(Modifier.fillMaxWidth().hoverLift(scale = 1.006f, lift = 2.dp)) {
        if (widthClass == WidthClass.Expanded) {
            Row(Modifier.padding(20.dp), horizontalArrangement = Arrangement.spacedBy(28.dp)) {
                Column(Modifier.weight(2f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    CardHeader(demo)
                    Text(demo.body, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Box(Modifier.weight(3f), contentAlignment = Alignment.Center) { demo.content() }
            }
        } else {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                CardHeader(demo)
                demo.content()
                Text(demo.body, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun CardHeader(demo: LabDemo) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(demo.title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
            SuggestionChip(onClick = {}, label = { Text(demo.app) })
        }
        Text(demo.techniques.joinToString(" · "), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
    }
}

/** A dark stage standing in for the camera feed the Zone5 overlays normally sit on. */
@Composable
private fun Stage(height: Dp, modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier
            .fillMaxWidth()
            .height(height)
            .clip(MaterialTheme.shapes.large)
            .background(Brush.verticalGradient(listOf(Color(0xFF16221A), Color(0xFF0B140E)))),
        content = content,
    )
}

@Composable
private fun SegmentedDemo() {
    val metric = remember { listOf("Reps", "Time", "Calories") }
    var selectedMetric by remember { mutableStateOf(metric.first()) }
    val range = remember { listOf("Week", "Month", "Year", "All time") }
    var selectedRange by remember { mutableStateOf(range[1]) }
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SegmentedControl(metric, selectedMetric, onSegmentSelected = { selectedMetric = it }) { SegmentText(it) }
        SegmentedControl(range, selectedRange, onSegmentSelected = { selectedRange = it }) { SegmentText(it) }
        Text(
            "Showing $selectedMetric for the $selectedRange. Drag the selected segment, or tap another.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ProgressDemo() {
    var progress by remember { mutableFloatStateOf(0.62f) }
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            Box(Modifier.weight(1f).aspectRatio(16f / 9f)) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(7.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.tertiaryContainer),
                            ),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.size(40.dp))
                }
                RoundedRectangleProgressBorder(
                    progress = progress,
                    cornerRadius = 20.dp,
                    activeColor = MaterialTheme.colorScheme.primary,
                    inactiveColor = MaterialTheme.colorScheme.outlineVariant,
                    modifier = Modifier.fillMaxSize(),
                    lineWidth = 5.dp,
                )
            }
            ProgressCircle(progress, Modifier.size(84.dp))
            CircularCheckmarkProgressBar()
        }
        Slider(value = progress, onValueChange = { progress = it })
    }
}

@Composable
private fun DotMarkDemo() {
    var plane by remember { mutableStateOf(DotPlane.Body) }
    var fill by remember { mutableStateOf(DotFill.Filled) }
    val pulse by rememberInfiniteTransition(label = "halo").animateFloat(
        initialValue = 1.4f,
        targetValue = 2.0f,
        animationSpec = infiniteRepeatable(tween(1400, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "haloScale",
    )
    val tertiary = MaterialTheme.colorScheme.tertiary
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Stage(height = 170.dp) {
            Canvas(Modifier.fillMaxSize()) {
                val r = size.minDimension * 0.19f
                val y = size.height * 0.56f
                drawExerciseDot(Offset(size.width * 0.22f, y), r, AndroidGreen, plane, fill, glowScale = pulse)
                drawExerciseDot(Offset(size.width * 0.5f, y), r * 0.72f, tertiary, plane, fill, glowScale = pulse)
                drawExerciseDot(Offset(size.width * 0.76f, y), r * 0.48f, Color(0xFFFF9822), plane, fill, glowScale = pulse)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SegmentedControl(DotPlane.entries, plane, onSegmentSelected = { plane = it }, Modifier.weight(1f)) { SegmentText(it.label) }
            SegmentedControl(DotFill.entries, fill, onSegmentSelected = { fill = it }, Modifier.weight(1f)) { SegmentText(it.label) }
        }
    }
}

@Composable
private fun HoopDemo() {
    val t by rememberInfiniteTransition(label = "hoop").animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(1600, easing = LinearEasing)),
        label = "phase",
    )
    Stage(height = 220.dp) {
        Canvas(Modifier.fillMaxSize()) {
            val cx = size.width / 2f
            val torsoW = size.width * 0.15f
            val body = Color(0xFF2E3D33)
            drawCircle(body, radius = torsoW * 0.36f, center = Offset(cx, size.height * 0.13f))
            drawRoundRect(body, topLeft = Offset(cx - torsoW / 2f, size.height * 0.24f), size = Size(torsoW, size.height * 0.9f), cornerRadius = CornerRadius(torsoW * 0.35f))

            val rx = size.width * 0.36f
            val ry = rx * 0.27f
            val hoopCenter = Offset(cx + sin(t) * size.width * 0.05f, size.height * 0.6f + cos(t) * size.height * 0.03f)
            val left = hoopCenter.x - rx
            val torsoStart = ((cx - torsoW / 2f) - left) / (2f * rx)
            val torsoEnd = ((cx + torsoW / 2f) - left) / (2f * rx)
            drawNeonHoop(hoopCenter, rx, ry, strokeWidth = size.minDimension * 0.035f, color = AndroidGreen, torsoStart = torsoStart, torsoEnd = torsoEnd)
        }
    }
}

@Composable
private fun ImpactDemo() {
    Stage(height = 220.dp) { ImpactBurstDemo(Modifier.fillMaxSize()) }
}

@Composable
private fun MarkdownDemo() {
    var text by remember {
        mutableStateOf("**Bold**, *italic* and ***both***, a [link](https://github.com/alizeyn), plus free* and 5 * 3 that stay literal.")
    }
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Markdown in") },
            minLines = 2,
            textStyle = MaterialTheme.typography.bodyMedium,
        )
        Card(
            Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        ) {
            MarkdownText(text, Modifier.padding(16.dp))
        }
    }
}
