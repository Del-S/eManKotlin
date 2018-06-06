package cz.eman.test.activities

import android.os.Bundle
import cz.eman.test.BaseActivity
import cz.eman.test.R
import cz.eman.test.model.Settings

class SettingsActivity : BaseActivity() {

    private lateinit var settings: Settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        settings = Settings.getInstance(this)
    }

    override fun getContentViewId(): Int {
        return R.layout.activity_settings
    }

    override fun getNavigationMenuItemId(): Int {
        return R.id.action_show_settings
    }

}