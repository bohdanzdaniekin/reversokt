package io.github.mrnemo.reversokt.entity.data.translation

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ContextResults(
    @SerialName("rudeWords") val rudeWords: Boolean? = null,
    @SerialName("colloquialisms") val colloquialisms: Boolean? = null,
    @SerialName("riskyWords") val riskyWords: Boolean? = null,
    @SerialName("results") val results: List<Results> = listOf(),
    @SerialName("totalContextCallsMade") val totalContextCallsMade: Int? = null,
    @SerialName("timeTakenContext") val timeTakenContext: Int? = null
)