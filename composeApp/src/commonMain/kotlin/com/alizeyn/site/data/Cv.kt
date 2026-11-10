package com.alizeyn.site.data

import com.alizeyn.site.resources.Res
import com.alizeyn.site.resources.icon_billa
import com.alizeyn.site.resources.icon_neshan
import com.alizeyn.site.resources.icon_ohana
import com.alizeyn.site.resources.icon_pushups
import com.alizeyn.site.resources.icon_zone5
import com.alizeyn.site.resources.logo_foxyfitness
import com.alizeyn.site.resources.logo_neshan
import com.alizeyn.site.resources.logo_ohana
import com.alizeyn.site.resources.logo_shopreme
import org.jetbrains.compose.resources.DrawableResource

/** A run of text inside a paragraph. `accent` marks the highlighted phrases. */
data class Segment(val text: String, val accent: Boolean = false)

typealias Paragraph = List<Segment>

private fun plain(text: String) = Segment(text)
private fun accent(text: String) = Segment(text, accent = true)

object Profile {
    const val firstName = "Ali"
    const val lastName = "Zeynali"
    const val role = "Android Engineer"
    const val focus = "Design systems"
    const val email = "the.ali.zeynali@gmail.com"
    const val linkedin = "https://www.linkedin.com/in/alizeynali/"
    const val github = "https://github.com/alizeyn"
    const val location = "Vienna, Austria"
    const val tagline = "Ten years of Android, from Java to Jetpack Compose."
}

val about: List<Paragraph> = listOf(
    listOf(
        plain("I've been building Android apps for about ten years, from Java to Kotlin and from XML to "),
        accent("Jetpack Compose"),
        plain(", for products used by more than 10 million people. Along the way I've shipped iOS work in "),
        accent("SwiftUI"),
        plain(" as well."),
    ),
    listOf(
        plain("The constant through all of it is "),
        accent("user experience"),
        plain(". My strength is a holistic view of how a product looks and feels. I have an eye for detail, and the visual side of development is the part I enjoy most."),
    ),
    listOf(
        plain("After almost a decade on mobile I know the two platforms' preferences and differences well, with the heavier weight on Android."),
    ),
)

data class Role(
    val company: String,
    val title: String,
    val period: String,
    val arrangement: String,
    val place: String,
    val logo: DrawableResource,
    /** The company page on LinkedIn. */
    val linkedin: String,
    val summary: String,
    val highlights: List<String>,
    val stack: List<String>,
)

