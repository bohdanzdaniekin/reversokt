package io.github.mrnemo.reversokt.entity

enum class Language(
    val title: String,
    val direction: Direction = Direction.LTR
) {
    ARABIC("arabic", Direction.RTL),
    GERMAN("german"),
    SPANISH("spanish"),
    FRENCH("french"),
    HEBREW("hebrew", Direction.RTL),
    ITALIAN("italian"),
    JAPANESE("japanese"),
    DUTCH("dutch"),
    POLISH("polish"),
    PORTUGUESE("portuguese"),
    ROMANIAN("romanian"),
    RUSSIAN("russian"),
    SWEDISH("swedish"),
    TURKISH("turkish"),
    CHINESE("chinese"),
    ENGLISH("english"),
    UKRAINIAN("ukrainian");

    enum class Direction {
        LTR, RTL;
    }

    companion object {
        fun fromTitle(title: String): Language? = entries.find { it.title == title }

        val supported = setOf(
            ARABIC,
            GERMAN,
            SPANISH,
            FRENCH,
            HEBREW,
            ITALIAN,
            JAPANESE,
            DUTCH,
            POLISH,
            PORTUGUESE,
            ROMANIAN,
            RUSSIAN,
            TURKISH,
            CHINESE,
            ENGLISH,
            UKRAINIAN,
        )
    }
}
