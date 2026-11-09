package com.alizeyn.site

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alizeyn.site.data.Profile
import com.alizeyn.site.resources.Res
import com.alizeyn.site.resources.ic_dark_mode
import com.alizeyn.site.resources.ic_help
import com.alizeyn.site.resources.ic_light_mode
import com.alizeyn.site.resources.ic_work
import com.alizeyn.site.theme.SiteTheme
import com.alizeyn.site.ui.AboutScreen
import com.alizeyn.site.ui.ContactScreen
import com.alizeyn.site.ui.FaqScreen
import com.alizeyn.site.ui.HomeScreen
import com.alizeyn.site.ui.WorkScreen
import org.jetbrains.compose.resources.painterResource

/** Material 3 window-size classes: compact < 600dp, medium < 840dp, expanded otherwise. */
enum class WidthClass { Compact, Medium, Expanded }

fun widthClassFor(width: Dp): WidthClass = when {
    width < 600.dp -> WidthClass.Compact
    width < 840.dp -> WidthClass.Medium
    else -> WidthClass.Expanded
}

enum class Destination(val label: String, val route: String) {
    Home("Home", "home"),
    About("About", "about"),
    Work("Work", "work"),
    Faq("FAQ", "faq"),
    Contact("Contact", "contact");

    companion object {
        fun fromRoute(route: String): Destination = entries.firstOrNull { it.route == route } ?: Home
    }
}

@Composable
private fun DestinationIcon(destination: Destination, selected: Boolean) {
    when (destination) {
        Destination.Home -> Icon(if (selected) Icons.Filled.Home else Icons.Outlined.Home, contentDescription = null)
        Destination.About -> Icon(if (selected) Icons.Filled.Person else Icons.Outlined.Person, contentDescription = null)
        Destination.Work -> Icon(painterResource(Res.drawable.ic_work), contentDescription = null)
        Destination.Faq -> Icon(painterResource(Res.drawable.ic_help), contentDescription = null)
        Destination.Contact -> Icon(if (selected) Icons.Filled.Email else Icons.Outlined.Email, contentDescription = null)
    }
}

@Composable
fun App(
    initialDestination: Destination = Destination.Home,
    onDestinationChanged: (Destination) -> Unit = {},
) {
    val systemDark = isSystemInDarkTheme()
    var darkOverride by remember { mutableStateOf<Boolean?>(null) }
    val dark = darkOverride ?: systemDark

    SiteTheme(darkTheme = dark) {
        var destination by remember { mutableStateOf(initialDestination) }
        LaunchedEffect(destination) { onDestinationChanged(destination) }
        val uriHandler = LocalUriHandler.current
        val sendEmail = { uriHandler.openUri("mailto:${Profile.email}") }

        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            BoxWithConstraints {
                val widthClass = widthClassFor(maxWidth)
                val themeToggle: @Composable () -> Unit = {
                    ThemeToggle(dark = dark, onToggle = { darkOverride = !dark })
                }

                when (widthClass) {
                    WidthClass.Compact -> CompactLayout(
                        destination = destination,
                        onDestination = { destination = it },
                        themeToggle = themeToggle,
                        widthClass = widthClass,
                        onEmail = sendEmail,
                    )
                    else -> RailLayout(
                        destination = destination,
                        onDestination = { destination = it },
                        themeToggle = themeToggle,
                        widthClass = widthClass,
                        onEmail = sendEmail,
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemeToggle(dark: Boolean, onToggle: () -> Unit) {
    val painter: Painter = painterResource(if (dark) Res.drawable.ic_light_mode else Res.drawable.ic_dark_mode)
    IconButton(onClick = onToggle) {
        Icon(painter, contentDescription = if (dark) "Switch to light theme" else "Switch to dark theme")
    }
}

@Composable
private fun CompactLayout(
    destination: Destination,
    onDestination: (Destination) -> Unit,
    themeToggle: @Composable () -> Unit,
    widthClass: WidthClass,
    onEmail: () -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("${Profile.firstName} ${Profile.lastName}", style = MaterialTheme.typography.titleLarge) },
                actions = { themeToggle() },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        },
        bottomBar = {
            NavigationBar {
                Destination.entries.forEach { d ->
                    val selected = d == destination
                    NavigationBarItem(
                        selected = selected,
                        onClick = { onDestination(d) },
                        icon = { DestinationIcon(d, selected) },
                        label = { Text(d.label) },
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            Content(destination, widthClass, onDestination, onEmail)
        }
    }
}

@Composable
private fun RailLayout(
    destination: Destination,
    onDestination: (Destination) -> Unit,
    themeToggle: @Composable () -> Unit,
    widthClass: WidthClass,
    onEmail: () -> Unit,
) {
    Row(Modifier.fillMaxSize()) {
        NavigationRail(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            header = {
                Spacer(Modifier.height(8.dp))
                FloatingActionButton(
                    onClick = onEmail,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                ) {
                    Icon(Icons.Outlined.Email, contentDescription = "Send me an email")
                }
            },
        ) {
            Spacer(Modifier.weight(1f))
            Destination.entries.forEach { d ->
                val selected = d == destination
                NavigationRailItem(
                    selected = selected,
                    onClick = { onDestination(d) },
                    icon = { DestinationIcon(d, selected) },
                    label = { Text(d.label) },
                )
            }
            Spacer(Modifier.weight(1f))
            themeToggle()
            Spacer(Modifier.height(16.dp))
        }
        Box(Modifier.weight(1f).fillMaxSize()) {
            Content(destination, widthClass, onDestination, onEmail)
        }
    }
}

@Composable
private fun Content(
    destination: Destination,
    widthClass: WidthClass,
    onDestination: (Destination) -> Unit,
    onEmail: () -> Unit,
) {
    AnimatedContent(
        targetState = destination,
        transitionSpec = {
            (fadeIn() + slideInVertically { it / 24 }) togetherWith (fadeOut() + slideOutVertically { -it / 24 })
        },
        label = "destination",
    ) { target ->
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            when (target) {
                Destination.Home -> HomeScreen(widthClass, onEmail = onEmail, onAbout = { onDestination(Destination.About) })
                Destination.About -> AboutScreen(widthClass)
                Destination.Work -> WorkScreen(widthClass)
                Destination.Faq -> FaqScreen(widthClass)
                Destination.Contact -> ContactScreen(widthClass, onEmail = onEmail)
            }
        }
    }
}
