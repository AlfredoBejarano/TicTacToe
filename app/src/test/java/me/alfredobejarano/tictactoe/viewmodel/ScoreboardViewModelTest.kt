package me.alfredobejarano.tictactoe.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import me.alfredobejarano.tictactoe.data.Scoreboard.Result.*
import me.alfredobejarano.tictactoe.data.ScoreboardRepository
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Test class for the [ScoreboardViewModel] class.
 *
 * @author Alfredo Bejarano
 * @version 1.0
 * @since 23/10/18 - 11:46 PM
 */
@RunWith(MockitoJUnitRunner::class)
class ScoreboardViewModelTest {
    /**
     * Mocked repository class for the ViewModel constructor.
     */
    @Mock
    private lateinit var mockRepo: ScoreboardRepository

    /**
     * Mock observer for the scoreboard LiveData object in the ViewModel.
     */
    @Mock
    private lateinit var mockObserver: Observer<IntArray>

    /**
     * Rule that tells to any thread workload to be executed immediately in the UI thread.
     */
    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    /**
     * Tests that after creating a [ScoreboardViewModel]
     * object the Scoreboard is read from the db, that
     * means calling the [ScoreboardRepository.getGamesWonBy]
     * 3 times, one for the P1 wins, one for the P2 wins
     * and one for the draw games.
     */
    @Test
    fun viewModelCreated_scoreboardFetchedTest() {
        // Initialize a ViewModel using our mocked repository class.
        val vm = ScoreboardViewModel(mockRepo)
        // Observe the scoreboard data.
        vm.scoreboard.observeForever(mockObserver)
        // Assert that the mocked object getGamesWonBy function gets called three times.
        // Assert it gets called for the P1 wins.
        Mockito.verify(mockRepo).getGamesWonBy(RESULT_P1_WINS)
        // Assert it gets called for the P2 wins.
        Mockito.verify(mockRepo).getGamesWonBy(RESULT_P2_WINS)
        // And finally, for the draws.
        Mockito.verify(mockRepo).getGamesWonBy(RESULT_TIE)
        // After verifying that the scoreboard has been fetched,
        // assert that those changes got reported via an observer.
        Mockito.verify(mockObserver).onChanged(Mockito.any())
    }
}