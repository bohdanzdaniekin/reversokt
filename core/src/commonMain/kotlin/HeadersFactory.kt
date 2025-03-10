package io.github.mrnemo.reversokt

import arrow.core.Either
import arrow.core.raise.either
import io.github.mrnemo.reversokt.entity.ReversoError
import io.github.mrnemo.reversokt.entity.Service
import io.github.mrnemo.reversokt.utils.randomUserAgent
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.headers

interface HeadersFactory {

    operator fun invoke(service: Service, headers: Headers = Headers.Empty): Either<ReversoError, Headers>

    companion object : HeadersFactory {

        override fun invoke(service: Service, headers: Headers): Either<ReversoError, Headers> = either {
            headers {
                append(HttpHeaders.Accept, ContentType.Any.toString())
                append(HttpHeaders.Connection, "keep-alive")
                append(HttpHeaders.UserAgent, randomUserAgent())
                appendAll(headers)
            }
        }
    }
}