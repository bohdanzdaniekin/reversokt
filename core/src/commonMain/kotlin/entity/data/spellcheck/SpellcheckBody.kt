package io.github.mrnemo.reversokt.entity.data.spellcheck

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpellcheckBody(
    @SerialName("language")
    val language: String,
    @SerialName("getCorrectionDetails")
    val getCorrectionDetails: Boolean = true,
    @SerialName("origin")
    val origin: String = "interactive",
    @SerialName("text")
    val text: String
)
