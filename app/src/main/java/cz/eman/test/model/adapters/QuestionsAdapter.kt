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

class QuestionsAdapter(private val mActivity: FragmentActivity?) :
        RecyclerView.Adapter<QuestionsAdapter.ViewHolder>(), Filterable {

    private val mInflater: LayoutInflater = LayoutInflater.from(mActivity)  // Inflater to inflate views with
    private val mQuestions: MutableList<Question> = ArrayList()
    private var mQuestionsFiltered:MutableList<Question> = ArrayList()
    private var mActivityInterface: QuestionsInterface? = null
    private lateinit var mApiActions: ApiActions

    init {
        if(mActivity != null) {
            mApiActions = ApiActions.getInstance(mActivity.applicationContext)

            // Initiate callback connection to the activity or throw an Exception
            try {
                mActivityInterface = mActivity as QuestionsInterface
            } catch (e: Exception) {
                throw ClassCastException(mActivity!!.javaClass.name + " must implement QuestionsInterface")
            }
        }
    }

    fun displayQuestions(questions: MutableList<Question>?) {
        mQuestionsFiltered.clear()

        if (questions != null) {
            mQuestions.addAll(questions)
            mQuestionsFiltered.addAll(mQuestions)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return mQuestionsFiltered.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Creating single item view
        val view = mInflater.inflate(R.layout.item_question, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val question = mQuestionsFiltered[position]

        val dummyDate = mActivity?.getString(R.string.app_date)

        holder.bind(
                UtilFunctions.decodeHtmlString(question.title),
                question.owner?.displayName,
                UtilFunctions.createDateString(question.creationDate, dummyDate))

        val userImage = question.owner?.profileImage
        if(userImage != null && !userImage.isEmpty()) {
            Picasso.with(mActivity).load(userImage).into(holder.iqImage)
        }

        holder.itemView.setOnClickListener({mActivityInterface?.onQuestionClick(question.id)})

        if(position >= (mQuestions.size - 1))
            mApiActions.downloadQuestions(this::displayQuestions)
    }

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
                        // Name and Email match condition.
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
     *
     */
    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
            LayoutContainer {
        fun bind(title: String, ownerName: String?, date: String?) {
            iqTitle.text = title
            iqOwnerName.text = ownerName
            iqDate.text = date
        }
    }
}