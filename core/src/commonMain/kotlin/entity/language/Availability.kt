package io.github.mrnemo.reversokt.entity

object LanguageAvailability {
    private val context = setOf(
        Language.ARABIC,
        Language.GERMAN,
        Language.SPANISH,
        Language.FRENCH,
        Language.HEBREW,
        Language.ITALIAN,
        Language.JAPANESE,
        Language.DUTCH,
        Language.POLISH,
        Language.PORTUGUESE,
        Language.ROMANIAN,
        Language.RUSSIAN,
        Language.TURKISH,
        Language.CHINESE,
        Language.ENGLISH,
        Language.SWEDISH,
    )

    private val spell = setOf(Language.ENGLISH, Language.FRENCH, Language.ITALIAN, Language.SPANISH)

    private val synonymous = setOf(
        Language.ENGLISH,
        Language.RUSSIAN,
        Language.GERMAN,
        Language.SPANISH,
        Language.FRENCH,
        Language.POLISH,
        Language.ITALIAN,
        Language.ARABIC,
        Language.HEBREW,
        Language.JAPANESE,
        Language.DUTCH,
        Language.PORTUGUESE,
        Language.ROMANIAN,
    )

    private val translation = setOf(
        Language.ARABIC,
        Language.GERMAN,
        Language.SPANISH,
        Language.FRENCH,
        Language.HEBREW,
        Language.ITALIAN,
        Language.JAPANESE,
        Language.DUTCH,
        Language.POLISH,
        Language.PORTUGUESE,
        Language.ROMANIAN,
        Language.RUSSIAN,
        Language.TURKISH,
        Language.CHINESE,
        Language.ENGLISH,
        Language.UKRAINIAN,
    )

    private val conjugation = setOf(
        Language.ENGLISH,
        Language.RUSSIAN,
        Language.FRENCH,
        Language.SPANISH,
        Language.GERMAN,
        Language.ITALIAN,
        Language.PORTUGUESE,
        Language.HEBREW,
        Language.ARABIC,
        Language.JAPANESE,
    )

    fun forService(service: Service): Set<Language> {
        return when (service) {
            Service.CONTEXT -> context
            Service.SPELL -> spell
            Service.SYNONYMOUS -> synonymous
            Service.TRANSLATION -> translation
            Service.CONJUGATION -> conjugation
        }
    }
}