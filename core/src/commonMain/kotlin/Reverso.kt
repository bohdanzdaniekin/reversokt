package io.github.mrnemo.reversokt

import arrow.core.Either
import arrow.core.raise.catch
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Element
import io.github.mrnemo.reversokt.entity.ContextExample
import io.github.mrnemo.reversokt.entity.ContextPhrase
import io.github.mrnemo.reversokt.entity.Correction
import io.github.mrnemo.reversokt.entity.Example
import io.github.mrnemo.reversokt.entity.ReversoError
import io.github.mrnemo.reversokt.entity.ReversoSuccess
import io.github.mrnemo.reversokt.entity.Service
import io.github.mrnemo.reversokt.entity.VerbForm
import io.github.mrnemo.reversokt.entity.available
import io.github.mrnemo.reversokt.entity.data.spellcheck.SpellcheckBody
import io.github.mrnemo.reversokt.entity.data.spelling.ReversoSpellingResponse
import io.github.mrnemo.reversokt.entity.data.translation.Results
import io.github.mrnemo.reversokt.entity.data.translation.ReversoTranslationResponse
import io.github.mrnemo.reversokt.entity.data.translation.TranslationBody
import io.github.mrnemo.reversokt.entity.language.Language
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.client.utils.buildHeaders
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.Url
import io.ktor.http.isSuccess
import io.ktor.util.encodeBase64

