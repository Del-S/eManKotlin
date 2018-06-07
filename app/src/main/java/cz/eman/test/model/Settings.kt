package cz.eman.test.model

import android.content.Context
import android.content.SharedPreferences

class Settings private constructor(context: Context) {

    companion object : SingletonHolder<Settings, Context>(::Settings)

    private val PREFS_FILENAME = "TestPreferences"
    private val pref: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)
    private val prefEdit: SharedPreferences.Editor = pref.edit()

    private val PREFS_QUOTA_MAX = "quota_max"
    var quotaMax: Int = 0
        set(value) {
            if(field != value)
                savePrefInt(PREFS_QUOTA_MAX, value)
            field = value
        }

    private val PREFS_QUOTA_REMAINING = "quota_remaining"
    var quotaRemaining: Int = 0
        set(value) {
            if(field != value)
                savePrefInt(PREFS_QUOTA_REMAINING, value)

            field = value
        }

    private val PREFS_API_PAGE_SIZE = "api_page_size"
    var apiPageSize: Int = 0
        set(value) {
            if(field != value)
                savePrefInt(PREFS_API_PAGE_SIZE, value)
            field = value
        }

    private val PREFS_API_PAGE = "api_page"
    var apiPage: Int = 0
        set(value) {
            if(field != value)
                savePrefInt(PREFS_API_PAGE, value)
            field = value
        }

    init {
        quotaMax = pref.getInt(PREFS_QUOTA_MAX, 300)
        quotaRemaining = pref.getInt(PREFS_QUOTA_REMAINING, 300)
        apiPageSize = pref.getInt(PREFS_API_PAGE_SIZE, 5)
        apiPage = pref.getInt(PREFS_API_PAGE, 1)
    }

    private fun savePrefInt(name: String, value: Int?) {
        if(value != null) {
            prefEdit.putInt(name, value)
            prefEdit.apply()
        }
    }
}
