package com.alizeyn.site.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * A Pixel 10 style device frame: flat display with even bezels, a centred punch-hole camera,
 * and the power and volume keys on the right edge. Body proportions follow the phone (72 × 152.8 mm).
 */
@Composable
fun PixelFrame(
    screenshot: DrawableResource,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    width: Dp = 160.dp,
) {
    val bodyRadius = width * 0.15f
    val bezel = width * 0.028f
    val body = Color(0xFF1A1B1E)
    val edge = Color(0xFF3B3D42)
    Box(
        modifier
            .width(width)
            .aspectRatio(72f / 152.8f)
            .drawBehind {
                // Power and volume keys, just proud of the right edge.
                val keyW = size.width * 0.018f
                val x = size.width - keyW * 0.35f
                drawRoundRect(edge, topLeft = Offset(x, size.height * 0.20f), size = Size(keyW, size.height * 0.07f), cornerRadius = CornerRadius(keyW))
                drawRoundRect(edge, topLeft = Offset(x, size.height * 0.30f), size = Size(keyW, size.height * 0.13f), cornerRadius = CornerRadius(keyW))
            }
            .clip(RoundedCornerShape(bodyRadius))
            .background(Brush.verticalGradient(listOf(Color(0xFF2A2C31), body)))
            .border(1.dp, edge, RoundedCornerShape(bodyRadius))
            .padding(bezel),
    ) {
        Box(Modifier.fillMaxSize().clip(RoundedCornerShape(bodyRadius - bezel)).background(Color.Black)) {
            Image(
                painter = painterResource(screenshot),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopCenter,
                modifier = Modifier.fillMaxSize(),
            )
            // Punch-hole camera.
            Box(
                Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = width * 0.03f)
                    .size(width * 0.04f)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .border(1.dp, Color(0xFF2E2F33), CircleShape),
            )
        }
    }
}
