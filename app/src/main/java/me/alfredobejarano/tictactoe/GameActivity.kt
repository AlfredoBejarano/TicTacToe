package me.alfredobejarano.tictactoe

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_game.*
import me.alfredobejarano.tictactoe.adapter.BoardAdapter
import me.alfredobejarano.tictactoe.databinding.ActivityGameBinding
import me.alfredobejarano.tictactoe.utilities.Injector
import me.alfredobejarano.tictactoe.viewmodel.GameViewModel
import me.alfredobejarano.tictactoe.viewmodel.ScoreboardViewModel
import java.util.*
import javax.inject.Inject

/**
 * Main activity of this app, this activity displays the game board.
 * @author Alfredo Bejarano
 * @since October 24th, 2018 - 01:34 PM
 * @version 1.0
 */
class GameActivity : AppCompatActivity(), BoardAdapter.OnCellClickedListener {
    companion object {
        private const val SAVED_BOARD_EXTRA = "me.alfredobejarano.tictactoe.SAVED_BOARD_EXTRA"
        private const val SAVED_PLAYER_EXTRA = "me.alfredobejarano.tictactoe.SAVED_PLAYER_EXTRA"
    }

    /**
     * Injected dependency for a [ScoreboardViewModel.Factory] class.
     */
    @Inject
    lateinit var scoreboardVMFactory: ScoreboardViewModel.Factory
    /**
     * Reference for a [GameViewModel] class.
     */
    private lateinit var gameVM: GameViewModel

    /**
     * Reference for a [ScoreboardViewModel] class.
     */
    private lateinit var scoreboardVM: ScoreboardViewModel

    /**
     * Creates this activity, observes the ViewModels and initializes the injector.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initializes the dagger Injector.
        Injector.initialize(application)
        // Inject this class dependencies
        Injector.component.inject(this)
        // Retrieve the scoreboard ViewModel for this activity.
        scoreboardVM =
                ViewModelProviders.of(this, scoreboardVMFactory)[ScoreboardViewModel::class.java]
        // Retrieve the ViewModel for the game.
        gameVM = ViewModelProviders.of(this)[GameViewModel::class.java]
        // Observe the LiveData fields of the GameViewModel.
        observeGameViewModel()
        // Get the binding of this view layout.
        val binding = ActivityGameBinding.inflate(layoutInflater)
        // Set the scoreboard VM for the binding.
        binding.scoreboardVM = scoreboardVM
        // Set the binding lifecycle owner.
        binding.setLifecycleOwner(this)
        // Set this activity content view.
        setContentView(binding.root)
        // Check if there is a game restored, if so, restores it.
        if (savedInstanceState?.isEmpty == false) {
            val player = savedInstanceState.getChar(SAVED_PLAYER_EXTRA)
            val board = savedInstanceState.getCharArray(SAVED_BOARD_EXTRA)
            gameVM.restoreGame(board, player)
        }
    }

    /**
     * Saves a game board and player turn in the savedStateInstance.
     */
    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState?.putChar(SAVED_PLAYER_EXTRA, gameVM.currentPlayer)
        outState?.putCharArray(SAVED_BOARD_EXTRA, gameVM.board.value)
    }

    /**
     * Observes the game board and game status from the [GameViewModel] class of this UI controller.
     */
    private fun observeGameViewModel() {
        // Observe the GameViewModel board.
        gameVM.board.observe(this, Observer { board ->
            board_view?.adapter?.let {
                (it as BoardAdapter).setBoard(board)
            } ?: run {
                board_view?.adapter = BoardAdapter(board)
            }
        })
        // Observe the game status.
        gameVM.gameStatus.observe(this, Observer { results ->
            // Execute this check if the results are not null.
            if (results?.isNotEmpty() == true) {
                // Get if the game has ended or not.
                val gameEnded = results.keys.first()
                // If the game has ended
                if (gameEnded) {
                    val winner = String.format(
                        Locale.getDefault(), getString(R.string.winner), when (results[gameEnded]) {
                            GameViewModel.PLAYER_1_CHAR -> "Player 1"
                            GameViewModel.PLAYER_2_CHAR -> "Player 2"
                            else -> "No one"
                        }
                    )
                    // Display who won.
                    Toast.makeText(this, winner, Toast.LENGTH_SHORT).show()
                    // Store this game scoreboard.
                    scoreboardVM.saveScoreboard(results[gameEnded] ?: ' ')
                    // Restart the game.
                    gameVM.restartGame()
                    // Read the Scoreboard
                    scoreboardVM.readScoreboard()
                }
            }
        })
    }

    /**
     * Clears the board to start a new game.
     */
    fun restartGame(v: View) = gameVM.restartGame()

    /**
     * Deletes all the stored game results.
     */
    fun resetScoreboard(v: View) = scoreboardVM.clearScoreboard()

    override fun onCellClicked(cellPosition: Int) = gameVM.performPlay(cellPosition)
}
