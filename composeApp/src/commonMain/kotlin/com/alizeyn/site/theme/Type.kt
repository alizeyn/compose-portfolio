package com.alizeyn.site.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Material 3 type scale (https://m3.material.io/styles/typography/type-scale-tokens),
// with the display and headline roles tightened to echo the poster's lockups.
private val Base = Typography()

val SiteTypography = Typography(
    displayLarge = Base.displayLarge.copy(fontSize = 66.sp, lineHeight = 74.sp, fontWeight = FontWeight.SemiBold, letterSpacing = (-1.5).sp),
    displayLargeEmphasized = Base.displayLargeEmphasized.copy(fontSize = 66.sp, lineHeight = 74.sp, letterSpacing = (-1.5).sp),
    displayMedium = Base.displayMedium.copy(fontSize = 50.sp, lineHeight = 58.sp, fontWeight = FontWeight.SemiBold, letterSpacing = (-1).sp),
    displayMediumEmphasized = Base.displayMediumEmphasized.copy(fontSize = 50.sp, lineHeight = 58.sp, letterSpacing = (-1).sp),
    displaySmall = Base.displaySmall.copy(fontSize = 40.sp, lineHeight = 48.sp, fontWeight = FontWeight.SemiBold, letterSpacing = (-0.5).sp),
    displaySmallEmphasized = Base.displaySmallEmphasized.copy(fontSize = 40.sp, lineHeight = 48.sp, letterSpacing = (-0.5).sp),
    headlineLarge = Base.headlineLarge.copy(fontSize = 38.sp, lineHeight = 46.sp, fontWeight = FontWeight.SemiBold),
    headlineLargeEmphasized = Base.headlineLargeEmphasized.copy(fontSize = 38.sp, lineHeight = 46.sp),
    headlineMedium = Base.headlineMedium.copy(fontSize = 31.sp, lineHeight = 40.sp, fontWeight = FontWeight.SemiBold),
    headlineMediumEmphasized = Base.headlineMediumEmphasized.copy(fontSize = 31.sp, lineHeight = 40.sp),
    headlineSmall = Base.headlineSmall.copy(fontSize = 26.sp, lineHeight = 34.sp, fontWeight = FontWeight.Medium),
    headlineSmallEmphasized = Base.headlineSmallEmphasized.copy(fontSize = 26.sp, lineHeight = 34.sp),
    titleLarge = Base.titleLarge.copy(fontSize = 24.sp, lineHeight = 32.sp),
    titleLargeEmphasized = Base.titleLargeEmphasized.copy(fontSize = 24.sp, lineHeight = 32.sp),
    titleMedium = Base.titleMedium.copy(fontSize = 18.sp, lineHeight = 26.sp),
    titleSmall = Base.titleSmall,
    bodyLarge = Base.bodyLarge.copy(fontSize = 18.sp, lineHeight = 28.sp),
    bodyMedium = Base.bodyMedium.copy(fontSize = 15.sp, lineHeight = 22.sp),
    bodySmall = Base.bodySmall,
    labelLarge = Base.labelLarge.copy(fontSize = 15.sp, lineHeight = 20.sp),
    labelMedium = Base.labelMedium,
    labelSmall = Base.labelSmall,
)
