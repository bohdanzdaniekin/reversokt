package io.github.mrnemo.reversokt

import io.github.mrnemo.reversokt.entity.Service
import io.github.mrnemo.reversokt.utils.randomUserAgent
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.headers

interface ReversoHeadersBuilder : (Service, Headers) -> Headers {

    companion object {

        val default = object : ReversoHeadersBuilder {
            override fun invoke(service: Service, headers: Headers): Headers {
                return headers {
                    append(HttpHeaders.Accept, ContentType.Any.toString())
                    append(HttpHeaders.Connection, "keep-alive")
                    append(HttpHeaders.UserAgent, randomUserAgent())
                    appendAll(headers)
                }
            }
        }
    }
}