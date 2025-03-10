package io.github.mrnemo.reversokt.entity.data.spelling

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Stats(
    @SerialName("textLength")
    val textLength: Int? = null,
    @SerialName("wordCount")
    val wordCount: Int? = null,
    @SerialName("sentenceCount")
    val sentenceCount: Int? = null,
    @SerialName("longestSentence")
    val longestSentence: Int? = null
)