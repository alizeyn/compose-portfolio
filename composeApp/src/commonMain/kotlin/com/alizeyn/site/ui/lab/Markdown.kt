package com.alizeyn.site.ui.lab

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle

// Inline Markdown subset: *italic*, **bold**, ***both***, _italic_, [text](url), backslash escapes.
// Emphasis follows the CommonMark flanking rules, so a footnote star ("free*", "* Offer") stays
// literal. Pure Kotlin, ported from the Pushups app.

data class InlineMarkdownStyles(val bold: SpanStyle, val italic: SpanStyle, val link: SpanStyle)

fun parseInlineMarkdown(text: String, styles: InlineMarkdownStyles): AnnotatedString = buildAnnotatedString {
    render(parseInline(text), styles)
}

@Composable
fun MarkdownText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    color: Color = MaterialTheme.colorScheme.onSurface,
) {
    val linkColor = MaterialTheme.colorScheme.primary
    val annotated = remember(text, linkColor) {
        parseInlineMarkdown(
            text,
            InlineMarkdownStyles(
                bold = SpanStyle(fontWeight = FontWeight.Bold),
                italic = SpanStyle(fontStyle = FontStyle.Italic),
                link = SpanStyle(color = linkColor, textDecoration = TextDecoration.Underline),
            ),
        )
    }
    Text(annotated, modifier = modifier, style = style, color = color)
}

private sealed interface Node {
    class Text(val value: StringBuilder = StringBuilder()) : Node
    class Delimiter(val char: Char, var count: Int, val canOpen: Boolean, val canClose: Boolean) : Node {
        val originalCount: Int = count
    }
    class Emphasis(val strong: Boolean, val children: List<Node>) : Node
    class Link(val url: String, val children: List<Node>) : Node
}

private const val ESCAPABLE = "\\*_[]()"

private fun parseInline(text: String): List<Node> {
    val nodes = tokenize(text)
    processEmphasis(nodes)
    return nodes
}

private fun tokenize(text: String): MutableList<Node> {
    val nodes = mutableListOf<Node>()
    var pending: Node.Text? = null
    fun append(c: Char) {
        val target = pending ?: Node.Text().also { pending = it; nodes.add(it) }
        target.value.append(c)
    }
    fun flush() { pending = null }

    var i = 0
    while (i < text.length) {
        val c = text[i]
        when {
            c == '\\' && i + 1 < text.length && text[i + 1] in ESCAPABLE -> { append(text[i + 1]); i += 2 }
            c == '*' || c == '_' -> {
                var end = i
                while (end < text.length && text[end] == c) end++
                nodes.add(delimiter(text, c, start = i, end = end))
                flush()
                i = end
            }
            c == '[' -> {
                val link = parseLink(text, i)
                if (link == null) { append(c); i++ } else { nodes.add(link.first); flush(); i = link.second }
            }
            else -> { append(c); i++ }
        }
    }
    return nodes
}

private fun delimiter(text: String, char: Char, start: Int, end: Int): Node.Delimiter {
    val prev = text.getOrNull(start - 1) ?: ' '
    val next = text.getOrNull(end) ?: ' '
    val leftFlanking = !next.isWhitespace() && (!next.isPunctuation() || prev.isWhitespace() || prev.isPunctuation())
    val rightFlanking = !prev.isWhitespace() && (!prev.isPunctuation() || next.isWhitespace() || next.isPunctuation())
    return if (char == '_') {
        Node.Delimiter(
            char = char,
            count = end - start,
            canOpen = leftFlanking && (!rightFlanking || prev.isPunctuation()),
            canClose = rightFlanking && (!leftFlanking || next.isPunctuation()),
        )
    } else {
        Node.Delimiter(char = char, count = end - start, canOpen = leftFlanking, canClose = rightFlanking)
    }
}

private fun Char.isPunctuation(): Boolean = !isLetterOrDigit() && !isWhitespace()

private fun parseLink(text: String, start: Int): Pair<Node.Link, Int>? {
    val labelEnd = text.indexOf(']', startIndex = start + 1)
    if (labelEnd < 0 || text.getOrNull(labelEnd + 1) != '(') return null
    val urlEnd = text.indexOf(')', startIndex = labelEnd + 2)
    if (urlEnd < 0) return null
    val url = text.substring(labelEnd + 2, urlEnd).trim()
    if (url.isEmpty() || url.any { it.isWhitespace() }) return null
    val label = text.substring(start + 1, labelEnd)
    return Node.Link(url = url, children = parseInline(label)) to urlEnd + 1
}

private fun processEmphasis(nodes: MutableList<Node>) {
    var i = 0
    while (i < nodes.size) {
        val closer = nodes[i] as? Node.Delimiter
        if (closer == null || !closer.canClose) { i++; continue }
        val openerIndex = findOpener(nodes, closerIndex = i)
        if (openerIndex < 0) { i++; continue }
        val opener = nodes[openerIndex] as Node.Delimiter
        val used = if (opener.count >= 2 && closer.count >= 2) 2 else 1
        opener.count -= used
        closer.count -= used
        val inner = nodes.subList(openerIndex + 1, i).toList()
        repeat(inner.size) { nodes.removeAt(openerIndex + 1) }
        nodes.add(openerIndex + 1, Node.Emphasis(strong = used == 2, children = inner))
        i = openerIndex + 2
        if (opener.count == 0) { nodes.removeAt(openerIndex); i-- }
        if (closer.count == 0) nodes.removeAt(i)
    }
}

private fun findOpener(nodes: List<Node>, closerIndex: Int): Int {
    val closer = nodes[closerIndex] as Node.Delimiter
    for (j in closerIndex - 1 downTo 0) {
        val opener = nodes[j] as? Node.Delimiter ?: continue
        if (opener.char != closer.char || !opener.canOpen || opener.count == 0) continue
        // CommonMark "rule of three" keeps e.g. "*foo**bar*" from pairing across mismatched runs.
        val ruleOfThree = (closer.canOpen || opener.canClose) &&
            (opener.originalCount + closer.originalCount) % 3 == 0 &&
            !(opener.originalCount % 3 == 0 && closer.originalCount % 3 == 0)
        if (ruleOfThree) continue
        return j
    }
    return -1
}

private fun AnnotatedString.Builder.render(nodes: List<Node>, styles: InlineMarkdownStyles) {
    nodes.forEach { node ->
        when (node) {
            is Node.Text -> append(node.value.toString())
            is Node.Delimiter -> append(node.char.toString().repeat(node.count))
            is Node.Emphasis -> withStyle(if (node.strong) styles.bold else styles.italic) { render(node.children, styles) }
            is Node.Link -> withLink(LinkAnnotation.Url(url = node.url, styles = TextLinkStyles(style = styles.link))) {
                render(node.children, styles)
            }
        }
    }
}
