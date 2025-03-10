package io.github.mrnemo.reversokt.entity

data class ContextExample(
    val id: String,
    val source: String,
    val target: String,
    val sourcePhrases: List<ContextPhrase>,
    val targetPhrases: List<ContextPhrase>
)