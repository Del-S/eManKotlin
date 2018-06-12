package cz.eman.test.api

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.*

/**
 * All calls to the Api using Retrofit2.
 */
interface ApiInterface {

    companion object {
        // Static instance of this class lazy loaded retrofit configuration.
        val instance: ApiInterface by lazy {
            val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.stackexchange.com/2.2/")
                    .addConverterFactory(JacksonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            retrofit.create(ApiInterface::class.java)
        }
    }

    /**
     * Get questions based on selected parameters.
     *
     * @return Result containing list of questions.
     */
    @GET("questions")
    fun getQuestions(@Query("filter") filter: String,
                     @Query("order") order: String,
                     @Query("sort") sort: String,
                     @Query("site") site: String,
                     @Query("pagesize") pagesize: Int,
                     @Query("page") page: Int):
            Observable<Result>

}
