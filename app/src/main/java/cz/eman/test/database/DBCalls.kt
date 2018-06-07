package cz.eman.test.database

import com.raizlabs.android.dbflow.kotlinextensions.*
import cz.eman.test.model.Question
import cz.eman.test.model.Question_Table.id

class DBCalls: DBInterface {
    override fun saveQuestions(questions: List<Question>): Int {
        var i = 0
        for(q in questions)
            if (saveQuestion(q)) i++
        return i
    }

    override fun saveQuestion(question: Question): Boolean {
        if(!question.exists())
            return question.save()
        return false
    }

    override fun loadQuestions(): MutableList<Question> {
        return (select from Question::class).list
    }

    override fun loadQuestionById(questionId: Long): Question? {
        return (select from Question::class).where(id eq questionId).querySingle()
    }
}