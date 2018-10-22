package me.alfredobejarano.tictactoe.data

import androidx.room.TypeConverter

/**
 *
 * Class that tells to Room how to store/fetch value
 * from custom classes or classes that Room can't infer
 * by its own how to save or restore.
 *
 * @author Alfredo Bejarano
 * @since 22/10/18
 * @version 1.0
 **/
class Converters {
    /**
     * Tells to Room how to store a [Result][Scoreboard.Result]
     * enum value into the app database.
     */
    @TypeConverter
    fun scoreboardResultToInt(result: Scoreboard.Result) = result.ordinal

    /**
     * Tells to Room how to retrieve a [Result][Scoreboard.Result]
     * value stored as an [Int] (said int being the enum value ordinal).
     * from the local storage database.
     */
    @TypeConverter
    fun intToScoreboardResult(ordinal: Int) = Scoreboard.Result.values()[ordinal]
}