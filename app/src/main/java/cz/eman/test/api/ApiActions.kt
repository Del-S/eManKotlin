package cz.eman.test.api

import android.content.Context
import android.util.Log
import cz.eman.test.database.DBCalls
import cz.eman.test.database.DBInterface
import cz.eman.test.model.Question
import cz.eman.test.model.Settings
import cz.eman.test.model.SingletonHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.reflect.KFunction
import kotlin.reflect.KFunction1

/**
 * Api action used to download new Questions.
 * This is used to work as a middleman between the application and retrofit.
 * - Download is re-tried 3 times and tries can be reset manually in the app.
 */
class ApiActions private constructor(context: Context)  {

    companion object : SingletonHolder<ApiActions, Context>(::ApiActions)   // Singleton instance of this class

    private val TAG = "ApiActions"   // Logging tag
    private val db: DBInterface = DBCalls()                 // Database connection to save new questions.
    private val settings = Settings.getInstance(context)    // Load settings instance to get page settings for the api
    private var tries = 3            // Varibale for tries protecting download
    private val apiInterface = ApiInterface.instance          // Instance of retrofit API interface

    /**
     * Download question from the API.
     * - Removing tries works recursively.
     *
     * @param displayFunction function called when download succeeded
     * @param errorFunction function called when download failed
     */
    fun downloadQuestions(displayFunction: KFunction1<@ParameterName(name = "questions") MutableList<Question>?, Unit>,
                          errorFunction: KFunction<Unit>) {
        // Checking if new data can be downloaded
        if(tries > 0 ) {
            // Call retrofit to download questions and handle
            apiInterface.getQuestions("withbody", "desc", "creation", "cooking", settings.apiPageSize, settings.apiPage)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        settings.apiPage++      // Increase api page
                        settings.quotaRemaining = result.quotaRemaining  // Change remaining quota
                        tries = 3               // Reset tries for the next calls

                        val num = db.saveQuestions(result.items)    // Save questions to the database
                        Log.i(TAG, "There were $num questions saved in the database.")

                        displayFunction(result.items)   // Call display function
                    }, { error ->
                        Log.e(TAG, "Error while fetching questions.", error)
                        tries--     // Remove a try on fail
                        downloadQuestions(displayFunction, errorFunction)   // Recurse call to try the download again
                    })
        } else {
            errorFunction.call()    // Call error function on download fail
        }
    }

    /**
     * Manually triggered re-try of the download. Resets tries and triggers download.
     *
     * @param displayFunction function called when download succeeded
     * @param errorFunction function called when download failed
     */
    fun retryDownload(displayFunction: KFunction1<@ParameterName(name = "questions") MutableList<Question>?, Unit>,
                      errorFunction: KFunction<Unit>) {
        tries = 3   // Reset try
        downloadQuestions(displayFunction, errorFunction)   // Trigger download
    }
}