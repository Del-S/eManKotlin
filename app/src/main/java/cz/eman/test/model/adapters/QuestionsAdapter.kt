package cz.eman.test.model.adapters

import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import cz.eman.test.R
import cz.eman.test.model.Question
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_question.*

class QuestionsAdapter(private val mActivity: FragmentActivity?) :
        RecyclerView.Adapter<QuestionsAdapter.ViewHolder>(), Filterable {

    private val mInflater: LayoutInflater = LayoutInflater.from(mActivity)  // Inflater to inflate views with
    private val mQuestions:MutableList<Question> = ArrayList()
    private var mQuestionsFiltered:MutableList<Question> = ArrayList()

    init {

    }

    fun addQuestions(questions: MutableList<Question>?) {
        mQuestionsFiltered.clear()

        if (questions != null) {
            mQuestions.addAll(questions)
            mQuestionsFiltered = mQuestions
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

        holder.bind(question.title,
                question.owner?.displayName)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                // Get filter string
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    // Return original list
                    mQuestionsFiltered = mQuestions
                } else {
                    val filteredList = ArrayList<Question>()
                    for (question in mQuestions) {
                        // Name and Email match condition.
                        if (question.title.toLowerCase().contains(charString.toLowerCase())
                                || question.owner?.displayName!!.contains(charSequence)) {
                            filteredList.add(question)
                        }
                    }

                    mQuestionsFiltered = filteredList
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
        fun bind(title: String, ownerName: String?) {
            iqTitle.text = title
            iqOwnerName.text = ownerName
        }
    }
}