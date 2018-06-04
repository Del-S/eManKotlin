package cz.eman.test

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Toast
import cz.eman.test.activities.QuestionsActivity
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.element_bottom_menu.*


abstract class BaseActivity : AppCompatActivity(),
        BottomNavigationView.OnNavigationItemSelectedListener {

    private var mToolbar: Toolbar? = null
    private var navigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewId()) // Sets view based on child activity

        mToolbar = app_toolbar
        navigationView = bottom_menu

        // Initiate toolbar
        if (mToolbar != null) {
            setSupportActionBar(mToolbar)
            if (supportActionBar != null) {
                supportActionBar!!.setDisplayShowTitleEnabled(false)    // Disable default menu title
            }
        }

        // Load bottom navigation
        if (navigationView != null) {
            //disableShiftMode(navigationView)
            navigationView?.setOnNavigationItemSelectedListener(this)
        }
        invalidateOptionsMenu()    // Notify that menu has changed
    }

    override fun onStart() {
        super.onStart()
        // Update bottom menu buttons to which one is active
        updateNavigationBarState()
    }

    public override fun onPause() {
        super.onPause()
        // Remove inter-activity transition to avoid screen tossing on tapping bottom navigation items
        overridePendingTransition(0, 0)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Starting activities in bottom menu
        navigationView!!.postDelayed({
            when (item.itemId) {
                R.id.action_show_questions ->
                    showActivity(QuestionsActivity::class.java)
                R.id.action_show_settings ->
                    Toast.makeText(this, "Not Implemented yet", Toast.LENGTH_SHORT).show();
                    //showActivity(MapActivity::class.java)
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
            val nav = navigationView
            if (nav != null) {
                val menu = nav.menu
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
