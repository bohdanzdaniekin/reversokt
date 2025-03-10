package io.github.mrnemo.reversokt

import io.github.mrnemo.reversokt.entity.Service
import io.github.mrnemo.reversokt.entity.language.Language
import kotlin.test.Test
import kotlin.test.assertEquals

class UrlTest {

    private val endpoints = Reverso.Endpoints()

    @Test
    fun `test build Url for Context service ENG-FRA`() {
        val source = Language.ENGLISH
        val target = Language.FRENCH
        val text = "hello world"
        val expectedUrl = "https://context.reverso.net/translation/${source.title}-${target.title}/hello+world"

        val result = UrlFactory(endpoints, Service.CONTEXT, text, source, target)

        result.map { url ->
            assertEquals(expectedUrl, url.toString())
        }
    }

    @Test
    fun `test build Url for Context service UKR-ENG`() {
        val source = Language.UKRAINIAN
        val target = Language.ENGLISH
        val text = "Слава Україні!"
        val expectedUrl = "https://context.reverso.net/translation/${source.title}-${target.title}/%D0%A1%D0%BB%D0%B0%D0%B2%D0%B0+%D0%A3%D0%BA%D1%80%D0%B0%D1%97%D0%BD%D1%96%21"

        val result = UrlFactory(endpoints, Service.CONTEXT, text, source, target)

        result.map { url ->
            assertEquals(expectedUrl, url.toString())
        }
    }
}