package io.github.mrnemo.reversokt.entity.data.spelling

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Correction(
    @SerialName("group")
    val group: String? = null,
    @SerialName("type")
    val type: String? = null,
    @SerialName("shortDescription")
    val shortDescription: String? = null,
    @SerialName("longDescription")
    val longDescription: String? = null,
    @SerialName("startIndex")
    val startIndex: Int? = null,
    @SerialName("endIndex")
    val endIndex: Int? = null,
    @SerialName("mistakeText")
    val mistakeText: String? = null,
    @SerialName("correctionText")
    val correctionText: String? = null,
    @SerialName("suggestions")
    val suggestions: List<Suggestion> = listOf()
)