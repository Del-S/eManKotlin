package cz.eman.test.database

import cz.eman.test.model.Question

/**
 * Interface between the Application and DBFlow.
 * - Implemented are only actions needed in this application.
 */
interface DBInterface {
    /**
     * Save List of questions to the database.
     *
     * @return Int count of saved questions
     */
    fun saveQuestions(questions: List<Question>): Int

    /**
     * Save single question to the database.
     *
     * @return Boolean if question was saved or not
     */
    fun saveQuestion(question: Question): Boolean

    /**
     * Load List of questions from the database.
     *
     * @return MutableList<Question>
     */
    fun loadQuestions(): MutableList<Question>

    /**
     * Load Question by specific ID.
     *
     * @return Question?
     */
    fun loadQuestionById(questionId: Long): Question?
}