package io.github.mrnemo.reversokt.entity.data.translation

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class LanguageDetection(
    @SerialName("detectedLanguage") val detectedLanguage: String? = null,
    @SerialName("isDirectionChanged") val isDirectionChanged: Boolean? = null,
    @SerialName("originalDirection") val originalDirection: String? = null,
    @SerialName("originalDirectionContextMatches") val originalDirectionContextMatches: Int? = null,
    @SerialName("changedDirectionContextMatches") val changedDirectionContextMatches: Int? = null,
    @SerialName("timeTaken") val timeTaken: Int? = null
)