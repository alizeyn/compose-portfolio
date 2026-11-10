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
            // Copy the address first, then hand the mailto: URL to the current window. Navigating
            // the current window leaves no blank tab behind, and the copy covers browsers that
            // have no mail handler configured.
            onEmail = {
                window.navigator.clipboard.writeText(Profile.email)
                window.location.href = "mailto:${Profile.email}"
            },
        )
    }
}
