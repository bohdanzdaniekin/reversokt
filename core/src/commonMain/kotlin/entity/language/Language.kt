package io.github.mrnemo.reversokt.entity.language

import io.github.mrnemo.reversokt.entity.Service

enum class Language(
    val title: String,
    val shortName: String? = null,
    val codeSynonymous: String? = null,
    val codeTranslation: String? = null,
    val direction: Direction = Direction.LTR,
    val voice: String? = null,
) {

    ARABIC(
        title = "arabic",
        codeSynonymous = "ar",
        codeTranslation = "ara",
        direction = Direction.RTL,
        voice = "Mehdi22k"
    ),
    CHINESE(
        title = "chinese",
        codeTranslation = "chi",
        voice = "Lulu22k"
    ),
    DUTCH(
        title = "dutch",
        codeSynonymous = "nl",
        codeTranslation = "dut",
        voice = "Femke22k"
    ),
    ENGLISH(
        title = "english",
        codeSynonymous = "en",
        codeTranslation = "eng",
        shortName = "eng",
        voice = "Heather22k"
    ),
    FRENCH(
        title = "french",
        codeSynonymous = "fr",
        codeTranslation = "fra",
        shortName = "fra",
        voice = "Alice22k"
    ),
    GERMAN(
        title = "german",
        codeSynonymous = "de",
        codeTranslation = "ger",
        voice = "Claudia22k"
    ),
    HEBREW(
        title = "hebrew",
        codeSynonymous = "he",
        codeTranslation = "heb",
        direction = Direction.RTL,
        voice = "he-IL-Asaf"
    ),
    ITALIAN(
        title = "italian",
        codeSynonymous = "it",
        codeTranslation = "ita",
        shortName = "ita",
        voice = "Chiara22k"
    ),
    JAPANESE(
        title = "japanese",
        codeSynonymous = "ja",
        codeTranslation = "jpn",
        voice = "Sakura22k"
    ),
    POLISH(
        title = "polish",
        codeSynonymous = "pl",
        codeTranslation = "pol",
        voice = "Ania22k"
    ),
    PORTUGUESE(
        title = "portuguese",
        codeSynonymous = "pt",
        codeTranslation = "por",
        voice = "Celia22k"
    ),
    ROMANIAN(
        title = "romanian",
        codeSynonymous = "ro",
        voice = "ro-RO-Andrei"
    ),
    RUSSIAN(
        title = "russian",
        codeSynonymous = "ru",
        codeTranslation = "rus",
        voice = "Alyona22k"
    ),
    SPANISH(
        title = "spanish",
        codeSynonymous = "es",
        codeTranslation = "spa",
        shortName = "spa",
        voice = "Ines22k"
    ),
    SWEDISH(
        title = "swedish"
    ),
    TURKISH(
        title = "turkish",
        codeTranslation = "tur",
        voice = "Ipek22k"
    ),
    UKRAINIAN(
        title = "ukrainian",
        codeTranslation = "ukr"
    ),
    CZECH(
        title = "czech",
        codeTranslation = "cze"
    ),
    DANISH(
        title = "danish",
        codeTranslation = "dan"
    ),
    GREEK(
        title = "greek",
        codeTranslation = "gre"
    ),
    HINDI(
        title = "hindi",
        codeTranslation = "hin"
    ),
    HUNGARIAN(
        title = "hungarian",
        codeTranslation = "hun"
    ),
    KOREAN(
        title = "korean",
        codeTranslation = "kor"
    ),
    PERSIAN(
        title = "persian",
        codeTranslation = "per"
    ),
    SLOVAK(
        title = "slovak",
        codeTranslation = "slo"
    ),
    THAI(
        title = "thai",
        codeTranslation = "tha"
    );

    fun compatibleWith(service: Service, other: Language): Boolean {
        return when (service) {
            Service.CONTEXT -> Compatibility.context[this]?.contains(other) ?: false
            Service.TRANSLATION -> Compatibility.translation[this]?.contains(other) ?: false
            else -> true
        }
    }

    fun isAvailableFor(service: Service): Boolean = Availability.forService(service).contains(this)

    enum class Direction {
        LTR, RTL;

        val title: String
            get() = name.lowercase()
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
