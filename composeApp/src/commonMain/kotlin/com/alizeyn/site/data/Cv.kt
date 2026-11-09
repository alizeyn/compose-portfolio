package com.alizeyn.site.data

/** A run of text inside a paragraph. `accent` marks the highlighted phrases. */
data class Segment(val text: String, val accent: Boolean = false)

typealias Paragraph = List<Segment>

private fun plain(text: String) = Segment(text)
private fun accent(text: String) = Segment(text, accent = true)

object Profile {
    const val firstName = "Ali"
    const val lastName = "Zeynali"
    const val role = "Art Stuff"
    const val email = "the.ali.zeynali@gmail.com"
    const val linkedin = "https://www.linkedin.com/in/alizeynali/"
    const val github = "https://github.com/alizeyn"
    const val location = "Vienna, Austria"
    val tagline = listOf("I can design everything —", "well mostly apps —", "but hey, challenge me!")
    val shout = listOf("Wanna be my career", "switch hero?")
}

val about: List<Paragraph> = listOf(
    listOf(
        plain("I've been a mobile engineer (mostly Android) for the last 8–9 years, but I've never been able to let go of my "),
        accent("artsy"),
        plain(" side. Now I'm thinking of switching sides!"),
    ),
    listOf(
        plain("I was always better at "),
        accent("product design"),
        plain(". The creative energy was always trying to get out! Feels great to shape a product look and feel!"),
    ),
    listOf(
        plain("After all these years, I've built a solid understanding of user experience and the standards of different platforms, plus that extra twist of creativity."),
    ),
    listOf(
        plain("And of course I can implement the prototypes when needed. Well, I spent (almost) a decade doing this sh*t!"),
    ),
    listOf(accent("I'm flipping the coin — engineer to designer!")),
)

enum class Tone { Orange, Blue, Maroon }

data class Role(
    val company: String,
    val period: String,
    val arrangement: String,
    val place: String,
    val tone: Tone,
    val body: List<String>,
)

val roles: List<Role> = listOf(
    Role(
        company = "Foxyfitness",
        period = "Jan 2026 – Present",
        arrangement = "Hybrid",
        place = "Linz",
        tone = Tone.Orange,
        body = listOf(
            "I've learned how to integrate AI into everyday work. From prototyping to implementation!",
            "With more responsibilities in a small start-up, I noticed that I have a stronger taste for product design & UI/UX (rather than development).",
        ),
    ),
    Role(
        company = "Ohana",
        period = "Dec 2023 – Aug 2025",
        arrangement = "Remote",
        place = "Linz",
        tone = Tone.Blue,
        body = listOf(
            "My baby steps into the start-up game started with Ohana. Working with a lovely small team, came to appreciate the communication skills even more than before. Talking to the product designer, all the devs and marketing. I got familiar with different stages of start-up growth in practice. I sharpened my engineering skills while learning how a product comes together!",
        ),
    ),
    Role(
        company = "shopreme",
        period = "Nov 2021 – Nov 2023",
        arrangement = "On-site",
        place = "Graz",
        tone = Tone.Blue,
        body = listOf(
            "A mid-sized rather mature start-up. My first experience working in Austria! I got familiar with ecosystem and how things work around here.",
            "I did quite a lot of UI-heavy & animation-heavy work at shopreme!",
        ),
    ),
    Role(
        company = "Neshan",
        period = "Sep 2017 – Oct 2021",
        arrangement = "Hybrid",
        place = "Iran",
        tone = Tone.Maroon,
        body = listOf(
            "My very first serious position, started as a junior dev and went on from there. I saw a product take shape from 0 to 5M installs! And all the changes that happened with it!",
            "Here I dealt with UI/UX challenges on small screens, right-to-left layout (Persian and Arabic support).",
        ),
    ),
)

const val rolesFootnote =
    "I'm leaving out the technical details, and mostly focusing on the product aspect and human aspect of what I did in the past. Naturally, as it's more relevant to the position I'm applying for. In case you're interested you can check out all the details on my LinkedIn."

data class Faq(val question: String, val answer: List<String>)

val faqs: List<Faq> = listOf(
    Faq(
        question = "What did you study again?",
        answer = listOf("Well, software engineering. But I've always kept an interest in design and visual art."),
    ),
    Faq(
        question = "Language stuff?",
        answer = listOf("My German skills are like B1.", "English is fluent,", "and my mother tongue is Persian."),
    ),
    Faq(
        question = "Do you have, like hobbies?",
        answer = listOf("I enjoy photography, nature and music. I do a bit of each and trying to expand my horizons here and there. Huge fan of tea!"),
    ),
    Faq(
        question = "Right, and what is this career change about? Isn't being a mobile eng. good enough?",
        answer = listOf(
            "It surely is great! I just think I'm moving more towards what satisfies me the most and where my main taste, talent and passion are!",
            "My past experiences are just giving me more strength!",
        ),
    ),
)
