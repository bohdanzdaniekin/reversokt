package io.github.mrnemo.reversokt

import io.github.mrnemo.reversokt.entity.ReversoError
import io.github.mrnemo.reversokt.entity.Service
import io.github.mrnemo.reversokt.entity.language.Language
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.serialization.kotlinx.json.json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class UrlFactoryTest {

    private val endpoints = Reverso.Endpoints()

    @Test
    fun `should create valid Context URL for ENG-FRA`() {
        val source = Language.ENGLISH
        val target = Language.FRENCH
        val text = "hello world"
        val expectedUrl = "https://context.reverso.net/translation/${source.title}-${target.title}/hello+world"

        val result = UrlFactory(endpoints, Service.CONTEXT, text, source, target)

        assertTrue(result.isRight())
        result.map { url ->
            assertEquals(expectedUrl, url.toString())
        }
    }

    @Test
    fun `should create valid Context URL for UKR-ENG`() {
        val source = Language.UKRAINIAN
        val target = Language.ENGLISH
        val text = "Слава Україні!"
        val expectedUrl =
            "https://context.reverso.net/translation/${source.title}-${target.title}/%D0%A1%D0%BB%D0%B0%D0%B2%D0%B0+%D0%A3%D0%BA%D1%80%D0%B0%D1%97%D0%BD%D1%96%21"

        val result = UrlFactory(endpoints, Service.CONTEXT, text, source, target)

        assertTrue(result.isRight())
        result.map { url ->
            assertEquals(expectedUrl, url.toString())
        }
    }

    @Test
    fun `should return error for CONTEXT without target`() {
        val source = Language.ENGLISH
        val result = UrlFactory.invoke(endpoints, Service.CONTEXT, "hello", source, null)
        assertTrue(result.isLeft())
        result.mapLeft<ReversoError.InvalidTargetLanguageError>(::assertIs)
    }

    @Test
    fun `should create valid SPELL URL`() {
        val result = UrlFactory.invoke(
            endpoints = endpoints,
            service = Service.SPELL,
            text = "example",
            source = Language.ENGLISH,
            target = null
        )

        assertTrue(result.isRight())
        result.map { url ->
            assertEquals("https://orthographe.reverso.net/api/v1/Spelling", url.toString())
        }
    }

    @Test
    fun `should create valid SYNONYMOUS URL`() {
        val result = UrlFactory.invoke(
            endpoints,
            Service.SYNONYMOUS,
            "example",
            Language.ENGLISH,
            null
        )

        assertTrue(result.isRight())
        result.map { url ->
            assertEquals("https://synonyms.reverso.net/synonym/en/example", url.toString())
        }
    }

    @Test
    fun `should return error for SYNONYMOUS with invalid source`() {
        val source = Language.UKRAINIAN
        val result = UrlFactory.invoke(endpoints, Service.SYNONYMOUS, "happy", source, null)
        assertTrue(result.isLeft())
        result.mapLeft<ReversoError.InvalidSourceLanguageError>(::assertIs)
    }

    @Test
    fun `should create valid TRANSLATION URL`() {
        val result = UrlFactory.invoke(
            endpoints,
            Service.TRANSLATION,
            "example",
            Language.ENGLISH,
            null
        )

        assertTrue(result.isRight())
        result.map { url ->
            assertEquals("https://api.reverso.net/translate/v1/translation", url.toString())
        }
    }

    @Test
    fun `should create valid CONJUGATION URL`() {
        val result = UrlFactory.invoke(
            endpoints,
            Service.CONJUGATION,
            "example",
            Language.ENGLISH,
            null
        )

        assertTrue(result.isRight())
        result.map { url ->
            assertEquals("https://conjugator.reverso.net/conjugation-english-verb-example.html", url.toString())
        }
    }
}