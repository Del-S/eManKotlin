package cz.eman.test.activities

import cz.eman.test.BaseActivity
import cz.eman.test.R

class SettingsActivity : BaseActivity() {
    override fun getContentViewId(): Int {
        return R.layout.activity_settings
    }

    override fun getNavigationMenuItemId(): Int {
        return R.id.action_show_settings
    }

}