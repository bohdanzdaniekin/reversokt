package io.github.mrnemo.reversokt

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensureNotNull
import io.github.mrnemo.reversokt.entity.ReversoError
import io.github.mrnemo.reversokt.entity.Service
import io.github.mrnemo.reversokt.entity.language.Language
import io.ktor.http.URLProtocol
import io.ktor.http.Url
import io.ktor.http.appendEncodedPathSegments
import io.ktor.http.buildUrl
import io.ktor.http.encodeURLQueryComponent
import io.ktor.http.path

interface UrlFactory {

    operator fun invoke(
        endpoints: Reverso.Endpoints,
        service: Service,
        text: String,
        source: Language,
        target: Language?
    ): Either<ReversoError, Url>

    companion object : UrlFactory {

        override fun invoke(
            endpoints: Reverso.Endpoints,
            service: Service,
            text: String,
            source: Language,
            target: Language?
        ): Either<ReversoError, Url> = either {
            when (service) {
                Service.CONTEXT -> {
                    ensureNotNull(target) {
                        ReversoError.InvalidTargetLanguageError(service, source, target)
                    }
                    buildUrl {
                        protocol = URLProtocol.HTTPS
                        host = endpoints.forService(service)
                        appendEncodedPathSegments(
                            "/translation",
                            "${source.title.lowercase()}-${target.title.lowercase()}",
                            text.encodeURLQueryComponent(encodeFull = true, spaceToPlus = true)
                        )
                    }
                }
                Service.SPELL -> buildUrl {
                    protocol = URLProtocol.HTTPS
                    host = endpoints.forService(service)
                    appendEncodedPathSegments(
                        "/api/v1/Spelling"
                    )
                }
                Service.SYNONYMOUS -> {
                    val language = source.codeSynonymous

                    ensureNotNull(language) {
                        ReversoError.InvalidSourceLanguageError(Service.SPELL, source, null)
                    }
                    buildUrl {
                        protocol = URLProtocol.HTTPS
                        host = endpoints.forService(service)
                        appendEncodedPathSegments(
                            "/synonym",
                            language,
                            text.encodeURLQueryComponent(encodeFull = true, spaceToPlus = true)
                        )
                    }
                }
                Service.TRANSLATION -> buildUrl {
                    protocol = URLProtocol.HTTPS
                    host = endpoints.forService(service)
                    appendEncodedPathSegments(
                        "/translate/v1/translation"
                    )
                }
                Service.CONJUGATION -> buildUrl {
                    protocol = URLProtocol.HTTPS
                    host = buildString {
                        append(endpoints.forService(service))
                        append("/conjugation-")
                        append(source.title)
                        append("-verb-")
                        append(text.encodeURLQueryComponent(encodeFull = true, spaceToPlus = true))
                        append(".html")
                    }
                }
            }
        }
    }
}