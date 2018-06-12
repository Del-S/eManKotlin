package cz.eman.test.activities

/**
 * Interface between Question Activity and Fragment/Adapter
 */
interface QuestionsInterface {
    /**
     * Initiates loading questions from the database.
     * Fragment calls this function after view is created.
     */
    fun loadQuestions()

    /**
     * Triggers function on click to specific Question
     * in the list.
     */
    fun onQuestionClick(questionId: Long)

    /**
     * Triggers a display of download error in the Activity.
     */
    fun displayDownloadError()
}