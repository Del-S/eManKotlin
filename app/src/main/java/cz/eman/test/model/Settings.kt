package cz.eman.test.model

import android.content.Context
import android.content.SharedPreferences

/**
 * Setting class used to modify the app (Singleton class).
 * Every setting is saved only when it was changed.
 * - Mainly used to hold API page information and quota for the API.
 * - Could be extended to modify the app even more.
 */
class Settings private constructor(context: Context) {

    // Enable Singleton instance
    companion object : SingletonHolder<Settings, Context>(::Settings)

    // Preference files settings
    private val PREFS_FILENAME = "TestPreferences"
    private val pref: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)
    private val prefEdit: SharedPreferences.Editor = pref.edit()

    // Max API quota loaded from call result
    private val PREFS_QUOTA_MAX = "quota_max"
    var quotaMax: Int = 0
        set(value) {
            // Save only on change
            if(field != value)
                savePrefInt(PREFS_QUOTA_MAX, value)
            field = value
        }

    // API remaining quota loaded from call result
    private val PREFS_QUOTA_REMAINING = "quota_remaining"
    var quotaRemaining: Int = 0
        set(value) {
            // Save only on change
            if(field != value)
                savePrefInt(PREFS_QUOTA_REMAINING, value)
            field = value
        }

    // Page size for the API
    private val PREFS_API_PAGE_SIZE = "api_page_size"
    var apiPageSize: Int = 0
        set(value) {
            // Save only on change
            if(field != value)
                savePrefInt(PREFS_API_PAGE_SIZE, value)
            field = value
        }

    // Current page for the API
    private val PREFS_API_PAGE = "api_page"
    var apiPage: Int = 0
        set(value) {
            // Save only on change
            if(field != value)
                savePrefInt(PREFS_API_PAGE, value)
            field = value
        }

    /**
     * Initiating this class will also load preferences from SharedPreferences.
     */
    init {
        quotaMax = pref.getInt(PREFS_QUOTA_MAX, 300)
        quotaRemaining = pref.getInt(PREFS_QUOTA_REMAINING, 300)
        apiPageSize = pref.getInt(PREFS_API_PAGE_SIZE, 5)
        apiPage = pref.getInt(PREFS_API_PAGE, 1)
    }

    /**
     * Saving actual Int value into the SharedPreferences.
     *
     * @param name of value in the SharedPreferences
     * @param value int value saved
     */
    private fun savePrefInt(name: String, value: Int?) {
        if(value != null) {
            prefEdit.putInt(name, value)
            prefEdit.apply()
        }
    }
}
