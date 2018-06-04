package cz.eman.test.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import cz.eman.test.BaseActivity
import cz.eman.test.R
import cz.eman.test.database.DBCalls
import cz.eman.test.database.DBInterface
import cz.eman.test.fragments.QuestionsListFragment
import cz.eman.test.model.Question

class QuestionsActivity : BaseActivity(), QuestionsInterface {

    private var mFragmentManager: FragmentManager = supportFragmentManager
    private var mQuestions: MutableList<Question> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)

        if (savedInstanceState == null) {
            showFragment(QuestionsListFragment())
        }

         /*val apiService = ApiInterface.instance
         apiService.getQuestions("withbody", "desc", "creation", "cooking", 5, 1)
             .observeOn(AndroidSchedulers.mainThread())
             .subscribeOn(Schedulers.io())
             .subscribe ({
                 result ->
                 for(q in result.items) {
                     Log.d("TableDB", q.toString())
                 }
                 val num = db.saveQuestions(result.items)
                 Log.d("ResultDB", "There were $num saved in the db")

                 val questions:List<Question> = db.loadQuestions()
                 Log.d("ResultDB", "There is ${questions.size} saved in the db")
                 for(q in questions) {
                     Log.d("TableDB", q.toString())
                 }
             }, { error ->
                 error.printStackTrace()
             })*/
    }

    override fun loadQuestions() {
        val db: DBInterface = DBCalls()
        mQuestions = db.loadQuestions()
        displayQuestions()
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
            // ContactListFragment is not needed to be add to back stack
            if (fragmentTag != QuestionsListFragment::class.java.simpleName)
                fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commitAllowingStateLoss()
        }
    }

    /**
     * Posts loaded contacts into the ContactListFragment to display.
     */
    private fun displayQuestions() {
        // Post contacts to the fragment
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