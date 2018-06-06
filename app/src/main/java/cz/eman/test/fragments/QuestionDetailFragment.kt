package cz.eman.test.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cz.eman.test.R
import kotlinx.android.synthetic.main.fragment_question_detail.*

class QuestionDetailFragment : Fragment() {

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

        fqdTitle.text = arguments?.getLong(ARG_QUESTION_ID).toString()
    }

}