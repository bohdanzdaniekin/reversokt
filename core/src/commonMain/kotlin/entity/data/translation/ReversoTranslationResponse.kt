package io.github.mrnemo.reversokt.entity.data.translation

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ReversoTranslationResponse(
    @SerialName("id") val id: String? = null,
    @SerialName("from") val from: String? = null,
    @SerialName("to") val to: String? = null,
    @SerialName("input") val input: List<String> = listOf(),
    @SerialName("correctedText") val correctedText: String? = null,
    @SerialName("translation") val translations: List<String> = listOf(),
    @SerialName("engines") val engines: List<String> = listOf(),
    @SerialName("languageDetection") val languageDetection: LanguageDetection? = LanguageDetection(),
    @SerialName("contextResults") val contextResults: ContextResults? = ContextResults(),
    @SerialName("truncated") val truncated: Boolean? = null,
    @SerialName("timeTaken") val timeTaken: Int? = null
)