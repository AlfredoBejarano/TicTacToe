package me.alfredobejarano.tictactoe

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import me.alfredobejarano.tictactoe.adapter.BoardAdapter
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * Instrumentation test suite for the [GameActivity].
 *
 * @author Alfredo Bejarano
 * @version 1.0
 * @since 24/10/18
 */
class GameActivityTest {
    @Rule
    @JvmField
    val gameActivityTestRule =
        ActivityTestRule<GameActivity>(GameActivity::class.java)

    @Before
    fun clearDB() {
        // Deletes the local database before running any test.
        InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .deleteDatabase(BuildConfig.DATABASE_NAME)
    }

    /**
     * Plays a round and ties the game.
     */
    @Test
    fun playGame_endInDraw() {
        // Click the top left cell for P1.
        onView(withId(R.id.board_view))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<BoardAdapter.CellViewHolder>(0, click())
            )
        // Click the top right cell for P2.
        onView(withId(R.id.board_view))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<BoardAdapter.CellViewHolder>(2, click())
            )
        // Click the bottom left cell for P1.
        onView(withId(R.id.board_view))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<BoardAdapter.CellViewHolder>(6, click())
            )
        // Click the bottom right cell for P2.
        onView(withId(R.id.board_view))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<BoardAdapter.CellViewHolder>(8, click())
            )
        // Click the center cell for P1.
        onView(withId(R.id.board_view))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<BoardAdapter.CellViewHolder>(4, click())
            )
        // Click the middle left cell for P2.
        onView(withId(R.id.board_view))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<BoardAdapter.CellViewHolder>(3, click())
            )
        // Click the middle right cell for P1.
        onView(withId(R.id.board_view))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<BoardAdapter.CellViewHolder>(5, click())
            )
        // Click the middle top cell for P2.
        onView(withId(R.id.board_view))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<BoardAdapter.CellViewHolder>(1, click())
            )
        // Click the middle bottom cell for P1.
        onView(withId(R.id.board_view))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<BoardAdapter.CellViewHolder>(7, click())
            )
        // Assert that a Toast stating that no player won displays.
        onView(withText("No one wins!"))
            .inRoot(
                withDecorView(
                    not(gameActivityTestRule.activity.window.decorView)
                )
            ).check(matches(isDisplayed()))
    }

    /**
     * Plays a round when the P1 (x) will win by matching a column.
     */
    @Test
    fun playGame_p1WinsByColumn() {
        // Click the top left cell for P1.
        onView(withId(R.id.board_view))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<BoardAdapter.CellViewHolder>(0, click())
            )
        // Click the top middle cell for P2.
        onView(withId(R.id.board_view))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<BoardAdapter.CellViewHolder>(1, click())
            )
        // Click the middle left cell for P1.
        onView(withId(R.id.board_view))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<BoardAdapter.CellViewHolder>(3, click())
            )
        // Click the center cell for P2.
        onView(withId(R.id.board_view))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<BoardAdapter.CellViewHolder>(4, click())
            )
        // Click the bottom left cell for P1.
        onView(withId(R.id.board_view))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<BoardAdapter.CellViewHolder>(6, click())
            )
        // Assert that a Toast stating that the p1 won displays.
        onView(withText("Player 1 wins!"))
            .inRoot(
                withDecorView(
                    not(gameActivityTestRule.activity.window.decorView)
                )
            ).check(matches(isDisplayed()))
    }

    /**
     * Plays a round when the P1 (x) will win by matching a right-to-left cross section.
     */
    @Test
    fun playGame_p1WinsByRightToLeftCross() {
        // Click the top right cell for P1.
        onView(withId(R.id.board_view))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<BoardAdapter.CellViewHolder>(2, click())
            )
        // Click the top left cell for P2.
        onView(withId(R.id.board_view))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<BoardAdapter.CellViewHolder>(0, click())
            )
        // Click the center cell for P1.
        onView(withId(R.id.board_view))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<BoardAdapter.CellViewHolder>(4, click())
            )
        // Click the middle left cell for P2.
        onView(withId(R.id.board_view))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<BoardAdapter.CellViewHolder>(3, click())
            )
        // Click the bottom left cell for P1.
        onView(withId(R.id.board_view))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<BoardAdapter.CellViewHolder>(6, click())
            )
        // Assert that a Toast stating that the p1 won displays.
        onView(withText("Player 1 wins!"))
            .inRoot(
                withDecorView(
                    not(gameActivityTestRule.activity.window.decorView)
                )
            ).check(matches(isDisplayed()))
    }

    /**
     * Plays a round when the P2 (o) will win by matching a row.
     */
    @Test
    fun playGame_p2WinsByRow() {
        // Click the top left cell for P1.
        onView(withId(R.id.board_view))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<BoardAdapter.CellViewHolder>(0, click())
            )
        // Click the middle left cell for P2.
        onView(withId(R.id.board_view))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<BoardAdapter.CellViewHolder>(3, click())
            )
        // Click the bottom right cell for P1.
        onView(withId(R.id.board_view))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<BoardAdapter.CellViewHolder>(8, click())
            )
        // Click the center cell for P2.
        onView(withId(R.id.board_view))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<BoardAdapter.CellViewHolder>(4, click())
            )
        // Click the top middle cell for P1.
        onView(withId(R.id.board_view))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<BoardAdapter.CellViewHolder>(1, click())
            )
        // Click the middle right cell for P2.
        onView(withId(R.id.board_view))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<BoardAdapter.CellViewHolder>(5, click())
            )
        // Assert that a Toast stating that the p1 won displays.
        onView(withText("Player 2 wins!"))
            .inRoot(
                withDecorView(
                    not(gameActivityTestRule.activity.window.decorView)
                )
            ).check(matches(isDisplayed()))
    }

    /**
     * Plays a round when the P2 (o) will win by matching a left-to-right cross section.
     */
    @Test
    fun playGame_p2WinsByLeftToRightCross() {
        // Click the top right cell for P1.
        onView(withId(R.id.board_view))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<BoardAdapter.CellViewHolder>(2, click())
            )
        // Click the top left cell for P2.
        onView(withId(R.id.board_view))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<BoardAdapter.CellViewHolder>(0, click())
            )
        // Click the right middle cell for P1.
        onView(withId(R.id.board_view))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<BoardAdapter.CellViewHolder>(5, click())
            )
        // Click the center cell for P2.
        onView(withId(R.id.board_view))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<BoardAdapter.CellViewHolder>(4, click())
            )
        // Click the bottom left cell for P1.
        onView(withId(R.id.board_view))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<BoardAdapter.CellViewHolder>(6, click())
            )
        // Click the bottom right cell for P2.
        onView(withId(R.id.board_view))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<BoardAdapter.CellViewHolder>(8, click())
            )
        // Assert that a Toast stating that the p1 won displays.
        onView(withText("Player 2 wins!"))
            .inRoot(
                withDecorView(
                    not(gameActivityTestRule.activity.window.decorView)
                )
            ).check(matches(isDisplayed()))
    }
}