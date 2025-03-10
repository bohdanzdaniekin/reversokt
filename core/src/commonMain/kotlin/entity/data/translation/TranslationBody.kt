package io.github.mrnemo.reversokt.entity.data.translation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TranslationBody(
    @SerialName("input")
    val input: String,
    @SerialName("from")
    val from: String,
    @SerialName("to")
    val to: String,
    @SerialName("format")
    val format: String = "text",
    @SerialName("options")
    val options: Options = Options()
)

@Serializable
data class Options(
    @SerialName("origin")
    val origin: String = "reversomobile",
    @SerialName("sentenceSplitter")
    val sentenceSplitter: Boolean = false,
    @SerialName("contextResults")
    val contextResults: Boolean = true,
    @SerialName("languageDetection")
    val languageDetection: Boolean = true
)
