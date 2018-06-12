package cz.eman.test.activities

import android.os.Bundle
import cz.eman.test.BaseActivity
import cz.eman.test.R
import cz.eman.test.model.Settings
import kotlinx.android.synthetic.main.activity_settings.*

/**
 * Activity to display settings and in the future enable to change them.
 */
class SettingsActivity : BaseActivity() {

    private lateinit var settings: Settings // Setting class holding the data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        settings = Settings.getInstance(this)   // Load settings instance (could be in the declaration also)

        // Set values to the Views
        asApiPageSizeValue.text = settings.apiPageSize.toString()   // Set page size
        asApiPageValue.text = settings.apiPage.toString()           // Set current page value
        asQuotaValue.text = String.format(getString(R.string.as_quota_value),
                settings.quotaRemaining, settings.quotaMax)         // Set quotas
    }

    /**
     * Gets layout of current activity
     *
     * @return int id layout
     */
    override fun getContentViewId(): Int {
        return R.layout.activity_settings
    }

    /**
     * Gets position in bottom navigation menu
     *
     * @return int id of item in the menu
     */
    override fun getNavigationMenuItemId(): Int {
        return R.id.action_show_settings
    }

}