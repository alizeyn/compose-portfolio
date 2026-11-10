package com.alizeyn.site

import androidx.compose.ui.window.ComposeViewport
import com.alizeyn.site.data.Profile
import kotlinx.browser.document
import kotlinx.browser.window

fun main() {
    val initial = Destination.fromRoute(window.location.hash.removePrefix("#"))
    ComposeViewport(document.body!!) {
        App(
            initialDestination = initial,
            onDestinationChanged = { destination ->
                val hash = if (destination == Destination.Home) "" else "#${destination.route}"
                if (window.location.hash != hash) {
                    window.history.replaceState(null, "", window.location.pathname + window.location.search + hash)
                }
            },
            // Navigating the current window to a mailto: URL hands off to the mail client without
            // leaving a blank tab behind, which window.open would do on desktop.
            onEmail = { window.location.href = "mailto:${Profile.email}" },
        )
    }
}
