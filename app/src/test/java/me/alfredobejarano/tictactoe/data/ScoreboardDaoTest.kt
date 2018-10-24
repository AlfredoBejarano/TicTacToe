package me.alfredobejarano.tictactoe.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

/**
 *
 * Test class for the [ScoreboardDao] implementation of Room.
 *
 * @author Alfredo Bejarano
 * @since 23/10/18 - 10:23 PM
 * @version 1.0
 **/
@RunWith(RobolectricTestRunner::class)
class ScoreboardDaoTest {
    /**
     * Reference to a in-memory Room database to perform tests on.
     */
    private lateinit var inMemoryDB: AppDatabase

    /**
     * reference to the [ScoreboardDao] to be tested.
     */
    private lateinit var testDao: ScoreboardDao

    /**
     * Rule that tells to any thread workload to be executed immediately in the UI thread.
     */
    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    /**
     * Before any test gets executed, create the in-memory database reference.
     */
    @Before
    fun createInMemoryDB() {
        inMemoryDB = Room.inMemoryDatabaseBuilder(
            RuntimeEnvironment.systemContext,
            AppDatabase::class.java
        ).allowMainThreadQueries()
            .build().also {
                // Initialize the testDao property.
                testDao = it.getScoreboardDao()
            }
    }

    /**
     * Checks that a record gets inserted correctly.
     */
    @Test
    fun insertOrUpdateTest() {
        // Mock a Scoreboard object.
        val record = Mockito.mock(Scoreboard::class.java)
        // When the mock result property gets called, return a Tie.
        Mockito.`when`(record.result).thenReturn(
            Scoreboard.Result.RESULT_TIE
        )
        // Insert the record to the DB using the dao.
        testDao.insertOrUpdate(record)
        // Assert that the amount of games with the mock result is one.
        assert(testDao.countGamesByResult(record.result) == 1)
    }

    /**
     * Asserts that the count of a game won by a given result matches
     * the amount of records that have that result value.
     */
    @Test
    fun countGamesByResultTest() {
        // Define how many times a mock is going to be persisted.
        val times = 5
        repeat(times) {
            // Create the mock.
            val mock = Mockito.mock(Scoreboard::class.java)
            // Respond with a P1 game winning when the record result property gets called.
            Mockito.`when`(mock.result).thenReturn(Scoreboard.Result.RESULT_P1_WINS)
            // Insert the mock
            testDao.insertOrUpdate(mock)
        }
        // Assert that the times value is the same as the amount of records matching the result.
        assert(testDao.countGamesByResult(Scoreboard.Result.RESULT_P1_WINS) == times)
    }

    /**
     * Checks that after inserting something into the database
     * and then nuking the table, the table is empty.
     */
    @Test
    fun deleteAllTest() {
        // Create the mock.
        val mock = Mockito.mock(Scoreboard::class.java)
        // Respond with a P1 game winning when the record result property gets called.
        Mockito.`when`(mock.result).thenReturn(Scoreboard.Result.RESULT_P2_WINS)
        // Insert the mock
        testDao.insertOrUpdate(mock)
        // Assert that the amount of games with the mock result is one.
        assert(testDao.countGamesByResult(mock.result) == 1)
        // Now, proceed to nuke the table.
        testDao.deleteAll()
        // Finally, assert that the table is completely empty.
        assert(
            testDao.countGamesByResult(Scoreboard.Result.RESULT_TIE) == 0
                    && testDao.countGamesByResult(Scoreboard.Result.RESULT_P1_WINS) == 0
                    && testDao.countGamesByResult(Scoreboard.Result.RESULT_TIE) == 0
        )
    }

    /**
     * After running the tests, close the database connection.
     */
    @After
    fun closeDB() {
        inMemoryDB.close()
    }
}