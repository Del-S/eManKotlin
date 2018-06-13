package cz.eman.test.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import cz.eman.test.R
import cz.eman.test.database.DBCalls
import kotlinx.android.synthetic.main.fragment_question_detail.*
import cz.eman.test.utils.UtilFunctions

/**
 * Fragment to display Question information.
 */
class QuestionDetailFragment : Fragment() {

    private val dbCalls = DBCalls() // Class used to load Questions from the database

    companion object Factory {
        const val ARG_QUESTION_ID = "argQuestionId"     // Question ID is used as a parameter to display specific question

        /**
         * Create an instance of the Fragment with a parameter.
         * - Parameter is saved in the arguments.
         *
         * @param questionId to save into args
         * @return instance of this fragment
         */
        fun newInstance(questionId: Long): QuestionDetailFragment {
            val args = Bundle()
            args.putLong(ARG_QUESTION_ID, questionId)

            val fragment = QuestionDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_question_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val questionId = arguments?.getLong(ARG_QUESTION_ID)    // Load Question ID from the arguments
        if (questionId != null) {
            val question = dbCalls.loadQuestionById(questionId) // Load Question from the Database based on ID

            // Display question information
            if(question != null) {
                val dummyDate = activity?.getString(R.string.app_date)     // Load dummy date
                // Check if the Question is answered and set this variable
                val textIsAnswered: String? = if(question.isAnswered)
                    activity?.getString(R.string.fqd_table_is_answered_true)
                else
                    activity?.getString(R.string.fqd_table_is_answered_false)
                val ownerImage = question.owner?.profileImage    // Load owner image

                // Setting data for the question block
                fqdTitle.text = UtilFunctions.decodeHtmlString(question.title)  // Set question title (HTML decoded)
                fqdCreatedDate.text = UtilFunctions.createDateString(question.creationDate, dummyDate)  // Set creation date
                fqdBody.text = UtilFunctions.decodeHtmlString(question.body)    // Set body of the question to the detail (HTML decoded)
                fqdIsAnswered.text = textIsAnswered     // Set answered text
                fqdAnswerCount.text = question.answerCount.toString()   // Set answer count
                fqdViewCount.text = question.viewCount.toString()       // Set view Count
                fqdScore.text = question.score.toString()               // Set question score

                // Setting question owner data
                if(ownerImage != null && !ownerImage.isEmpty()) {
                    // Picasso loads data from cache or URL and displays it
                    Picasso.with(activity).load(ownerImage).into(fqdImage)
                }
                fqdOwnerName.text = question.owner?.displayName     // Set owner display name
                fqdOwnerReputation.text = String.format(getString(R.string.fqd_user_reputation),
                        question.owner?.reputation)     // Set owner reputation
            }
        }
    }

}