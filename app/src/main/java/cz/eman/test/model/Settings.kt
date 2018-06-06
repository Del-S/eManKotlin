package cz.eman.test.model

import android.content.Context
import android.content.SharedPreferences

internal class Settings private constructor(context: Context) {

    companion object : SingletonHolder<Settings, Context>(::Settings)

    private val PREFS_FILENAME = "TestPreferences"
    private val pref: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)
    private val prefEdit: SharedPreferences.Editor = pref.edit()

    private val PREFS_QUOTA_MAX = "quota_max"
    private var quotaMax: Int? = null
        set(value) = savePrefInt(PREFS_QUOTA_MAX, value)

    private val PREFS_QUOTA_REMAINING = "quota_remaining"
    private var quotaRemaining: Int? = null
        set(value) = savePrefInt(PREFS_QUOTA_REMAINING, value)

    private val PREFS_API_PAGE_SIZE = "api_page_size"
    private var apiPageSize: Int? = null
        set(value) = savePrefInt(PREFS_API_PAGE_SIZE, value)

    private val PREFS_API_PAGE = "api_page"
    private var apiPage: Int? = null
        set(value) = savePrefInt(PREFS_API_PAGE, value)

    init {
        quotaMax = pref.getInt(PREFS_QUOTA_MAX, 300)
        quotaRemaining = pref.getInt(PREFS_QUOTA_REMAINING, 300)
        apiPageSize = pref.getInt(PREFS_API_PAGE_SIZE, 5)
        apiPage = pref.getInt(PREFS_API_PAGE, 1)
    }

    fun resetPreferences() {
        apiPageSize = 300
        apiPage = 1
    }

    private fun savePrefInt(name: String, value: Int?) {
        if(value != null) {
            prefEdit.putInt(name, value)
            prefEdit.apply()
        }
    }
}
