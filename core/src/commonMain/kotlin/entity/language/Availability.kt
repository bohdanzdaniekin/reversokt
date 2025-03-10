package io.github.mrnemo.reversokt.entity.language

import io.github.mrnemo.reversokt.entity.Service

object Availability {

    private val context = setOf(
        Language.ARABIC,
        Language.GERMAN,
        Language.ENGLISH,
        Language.SPANISH,
        Language.FRENCH,
        Language.HEBREW,
        Language.ITALIAN,
        Language.JAPANESE,
        Language.KOREAN,
        Language.DUTCH,
        Language.POLISH,
        Language.PORTUGUESE,
        Language.ROMANIAN,
        Language.RUSSIAN,
        Language.SWEDISH,
        Language.TURKISH,
        Language.UKRAINIAN,
        Language.CHINESE
    )

    private val spell = setOf(
        Language.ENGLISH,
        Language.FRENCH,
        Language.ITALIAN,
        Language.SPANISH
    )

    private val synonymous = setOf(
        Language.ARABIC,
        Language.GERMAN,
        Language.ENGLISH,
        Language.SPANISH,
        Language.FRENCH,
        Language.HEBREW,
        Language.ITALIAN,
        Language.JAPANESE,
        Language.DUTCH,
        Language.POLISH,
        Language.PORTUGUESE,
        Language.ROMANIAN,
        Language.RUSSIAN
    )

    private val translation = setOf(
        Language.ARABIC,
        Language.CHINESE,
        Language.CZECH,
        Language.DANISH,
        Language.DUTCH,
        Language.ENGLISH,
        Language.FRENCH,
        Language.GERMAN,
        Language.GREEK,
        Language.HEBREW,
        Language.HINDI,
        Language.HUNGARIAN,
        Language.ITALIAN,
        Language.JAPANESE,
        Language.KOREAN,
        Language.PERSIAN,
        Language.POLISH,
        Language.PORTUGUESE,
        Language.ROMANIAN,
        Language.RUSSIAN,
        Language.SLOVAK,
        Language.SPANISH,
        Language.SWEDISH,
        Language.THAI,
        Language.TURKISH,
        Language.UKRAINIAN
    )

    private val conjugation = setOf(
        Language.ENGLISH,
        Language.FRENCH,
        Language.SPANISH,
        Language.GERMAN,
        Language.ITALIAN,
        Language.PORTUGUESE,
        Language.HEBREW,
        Language.RUSSIAN,
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