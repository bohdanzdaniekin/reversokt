package io.github.mrnemo.reversokt

import io.github.mrnemo.reversokt.entity.ReversoError
import io.github.mrnemo.reversokt.entity.ReversoSuccess
import io.github.mrnemo.reversokt.entity.language.Language
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReversoTest {

    private lateinit var client: HttpClient
    private lateinit var reverso: Reverso

    @BeforeAll
    fun setup() {
        client = HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json()
            }
            Logging {
                level = LogLevel.ALL
            }
            engine {
                config {
                    followRedirects(true)
                }
            }
        }
        reverso = Reverso(Reverso.Endpoints(), client)
    }

    @AfterAll
    fun tearDown() {
        client.close()
    }

    @Test
    fun `should return valid context translations`(): Unit = runBlocking {
        val result = reverso.contextOf("hello", Language.ENGLISH, Language.FRENCH)

        println(result)
        assertTrue(result.isRight())
        val success = result.getOrNull() as ReversoSuccess.Context
        assertEquals("hello", success.text)
        assertTrue(success.translations.isNotEmpty())
    }

    @Test
    fun `should return error on invalid language for context`(): Unit = runBlocking {
        val result = reverso.contextOf("hello", Language.CZECH, Language.FRENCH)
        println(result)
        assertTrue(result.isLeft())
        assertIs<ReversoError.LanguageCompatibilityError>(result.swap().getOrNull())
    }

    @Test
    fun `should return spelling corrections`(): Unit = runBlocking {
        val result = reverso.spellcheckOf("bonjpur", Language.FRENCH)

        println(result)
        assertTrue(result.isRight())
        val success = result.getOrNull() as ReversoSuccess.Spelling
        assertTrue(success.corrections.isNotEmpty())
    }

    @Test
    fun `should return synonyms`(): Unit = runBlocking {
        val result = reverso.synonymousOf("great", Language.ENGLISH)

        println(result)
        assertTrue(result.isRight())
        val success = result.getOrNull() as ReversoSuccess.Synonymous
        assertTrue(success.synonyms.isNotEmpty())
    }

    @Test
    fun `should return translations`(): Unit = runBlocking {
        val result = reverso.translationOf("hello", Language.ENGLISH, Language.SPANISH)

        println(result)
        assertTrue(result.isRight())
        val success = result.getOrNull() as ReversoSuccess.Translation
        assertTrue(success.translations.isNotEmpty())
    }

    @Test
    fun `should return conjugations`(): Unit = runBlocking {
        val result = reverso.conjugationOf("parler", Language.FRENCH)

        println(result)
        assertTrue(result.isRight())
        val success = result.getOrNull() as ReversoSuccess.Conjugation
        assertEquals("parler", success.text)
        assertTrue(success.verbForms.isNotEmpty())
    }
}