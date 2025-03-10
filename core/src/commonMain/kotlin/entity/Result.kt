package io.github.mrnemo.reversokt.entity

import io.github.mrnemo.reversokt.entity.data.spelling.Sentence
import io.github.mrnemo.reversokt.entity.data.spelling.Stats
import io.github.mrnemo.reversokt.entity.data.spelling.Suggestion
import io.github.mrnemo.reversokt.entity.language.Availability
import io.github.mrnemo.reversokt.entity.language.Compatibility
import io.github.mrnemo.reversokt.entity.language.Language
import io.ktor.http.Headers
import io.ktor.http.Url

interface ReversoError {

    val message: String

    data class RequestError(val error: Throwable) : ReversoError {

        override val message: String
            get() = error.message ?: "An  ${error::class.simpleName} error occurred"
    }

    data class ResponseError(
        val service: Service,
        val source: Language,
        val target: Language? = null,
        val headers: Headers,
        val url: Url,
        val value: Int,
        val description: String
    ) : ReversoError {

        override val message: String
            get() = """Service $service ($source -> $target) responded with $value ($description) error for $url""".trimMargin()
    }

    data class LanguageCompatibilityError(
        val service: Service,
        val source: Language,
        val target: Language
    ) : ReversoError {

        override val message: String
            get() = "$source is not compatible with $target for $service."
    }

    data class LanguageAvailabilityError(
        val service: Service,
        val source: Language
    ) : ReversoError {

        override val message: String
            get() = """$source is not available for $service. 
                Available languages for $service: ${service.available.joinToString()}""".trimMargin()
    }

    data class InvalidSourceLanguageError(
        val service: Service,
        val source: Language?,
        val target: Language?
    ) : ReversoError {

        override val message: String
            get() = "Invalid source language $source for $service."
    }

    data class InvalidTargetLanguageError(
        val service: Service,
        val source: Language?,
        val target: Language?
    ) : ReversoError {

        override val message: String
            get() = "Invalid target language $target for $service."
    }

    data class EmptyResponseError(
        val service: Service,
        val source: Language,
        val target: Language? = null,
        val text: String
    ) : ReversoError {

        override val message: String
            get() = ""
    }
}

interface ReversoSuccess {

    val text: String
    val source: Language

    data class Context(
        override val text: String,
        override val source: Language,
        val target: Language,
        val translations: List<String>,
        val examples: List<Example>
    ) : ReversoSuccess

    data class Spelling(
        override val text: String,
        override val source: Language,
        val stats: Stats,
        val sentences: List<Sentence>,
        val corrections: List<Correction>
    ) : ReversoSuccess

    data class Synonymous(
        override val text: String,
        override val source: Language,
        val synonyms: List<String>
    ) : ReversoSuccess

    data class Translation(
        override val text: String,
        override val source: Language,
        val target: Language,
        val translations: List<String>,
        val detectedLanguage: String?,
        val voice: String?,
        val examples: List<ContextExample>?,
        val rude: Boolean?
    ) : ReversoSuccess

    data class Conjugation(
        override val text: String,
        override val source: Language,
        val infinitive: String,
        val verbForms: List<VerbForm>
    ) : ReversoSuccess
}

data class Example(
    val id: String,
    val source: String,
    val target: String
)

data class Correction(
    val id: Long,
    val text: String,
    val type: String,
    val explanation: String,
    val corrected: String,
    val suggestions: List<Suggestion>
)