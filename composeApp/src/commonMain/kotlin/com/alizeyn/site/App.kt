package com.alizeyn.site

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ShortNavigationBar
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.material3.ShortNavigationBarItemDefaults
import androidx.compose.material3.WideNavigationRailDefaults
import androidx.compose.material3.WideNavigationRailItemDefaults
import androidx.compose.material3.WideNavigationRail
import androidx.compose.material3.WideNavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alizeyn.site.data.Profile
import com.alizeyn.site.resources.Res
import com.alizeyn.site.resources.ic_all_inclusive
import com.alizeyn.site.resources.ic_dark_mode
import com.alizeyn.site.resources.ic_light_mode
import com.alizeyn.site.resources.ic_work
import com.alizeyn.site.theme.SiteTheme
import com.alizeyn.site.ui.BeyondScreen
import com.alizeyn.site.ui.Chrome
import com.alizeyn.site.ui.Platform
import com.alizeyn.site.ui.animated
import com.alizeyn.site.ui.chromeFor
import com.alizeyn.site.ui.materialChrome
import com.alizeyn.site.ui.ContactScreen
import com.alizeyn.site.ui.HomeScreen
import com.alizeyn.site.ui.WorkScreen
import org.jetbrains.compose.resources.painterResource
import kotlinx.coroutines.launch

/** Material 3 window-size classes: compact < 600dp, medium < 840dp, expanded otherwise. */
enum class WidthClass { Compact, Medium, Expanded }

fun widthClassFor(width: Dp): WidthClass = when {
    width < 600.dp -> WidthClass.Compact
    width < 840.dp -> WidthClass.Medium
    else -> WidthClass.Expanded
}

enum class Destination(val label: String, val route: String) {
    Home("Home", "home"),
    Work("Work", "work"),
    Beyond("Beyond", "beyond"),
    Contact("Contact", "contact");

    companion object {
        /** `about` used to be its own tab; it now lives on Home. */
        fun fromRoute(route: String): Destination = entries.firstOrNull { it.route == route } ?: Home
    }
}

@Composable
private fun DestinationIcon(destination: Destination, selected: Boolean) {
    when (destination) {
        Destination.Home -> Icon(if (selected) Icons.Filled.Home else Icons.Outlined.Home, contentDescription = null)
        Destination.Work -> Icon(painterResource(Res.drawable.ic_work), contentDescription = null)
        Destination.Beyond -> Icon(painterResource(Res.drawable.ic_all_inclusive), contentDescription = null)
        Destination.Contact -> Icon(if (selected) Icons.Filled.Email else Icons.Outlined.Email, contentDescription = null)
    }
}

@Composable
fun App(
    initialDestination: Destination = Destination.Home,
    onDestinationChanged: (Destination) -> Unit = {},
    onEmail: (() -> Unit)? = null,
) {
    // Light is the default; the toggle flips it for the session.
    var dark by remember { mutableStateOf(false) }

    SiteTheme(darkTheme = dark) {
        var destination by remember { mutableStateOf(initialDestination) }
        LaunchedEffect(destination) { onDestinationChanged(destination) }
        // While Beyond is on screen the chrome follows its platform; elsewhere it is plain Material.
        var platform by remember { mutableStateOf(Platform.Ios) }
        val chrome = (if (destination == Destination.Beyond) chromeFor(platform) else materialChrome()).animated()
        val uriHandler = LocalUriHandler.current
        val snackbar = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        // The platform handler copies the address and tries the mail client; the snackbar makes
        // sure something visible happens even when the browser has no mail handler.
        val sendEmail = {
            onEmail?.invoke() ?: uriHandler.openUri("mailto:${Profile.email}")
            scope.launch {
                snackbar.currentSnackbarData?.dismiss()
                snackbar.showSnackbar("${Profile.email} copied. If your mail app didn't open, just paste it.")
            }
            Unit
        }

        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            BoxWithConstraints {
                val widthClass = widthClassFor(maxWidth)
                val themeToggle: @Composable () -> Unit = {
                    ThemeToggle(dark = dark, onToggle = { dark = !dark }, chrome = chrome)
                }

                when (widthClass) {
                    WidthClass.Compact -> CompactLayout(
                        destination = destination,
                        onDestination = { destination = it },
                        themeToggle = themeToggle,
                        widthClass = widthClass,
                        onEmail = sendEmail,
                        chrome = chrome,
                        onPlatform = { platform = it },
                    )
                    else -> RailLayout(
                        destination = destination,
                        onDestination = { destination = it },
                        themeToggle = themeToggle,
                        widthClass = widthClass,
                        onEmail = sendEmail,
                        chrome = chrome,
                        onPlatform = { platform = it },
                    )
                }
                SnackbarHost(
                    hostState = snackbar,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = if (widthClass == WidthClass.Compact) 88.dp else 16.dp),
                )
            }
        }
    }
}

