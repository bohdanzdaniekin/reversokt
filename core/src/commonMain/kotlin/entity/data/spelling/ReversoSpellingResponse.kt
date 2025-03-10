package io.github.mrnemo.reversokt.entity.data.spelling

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReversoSpellingResponse(
    @SerialName("id")
    val id: String? = null,
    @SerialName("language")
    val language: String? = null,
    @SerialName("text")
    val text: String? = null,
    @SerialName("engine")
    val engine: String? = null,
    @SerialName("truncated")
    val truncated: Boolean? = null,
    @SerialName("timeTaken")
    val timeTaken: Int? = null,
    @SerialName("corrections")
    val corrections: List<Correction> = listOf(),
    @SerialName("sentences")
    val sentences: List<Sentence> = listOf(),
    @SerialName("autoReplacements")
    val autoReplacements: List<String> = listOf(),
    @SerialName("stats")
    val stats: Stats = Stats()
)