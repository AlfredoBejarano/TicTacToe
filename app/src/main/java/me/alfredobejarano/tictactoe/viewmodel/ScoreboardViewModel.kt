package me.alfredobejarano.tictactoe.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.alfredobejarano.tictactoe.data.Scoreboard
import me.alfredobejarano.tictactoe.data.Scoreboard.Result.*
import me.alfredobejarano.tictactoe.data.ScoreboardRepository
import me.alfredobejarano.tictactoe.utilities.runOnIOThread
import javax.inject.Inject

/**
 *
 * Simple [ViewModel] class that handles UI events related
 * to the Scoreboard.
 *
 * @author Alfredo Bejarano
 * @since 23/10/18
 * @version 1.0
 **/
class ScoreboardViewModel
@Inject constructor(private val repo: ScoreboardRepository) : ViewModel() {
    /**
     * [MutableLiveData] object that allows to the UI observe the amount of games won by a player.
     * The array structure is as follow (P1 wins, P2 Wins, Ties).
     */
    var scoreboard = MutableLiveData<IntArray>()

    init {
        runOnIOThread {
            val scores = IntArray(3)
            scores[0] = repo.getGamesWonBy(RESULT_P1_WINS)
            scores[1] = repo.getGamesWonBy(RESULT_P2_WINS)
            scores[2] = repo.getGamesWonBy(RESULT_TIE)
            scoreboard.postValue(scores)
        }
    }

    /**
     * Saves the result of a game into the local app database.
     * @param winner The player who won the game.
     */
    fun saveScoreboard(winner: Char) {
        // Create the Scoreboard object to persist.
        val scoreboard = Scoreboard(
            when (winner) {
                GameViewModel.PLAYER_1_CHAR -> RESULT_P1_WINS
                GameViewModel.PLAYER_2_CHAR -> RESULT_P2_WINS
                else -> RESULT_TIE
            }
        )
        // Persist it using the Scoreboard Repository class.
        repo.saveScoreboard(scoreboard)
    }

    /**
     * Factory class for getting a [ScoreboardViewModel] instance for an
     * activity or fragment, this is required as this ViewModel class has
     * a custom constructor.
     */
    class Factory @Inject constructor(private val repo: ScoreboardRepository) : ViewModelProvider.Factory {

        /**
         * Creates a [ScoreboardViewModel] class.
         */
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            ScoreboardViewModel(repo) as T
    }
}