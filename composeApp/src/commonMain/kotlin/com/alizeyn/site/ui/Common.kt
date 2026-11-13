package com.alizeyn.site.ui

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.IntrinsicMeasureScope
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alizeyn.site.WidthClass
import com.alizeyn.site.data.Paragraph

/** Material 3 layout margins: 16dp compact, 24dp medium and expanded. */
fun WidthClass.margin(): Dp = if (this == WidthClass.Compact) 16.dp else 24.dp

/**
 * A scrolling page column capped at a readable measure and centred. Text inside is selectable
 * and copyable; the selection colours come from the Material theme's primary.
 */
@Composable
fun Page(widthClass: WidthClass, content: @Composable ColumnScope.() -> Unit) {
    SelectionContainer {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(PaddingValues(horizontal = widthClass.margin()))
                .widthIn(max = 1040.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Spacer(Modifier.height(if (widthClass == WidthClass.Compact) 0.dp else 24.dp))
            content()
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
fun SectionHeader(eyebrow: String, title: String, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(eyebrow, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Text(title, style = MaterialTheme.typography.headlineLargeEmphasized, color = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
fun AccentParagraph(paragraph: Paragraph, modifier: Modifier = Modifier) {
    val accent = MaterialTheme.colorScheme.primary
    val text = buildAnnotatedString {
        paragraph.forEach { segment ->
            if (segment.accent) {
                withStyle(SpanStyle(color = accent, fontWeight = FontWeight.SemiBold)) { append(segment.text) }
            } else {
                append(segment.text)
            }
        }
    }
    Text(text, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface, modifier = modifier)
}


/**
 * A wrap layout for single-line pills whose intrinsic height is exact: children are measured
 * unbounded (they never wrap themselves), so the estimate rows in intrinsic-height parents
 * such as `Row(Modifier.height(IntrinsicSize.Max))` match the real layout. The built-in
 * FlowRow can disagree with itself by a row, which clips the last pills.
 */
@Composable
fun PillFlow(modifier: Modifier = Modifier, spacing: Dp = 6.dp, content: @Composable () -> Unit) {
    val policy = remember(spacing) { PillFlowPolicy(spacing) }
    Layout(content, modifier, policy)
}

private class PillFlowPolicy(private val spacing: Dp) : MeasurePolicy {
    private fun rows(widths: List<Int>, heights: List<Int>, maxWidth: Int, gap: Int): Int {
        var height = 0
        var rowWidth = 0
        var rowHeight = 0
        widths.forEachIndexed { i, w ->
            val next = if (rowWidth == 0) w else rowWidth + gap + w
            if (rowWidth > 0 && next > maxWidth) {
                height += rowHeight + gap
                rowWidth = w
                rowHeight = heights[i]
            } else {
                rowWidth = next
                rowHeight = maxOf(rowHeight, heights[i])
            }
        }
        return height + rowHeight
    }

    override fun MeasureScope.measure(measurables: List<Measurable>, constraints: Constraints): MeasureResult {
        val gap = spacing.roundToPx()
        val placeables = measurables.map { it.measure(Constraints()) }
        val maxWidth = constraints.maxWidth
        val positions = ArrayList<IntOffset>(placeables.size)
        var x = 0
        var y = 0
        var rowHeight = 0
        placeables.forEach { p ->
            if (x > 0 && x + gap + p.width > maxWidth) {
                y += rowHeight + gap
                x = 0
                rowHeight = 0
            }
            positions += IntOffset(if (x == 0) 0 else x + gap, y)
            x = if (x == 0) p.width else x + gap + p.width
            rowHeight = maxOf(rowHeight, p.height)
        }
        val width = if (constraints.hasBoundedWidth) maxWidth else placeables.sumOf { it.width } + gap * (placeables.size - 1).coerceAtLeast(0)
        val height = (y + rowHeight).coerceIn(constraints.minHeight, constraints.maxHeight)
        return layout(width.coerceIn(constraints.minWidth, constraints.maxWidth), height) {
            placeables.forEachIndexed { i, p -> p.placeRelative(positions[i]) }
        }
    }

    override fun IntrinsicMeasureScope.maxIntrinsicHeight(measurables: List<IntrinsicMeasurable>, width: Int): Int =
        rows(measurables.map { it.maxIntrinsicWidth(Int.MAX_VALUE) }, measurables.map { it.maxIntrinsicHeight(Int.MAX_VALUE) }, width, spacing.roundToPx())

    override fun IntrinsicMeasureScope.minIntrinsicHeight(measurables: List<IntrinsicMeasurable>, width: Int): Int =
        maxIntrinsicHeight(measurables, width)

    override fun IntrinsicMeasureScope.maxIntrinsicWidth(measurables: List<IntrinsicMeasurable>, height: Int): Int =
        measurables.sumOf { it.maxIntrinsicWidth(Int.MAX_VALUE) } + spacing.roundToPx() * (measurables.size - 1).coerceAtLeast(0)

    override fun IntrinsicMeasureScope.minIntrinsicWidth(measurables: List<IntrinsicMeasurable>, height: Int): Int =
        measurables.maxOfOrNull { it.maxIntrinsicWidth(Int.MAX_VALUE) } ?: 0
}
