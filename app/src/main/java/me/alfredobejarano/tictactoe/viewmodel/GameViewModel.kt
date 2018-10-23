package me.alfredobejarano.tictactoe.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.alfredobejarano.tictactoe.data.ScoreboardRepository
import javax.inject.Inject

/**
 *
 * Documentation goes here
 *
 * @author Alfredo Bejarano
 * @since 22/10/18 - 11:39 PM
 * @version 1.0
 **/
class GameViewModel
@Inject constructor(repo: ScoreboardRepository) : ViewModel() {
    companion object {
        /**
         * Defines the size of the grid, in the case of a
         * common Tic-Tac-Toe game, it is 3 by 3.
         */
        private const val GRID_SIZE = 3

        /**
         * Defines the character for the player 1.
         */
        private const val PLAYER_1_CHAR = 'X'

        /**
         * Defines the character for the player 2.
         */
        private const val PLAYER_2_CHAR = 'O'
    }

    /**
     * Private property that defines the current plays on the board.
     */
    private val board = CharArray(9).also { array ->
        // When created, set all the elements as empty.
        array.forEachIndexed { index, _ ->
            array[index] = ' '
        }
    }

    /**
     * Defines the current player playing, by default is the player 1.
     */
    private var currentPlayer = PLAYER_1_CHAR

    /**
     * [MutableLiveData] object that provides observation of the board to the UI.
     */
    val boardLiveData = MutableLiveData<CharArray>()

    /**
     * Fills an element in the array at a given position
     * (square), as the board is a grid, the position is
     * communicated using x,y coordinates.
     * @param xPos The x position of the pressed square.
     * @param yPos the y position of the pressed square.
     */
    fun performPlay(xPos: Int, yPos: Int) {
        // If the element at the position is empty, we can fill it with the current player char.
        if (getCellValue(xPos, yPos) == ' ') {
            // Perform the play.
            board[GRID_SIZE * xPos * yPos] = currentPlayer
            // Report to the UI that the board has changed.
            boardLiveData.value = board
            // Switch the players.
            switchPlayers()
        }
    }

    /**
     * This function changes the char value for the player, switches
     * to player 1 if player 2 has just played and vice versa.
     */
    private fun switchPlayers() {
        currentPlayer = if (currentPlayer == PLAYER_1_CHAR) {
            PLAYER_2_CHAR // Switch to player 2 if the player was player 1.
        } else {
            PLAYER_1_CHAR // If the previous player was other than player 1, switch to player 2.
        }
    }

    private fun getCellValue(xPos: Int, yPos: Int) = board[GRID_SIZE]

    private fun checkGameEnd() {
        // Todo - continue on this algorithm tomorrow
    }

    /**
     * Checks if the 3 values of a given row are the same.
     * @param row The row to check if the values are the same.
     */
    private fun isGameWonHorziontally(row: Int) =
        getCellValue(0, row) == getCellValue(1, row) &&
                getCellValue(0, row) == getCellValue(2, row)

    /**
     * Checks if the 3 values of a given column are the same.
     * @param column The row to check if the values are the same.
     */
    private fun isGameWonVertically(column: Int) =
        getCellValue(column, 0) == getCellValue(column, 1) &&
                getCellValue(column, 0) == getCellValue(column, 2)

    /**
     * Checks if the values from a diagonal (upper left to bottom right and vice-versa)
     * cross are the same.
     */
    private fun isGameWonByCross() =
        (getCellValue(0, 0) == getCellValue(1, 1) &&
                getCellValue(0, 0) == getCellValue(2, 2)) ||
                (getCellValue(2, 0) == getCellValue(1, 1) &&
                        getCellValue(2, 0) == getCellValue(0, 2))
}