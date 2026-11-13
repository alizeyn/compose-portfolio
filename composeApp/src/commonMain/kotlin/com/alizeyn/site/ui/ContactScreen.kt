package com.alizeyn.site.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.alizeyn.site.WidthClass
import com.alizeyn.site.data.Profile
import com.alizeyn.site.resources.Res
import com.alizeyn.site.resources.ic_content_copy
import com.alizeyn.site.resources.ic_github
import com.alizeyn.site.resources.ic_linkedin
import com.alizeyn.site.resources.ic_open_in_new
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private const val MAPS_URL = "https://www.google.com/maps/place/Vienna,+Austria"

@Composable
fun ContactScreen(widthClass: WidthClass, onEmail: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    Page(widthClass) {
        Reveal { SectionHeader("Contact", "Say hi") }
        Reveal(delayMillis = 80) {
            Text(
                "Looking for someone who can shape a product's look and feel and still ship the thing? Let's talk.",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        Reveal(delayMillis = 160) {
            Card(
                Modifier.fillMaxWidth().hoverLift(),
                shape = MaterialTheme.shapes.extraLarge,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
            ) {
                Column(Modifier.padding(vertical = 8.dp)) {
                    ContactRow(
                        headline = "Email me",
                        supporting = "${Profile.email} · copies the address",
                        leading = { Icon(Icons.Filled.Email, contentDescription = null) },
                        trailing = Res.drawable.ic_content_copy,
                        onClick = onEmail,
                    )
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    ContactRow(
                        headline = "Find me on LinkedIn",
                        supporting = "linkedin.com/in/alizeynali",
                        leading = { Icon(painterResource(Res.drawable.ic_linkedin), contentDescription = null) },
                        trailing = Res.drawable.ic_open_in_new,
                        onClick = { uriHandler.openUri(Profile.linkedin) },
                    )
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    ContactRow(
                        headline = "See my code on GitHub",
                        supporting = "github.com/alizeyn",
                        leading = { Icon(painterResource(Res.drawable.ic_github), contentDescription = null) },
                        trailing = Res.drawable.ic_open_in_new,
                        onClick = { uriHandler.openUri(Profile.github) },
                    )
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    ContactRow(
                        headline = "Where I am",
                        supporting = "${Profile.location} · open in Maps",
                        leading = { Icon(Icons.Filled.Place, contentDescription = null) },
                        trailing = Res.drawable.ic_open_in_new,
                        onClick = { uriHandler.openUri(MAPS_URL) },
                    )
                }
            }
        }
        Reveal(delayMillis = 240) { ChatWithAli(onEmail) }
    }
}

/** A list row that reads as a link: primary-coloured leading icon, link-coloured detail, trailing action icon. */
@Composable
private fun ContactRow(
    headline: String,
    supporting: String,
    leading: @Composable () -> Unit,
    trailing: DrawableResource,
    onClick: () -> Unit,
) {
    ListItem(
        headlineContent = { Text(headline) },
        supportingContent = { Text(supporting, color = MaterialTheme.colorScheme.primary) },
        leadingContent = leading,
        trailingContent = {
            Icon(painterResource(trailing), contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
        },
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent,
            leadingIconColor = MaterialTheme.colorScheme.primary,
        ),
        modifier = Modifier.clickable(onClick = onClick),
    )
}

private val chatLines = listOf(
    "Hey, thanks for scrolling all the way down.",
    "Got a product that needs both the engineering and the design? That is my favourite kind.",
    "Tell me about it. I usually reply within a day.",
)

/**
 * A short chat with an animated me: the portrait as a round avatar with a pulsing presence dot,
 * typing dots, then each line popping in as a Material bubble. The last bubble is the reply,
 * on your side of the thread, and sends an email.
 */
@Composable
private fun ChatWithAli(onEmail: () -> Unit) {
    var shown by remember { mutableIntStateOf(0) }
    var typing by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(600)
        for (i in chatLines.indices) {
            typing = true
            delay(if (i == 0) 1000L else 1400L)
            typing = false
            shown = i + 1
            delay(700)
        }
        delay(400)
        shown = chatLines.size + 1
    }
    val popIn = fadeIn(tween(220)) + scaleIn(spring(dampingRatio = 0.6f, stiffness = 500f), initialScale = 0.7f, transformOrigin = TransformOrigin(0f, 1f)) + expandVertically(spring(dampingRatio = 0.8f, stiffness = 500f))

    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Bottom) {
            Avatar()
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                chatLines.forEachIndexed { i, line ->
                    AnimatedVisibility(visible = i < shown, enter = popIn) { Bubble(line, mine = false) }
                }
                AnimatedVisibility(visible = typing, enter = popIn) { TypingDots() }
            }
        }
        AnimatedVisibility(visible = shown > chatLines.size, enter = fadeIn(tween(220)) + scaleIn(spring(dampingRatio = 0.6f, stiffness = 500f), initialScale = 0.7f, transformOrigin = TransformOrigin(1f, 1f)) + expandVertically()) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Surface(
                    onClick = onEmail,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomEnd = 4.dp, bottomStart = 20.dp),
                    color = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.hoverLift(),
                ) {
                    Row(Modifier.padding(horizontal = 18.dp, vertical = 12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("Hi Ali, I have something for you…", style = MaterialTheme.typography.bodyLarge)
                        Icon(Icons.Filled.Send, contentDescription = "Send an email", modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun Avatar() {
    val pulse = rememberInfiniteTransition(label = "presence")
    val halo by pulse.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1600, easing = LinearEasing), RepeatMode.Restart),
        label = "halo",
    )
    Box(Modifier.size(48.dp)) {
        Portrait(size = 48.dp, shape = CircleShape)
        // Presence dot with a ring that grows and fades, Material style.
        Box(Modifier.align(Alignment.BottomEnd).size(16.dp)) {
            Box(
                Modifier.size(16.dp).clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = (1f - halo) * 0.5f))
                    .offset(),
            )
            Box(
                Modifier.align(Alignment.Center).size(10.dp).clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(2.dp).clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
            )
        }
    }
}

@Composable
private fun Bubble(text: String, mine: Boolean) {
    Surface(
        shape = if (mine) RoundedCornerShape(20.dp, 20.dp, 4.dp, 20.dp) else RoundedCornerShape(20.dp, 20.dp, 20.dp, 4.dp),
        color = if (mine) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
        contentColor = if (mine) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer,
    ) {
        Text(text, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.widthIn(max = 460.dp).padding(horizontal = 16.dp, vertical = 10.dp))
    }
}

/** Three dots that rise one after another, the classic "typing" indicator. */
@Composable
private fun TypingDots() {
    val t = rememberInfiniteTransition(label = "typing")
    val phase by t.animateFloat(
        initialValue = 0f, targetValue = 3f,
        animationSpec = infiniteRepeatable(tween(900, easing = LinearEasing), RepeatMode.Restart),
        label = "typingPhase",
    )
    Surface(
        shape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 4.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
    ) {
        Row(Modifier.padding(horizontal = 16.dp, vertical = 14.dp), horizontalArrangement = Arrangement.spacedBy(5.dp), verticalAlignment = Alignment.CenterVertically) {
            repeat(3) { i ->
                // Each dot bobs for a third of the cycle, in sequence.
                val local = ((phase - i + 3f) % 3f)
                val lift = if (local < 1f) kotlin.math.sin(local * kotlin.math.PI).toFloat() else 0f
                Box(
                    Modifier.offset(y = (-4 * lift).dp).size(8.dp).clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.45f + 0.55f * lift)),
                )
            }
        }
    }
}
