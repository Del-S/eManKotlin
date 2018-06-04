package cz.eman.test.database

import cz.eman.test.model.Question

interface DBInterface {
    fun saveQuestions(questions: List<Question>): Int
    fun saveQuestion(question: Question): Boolean
    fun loadQuestions(): MutableList<Question>
}