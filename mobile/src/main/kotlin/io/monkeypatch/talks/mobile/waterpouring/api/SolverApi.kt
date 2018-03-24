package io.monkeypatch.talks.mobile.waterpouring.api

import io.monkeypatch.talks.mobile.waterpouring.Configuration
import io.monkeypatch.talks.waterpouring.model.Move
import io.monkeypatch.talks.waterpouring.model.State
import io.monkeypatch.talks.waterpouring.model.waterPouringMapper
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeoutException


/**
 * REST API interface
 */
interface SolverInterface {

    /**
     * Ask backend to solve the [Water Pouring Puzzle](https://en.wikipedia.org/wiki/Water_pouring_puzzle)
     * @param states [Pair] of initial and final [State]
     */
    @POST("/api/solve")
    fun solve(@Body states: @JvmSuppressWildcards Pair<State, State>): Single<List<Move>>
}

/**
 * Providing the solve method
 */
object SolverApi {

    private val solverInterface: SolverInterface by lazy {
        // Configure logger
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Configure HTTP client
        val httpClient = OkHttpClient.Builder().apply {
            addInterceptor(logging)
        }

        // Build retrofit instance

        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(Configuration.url)
                .addConverterFactory(JacksonConverterFactory.create(waterPouringMapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build()

        // Create SolverInterface instance with retrofit
        retrofit.create(SolverInterface::class.java)
    }


    /**
     * Solve the water pouring problem
     *
     * @param initialState the initial [State]
     * @param finalState the expected [State]
     * @return a [Single] of [List] of [Move] required to solve the problem
     * @throws [TimeoutException] if too long to solve the problem
     * @throws [IllegalStateException] if there is a parsing issue, or if the problem is not solvable
     */
    fun solve(initialState: State, finalState: State): Single<List<Move>> =
            solverInterface.solve(initialState to finalState)
}

