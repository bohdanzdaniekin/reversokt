package io.github.mrnemo.reversokt

import io.github.mrnemo.reversokt.entity.ReversoError
import io.github.mrnemo.reversokt.entity.ReversoSuccess
import io.github.mrnemo.reversokt.entity.language.Language
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.runBlocking

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReversoTest {

    private lateinit var client: HttpClient
    private lateinit var reverso: Reverso

    @BeforeAll
    fun setup() {
        client = HttpClient(OkHttp) {
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
    fun `should return valid context translations`() = runBlocking {
        val result = reverso.contextOf("hello", Language.ENGLISH, Language.FRENCH)

        assertTrue(result.isRight())
        val success = result.getOrNull() as ReversoSuccess.Context
        assertEquals("hello", success.text)
        assertTrue(success.translations.isNotEmpty())
    }

    @Test
    fun `should return error on invalid language for context`() = runBlocking {
        val result = reverso.contextOf("hello", Language.CZECH, Language.FRENCH)
        assertTrue(result.isLeft())
        assertIs<ReversoError.LanguageCompatibilityError>(result.swap().getOrNull())
    }

    @Test
    fun `should return spelling corrections`() = runBlocking {
        val result = reverso.spellcheckOf("bonjpur", Language.FRENCH)

        assertTrue(result.isRight())
        val success = result.getOrNull() as ReversoSuccess.Spelling
        assertTrue(success.corrections.isNotEmpty())
    }

    @Test
    fun `should return synonyms`() = runBlocking {
        val result = reverso.synonymousOf("great", Language.ENGLISH)

        assertTrue(result.isRight())
        val success = result.getOrNull() as ReversoSuccess.Synonymous
        assertTrue(success.synonyms.isNotEmpty())
    }

    @Test
    fun `should return translations`() = runBlocking {
        val result = reverso.translationOf("hello", Language.ENGLISH, Language.SPANISH)

        assertTrue(result.isRight())
        val success = result.getOrNull() as ReversoSuccess.Translation
        assertTrue(success.translations.isNotEmpty())
    }

    @Test
    fun `should return conjugations`() = runBlocking {
        val result = reverso.conjugationOf("parler", Language.FRENCH)

        assertTrue(result.isRight())
        val success = result.getOrNull() as ReversoSuccess.Conjugation
        assertEquals("parler", success.text)
        assertTrue(success.verbForms.isNotEmpty())
    }
}