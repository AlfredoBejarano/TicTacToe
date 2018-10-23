package me.alfredobejarano.tictactoe.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class that defines the result of a game.
 * @author Alfredo Bejarano
 * @since October 22nd, 2018 - 02:52 PM
 * @version 1.0
 */
@Entity(tableName = "scoreboards_table")
data class Scoreboard(
    var result: Result
) {
    /**
     * Used by Room to identify a game result.
     */
    @ColumnInfo(name = "pk")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    /**
     * Inner enum class that provides values representing the result of a game.
     * @author Alfredo Bejarano
     * @since October 22th, 2018 - 02:47 PM
     *
     */
    enum class Result {
        /**
         * Result value when the Player 1 (x) wins a game.
         */
        RESULT_P1_WINS,
        /**
         * Result value when the player two (o) wins a game.
         */
        RESULT_P2_WINS,
        /**
         * Result value for a match were no player beat the other.
         */
        RESULT_TIE
    }
}