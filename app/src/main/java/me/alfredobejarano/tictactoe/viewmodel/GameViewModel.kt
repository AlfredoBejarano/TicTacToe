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

        /**
         * Defines an empty position.
         */
        private const val EMPTY_CHAR = ' '
    }

    /**
     * Private property that defines the current plays on the mBoard.
     */
    private var mBoard = CharArray(9).also { array ->
        // When created, set all the elements as empty.
        array.forEachIndexed { index, _ ->
            array[index] = EMPTY_CHAR
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
     * [MutableLiveData] object that provides observation of about which player won a game.
     */
    var gameStatus = MutableLiveData<HashMap<Boolean, Char>>()

    /**
     * Restores the data of a game that has been stored in OnSavedStateInstance.
     */
    fun restoreGame(savedBoard: CharArray, player: Char) {
        board.postValue(savedBoard)
        mBoard = savedBoard
        currentPlayer = player
        refreshGameStatus()
    }

    /**
     * Sets all the board values to the default ones.
     */
    fun restartGame() {
        // Set the current player as the Player 1.
        currentPlayer = PLAYER_1_CHAR
        // Empty all the board positions.
        mBoard.forEachIndexed { index, _ ->
            mBoard[index] = EMPTY_CHAR
        }
        // Report the board changes to the UI.
        board.postValue(mBoard)
    }

    /**
     * Fills an element in the array at a given position
     * (square), as the mBoard is a grid, the position is
     * communicated using x,y coordinates.
     * @param cell The number of cell that the play is going to be performed.
     */
    fun performPlay(cell: Int) = runOnIOThread {
        // If the element at the position is empty, we can fill it with the current player char.
        if (mBoard[cell] == EMPTY_CHAR && !ended) {
            // Perform the play.
            mBoard[cell] = currentPlayer
            // Report to the UI that the mBoard has changed.
            board.postValue(mBoard)
            // Switch the players.
            switchPlayers()
            // After performing a play, check if the game has finished.
            refreshGameStatus()
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

    /**
     * Checks if a game has been finished, that means if a game
     * has ben won / tied and reports to the ended flag if the game
     * can be continued or not.
     */
    private fun refreshGameStatus() {
        // Get the boolean value, reporting if the game has ended or not.
        val status = getGameStatus()
        // Report the new value of the ended flag.
        ended = status.keys.first()
        // Report to the UI the winner.
        gameStatus.postValue(status)
    }

    /**
     * After making a play, checks the game results, this allows to
     * keep playing or report a game win or tie.
     * @return HashMap containing the results of the last play, the key being if the game has finished
     * and a char value defining which player won.
     */
    private fun getGameStatus(): HashMap<Boolean, Char> {
        // Define the variable for stopping the game or not.
        var stopGame: Boolean
        // Define the variable for the winner player.
        val winnerPlayer: Char
        // Now, check if the game has been won by a row or a column match.
        for (i in 0 until 3) {
            // Stop the game if there is a row or column match.
            stopGame = isGameWonHorizontally(i) || isGameWonVertically(i)
            if (stopGame) {
                // If the game has to be stopped, set the winner player as the char
                // in the current position, if not, set is as the draw character.
                winnerPlayer = if (stopGame) mBoard[i] else EMPTY_CHAR
                // Finally, report the results.
                return HashMap<Boolean, Char>().also {
                    it[stopGame] = winnerPlayer
                }
            }
        }
        winnerPlayer = when {
            isGameWonByCross() -> mBoard[4] // Set the char at the center as the winner if there is a cross match.
            isGameDraw() -> GAME_TIE_CHAR // Set the tie char if there is no winner.
            else -> EMPTY_CHAR // Set the empty char if the game has to continue.
        }
        // If the winner player is different from the empty char, the game has to be stopped.
        stopGame = winnerPlayer != EMPTY_CHAR
        // Finally, report the results.
        return HashMap<Boolean, Char>().also {
            it[stopGame] = winnerPlayer
        }
    }

    /**
     * Checks if a position in the mBoard is an empty char (' '),
     * if there is any empty position, it means the game can be
     * continued, if there aren't any more empty spaces and no win
     * condition has been met (see the [getGameStatus] function) the game
     * is a Draw.
     * @see getGameStatus
     * @return true if the mBoard has no empty positions.
     */
    private fun isGameDraw() = !mBoard.contains(' ')

    /**
     * Checks if the 3 consecutive values are the same.
     * @param row The row number to check.
     */
    private fun isGameWonHorizontally(row: Int): Boolean {
        // For checking values in a row, it has to be multiplied by the grid size (3).
        val rowAsPosition = row * 3
        // Check if the 3 consecutive values are equals within each other.
        return mBoard[rowAsPosition] != EMPTY_CHAR &&
                mBoard[rowAsPosition] == mBoard[rowAsPosition + 1] &&
                mBoard[rowAsPosition + 1] == mBoard[rowAsPosition + 2]
    }

    /**
     * Checks if the 3 values of a given column are the same.
     * @param column The row to check if the values are the same.
     */
    private fun isGameWonVertically(column: Int) =
        mBoard[column] != EMPTY_CHAR &&
                mBoard[column] == mBoard[column + 3] &&
                mBoard[column + 3] == mBoard[column + 6]

    /**
     * Checks if the values from a diagonal (upper left to bottom right and vice-versa)
     * cross are the same.
     */
    private fun isGameWonByCross(): Boolean {
        // Check a left-to-right cross.
        val isLeftCross = mBoard[0] != EMPTY_CHAR &&
                mBoard[0] == mBoard[4] && mBoard[4] == mBoard[8]
        // Check a right-to-left cross.
        val isRightCross = mBoard[2] != EMPTY_CHAR &&
                mBoard[2] == mBoard[4] && mBoard[4] == mBoard[6]
        // Check for the cross
        return isLeftCross || isRightCross
    }
}