package me.alfredobejarano.tictactoe.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.alfredobejarano.tictactoe.utilities.runOnIOThread

/**
 *
 * Simple [ViewModel] subclass that handles all the UI interactions related
 * to the TicTacToe game.
 *
 * @author Alfredo Bejarano
 * @since 22/10/18 - 11:39 PM
 * @version 1.0
 **/
class GameViewModel : ViewModel() {
    companion object {
        /**
         * Defines the size of the grid, in the case of a
         * common Tic-Tac-Toe game, it is 3 by 3.
         */
        private const val GRID_SIZE = 3

        /**
         * Defines the character for the player 1.
         */
        const val PLAYER_1_CHAR = 'X'

        /**
         * Defines the character for the player 2.
         */
        const val PLAYER_2_CHAR = 'O'

        /**
         * Defines the char for when a game has been tied.
         */
        private const val GAME_TIE_CHAR = 'T'
    }

    /**
     * Private property that defines the current plays on the mBoard.
     */
    private var mBoard = CharArray(9).also { array ->
        // When created, set all the elements as empty.
        array.forEachIndexed { index, _ ->
            array[index] = ' '
        }
    }

    /**
     * Flag that defines if the game has finished or it is ongoing.
     */
    private var ended = false

    /**
     * Defines the current player playing, by default is the player 1.
     */
    private var currentPlayer = PLAYER_1_CHAR

    /**
     * [MutableLiveData] object that provides observation of the mBoard to the UI.
     */
    val board = MutableLiveData<CharArray>()

    /**
     * [MutableLiveData] object that provides observation of tabout which player won a game.
     */
    var gameStatus = MutableLiveData<HashMap<Boolean, Char>>()

    /**
     * Restores the data of a game that has been stored in OnSavedStateInstance.
     */
    fun restoreGame(savedBoard: CharArray, player: Char) {
        board.postValue(savedBoard)
        mBoard = savedBoard
        currentPlayer = player
    }

    /**
     * Sets all the board values to the default ones.
     */
    fun restartGame() {
        // Set the current player as the Player 1.
        currentPlayer = PLAYER_1_CHAR
        // Empty all the board positions.
        mBoard.forEachIndexed { index, _ ->
            mBoard[index] = ' '
        }
        // Report the board changes to the UI.
        board.postValue(mBoard)
    }

    /**
     * Fills an element in the array at a given position
     * (square), as the mBoard is a grid, the position is
     * communicated using x,y coordinates.
     * @param xPos The x position of the pressed square.
     * @param yPos the y position of the pressed square.
     */
    fun performPlay(xPos: Int, yPos: Int) = runOnIOThread {
        // If the element at the position is empty, we can fill it with the current player char.
        if (getCellValue(xPos, yPos) == ' ' && !ended) {
            // Perform the play.
            mBoard[GRID_SIZE * xPos * yPos] = currentPlayer
            // Report to the UI that the mBoard has changed.
            board.postValue(mBoard)
            // Switch the players.
            switchPlayers()
        }
        // After performing a play, check if the game has finished.
        checkGameFinished()
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

    /**
     * Retrieves the current char value of a given cell.
     * @return [Char] value at a given cell.
     */
    private fun getCellValue(xPos: Int, yPos: Int) = mBoard[GRID_SIZE * xPos * yPos]

    /**
     * Checks if a game has been finished, that means if a game
     * has ben won / tied and reports to the ended flag if the game
     * can be continued or not.
     */
    private fun checkGameFinished() {
        // Get the boolean value, reporting if the game has ended or not.
        val results = getGameResults()
        // Report the new value of the ended flag.
        ended = results.keys.first()
        // Report to the UI the winner.
        gameStatus.postValue(results)
    }

    /**
     * After making a play, checks the game results, this allows to
     * keep playing or report a game win or tie.
     * @return HashMap containing the results of the last play, the key being if the game has finished
     * and a char value defining which player won.
     */
    private fun getGameResults(): HashMap<Boolean, Char> {
        // Create the results HashMap.
        val results = HashMap<Boolean, Char>()
        // Check if the game has been won by a cross.
        if (isGameWonByCross(true) || isGameWonByCross(false)) {
            /*
                If the game has been won by a cross, it means those 3 crossing cells
                (left to right or right to left) are the same, so, for winning by cross,
                the center cell has to be the same as the other that performs the cross,
                so lets take the char in the center cell.
             */
            results[true] = getCellValue(1, 1)
            return results
        }
        // Now, check if the game has been won by a Row or a Column.
        for (i in 0 until 3) {
            /*
                So, if a game is won by a row, any value of the current row
                is the same, so we can take the 0,0 position and it will be the
                same char as any 0,n position, the same applies for a column.
            */
            if (isGameWonHorizontally(i) || isGameWonVertically(i)) {
                results[true] = getCellValue(i, i)
                return results
            }
        }
        /*
            After checking all the possible game win conditions,
            lets check if the game is not a draw.
        */
        results[isGameDraw()] = GAME_TIE_CHAR
        return results
    }

    /**
     * Checks if a position in the mBoard is an empty char (' '),
     * if there is any empty position, it means the game can be
     * continued, if there aren't any more empty spaces and no win
     * condition has been met (see the [getGameResults] function) the game
     * is a Draw.
     * @see getGameResults
     * @return true if the mBoard has no empty positions.
     */
    private fun isGameDraw(): Boolean {
        // Iterate through each char on the mBoard.
        mBoard.forEach {
            // If there is an empty position, return false, as the game can't be a draw yet.
            if (it == ' ') {
                return false
            }
        }
        // If the mBoard has no empty positions, the game is a draw.
        return true
    }

    /**
     * Checks if the 3 values of a given row are the same.
     * @param row The row to check if the values are the same.
     */
    private fun isGameWonHorizontally(row: Int) =
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
    private fun isGameWonByCross(leftCross: Boolean): Boolean {
        // If it is a left cross check, the starting cell is 0,0; if not the starting cell is 0,2.
        val startRow = if (leftCross) 0 else 2
        // If it is a left cross check, the last cell is 2,2; if not the last cell is 0,2.
        val lastRow = if (leftCross) 2 else 0
        // Check for the cross
        return getCellValue(startRow, 0) == getCellValue(1, 1) &&
                getCellValue(startRow, 0) == getCellValue(lastRow, 2)
    }
}