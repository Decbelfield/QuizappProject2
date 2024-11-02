package com.example.quizappproject2
enum class QuestionType {
    MULTIPLE_CHOICE,
    MULTIPLE_ANSWERS
}

data class Question(
    val text: String,
    val options: List<String>,
    val correctAnswer: String? = null,
    val correctAnswers: List<String> = emptyList(),
    val type: QuestionType
)
