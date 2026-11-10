package com.alizeyn.site.ui.lab

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

// An iOS-style segmented control: a custom Layout places an animated thumb under the selected
// segment; raw pointer input handles press, drag-to-switch and cancel. Ported from the Pushups app.

private const val NO_SEGMENT_INDEX = -1
private val TRACK_PADDING = 2.dp
private val PRESSED_TRACK_PADDING = 1.dp
private val SEGMENT_PADDING = 6.dp
private const val PRESSED_UNSELECTED_ALPHA = .6f
private val BACKGROUND_SHAPE = RoundedCornerShape(10.dp)

@Composable
fun <T : Any> SegmentedControl(
    segments: List<T>,
    selectedSegment: T,
    onSegmentSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit,
) {
    val state = remember { SegmentedControlState() }
    state.segmentCount = segments.size
    state.selectedSegment = segments.indexOf(selectedSegment)
    state.onSegmentSelected = { onSegmentSelected(segments[it]) }

    // Animate between whole-number indices so no pixel math is needed.
    val selectedIndexOffset by animateFloatAsState(state.selectedSegment.toFloat(), label = "thumb")

    Layout(
        content = {
            Thumb(state)
            Dividers(state)
            Segments(state, segments, content)
        },
        modifier = modifier
            .fillMaxWidth()
            .then(state.inputModifier)
            .background(MaterialTheme.colorScheme.surfaceContainerHighest, BACKGROUND_SHAPE)
            .padding(TRACK_PADDING),
    ) { measurables, constraints ->
        val (thumbMeasurable, dividersMeasurable, segmentsMeasurable) = measurables
        val segmentsPlaceable = segmentsMeasurable.measure(constraints)
        state.updatePressedScale(segmentsPlaceable.height, this)
        val thumbPlaceable = thumbMeasurable.measure(
            Constraints.fixed(width = segmentsPlaceable.width / segments.size, height = segmentsPlaceable.height),
        )
        val dividersPlaceable = dividersMeasurable.measure(
            Constraints.fixed(width = segmentsPlaceable.width, height = segmentsPlaceable.height),
        )
        layout(segmentsPlaceable.width, segmentsPlaceable.height) {
            val segmentWidth = segmentsPlaceable.width / segments.size
            thumbPlaceable.placeRelative(x = (selectedIndexOffset * segmentWidth).toInt(), y = 0)
            dividersPlaceable.placeRelative(IntOffset.Zero)
            segmentsPlaceable.placeRelative(IntOffset.Zero)
        }
    }
}

@Composable
fun SegmentText(text: String) {
    Text(text, maxLines = 1, overflow = TextOverflow.Ellipsis)
}

@Composable
private fun Thumb(state: SegmentedControlState) {
    val scheme = MaterialTheme.colorScheme
    val thumb = if (scheme.background.luminance() < 0.5f) scheme.surfaceBright else scheme.surfaceContainerLowest
    Box(
        Modifier
            .then(state.segmentScaleModifier(pressed = state.pressedSegment == state.selectedSegment, segment = state.selectedSegment))
            .background(thumb, BACKGROUND_SHAPE),
    )
}

@Composable
private fun Dividers(state: SegmentedControlState) {
    val alphas = (0 until state.segmentCount).map { i ->
        val selectionAdjacent = i == state.selectedSegment || i - 1 == state.selectedSegment
        animateFloatAsState(if (selectionAdjacent) 0f else 1f, label = "divider")
    }
    val color = MaterialTheme.colorScheme.outlineVariant
    Canvas(Modifier.fillMaxSize()) {
        val segmentWidth = size.width / state.segmentCount
        val dividerPadding = TRACK_PADDING + PRESSED_TRACK_PADDING
        alphas.forEachIndexed { i, alpha ->
            val x = i * segmentWidth
            if (x > 0) {
                drawLine(color, alpha = alpha.value, start = Offset(x, dividerPadding.toPx()), end = Offset(x, size.height - dividerPadding.toPx()))
            }
        }
    }
}

