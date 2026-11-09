package com.alizeyn.site.data

import com.alizeyn.site.resources.Res
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
    const val tagline = "Ten years of Android, from Java to Jetpack Compose. I build the UI components other teams build with."
    const val shoutTitle = "Fluent in both languages."
    const val shoutBody =
        "I translate UX intent into component APIs, and technical constraints back into design conversations. " +
            "Nearly a decade on Android teams, a product designer's eye, and a library author's habits: " +
            "clean API surfaces, documentation, changelogs, and no surprise breaking changes."
}

val about: List<Paragraph> = listOf(
    listOf(
        plain("I've been building Android apps for about ten years: Java to Kotlin, XML to "),
        accent("Jetpack Compose"),
        plain(", for products with more than 10 million users. The constant through all of it: the UI layer is where I do my best work."),
    ),
    listOf(
        plain("At shopreme I learned to think like a "),
        accent("library author"),
        plain(": a clean public API surface with the internals kept internal, parity with iOS, deprecation instead of breaking changes, and documentation and changelogs people actually read. That is exactly the mindset a design-system component library needs."),
    ),
    listOf(
        plain("I care about design as much as code. I prototype, I argue about spacing and motion, and I read a "),
        accent("Figma"),
        plain(" file as fluently as a stack trace. Over the past year I've folded AI tooling into everyday work, from prototyping to implementation."),
    ),
    listOf(
        plain("This site is a small proof: "),
        accent("Compose Multiplatform drawn with Material 3"),
        plain(", adaptive navigation per window size class, light and dark schemes, and a GitHub Actions pipeline that ships every push."),
    ),
)

data class Role(
    val company: String,
    val title: String,
    val period: String,
    val arrangement: String,
    val place: String,
    val logo: DrawableResource,
    val summary: String,
    val highlights: List<String>,
    val stack: List<String>,
)

val roles: List<Role> = listOf(
    Role(
        company = "Foxyfitness",
        title = "Android Engineer",
        period = "Jan 2026 – Present",
        arrangement = "Hybrid",
        place = "Linz",
        logo = Res.drawable.logo_foxyfitness,
        summary = "Fitness start-up. A small team, so everyone wears several hats.",
        highlights = listOf(
            "Integrated AI tooling into the everyday workflow, from prototyping to implementation.",
            "A growing share of product design and UI/UX work alongside Android development.",
        ),
        stack = listOf("Kotlin", "Jetpack Compose", "AI-assisted workflows"),
    ),
    Role(
        company = "Ohana",
        title = "Senior Android Developer",
        period = "Dec 2023 – Aug 2025",
        arrangement = "Remote",
        place = "Linz",
        logo = Res.drawable.logo_ohana,
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
    SkillGroup("Build with", listOf("Kotlin", "Jetpack Compose", "Material 3", "Coroutines & Flow", "Hilt", "Room", "Compose Multiplatform")),
    SkillGroup("Keep it honest", listOf("Unit & UI tests", "Code review", "GitHub Actions", "GitLab CI", "Jenkins", "Clean architecture", "SOLID")),
    SkillGroup("Design side", listOf("Design systems", "Component APIs", "Figma", "RTL layouts", "Prototyping", "Motion")),
    SkillGroup("Also", listOf("Java", "RxJava", "SDK docs & changelogs", "Git", "Agile")),
)

data class Faq(val question: String, val answer: List<String>)

val faqs: List<Faq> = listOf(
    Faq(
        question = "Engineer or designer?",
        answer = listOf(
            "Both, and that's the point. Ten years of Android engineering, with product design as the side that kept growing.",
            "In a design-system team that isn't a split, it's the job: translating UX intent into component APIs and back.",
        ),
    ),
    Faq(
        question = "What's your Compose story?",
        answer = listOf(
            "Introduced it for new projects at shopreme, then used it daily at Ohana, including a full Jetpack Navigation migration and shared component styles across modules.",
            "This site is Compose too, drawn with Material 3 and compiled to WebAssembly.",
        ),
    ),
    Faq(
        question = "RTL, really?",
        answer = listOf(
            "Four years on Neshan, a Persian-language app for 10 million people. Right-to-left layouts, Arabic-script typography and small screens were the daily normal.",
        ),
    ),
    Faq(
        question = "Language stuff?",
        answer = listOf("My German is around B1.", "English is fluent,", "and my mother tongue is Persian."),
    ),
    Faq(
        question = "What did you study again?",
        answer = listOf("B.Sc. in Software Engineering, Ferdowsi University of Mashhad. Design and visual art never stopped being a side study."),
    ),
    Faq(
        question = "Do you have, like hobbies?",
        answer = listOf("Generative art, producing music, photography, hiking. Huge fan of tea, and coffee when the tea runs out."),
    ),
)
