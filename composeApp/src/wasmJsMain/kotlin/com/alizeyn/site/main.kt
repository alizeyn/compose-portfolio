package com.alizeyn.site

import androidx.compose.ui.window.ComposeViewport
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
        )
    }
}