@Composable
private fun <T> Segments(state: SegmentedControlState, segments: List<T>, content: @Composable (T) -> Unit) {
    CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.labelLarge) {
        Row(horizontalArrangement = spacedBy(TRACK_PADDING), modifier = Modifier.fillMaxWidth().selectableGroup()) {
            segments.forEachIndexed { i, segment ->
                val isSelected = i == state.selectedSegment
                val isPressed = i == state.pressedSegment
                val alpha by animateFloatAsState(if (!isSelected && isPressed) PRESSED_UNSELECTED_ALPHA else 1f, label = "press")
                val semanticsModifier = Modifier.semantics(mergeDescendants = true) {
                    selected = isSelected
                    role = Role.Button
                    onClick { state.onSegmentSelected(i); true }
                    stateDescription = if (isSelected) "Selected" else "Not selected"
                }
                Box(
                    Modifier
                        .weight(1f)
                        .then(semanticsModifier)
                        .padding(SEGMENT_PADDING)
                        .alpha(alpha)
                        .then(state.segmentScaleModifier(isPressed && isSelected, i))
                        .wrapContentWidth(),
                ) {
                    content(segment)
                }
            }
        }
    }
}

private class SegmentedControlState {
    var segmentCount by mutableIntStateOf(0)
    var selectedSegment by mutableIntStateOf(0)
    var onSegmentSelected: (Int) -> Unit by mutableStateOf({})
    var pressedSegment by mutableIntStateOf(NO_SEGMENT_INDEX)
    var pressedSelectedScale by mutableStateOf(1f)
        private set

    fun updatePressedScale(controlHeight: Int, density: Density) {
        with(density) {
            val pressedHeight = controlHeight - (PRESSED_TRACK_PADDING * 2).toPx()
            pressedSelectedScale = pressedHeight / controlHeight
        }
    }

    fun segmentScaleModifier(pressed: Boolean, segment: Int): Modifier = Modifier.composed {
        val scale by animateFloatAsState(if (pressed) pressedSelectedScale else 1f, label = "scale")
        val xOffset by animateDpAsState(if (pressed) PRESSED_TRACK_PADDING else 0.dp, label = "shift")
        graphicsLayer {
            scaleX = scale
            scaleY = scale
            transformOrigin = TransformOrigin(
                pivotFractionX = when (segment) {
                    0 -> 0f
                    segmentCount - 1 -> 1f
                    else -> .5f
                },
                pivotFractionY = .5f,
            )
            translationX = when (segment) {
                0 -> xOffset.toPx()
                segmentCount - 1 -> -xOffset.toPx()
                else -> 0f
            }
        }
    }

    val inputModifier = Modifier.pointerInput(segmentCount) {
        val segmentWidth = size.width / segmentCount
        fun segmentIndex(change: PointerInputChange): Int =
            ((change.position.x / size.width.toFloat()) * segmentCount).toInt().coerceIn(0, segmentCount - 1)

        awaitEachGesture {
            val down = awaitFirstDown()
            pressedSegment = segmentIndex(down)
            val downOnSelected = pressedSegment == selectedSegment
            val segmentBounds = Rect(
                left = pressedSegment * segmentWidth.toFloat(),
                right = (pressedSegment + 1) * segmentWidth.toFloat(),
                top = 0f,
                bottom = size.height.toFloat(),
            )
            if (downOnSelected) {
                horizontalDrag(down.id) { change ->
                    pressedSegment = segmentIndex(change)
                    if (pressedSegment != selectedSegment) onSegmentSelected(pressedSegment)
                }
            } else {
                waitForUpOrCancellation(inBounds = segmentBounds)?.let { onSegmentSelected(pressedSegment) }
            }
            pressedSegment = NO_SEGMENT_INDEX
        }
    }
}

private suspend fun AwaitPointerEventScope.waitForUpOrCancellation(inBounds: Rect): PointerInputChange? {
    while (true) {
        val event = awaitPointerEvent(PointerEventPass.Main)
        if (event.changes.all { it.changedToUp() }) return event.changes[0]
        if (event.changes.any { it.isConsumed || !inBounds.contains(it.position) }) return null
        val consumeCheck = awaitPointerEvent(PointerEventPass.Final)
        if (consumeCheck.changes.any { it.isConsumed }) return null
    }
}