class Reverso(
    private val endpoints: Endpoints,
    private val client: HttpClient,
    private val urlFactory: UrlFactory = UrlFactory,
    private val headersFactory: HeadersFactory = HeadersFactory
) : AbstractReverso {

    data class Endpoints(
        private val context: String = """context.reverso.net""",
        private val spellcheck: String = """orthographe.reverso.net""",
        private val synonymous: String = """synonyms.reverso.net""",
        private val translation: String = """api.reverso.net""",
        val voice: String = """voice.reverso.net/RestPronunciation.svc/v1/output=json/GetVoiceStream""",
        private val conjugation: String = """conjugator.reverso.net""",
    ) {

        fun forService(service: Service): String = when (service) {
            Service.CONTEXT -> context
            Service.SPELL -> spellcheck
            Service.SYNONYMOUS -> synonymous
            Service.TRANSLATION -> translation
            Service.CONJUGATION -> conjugation
        }
    }

    override suspend fun contextOf(
        text: String,
        source: Language,
        target: Language
    ): Either<ReversoError, ReversoSuccess.Context> = either {
        ensure(source.compatibleWith(Service.CONTEXT, target)) {
            ReversoError.LanguageCompatibilityError(Service.CONTEXT, source, target)
        }

        val document = request(
            method = HttpMethod.Get,
            url = urlFactory(endpoints, Service.CONTEXT, text, source, target).bind(),
            headers = headersFactory(Service.CONTEXT, Headers.Empty).bind(),
        ).map { response ->
            response.request.headers
            ensure(response.status.isSuccess()) {
                ReversoError.ResponseError(
                    service = Service.CONTEXT,
                    source = source,
                    target = target,
                    headers = response.request.headers,
                    url = response.request.url,
                    value = response.status.value,
                    description = response.status.description
                )
            }

            response.bodyAsText()
        }.map(Ksoup::parse).bind()

        val sourceExamples = document
            .select(".example > div.src.${source.direction.title} > span.text")

        val targetExamples = document
            .select(".example > div.trg.${target.direction.title} > span.text")

        val translations = document
            .select("#translations-content span.display-term")
            .eachText()
            .map(String::trim)

        val examples = sourceExamples.mapIndexed { index, example ->
            Example(
                id = index.toString(),
                source = example.text().trim(),
                target = targetExamples.getOrNull(index)?.text()?.trim().orEmpty()
            )
        }

        ReversoSuccess.Context(
            text = text,
            source = source,
            target = target,
            translations = translations,
            examples = examples
        )
    }

    override suspend fun spellcheckOf(text: String, source: Language): Either<ReversoError, ReversoSuccess.Spelling> =
        either {
            ensure(source.isAvailableFor(Service.SPELL)) {
                ReversoError.LanguageAvailabilityError(Service.SPELL, source)
            }

            val language = Service.SPELL
                .available
                .firstOrNull { language -> language == source }
                ?.shortName

            ensureNotNull(language) {
                ReversoError.InvalidSourceLanguageError(Service.SPELL, source, null)
            }

            val response = request(
                method = HttpMethod.Post,
                url = urlFactory(endpoints, Service.SPELL, text, source, null).bind(),
                headers = headersFactory(
                    Service.SPELL,
                    buildHeaders { append(HttpHeaders.ContentType, ContentType.Application.Json.toString()) }
                ).bind(),
                body = SpellcheckBody(
                    language = language,
                    text = text
                )
            ).map { response ->
                ensure(response.status.isSuccess()) {
                    ReversoError.ResponseError(
                        service = Service.SPELL,
                        source = source,
                        headers = response.request.headers,
                        url = response.request.url,
                        value = response.status.value,
                        description = response.status.description
                    )
                }

                val body = response.body<ReversoSpellingResponse?>()

                ensureNotNull(body) {
                    ReversoError.EmptyResponseError(
                        service = Service.SPELL,
                        source = source,
                        target = null,
                        text = text
                    )
                }

                body
            }.bind()

            // TODO: Extend the response with related data
            ReversoSuccess.Spelling(
                text = text,
                source = source,
                stats = response.stats,
                sentences = response.sentences,
                corrections = response.corrections.mapIndexed { index, correction ->
                    Correction(
                        id = index.toLong(),
                        text = text,
                        type = correction.type.orEmpty(),
                        explanation = correction.longDescription.orEmpty(),
                        corrected = correction.correctionText.orEmpty(),
                        suggestions = correction.suggestions,
                    )
                }
            )
        }

    override suspend fun synonymousOf(text: String, source: Language): Either<ReversoError, ReversoSuccess.Synonymous> =
        either {
            ensure(source.isAvailableFor(Service.SYNONYMOUS)) {
                ReversoError.LanguageAvailabilityError(Service.SYNONYMOUS, source)
            }

            val language = Service.SYNONYMOUS
                .available
                .firstOrNull { language -> language == source }
                ?.codeSynonymous

            ensureNotNull(language) {
                ReversoError.InvalidSourceLanguageError(Service.SPELL, source, null)
            }

            val document = request(
                method = HttpMethod.Get,
                url = urlFactory(endpoints, Service.SYNONYMOUS, text, source, null).bind(),
                headers = headersFactory(Service.SYNONYMOUS, Headers.Empty).bind(),
            ).map { response ->
                ensure(response.status.isSuccess()) {
                    ReversoError.ResponseError(
                        service = Service.SYNONYMOUS,
                        source = source,
                        headers = response.request.headers,
                        url = response.request.url,
                        value = response.status.value,
                        description = response.status.description
                    )
                }

                response.bodyAsText()
            }.map(Ksoup::parse).bind()

            val synonymous = document
                .select("a.synonym.relevant")
                .map(Element::text)

            ensure(synonymous.isNotEmpty()) {
                ReversoError.EmptyResponseError(
                    service = Service.SYNONYMOUS,
                    source = source,
                    text = text
                )
            }

            ReversoSuccess.Synonymous(
                text = text,
                source = source,
                synonyms = synonymous
            )
        }

    override suspend fun translationOf(
        text: String,
        source: Language,
        target: Language
    ): Either<ReversoError, ReversoSuccess.Translation> = either {
        ensure(source.compatibleWith(Service.TRANSLATION, target)) {
            ReversoError.LanguageCompatibilityError(Service.TRANSLATION, source, target)
        }

        val from = Service.TRANSLATION
            .available
            .firstOrNull { language -> language == source }
            ?.codeTranslation

        ensureNotNull(from) {
            ReversoError.InvalidSourceLanguageError(Service.TRANSLATION, source, target)
        }

        val to = Service.TRANSLATION
            .available
            .firstOrNull { language -> language == target }
            ?.codeTranslation

        ensureNotNull(to) {
            ReversoError.InvalidTargetLanguageError(Service.TRANSLATION, source, target)
        }

        val response = request(
            method = HttpMethod.Post,
            url = urlFactory(endpoints, Service.TRANSLATION, text, source, target).bind(),
            headers = headersFactory(
                Service.TRANSLATION,
                buildHeaders { append(HttpHeaders.ContentType, ContentType.Application.Json.toString()) }
            ).bind(),
            body = TranslationBody(input = text, from = from, to = to)
        ).map { response ->
            ensure(response.status.isSuccess()) {
                ReversoError.ResponseError(
                    service = Service.TRANSLATION,
                    source = source,
                    target = target,
                    headers = response.request.headers,
                    url = response.request.url,
                    value = response.status.value,
                    description = response.status.description
                )
            }

            val body = response.body<ReversoTranslationResponse?>()

            ensureNotNull(body) {
                ReversoError.EmptyResponseError(
                    service = Service.SPELL,
                    source = source,
                    target = null,
                    text = text
                )
            }

            body
        }.bind()

        val contextTranslations = response.contextResults?.results?.mapNotNull(Results::translation).orEmpty()
        val translation = response.translations.firstOrNull()?.encodeBase64()

        val voice = if (target.voice != null && translation != null) {
            "${endpoints.voice}/voiceName=${target.voice}?inputText=$translation"
        } else null

        fun matchContextPhrases(element: String): List<ContextPhrase> {
            return "<em>(.*?)</em>".toRegex().findAll(element).map { match ->
                ContextPhrase(
                    phrase = match.groupValues[1],
                    index = match.range.first,
                    length = match.groupValues[1].length
                )
            }.toList()
        }

        val contextResult = response.contextResults?.results?.firstOrNull()
        val contextExamples = if (contextResult != null) {
            val sourceExamples = contextResult.sourceExamples
            val targetExamples = contextResult.targetExamples
            val regex = "<[^>]*>".toRegex(RegexOption.IGNORE_CASE)
            sourceExamples.mapIndexed { index, example ->
                val sourcePhrases = matchContextPhrases(example)
                val targetPhrases = matchContextPhrases(targetExamples[index])
                ContextExample(
                    id = index.toString(),
                    source = example.replace(regex, ""),
                    target = targetExamples[index].replace(regex, ""),
                    sourcePhrases = sourcePhrases,
                    targetPhrases = targetPhrases
                )
            }
        } else null

        ReversoSuccess.Translation(
            text = text,
            source = source,
            target = target,
            translations = response.translations + contextTranslations,
            detectedLanguage = response.languageDetection?.detectedLanguage,
            voice = voice,
            examples = contextExamples,
            rude = response.contextResults?.results?.firstOrNull()?.rude
        )
    }

    override suspend fun conjugationOf(
        text: String,
        source: Language
    ): Either<ReversoError, ReversoSuccess.Conjugation> = either {
        ensure(source.isAvailableFor(Service.CONJUGATION)) {
            ReversoError.LanguageAvailabilityError(Service.CONJUGATION, source)
        }

        val document = request(
            method = HttpMethod.Get,
            url = urlFactory(endpoints, Service.CONJUGATION, text, source, null).bind(),
            headers = headersFactory(Service.CONJUGATION, Headers.Empty).bind(),
        ).map { response ->
            ensure(response.status.isSuccess()) {
                ReversoError.ResponseError(
                    service = Service.CONJUGATION,
                    source = source,
                    headers = response.request.headers,
                    url = response.request.url,
                    value = response.status.value,
                    description = response.status.description
                )
            }
            response.bodyAsText()
        }.map(Ksoup::parse).bind()

        val verbForms = document.select("div[class=\"blue-box-wrap\"]").mapIndexed { index, element ->
            val header = element.attr("mobile-title").trim()

            val suffix = if (source in listOf(Language.RUSSIAN, Language.HEBREW, Language.ARABIC)) "-term" else ""
            val selector = "i.verbtxt$suffix"
            val verbs = element.select(selector).mapNotNull { word ->
                if (word.parents().select(".transliteration").isEmpty()) {
                    word.text()
                } else {
                    null
                }
            }
            VerbForm(
                id = index,
                conjugation = header,
                verbs = verbs
            )
        }

        val infinitive = document.select("#ch_lblVerb").text()

        ReversoSuccess.Conjugation(
            text = text,
            source = source,
            infinitive = infinitive,
            verbForms = verbForms
        )
    }

    private suspend fun request(
        method: HttpMethod = HttpMethod.Get,
        url: Url,
        headers: Headers,
    ): Either<ReversoError, HttpResponse> = request<Any?>(method, url, headers, null)

    private suspend inline fun <reified T> request(
        method: HttpMethod = HttpMethod.Get,
        url: Url,
        headers: Headers,
        body: T? = null,
    ): Either<ReversoError, HttpResponse> = either {
        catch({
            client.request {
                this.method = method
                url(url)
                this.headers.appendAll(headers)
                if (body != null) setBody(body)
                if (body != null) println(body.toString())
            }
        }) { error ->
            raise(ReversoError.RequestError(error))
        }
    }
}