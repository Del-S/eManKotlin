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
import kotlin.reflect.KFunction1

class ApiActions private constructor(context: Context)  {

    companion object : SingletonHolder<ApiActions, Context>(::ApiActions)

    private val TAG = "ApiActions"   // Logging tag
    private val db: DBInterface = DBCalls()
    private val settings = Settings.getInstance(context)

    fun downloadQuestions(displayFunction: KFunction1<@ParameterName(name = "questions") MutableList<Question>?, Unit>) {
        val apiService = ApiInterface.instance
        apiService.getQuestions("withbody", "desc", "creation", "cooking", settings.apiPageSize, settings.apiPage)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe ({
                result ->
                settings.apiPage++
                settings.quotaRemaining = result.quotaRemaining

                val num = db.saveQuestions(result.items)
                Log.i(TAG, "There were $num questions saved in the database.")

                displayFunction(result.items)
            }, { error ->
                error.printStackTrace()
            })
    }
}