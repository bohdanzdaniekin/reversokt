package io.github.mrnemo.reversokt.entity.data.translation

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Results(
    @SerialName("translation") val translation: String? = null,
    @SerialName("sourceExamples") val sourceExamples: List<String> = listOf(),
    @SerialName("targetExamples") val targetExamples: List<String> = listOf(),
    @SerialName("rude") val rude: Boolean? = null,
    @SerialName("colloquial") val colloquial: Boolean? = null,
    @SerialName("partOfSpeech") val partOfSpeech: String? = null,
    @SerialName("frequency") val frequency: Int? = null,
    @SerialName("vowels") val vowels: String? = null,
    @SerialName("transliteration") val transliteration: String? = null
)