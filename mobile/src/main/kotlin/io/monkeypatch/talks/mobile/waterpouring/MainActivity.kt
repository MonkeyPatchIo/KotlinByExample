package io.monkeypatch.talks.mobile.waterpouring

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import io.monkeypatch.talks.mobile.waterpouring.api.SolverApi
import io.monkeypatch.talks.waterpouring.model.Move
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.HttpException

class MainActivity : AppCompatActivity() {

    private val TAG = this::class.simpleName

    private val mainModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Init recycler view
        resultGroup.visibility = View.GONE
        textViewError.visibility = View.GONE

        bindViews()

        initActions()
    }

    private fun bindViews() {
        mainModel.bindViews(drink1, drink2, drink3, drink4)
    }

    private fun initActions() {
        buttonPlayResult.setOnClickListener {
            val animationToPlay = mainModel.resultAnimationToPlay
            if (animationToPlay != null) {
                // FIXME solve anim
                var (moves, leftAnimation, rightAnimation) = animationToPlay

                resultGlass1.setLevels(leftAnimation) {
                    textViewStep.text = moves[0]?.toString()
                    moves = if (moves.isEmpty()) moves else moves.drop(1)
                }

                resultGlass2.setLevels(rightAnimation) {}
            }
        }

        buttonRandom.setOnClickListener {
            mainModel.shuffle()
        }

        buttonPlay.setOnClickListener { solve() }
    }

    /**
     * Call the service to solve the initial state to the finalState.
     */
    private fun solve() {
        val moves: Single<List<Move>> = SolverApi.solve(mainModel.initialState, mainModel.finalState)

        moves.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    // FIXME hide progress
                    progressBar.visibility = View.VISIBLE
                    textViewError.text = getString(R.string.waiting)
                }
                .doOnEvent { _, _ ->
                    // FIXME hide progress
                    progressBar.visibility = View.GONE
                }
                .subscribe(
                        { solution ->
                            mainModel.setResult(solution)

                            // Initialize result glass to first state
                            displaySolution()
                        },
                        { err -> handleError(err) })
    }

    private fun handleError(err: Throwable) {
        when (err) {
            is HttpException -> {
                val message = if (err.code() == 400) getString(R.string.no_solution) else err.message
                displayNoSolution(message)
            }
            else -> displayNoSolution(err.message)
        }
        Log.e(TAG, "Error during solve $err")
    }

    /**
     * Display a message when there is no solution.
     */
    private fun displaySolution() {
        // FIXME update display
        textViewError.visibility = View.GONE
        resultGroup.visibility = View.VISIBLE
        val (glass1, glass2) = mainModel.initialState
        resultGlass1.glassChanged(glass1)
        resultGlass2.glassChanged(glass2)
    }

    /**
     * Display a message when there is no solution.
     */
    private fun displayNoSolution(text: String?) {
        // FIXME update display
        textViewError.visibility = View.VISIBLE
        textViewError.text = text
    }
}