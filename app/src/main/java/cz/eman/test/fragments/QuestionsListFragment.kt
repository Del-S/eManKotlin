package cz.eman.test.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cz.eman.test.R
import cz.eman.test.activities.QuestionsInterface
import cz.eman.test.api.ApiActions
import cz.eman.test.model.Question
import cz.eman.test.model.adapters.QuestionsAdapter
import kotlinx.android.synthetic.main.fragment_questions_list.*
import utils.SimpleDividerItemDecoration

class QuestionsListFragment : Fragment() {

    private lateinit var mSearch: SearchView
    private lateinit var mEmpty: TextView
    private lateinit var mQuestionsList: RecyclerView
    private var mAdapter: QuestionsAdapter? = null
    private var mActivityInterface: QuestionsInterface? = null
    private val mApiActions = ApiActions.instance

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        // Check if startup activity implements listener
        try {
            mActivityInterface = context as QuestionsInterface?
        } catch (e: Exception) {
            throw ClassCastException(context!!.javaClass.toString() + " must implement QuestionsInterface")
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_questions_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mSearch = fqlSearch
        mEmpty = fqlEmpty
        mQuestionsList = fqlList

        // Get data and initiate list adapter
        mAdapter = QuestionsAdapter(activity)

        // Recycler view for employees
        val divider = ContextCompat.getDrawable(activity!!, R.drawable.row_with_divider)
        mQuestionsList.layoutManager = LinearLayoutManager(activity)
        mQuestionsList.addItemDecoration(SimpleDividerItemDecoration(divider!!))
        mQuestionsList.adapter = mAdapter

        // Enable searching by name and email.
        mSearch.setOnClickListener({ mSearch.isIconified = false })
        mSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // filter recycler view when query submitted
                mAdapter?.filter?.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // filter recycler view when text is changed
                mAdapter?.filter?.filter(newText)
                return false
            }
        })

        mActivityInterface?.loadQuestions()
    }

    /**
     * @param questions to display
     */
    fun displayQuestions(questions: MutableList<Question>?) {
        // Display recycler view or empty list message
        if (questions != null && questions.isEmpty()) {
            mEmpty.visibility = View.VISIBLE
            mQuestionsList.visibility = View.GONE
            mApiActions.downloadQuestions(this::displayQuestions)
        } else {
            mEmpty.visibility = View.GONE
            mQuestionsList.visibility = View.VISIBLE
        }

        mAdapter?.displayQuestions(questions)
    }
}