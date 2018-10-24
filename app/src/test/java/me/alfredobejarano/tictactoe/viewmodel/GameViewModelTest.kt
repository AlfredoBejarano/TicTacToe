package me.alfredobejarano.tictactoe.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

/**
 * Test suite for the [GameViewModel] class.
 *
 * @author Alfredo Bejarano
 * @version 1.0
 * @since 24/10/18 - 12:37 AM
 */
@RunWith(MockitoJUnitRunner::class)
class GameViewModelTest {
    companion object {
        private const val OBSERVATION_TIMEOUT = 2500L
    }

    // Create a new object for the test to run against it.
    private val vm = GameViewModel()
    /**
     * Rule that tells to any thread workload to be executed immediately in the UI thread.
     */
    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    /**
     * Mock observer for the game status.
     */
    @Mock
    private lateinit var mockStatusObserver: Observer<HashMap<Boolean, Char>>

    /**
     * Mock observer for the board.
     */
    @Mock
    private lateinit var mockBoardObserver: Observer<CharArray>

    /**
     * Tests that a game restores correctly with a given set of parameters.
     */
    @Test
    fun restoreGame() {
        // Simple array of chars that defines a board that is going to be restored.
        val boardToRestore =
            charArrayOf('X', 'O', 'X', 'X', 'O', ' ', ' ', ' ', ' ')
        // Create a HashMap with the expected results for this test restored game.
        val expectedRestoreStatus = HashMap<Boolean, Char>().also {
            it[false] = ' ' // Expect a game that hasn't finished.
        }
        // Restore the current player pending turn.
        val lastPlayer = GameViewModel.PLAYER_2_CHAR
        // Observe changes to the board.
        vm.board.observeForever(mockBoardObserver)
        // Observe changes to the game status.
        vm.gameStatus.observeForever(mockStatusObserver)
        // Restore the game
        vm.restoreGame(boardToRestore, lastPlayer)
        // Check that the expected board gets restored.
        verify(mockBoardObserver, timeout(OBSERVATION_TIMEOUT))
            .onChanged(boardToRestore)
        // Check that the restore game status is the one expected.
        verify(mockStatusObserver, timeout(OBSERVATION_TIMEOUT))
            .onChanged(expectedRestoreStatus)
    }

