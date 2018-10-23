package me.alfredobejarano.tictactoe.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 *
 * Dao interface that defines database operations
 * for the [Scoreboard] entity as kotlin functions.
 *
 * @author Alfredo Bejarano
 * @since 22/10/18
 * @version 1.0
 **/
@Dao
interface ScoreboardDao {
    /**
     * Inserts a given [Scoreboard] object into the database.
     * @param record The Scoreboard to be inserted or updated if
     * a record with the same primary key already exists.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(record: Scoreboard)

    /**
     * Returns the amount of games that finalized in a given result.
     * @param result The expected result for the games to be counted.
     * @return The amount of records that match the given result, wrapped in a [LiveData]
     * object for observation.
     */
    @Query("SELECT COUNT(*) FROM scoreboards_table WHERE result = :result")
    fun countGamesByResult(result: Scoreboard.Result): Int

    /**
     * Nukes the [Scoreboard] table, deleting all records from it.
     */
    @Query("DELETE FROM scoreboards_table")
    fun deleteAll()
}