@Composable
private fun ThemeToggle(dark: Boolean, onToggle: () -> Unit, chrome: Chrome) {
    val rotation by animateFloatAsState(if (dark) 180f else 0f, spring(stiffness = 200f, dampingRatio = 0.7f), label = "toggleSpin")
    FilledTonalIconToggleButton(
        checked = dark,
        onCheckedChange = { onToggle() },
        shapes = IconButtonDefaults.toggleableShapes(),
        colors = IconButtonDefaults.filledTonalIconToggleButtonColors(
            containerColor = chrome.indicator, contentColor = chrome.selected,
            checkedContainerColor = chrome.indicator, checkedContentColor = chrome.selected,
        ),
    ) {
        AnimatedContent(
            targetState = dark,
            transitionSpec = { (fadeIn(tween(250)) + scaleIn(initialScale = 0.6f)) togetherWith (fadeOut(tween(150)) + scaleOut(targetScale = 0.6f)) },
            label = "toggleIcon",
        ) { isDark ->
            val painter: Painter = painterResource(if (isDark) Res.drawable.ic_light_mode else Res.drawable.ic_dark_mode)
            Icon(
                painter,
                contentDescription = if (isDark) "Switch to light theme" else "Switch to dark theme",
                modifier = Modifier.rotate(rotation),
            )
        }
    }
}

@Composable
private fun CompactLayout(
    destination: Destination,
    onDestination: (Destination) -> Unit,
    themeToggle: @Composable () -> Unit,
    widthClass: WidthClass,
    onEmail: () -> Unit,
    chrome: Chrome,
    onPlatform: (Platform) -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("${Profile.firstName} ${Profile.lastName}", style = MaterialTheme.typography.titleLarge, fontFamily = chrome.family) },
                actions = { themeToggle() },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = chrome.topBar,
                    titleContentColor = chrome.content,
                ),
            )
        },
        bottomBar = {
            ShortNavigationBar(containerColor = chrome.bar) {
                Destination.entries.forEach { d ->
                    val selected = d == destination
                    ShortNavigationBarItem(
                        selected = selected,
                        onClick = { onDestination(d) },
                        icon = { DestinationIcon(d, selected) },
                        label = { Text(d.label, fontFamily = chrome.family) },
                        colors = ShortNavigationBarItemDefaults.colors(
                            selectedIconColor = chrome.selected, selectedTextColorTopIconPosition = chrome.content, selectedIndicatorColor = chrome.indicator,
                            unselectedIconColor = chrome.unselected, unselectedTextColor = chrome.unselected,
                        ),
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            Content(destination, widthClass, onDestination, onEmail, onPlatform)
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
    chrome: Chrome,
    onPlatform: (Platform) -> Unit,
) {
    Row(Modifier.fillMaxSize()) {
        Box {
            WideNavigationRail(
                colors = WideNavigationRailDefaults.colors(containerColor = chrome.rail, contentColor = chrome.content),
                header = {
                    FloatingActionButton(
                        onClick = onEmail,
                        containerColor = chrome.fabContainer,
                        contentColor = chrome.fabContent,
                        modifier = Modifier.padding(start = 20.dp, top = 8.dp),
                    ) {
                        Icon(Icons.Outlined.Email, contentDescription = "Send me an email")
                    }
                },
            ) {
                Destination.entries.forEach { d ->
                    val selected = d == destination
                    WideNavigationRailItem(
                        selected = selected,
                        onClick = { onDestination(d) },
                        icon = { DestinationIcon(d, selected) },
                        label = { Text(d.label, fontFamily = chrome.family) },
                        railExpanded = false,
                        colors = WideNavigationRailItemDefaults.colors(
                            selectedIconColor = chrome.selected, selectedTextColorTopIconPosition = chrome.content, selectedIndicatorColor = chrome.indicator,
                            unselectedIconColor = chrome.unselected, unselectedTextColor = chrome.unselected,
                        ),
                    )
                }
            }
            Box(Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp)) { themeToggle() }
        }
        Box(Modifier.weight(1f).fillMaxSize()) {
            Content(destination, widthClass, onDestination, onEmail, onPlatform)
        }
    }
}

@Composable
private fun Content(
    destination: Destination,
    widthClass: WidthClass,
    onDestination: (Destination) -> Unit,
    onEmail: () -> Unit,
    onPlatform: (Platform) -> Unit,
) {
    AnimatedContent(
        targetState = destination,
        transitionSpec = {
            (fadeIn(tween(350)) + slideInVertically(spring(stiffness = 300f)) { it / 20 } + scaleIn(initialScale = 0.985f)) togetherWith
                (fadeOut(tween(200)) + slideOutVertically { -it / 30 })
        },
        label = "destination",
    ) { target ->
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            when (target) {
                Destination.Home -> HomeScreen(widthClass, onEmail = onEmail, onWork = { onDestination(Destination.Work) })
                Destination.Work -> WorkScreen(widthClass)
                Destination.Beyond -> BeyondScreen(widthClass, onPlatform = onPlatform)
                Destination.Contact -> ContactScreen(widthClass, onEmail = onEmail)
            }
        }
    }
}
