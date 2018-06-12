package cz.eman.test.database

import com.raizlabs.android.dbflow.kotlinextensions.*
import cz.eman.test.model.Question
import cz.eman.test.model.Question_Table.id

/**
 * Implementation of Database calls specified in DBInterface.
 */
class DBCalls: DBInterface {

    /**
     * Save List of questions to the database.
     *
     * @return Int count of saved questions
     */
    override fun saveQuestions(questions: List<Question>): Int {
        var i = 0
        for(q in questions)
            if (saveQuestion(q)) i++
        return i
    }

    /**
     * Save single question to the database.
     *
     * @return Boolean if question was saved or not
     */
    override fun saveQuestion(question: Question): Boolean {
        if(!question.exists())
            return question.save()
        return false
    }

    /**
     * Load List of questions from the database.
     *
     * @return MutableList<Question>
     */
    override fun loadQuestions(): MutableList<Question> {
        return (select from Question::class).list
    }

    /**
     * Load Question by specific ID.
     *
     * @return Question?
     */
    override fun loadQuestionById(questionId: Long): Question? {
        return (select from Question::class).where(id eq questionId).querySingle()
    }
}