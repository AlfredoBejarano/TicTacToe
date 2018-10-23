package me.alfredobejarano.tictactoe.data

import me.alfredobejarano.tictactoe.utilities.runOnIOThread
import javax.inject.Inject

/**
 *
 * Repository class that represents the single source of truth for the app scoreboards records.
 *
 * @author Alfredo Bejarano
 * @since 22/10/18 - 10:42 PM
 * @version 1.0
 **/
class ScoreboardRepository
@Inject constructor(private val dao: ScoreboardDao) {
    /**
     * Saves a finished game into the local app storage.
     *
     * **Note:** This function is **thread-safe**, this means
     * _(in this context, at least)_, that this function can be called
     * from the UI thread and it will be executed in a **worker thread**.
     */
    fun saveScoreboard(scoreboard: Scoreboard) = runOnIOThread {
        dao.insertOrUpdate(scoreboard)
    }

    /**
     * Deletes all the recorded game results from the scoreboard.
     *
     * **Note:** This function is **thread-safe**, so this function
     * can be called from the UI without any issues, as its work
     * is going to be handled to a worker thread by its own.
     */
    fun clearGameRecords() = runOnIOThread {
        dao.deleteAll()
    }

    /**
     * Returns the amount of games won by a given winner using the
     * values from the [Result enum class][Scoreboard.Result].
     *
     * **Note:** This function is **NOT** thread-safe, this means
     * (in this context) that this function work has to be handled
     * manually to a other thread, it will make a crash if executed
     * directly on the UI thread.
     * @see runOnIOThread
     * @see Thread
     * @return Amount of games as an [Int], wrapped in a LiveData object, allowing observation.
     */
    fun getGamesWonBy(winner: Scoreboard.Result) =
        dao.countGamesByResult(winner)
}