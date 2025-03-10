package io.github.mrnemo.reversokt

import arrow.core.Either
import io.github.mrnemo.reversokt.entity.ReversoError
import io.github.mrnemo.reversokt.entity.ReversoSuccess
import io.github.mrnemo.reversokt.entity.language.Language

internal interface AbstractReverso {

    suspend fun contextOf(text: String, source: Language, target: Language): Either<ReversoError, ReversoSuccess.Context>

    suspend fun spellcheckOf(text: String, source: Language): Either<ReversoError, ReversoSuccess.Spelling>

    suspend fun synonymousOf(text: String, source: Language): Either<ReversoError, ReversoSuccess.Synonymous>

    suspend fun translationOf(text: String, source: Language, target: Language): Either<ReversoError, ReversoSuccess.Translation>

    suspend fun conjugationOf(text: String, source: Language): Either<ReversoError, ReversoSuccess.Conjugation>
}