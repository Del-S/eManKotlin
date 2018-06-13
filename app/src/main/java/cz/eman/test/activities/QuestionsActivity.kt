package cz.eman.test.activities

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
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

/**
 * Questions activity to display Question list and Detail.
 * - Works for Tablet and Mobile devices with different layouts.
 */
class QuestionsActivity : BaseActivity(), QuestionsInterface {

    private lateinit var mApiActions: ApiActions            // API connection to enable download questions
    private lateinit var mConstError: ConstraintLayout      // Error message constraint layout to enable display
    private lateinit var mErrorRetry: Button                // Button to run re-try of the question download
    private var mFragmentManager: FragmentManager = supportFragmentManager  // Fragment manager to display Fragments
    private var mQuestions: MutableList<Question> = ArrayList() // List of question to display in the list
    private val mDatabase: DBInterface = DBCalls()              // Database connection to load saved questions
    private var isTablet: Boolean = false                       // Check if device is tablet or not
    private val TAG_SHOW_ERROR = "errorDisplayed"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isTablet = resources.getBoolean(R.bool.isTablet)        // Check if the device is tablet or not
        mConstError = aqConstError                              // Initiate view for error message
        mErrorRetry = aqErrorButtonRetry                        // Initiate button view to enable download re-try error
        mApiActions = ApiActions.getInstance(this)          // Initiate API connection

        // Showing fragments if this Activity does not have a savedInstance
        if (savedInstanceState == null) {
            showFragment(QuestionsListFragment())
        }

        // Set onclick listener to re-try button
        mErrorRetry.setOnClickListener({
            mApiActions.retryDownload(this::displayQuestions,
                    this::displayDownloadError)                 // Re-run download
            mConstError.visibility = View.GONE                  // Hide error view
        })
    }

    /**
     * Save variables into InstanceState to enable keeping display orientation changes
     * with the same design.
     */
    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)

        savedInstanceState.putInt(TAG_SHOW_ERROR, mConstError.visibility)   // Save error constraint visibility
    }

    /**
     * Restore variables from InstanceState to display views that should be displayed
     * after orientation change.
     */
    public override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        if(savedInstanceState.getInt(TAG_SHOW_ERROR) == View.VISIBLE)   // Check error constraint visibility
            displayDownloadError()  // Display error message
    }

    /**
     * Override super back pressed button.
     * - Enables mobile screen to return from detail to list.
     */
    override fun onBackPressed() {
        val count = mFragmentManager.backStackEntryCount    // Counts fragments

        // Pop fragment or run function from super
        if (count > 0) {
            mFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    /**
     * Loading Questions from the database for display.
     */
    override fun loadQuestions() {
        mQuestions = mDatabase.loadQuestions()  // Load questions from the database.
        displayQuestions(mQuestions)            // Display questions.
    }

    /**
     * Implement on question click to display Question information.
     */
    override fun onQuestionClick(questionId: Long) {
        showFragment(QuestionDetailFragment
                .newInstance(questionId))   // Create a new instance of QuestionDetailFragment.
    }

    /**
     * Displays fragments on the screen.
     * Different functions for Mobile and Tablet screens.
     *
     * @param fragment to display
     */
    private fun showFragment(fragment: Fragment?) {
        if (fragment != null) {
            if(!isTablet) {
                showFragmentMobile(fragment)
            } else {
                showFragmentTablet(fragment)
            }
        }
    }

    /**
     * Show fragments for the Mobile device.
     * Fragments are changed in one FrameLayout.
     */
    private fun showFragmentMobile(fragment: Fragment) {
        val fragmentTag = fragment.javaClass.simpleName                         // Load fragment class name
        val fragmentTransaction = mFragmentManager.beginTransaction()           // Begin fragment transaction
        fragmentTransaction.replace(R.id.aqFrameLayout, fragment, fragmentTag)  // Replace current fragment with new one
        if (fragmentTag != QuestionsListFragment::class.java.simpleName)        // QuestionsListFragment is not needed to be add to back stack
            fragmentTransaction.addToBackStack(null)                      // Add fragment to back stack to enable to pop it out
        fragmentTransaction.commitAllowingStateLoss()                           // Commit the fragment to display
    }

    /**
     * Show fragments for the Tablet device.
     * Both List and detail are shown on one screen in two different FrameLayouts.
     */
    private fun showFragmentTablet(fragment: Fragment) {
        val fragmentTag = fragment.javaClass.simpleName                     // Load fragment class name
        val fragmentTransaction = mFragmentManager.beginTransaction()       // Begin fragment transaction
        if(fragmentTag == QuestionsListFragment::class.java.simpleName) {   // Decide to display fragment in specific Layout
            fragmentTransaction.replace(R.id.aqFrameLayoutList, fragment, fragmentTag)   // Display ListFragment
            showNewDetailFragment()
        } else {
            fragmentTransaction.replace(R.id.aqFrameLayoutDetail, fragment, fragmentTag) // Display DetailFragment
        }
        fragmentTransaction.commitAllowingStateLoss()   // Commit the fragment to display
    }

    /**
     * Shows a new detail fragment on startup and when there is not detail fragment displayed.
     */
    private fun showNewDetailFragment() {
        val fragment: Fragment? = mFragmentManager
                .findFragmentByTag(QuestionDetailFragment::class.java.simpleName)   // Try to find Detail fragment in manager
        val questionId = mDatabase.loadFirstQuestionId()                            // Load id of first question from the database

        // Trigger this only if fragment is not displayed and there is a question to display
        if((fragment == null || !fragment.isVisible)
                && questionId != null) {
            val detailFragment = QuestionDetailFragment.newInstance(questionId)     // Create a new fragment for specific question
            val fragmentTransaction = mFragmentManager.beginTransaction()           // Begin fragment transaction
            fragmentTransaction.replace(R.id.aqFrameLayoutDetail,
                    detailFragment,
                    detailFragment.javaClass.simpleName)    // Display DetailFragment
            fragmentTransaction.commitAllowingStateLoss()   // Commit the fragment to display
        }
    }

    /**
     * Posts loaded questions into the QuestionsListFragment to display.
     */
    private fun displayQuestions(questions: MutableList<Question>?) {
        // Post questions to the list fragment
        (mFragmentManager
                .findFragmentByTag(QuestionsListFragment::class.java.simpleName) as
                QuestionsListFragment?)?.displayDatabaseQuestions(questions)
    }

    /**
     * Display download error to enable re-try of the download.
     */
    override fun displayDownloadError() {
        mConstError.visibility = View.VISIBLE
    }

    /**
     * Gets layout of current activity
     *
     * @return int id layout
     */
    override fun getContentViewId(): Int {
        return R.layout.activity_questions
    }

    /**
     * Gets position in bottom navigation menu
     *
     * @return int id of item in the menu
     */
    override fun getNavigationMenuItemId(): Int {
        return R.id.action_show_questions
    }

}