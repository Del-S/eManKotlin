package cz.eman.test.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import cz.eman.test.BaseActivity
import cz.eman.test.R
import cz.eman.test.database.DBCalls
import cz.eman.test.database.DBInterface
import cz.eman.test.fragments.QuestionDetailFragment
import cz.eman.test.fragments.QuestionsListFragment
import cz.eman.test.model.Question

class QuestionsActivity : BaseActivity(), QuestionsInterface {

    private var mFragmentManager: FragmentManager = supportFragmentManager
    private var mQuestions: MutableList<Question> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            showFragment(QuestionsListFragment())
        }
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
        displayQuestions()
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
            fragmentTransaction.replace(R.id.aq_frame_layout, fragment, fragmentTag)
            // QuestionsListFragment is not needed to be add to back stack
            if (fragmentTag != QuestionsListFragment::class.java.simpleName)
                fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commitAllowingStateLoss()
        }
    }

    /**
     * Posts loaded questions into the QuestionsListFragment to display.
     */
    private fun displayQuestions() {
        // Post questions to the list fragment
        (supportFragmentManager
                .findFragmentByTag(QuestionsListFragment::class.java.simpleName) as QuestionsListFragment?)?.displayQuestions(mQuestions)
    }

    override fun getContentViewId(): Int {
        return R.layout.activity_questions
    }

    override fun getNavigationMenuItemId(): Int {
        return R.id.action_show_questions
    }

}