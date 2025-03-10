package io.github.mrnemo.reversokt.entity.data.spelling

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Suggestion(
    @SerialName("text")
    val text: String? = null,
    @SerialName("category")
    val category: String? = null
)