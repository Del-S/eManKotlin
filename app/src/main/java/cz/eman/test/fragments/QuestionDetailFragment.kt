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
import kotlinx.android.synthetic.main.item_question.*

class QuestionDetailFragment : Fragment() {

    val dbCalls = DBCalls()

    companion object Factory {

        const val ARG_QUESTION_ID = "argQuestionId"

        /**
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

        val questionId = arguments?.getLong(ARG_QUESTION_ID)
        if (questionId != null) {
            val question = dbCalls.loadQuestionById(questionId)

            if(question != null) {
                val dummyDate = activity?.getString(R.string.app_date)

                fqdTitle.text = UtilFunctions.decodeHtmlString(question.title)
                fqdCreatedDate.text = UtilFunctions.createDateString(question.creationDate, dummyDate)
                fqdBody.text = UtilFunctions.decodeHtmlString(question.body)

                val textIsAnswered: String? = if(question.isAnswered)
                    activity?.getString(R.string.fqd_table_is_answered_true)
                else
                    activity?.getString(R.string.fqd_table_is_answered_false)

                fqdIsAnswered.text = textIsAnswered
                fqdAnswerCount.text = question.answerCount.toString()
                fqdViewCount.text = question.viewCount.toString()
                fqdScore.text = question.score.toString()

                // Owner data
                val userImage = question.owner?.profileImage
                if(userImage != null && !userImage.isEmpty()) {
                    Picasso.with(activity).load(userImage).into(fqdImage)
                }
                fqdOwnerName.text = question.owner?.displayName
                fqdOwnerReputation.text = String.format(getString(R.string.fqd_user_reputation),
                        question.owner?.reputation)
            }
        }
    }

}