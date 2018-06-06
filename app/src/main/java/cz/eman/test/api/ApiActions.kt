package cz.eman.test.api

import android.util.Log
import cz.eman.test.database.DBCalls
import cz.eman.test.database.DBInterface
import cz.eman.test.model.Question
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.reflect.KFunction1

class ApiActions private constructor()  {

    private object Holder { val INSTANCE = ApiActions() }

    companion object {
        val instance: ApiActions by lazy { Holder.INSTANCE }
    }

    private val TAG = "ApiActions"   // Logging tag
    private val db: DBInterface = DBCalls()

    fun downloadQuestions(displayFunction: KFunction1<@ParameterName(name = "questions") MutableList<Question>?, Unit>) {
        val apiService = ApiInterface.instance
        apiService.getQuestions("withbody", "desc", "creation", "cooking", 5, 0)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe ({
                result ->
                val num = db.saveQuestions(result.items)
                Log.i(TAG, "There were $num questions saved in the database.")

                displayFunction(result.items)
            }, { error ->
                error.printStackTrace()
            })
    }
}