    /**
     * Tests that a game board gets reset correctly.
     */
    @Test
    fun restartGame() {
        // Create a ViewModel to perform tests on it.
        val vm = GameViewModel()
        // Observe changes in the board.
        vm.board.observeForever(mockBoardObserver)
        // Restore a game
        vm.restoreGame(charArrayOf('X', 'O', ' ', ' ', ' ', ' ', ' ', ' ', ' '), 'X')
        // Now, restart the game
        vm.restartGame()
        // Verify that the board is empty after restarting the game.
        verify(mockBoardObserver, times(2))
            .onChanged(charArrayOf(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '))
    }

    /**
     * Tests that a play performs correctly, this means it cannot be performed
     * if the given position is already full, also it switches players and
     * updates the board.
     */
    @Test
    fun performPlay() {
        // Create a HashMap with the expected results for this test's game.
        val expectedBoard = charArrayOf('X', 'O', ' ', ' ', ' ', ' ', ' ', ' ', ' ')
        // Observe the game status using the mock observer.
        vm.gameStatus.observeForever(mockStatusObserver)
        // Observe the game board using the mock observer.
        vm.board.observeForever(mockBoardObserver)
        // Perform a play in the 0,0 position.
        vm.performPlay(0)
        // Verify that the observer saw a change.
        verify(mockStatusObserver, timeout(OBSERVATION_TIMEOUT))
            .onChanged(any())
        // Sets the order for the board changes.
        inOrder(mockBoardObserver)
        // Finally, check if the players were switched, using the expected board
        verify(mockBoardObserver)
            .onChanged(charArrayOf('X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '))
        // Perform a play in the same position.
        vm.performPlay(0)
        // Assert that no changes were made to the game status,
        // Plays on an already filled position are not allowed.
        verifyZeroInteractions(mockStatusObserver)
        // Make a play in an empty cell.
        vm.performPlay(1)
        // Wait for the board live data to respond.
        Thread.sleep(OBSERVATION_TIMEOUT)
        // Finally, check if the players were switched, using the expected board
        verify(mockBoardObserver, times(2))
            .onChanged(expectedBoard)
    }

    /**
     * Plays a game when the P1 wins by matching a row and
     * asserts that the game results respond with said scoreboard.
     */
    @Test
    fun playGame_P1Wins_By_RowMatch_Test() {
        val expectedResults = HashMap<Boolean, Char>().also { it[true] = 'X' }
        vm.gameStatus.observeForever(mockStatusObserver)
        vm.performPlay(0) // P1 plays
        vm.performPlay(3) // P2 plays
        vm.performPlay(1) // P1 plays
        vm.performPlay(4) // P2 plays
        vm.performPlay(2) // P1 plays
        verify(mockStatusObserver, timeout(OBSERVATION_TIMEOUT))
            .onChanged(expectedResults)
    }

    /**
     * Plays a game when the P2 wins by matching a column and
     * asserts that the game results respond with said scoreboard.
     */
    @Test
    fun playGame_P2Wins_By_ColumnMatch_Test() {
        val expectedResults = HashMap<Boolean, Char>().also { it[true] = 'O' }
        vm.gameStatus.observeForever(mockStatusObserver)
        vm.performPlay(0) // P1 plays
        vm.performPlay(1) // P2 plays
        vm.performPlay(8) // P1 plays
        vm.performPlay(4) // P2 plays
        vm.performPlay(6) // P1 plays
        vm.performPlay(7) // P2 plays
        verify(mockStatusObserver, timeout(OBSERVATION_TIMEOUT))
            .onChanged(expectedResults)
    }

    /**
     * Plays a game when the P1 wins by matching a left to right cross
     * asserts that the game results respond with said scoreboard.
     */
    @Test
    fun playGame_P1Wins_ByLeftToRightCrossMatch_Test() {
        val expectedResults = HashMap<Boolean, Char>().also { it[true] = 'X' }
        vm.gameStatus.observeForever(mockStatusObserver)
        vm.performPlay(0) // P1 plays
        vm.performPlay(3) // P2 plays
        vm.performPlay(4) // P1 plays
        vm.performPlay(2) // P2 plays
        vm.performPlay(8) // P1 plays
        verify(mockStatusObserver, timeout(OBSERVATION_TIMEOUT))
            .onChanged(expectedResults)
    }

    /**
     * Plays a game when the P2 wins by matching a right to left cross
     * asserts that the game results respond with said scoreboard.
     */
    @Test
    fun playGame_P2Wins_ByRightToLeftCrossMatch_Test() {
        val expectedResults = HashMap<Boolean, Char>().also { it[true] = 'O' }
        vm.gameStatus.observeForever(mockStatusObserver)
        vm.performPlay(0) // P1 plays
        vm.performPlay(2) // P2 plays
        vm.performPlay(8) // P1 plays
        vm.performPlay(4) // P2 plays
        vm.performPlay(3) // P1 plays
        vm.performPlay(6) // P2 plays
        verify(mockStatusObserver, timeout(OBSERVATION_TIMEOUT))
            .onChanged(expectedResults)
    }

    /**
     * Plays a game when no player wins and asserts that
     * the game results respond with said scoreboard.
     */
    @Test
    fun playGame_DrawMatch_Test() {
        val expectedResults = HashMap<Boolean, Char>().also { it[true] = 'T' }
        vm.gameStatus.observeForever(mockStatusObserver)
        vm.performPlay(1) // P1 plays
        vm.performPlay(0) // P2 plays
        vm.performPlay(4) // P1 plays
        vm.performPlay(2) // P2 plays
        vm.performPlay(5) // P1 plays
        vm.performPlay(3) // P2 plays
        vm.performPlay(6) // P1 plays
        vm.performPlay(7) // P2 plays
        vm.performPlay(8) // P1 plays
        verify(mockStatusObserver, timeout(OBSERVATION_TIMEOUT))
            .onChanged(expectedResults)
    }
}