package io.github.mrnemo.reversokt.entity.data.spelling

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Sentence(
    @SerialName("startIndex")
    val startIndex: Int? = null,
    @SerialName("endIndex")
    val endIndex: Int? = null,
    @SerialName("status")
    val status: String? = null
)