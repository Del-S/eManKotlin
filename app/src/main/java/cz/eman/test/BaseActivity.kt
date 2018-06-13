package cz.eman.test

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.view.MenuItem
import android.widget.Toast
import cz.eman.test.activities.QuestionsActivity
import cz.eman.test.activities.SettingsActivity
import kotlinx.android.synthetic.main.element_bottom_menu.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Base activity handles functions for all child activities like:
 * - Menu functions
 * - Showing new activity
 * - Closing the app
 */
abstract class BaseActivity : AppCompatActivity(),
        BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var navigationView: BottomNavigationView   // Bottom navigation (must be included in layout)
    private var shouldClose = false    // Checker if application should be closed or not

    // Companion object containing static variables
    companion object {
        val mLocale = Locale("en")  // Application locale for date formatter
        val mSimpleDateFormatter = SimpleDateFormat("dd.MM.yyyy", mLocale)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewId())   // Sets view based on child activity

        navigationView = bottom_menu    // Initiate bottom menu
        navigationView.setOnNavigationItemSelectedListener(this)    // This instance works as a navigation click listener
    }

    override fun onStart() {
        super.onStart()
        // Update bottom menu buttons to which one is active
        updateNavigationBarState()
    }

    override fun onPause() {
        super.onPause()
        // Remove inter-activity transition to avoid screen tossing on tapping bottom navigation items
        overridePendingTransition(0, 0)
    }

    override fun onBackPressed() {
        // Closes the app after 2 clicks of back button
        if (shouldClose) {
            ActivityCompat.finishAffinity(this)
        } else {
            // If the button was clicked for the first time it will notify the user about leaving.
            shouldClose = true
            Toast.makeText(this, R.string.app_leaving_notice, Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ shouldClose = false }, 3000)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Starting activities using bottom menu
        navigationView.postDelayed({
            when (item.itemId) {
                R.id.action_show_questions ->
                    showActivity(QuestionsActivity::class.java)
                R.id.action_show_settings ->
                    showActivity(SettingsActivity::class.java)
                else ->
                    showActivity(QuestionsActivity::class.java)
            }
        }, 100)
        return true
    }

    /**
     * Starts activity if its not current running activity
     *
     * @param a class of the activity to show
     */
    private fun showActivity(a: Class<*>) {
        if (this.javaClass != a) {
            val i = Intent(this, a)

            // Pass bundle if there is some (used for Notifications)
            val bundle = intent.extras

            // Put Bundle into Intent
            if (bundle != null && !bundle.isEmpty) {
                i.putExtras(bundle)
            }

            // Start new activity and finish current
            startActivity(i)
            finish()
        }
    }

    /**
     * Checks for action in bottom navigation and tries to highlight current tab
     */
    private fun updateNavigationBarState() {
        val actionId = getNavigationMenuItemId()
        if (actionId >= 0) {
            val menu = navigationView.menu
            var i = 0
            val size = menu.size()
            while (i < size) {
                // Check selected menu item
                val item = menu.getItem(i)
                val shouldBeChecked = item.itemId == actionId
                if (shouldBeChecked) {
                    item.isChecked = true
                    break
                }
                i++
            }
        }
    }

    /**
     * Gets layout of current activity
     *
     * @return int id layout
     */
    protected abstract fun getContentViewId(): Int

    /**
     * Gets position in bottom navigation menu
     *
     * @return int id of item in the menu
     */
    protected abstract fun getNavigationMenuItemId(): Int

}
