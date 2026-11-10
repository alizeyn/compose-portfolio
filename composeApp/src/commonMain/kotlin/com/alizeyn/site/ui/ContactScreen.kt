package com.alizeyn.site.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MediumExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.alizeyn.site.WidthClass
import com.alizeyn.site.data.Profile
import com.alizeyn.site.resources.Res
import com.alizeyn.site.resources.ic_open_in_new
import org.jetbrains.compose.resources.painterResource

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
        Reveal(pop = true, delayMillis = 160) {
            MediumExtendedFloatingActionButton(
                onClick = onEmail,
                icon = { Icon(Icons.Filled.Email, contentDescription = null) },
                text = { Text("Email me") },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.hoverLift().clip(MaterialTheme.shapes.large).sheen(),
            )
        }
        Reveal(delayMillis = 240) {
        Card(
            Modifier.fillMaxWidth().hoverLift(),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        ) {
            Column(Modifier.padding(vertical = 8.dp)) {
                ContactRow(
                    headline = "Email",
                    supporting = Profile.email,
                    leading = { Icon(Icons.Filled.Email, contentDescription = null) },
                    onClick = onEmail,
                )
                HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                ContactRow(
                    headline = "LinkedIn",
                    supporting = "linkedin.com/in/alizeynali",
                    leading = { Icon(painterResource(Res.drawable.ic_open_in_new), contentDescription = null) },
                    onClick = { uriHandler.openUri(Profile.linkedin) },
                )
                HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                ContactRow(
                    headline = "GitHub",
                    supporting = "github.com/alizeyn",
                    leading = { Icon(painterResource(Res.drawable.ic_open_in_new), contentDescription = null) },
                    onClick = { uriHandler.openUri(Profile.github) },
                )
                HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                ContactRow(
                    headline = "Based in",
                    supporting = Profile.location,
                    leading = { Icon(Icons.Filled.Place, contentDescription = null) },
                    onClick = null,
                )
            }
        }
        }
    }
}

@Composable
private fun ContactRow(
    headline: String,
    supporting: String,
    leading: @Composable () -> Unit,
    onClick: (() -> Unit)?,
) {
    ListItem(
        headlineContent = { Text(headline) },
        supportingContent = { Text(supporting) },
        leadingContent = leading,
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier,
    )
}
