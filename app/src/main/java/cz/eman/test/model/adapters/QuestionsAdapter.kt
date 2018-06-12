package cz.eman.test.model.adapters

import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso
import cz.eman.test.R
import cz.eman.test.activities.QuestionsInterface
import cz.eman.test.api.ApiActions
import cz.eman.test.model.Question
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_question.*
import cz.eman.test.utils.UtilFunctions
import java.util.*

/**
 * Adapter showing Questions in the RecyclerView.
 * - Enables filtering.
 * - Initiates download of new Questions.
 */
class QuestionsAdapter(private val mActivity: FragmentActivity?) :
        RecyclerView.Adapter<QuestionsAdapter.ViewHolder>(), Filterable {

    private val mInflater: LayoutInflater = LayoutInflater.from(mActivity)  // Inflater to inflate views with
    private val mQuestions: MutableList<Question> = ArrayList()             // List of all questions
    private var mQuestionsFiltered:MutableList<Question> = ArrayList()      // Filtered list of questions
    private lateinit var mActivityInterface: QuestionsInterface             // Interface with Activity
    private lateinit var mApiActions: ApiActions                            // Interface with API to download new data

    init {
        if(mActivity != null) {
            mApiActions = ApiActions.getInstance(mActivity.applicationContext)  // Initiates API connection instance

            // Initiate activity interface or throw an Exception
            try {
                mActivityInterface = mActivity as QuestionsInterface
            } catch (e: Exception) {
                throw ClassCastException(mActivity.javaClass.name + " must implement QuestionsInterface")
            }
        }
    }

    /**
     * Counts item in the list. It is count in filtered list or in all questions increased by one.
     * Increasing by one enables to display message about no end to the list (message about
     * downloading new questions)
     *
     * @return Int count of questions
     */
    override fun getItemCount(): Int {
        if(mQuestions.size == mQuestionsFiltered.size) {
            return mQuestions.size + 1
        }
        return mQuestionsFiltered.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflating single item view
        val view = mInflater.inflate(R.layout.item_question, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Check if we are at the end of the list
        if(position >= mQuestions.size) {
            // Display end of the list information
            holder.iqItemDownloadInfo.visibility = View.VISIBLE
            holder.iqItemInfo.visibility = View.GONE

            // Load new questions
            if(position >= mQuestions.size)
                mApiActions.downloadQuestions(this::displayQuestions,
                        this::displayDownloadError)
        } else {
            // Display information about question
            holder.iqItemDownloadInfo.visibility = View.GONE
            holder.iqItemInfo.visibility = View.VISIBLE

            val question = mQuestionsFiltered[position]             // Load question information
            val dummyDate = mActivity?.getString(R.string.app_date)                              // Load dummy date
            val ownerImage = question.owner?.profileImage            // Load owner profile

            // Binding text views to the holder
            holder.bind(
                    UtilFunctions.decodeHtmlString(question.title),
                    question.owner?.displayName,
                    UtilFunctions.createDateString(question.creationDate, dummyDate))

            // Display owner image
            if (ownerImage != null && !ownerImage.isEmpty()) {
                Picasso.with(mActivity).load(ownerImage).into(holder.iqImage)
            }

            // Display question detail on item click
            holder.itemView.setOnClickListener({ mActivityInterface.onQuestionClick(question.id) })
        }
    }

    /**
     * Enables filtering in the list.
     * - Filtered by Question title and Owner name.
     */
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                // Get filter string
                val charString = charSequence.toString()
                mQuestionsFiltered = if (charString.isEmpty()) {
                    // Return original list
                    mQuestions
                } else {
                    val filteredList = ArrayList<Question>()
                    for (question in mQuestions) {
                        // Check if question title and owner display name matches search conditions
                        if (question.title.toLowerCase().contains(charString.toLowerCase())
                                || question.owner?.displayName!!.contains(charSequence)) {
                            filteredList.add(question)
                        }
                    }

                    filteredList
                }

                // Add list to the Filter result
                val filterResults = Filter.FilterResults()
                filterResults.values = mQuestionsFiltered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
                // Update list after filtering
                mQuestionsFiltered = filterResults.values as MutableList<Question>
                notifyDataSetChanged()
            }
        }
    }

    /**
     * Add questions to the list.
     * - Clearing filtered list to prevent errors.
     */
    fun displayQuestions(questions: MutableList<Question>?) {
        if (questions != null) {
            mQuestionsFiltered.clear()
            mQuestions.addAll(questions)
            mQuestionsFiltered.addAll(mQuestions)
            notifyDataSetChanged()
        }
    }

    /**
     * Display download error in the Activity.
     */
    fun displayDownloadError() {
        mActivityInterface.displayDownloadError()
    }

    /**
     * Holder of information for question item.
     */
    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
            LayoutContainer {
        /**
         * Bind information to the holder.
         */
        fun bind(title: String, ownerName: String?, date: String?) {
            iqTitle.text = title
            iqOwnerName.text = ownerName
            iqDate.text = date
        }
    }
}