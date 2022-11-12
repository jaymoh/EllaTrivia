package com.hackinroms.ellatrivia.models

data class QuestionItem(
    val answer: String,
    val category: String,
    val choices: List<String>,
    val question: String
)