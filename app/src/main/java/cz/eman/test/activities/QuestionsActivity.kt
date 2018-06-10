package cz.eman.test.activities

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.util.Log
import android.view.View
import android.widget.Button
import cz.eman.test.BaseActivity
import cz.eman.test.R
import cz.eman.test.api.ApiActions
import cz.eman.test.database.DBCalls
import cz.eman.test.database.DBInterface
import cz.eman.test.fragments.QuestionDetailFragment
import cz.eman.test.fragments.QuestionsListFragment
import cz.eman.test.model.Question
import kotlinx.android.synthetic.main.activity_questions.*

class QuestionsActivity : BaseActivity(), QuestionsInterface {
    private lateinit var mApiActions: ApiActions
    private var mFragmentManager: FragmentManager = supportFragmentManager
    private var mQuestions: MutableList<Question> = ArrayList()
    private lateinit var mErrorRetry: Button
    private lateinit var mConstError: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mErrorRetry = aqErrorButtonRetry
        mConstError = aqConstError

        if (savedInstanceState == null) {
            showFragment(QuestionsListFragment())
        }

        mApiActions = ApiActions.getInstance(this)
        mErrorRetry.setOnClickListener({
            mApiActions.retryDownload(this::displayQuestions,
                    this::displayDownloadError)
            mConstError.visibility = View.GONE
        })
    }

    override fun onBackPressed() {
        // Counts fragments
        val count = mFragmentManager.backStackEntryCount

        // Pop fragment or run function from super
        if (count > 0) {
            mFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    override fun loadQuestions() {
        val db: DBInterface = DBCalls()
        mQuestions = db.loadQuestions()
        displayQuestions(mQuestions)
    }

    override fun onQuestionClick(questionId: Long) {
        showFragment(QuestionDetailFragment
                .newInstance(questionId))
    }

    /**
     * Displays specific fragment on the screen.
     *
     * @param fragment to display
     */
    private fun showFragment(fragment: Fragment?) {
        if (fragment != null) {
            val fragmentTag = fragment.javaClass.simpleName
            val fragmentTransaction = mFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.aqFrameLayout, fragment, fragmentTag)
            // QuestionsListFragment is not needed to be add to back stack
            if (fragmentTag != QuestionsListFragment::class.java.simpleName)
                fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commitAllowingStateLoss()
        }
    }

    /**
     * Posts loaded questions into the QuestionsListFragment to display.
     */
    private fun displayQuestions(questions: MutableList<Question>?) {
        // Post questions to the list fragment
        if(questions != null && !questions.isEmpty())
            (supportFragmentManager
                    .findFragmentByTag(QuestionsListFragment::class.java.simpleName) as
                    QuestionsListFragment?)?.displayQuestions(questions)
    }

    override fun displayDownloadError() {
        Log.e("svsvsvsvsdv", "Displaying error message")
        mConstError.visibility = View.VISIBLE
    }

    override fun getContentViewId(): Int {
        return R.layout.activity_questions
    }

    override fun getNavigationMenuItemId(): Int {
        return R.id.action_show_questions
    }

}