val roles: List<Role> = listOf(
    Role(
        company = "Foxyfitness",
        title = "Android & iOS Engineer",
        period = "Jan 2026 – Present",
        arrangement = "Hybrid",
        place = "Linz",
        logo = Res.drawable.logo_foxyfitness,
        linkedin = "https://www.linkedin.com/company/foxyfitness/",
        summary = "Fitness start-up. A small team, so everyone wears several hats.",
        highlights = listOf(
            "Shipped iOS alongside Android: SwiftUI work on Zone5 and the Push Up Counter, keeping both apps in step.",
            "Integrated AI tooling into the everyday workflow, from prototyping to implementation.",
            "A growing share of product design and UI/UX work alongside Android development.",
        ),
        stack = listOf("Kotlin", "Jetpack Compose", "SwiftUI", "AI-assisted workflows"),
    ),
    Role(
        company = "Ohana",
        title = "Senior Android Developer",
        period = "Dec 2023 – Aug 2025",
        arrangement = "Remote",
        place = "Linz",
        logo = Res.drawable.logo_ohana,
        linkedin = "https://www.linkedin.com/company/ohana-software-gmbh/",
        summary = "Parental-control app that helps families build healthy screen-time routines.",
        highlights = listOf(
            "Compose everywhere: daily feature work, a migration of the whole app to Jetpack Navigation, and shared styles and components adopted across modules.",
            "Replaced Firebase Dynamic Links ahead of the shutdown: evaluated SDK and API alternatives, migrated the existing deep links, led pricing talks with providers.",
            "Built two-factor authentication for Parent Mode with delivery to every guardian's device.",
            "Implemented paywalls on Qonversion and Google Play subscriptions, and supported the move from subscriptions to freemium.",
            "Reworked module dependencies for better separation of concerns and maintainability.",
        ),
        stack = listOf("Kotlin", "Jetpack Compose", "Jetpack Navigation", "Hilt", "Coroutines & Flow"),
    ),
    Role(
        company = "shopreme",
        title = "Android Developer",
        period = "Nov 2021 – Nov 2023",
        arrangement = "On-site",
        place = "Graz",
        logo = Res.drawable.logo_shopreme,
        linkedin = "https://www.linkedin.com/company/shopreme-self-checkout/",
        summary = "Scan & Go SDK and self-checkout platform used by brands like Rewe, Auchan and ALDI.",
        highlights = listOf(
            "SDK mindset: a clean public API surface with internals kept internal, API parity with iOS, and a deprecation policy so patch releases never break anyone.",
            "Wrote the documentation and changelogs that shipped with every release.",
            "Set up CI pipelines for the SDK and several customer apps; introduced Jetpack Compose for new projects.",
            "UI- and animation-heavy work across the SDK and the self-checkout terminal app.",
            "Migrated the last large Java classes to Kotlin and the database from GreenDAO to Room; built GPS-based store detection without beacons.",
        ),
        stack = listOf("Kotlin", "SDK & API design", "Jetpack Compose", "CI pipelines", "Room"),
    ),
    Role(
        company = "Neshan",
        title = "Android Developer",
        period = "Sep 2017 – Oct 2021",
        arrangement = "Hybrid",
        place = "Mashhad",
        logo = Res.drawable.logo_neshan,
        linkedin = "https://www.linkedin.com/company/neshanmaps/",
        summary = "Maps and navigation used by 10 million people in Iran, localised down to the speed bumps.",
        highlights = listOf(
            "Right-to-left from day one: Persian and Arabic layouts and typography on small screens, and the UI/UX challenges that come with them.",
            "Watched the product grow from zero to 5M+ installs; moved the codebase to MVVM along the way.",
            "Designed a hybrid GPS on LocationManager and Fused Location for faster fixes and fewer bad positions; cut battery use with WorkManager.",
            "Reached 98.3% crash-free users through Firebase crash analysis; built the push-notification system and location-based instant search.",
            "Onboarded new Android developers with merge-request rules, code style and tooling; mentored students at Ferdowsi University.",
        ),
        stack = listOf("Kotlin", "Java", "RTL layouts", "MVVM", "RxJava", "Map SDKs"),
    ),
)

const val rolesFootnote =
    "Product and people first here. The full technical detail, stack by stack, is on LinkedIn."

data class SkillGroup(val title: String, val skills: List<String>)

val toolkit: List<SkillGroup> = listOf(
    SkillGroup("Build with", listOf("Kotlin", "Jetpack Compose", "Material 3", "Coroutines & Flow", "Hilt", "Room", "Compose Multiplatform", "SwiftUI")),
    SkillGroup("Keep it honest", listOf("Unit & UI tests", "Code review", "GitHub Actions", "GitLab CI", "Jenkins", "Clean architecture", "SOLID")),
    SkillGroup("Design side", listOf("Design systems", "Component APIs", "Figma", "RTL layouts", "Prototyping", "Motion")),
    SkillGroup("Also", listOf("Java", "RxJava", "SDK docs & changelogs", "Git", "Agile")),
)


/** An app on the Play Store that I shipped or built the SDK for. */
data class App(
    val name: String,
    val company: String,
    val blurb: String,
    val icon: DrawableResource,
    val playUrl: String,
)

private fun play(id: String) = "https://play.google.com/store/apps/details?id=$id"

val apps: List<App> = listOf(
    App("Zone5", "Foxyfitness", "Camera-coached workouts that keep you in the zone. Android and iOS.", Res.drawable.icon_zone5, play("app.foxyfitness.android.zone5")),
    App("Push Up Counter", "Foxyfitness", "Automatic push-up counting, plans and leaderboards. Android and iOS.", Res.drawable.icon_pushups, play("app.foxyfitness.pushups")),
    App("Ohana", "Ohana", "Parental controls: screen time, app blocks and routines.", Res.drawable.icon_ohana, play("com.tryohana.android")),
    App("BILLA Scan & Go", "shopreme", "Scan, pay and go, built on the shopreme SDK.", Res.drawable.icon_billa, play("at.billa.scanandpay")),
    App("Neshan", "Neshan", "Maps and navigation for more than 10 million people.", Res.drawable.icon_neshan, play("org.rajman.neshan.traffic.tehran.navigator")),
)
