package io.github.mrnemo.reversokt.entity

import io.github.mrnemo.reversokt.entity.language.Availability
import io.github.mrnemo.reversokt.entity.language.Language

enum class Service {
    CONTEXT,
    SPELL,
    SYNONYMOUS,
    TRANSLATION,
    CONJUGATION,
}

val Service.available: Set<Language>
    get() = Availability.forService(this)