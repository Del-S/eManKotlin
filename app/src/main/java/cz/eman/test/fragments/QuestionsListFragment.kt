package cz.eman.test.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
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
import cz.eman.test.utils.SimpleDividerItemDecoration

/**
 * Fragment to display Question list.
 * - Also can trigger download.
 * - Sends new questions to the Adapter.
 */
class QuestionsListFragment : Fragment() {

    // Views for this fragment
    private lateinit var mSearch: SearchView            // View for searching in the list
    private lateinit var mEmpty: TextView               // Text for empty list
    private lateinit var mQuestionsList: RecyclerView   // RecyclerView to display questions
    private lateinit var mApiActions: ApiActions        // Interface with API to download new data
    private lateinit var mActivityInterface: QuestionsInterface  // Interface with Activity
    private var mAdapter: QuestionsAdapter? = null      // Adapter used to display question in the list

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if(context != null) {
            mApiActions = ApiActions.getInstance(context)   // Initiates API connection instance

            // Initiate activity interface or throw an Exception
            try {
                mActivityInterface = context as QuestionsInterface
            } catch (e: Exception) {
                throw ClassCastException(context.javaClass.toString() + " must implement QuestionsInterface")
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_questions_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initiate widgets (used due to lateinit)
        mSearch = fqlSearch
        mEmpty = fqlEmpty
        mQuestionsList = fqlList

        // Get data and initiate list adapter
        mAdapter = QuestionsAdapter(activity)

        // Recycler view for questions
        val divider = ContextCompat.getDrawable(activity!!, R.drawable.row_with_divider)
        mQuestionsList.layoutManager = LinearLayoutManager(activity)
        mQuestionsList.addItemDecoration(SimpleDividerItemDecoration(divider!!))
        mQuestionsList.adapter = mAdapter

        // Enable searching by title and owner name.
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

        // Call Activity to load questions
        mActivityInterface.loadQuestions()
    }

    /**
     * Display questions from database.
     * If this list is empty then try to download new ones from API.
     *
     * @param questions to display
     */
    fun displayDatabaseQuestions(questions: MutableList<Question>?) {
        if (questions != null && questions.isEmpty()) {
            // Display empty list message and hide the list
            mEmpty.visibility = View.VISIBLE
            mQuestionsList.visibility = View.GONE

            // Try to download new questions from the API
            mApiActions.downloadQuestions(this::displayQuestions,
                    this::displayDownloadError)
        } else {
            // Display questions in the list
            displayQuestions(questions)
        }
    }

    /**
     * Display questions by sending them to the Adapter.
     *
     * @param questions to display
     */
    private fun displayQuestions(questions: MutableList<Question>?) {
        if(mQuestionsList.visibility == View.GONE) {
            // Display recycler view and hide empty message
            mEmpty.visibility = View.GONE
            mQuestionsList.visibility = View.VISIBLE
        }

        // Send questions to display in the adapter
        mAdapter?.displayQuestions(questions)
    }

    /**
     * Display download error in the Activity.
     */
    fun displayDownloadError() {
        mActivityInterface.displayDownloadError()
    }
}