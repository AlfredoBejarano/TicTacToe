package me.alfredobejarano.tictactoe.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.alfredobejarano.tictactoe.BuildConfig

/**
 *
 * Class that provides access to the app local storage database using room.
 *
 * @author Alfredo Bejarano
 * @since 22/10/18 - 03:20 PM
 * @version 1.0
 **/
@Database(
    entities = [Scoreboard::class],
    version = BuildConfig.DATABASE_VERSION,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    /**
     * Returns an implementation of the [ScoreboardDao] interface
     */
    abstract fun getScoreboardDao(): ScoreboardDao

    companion object {
        /**
         * Singleton reference to an instance for this app
         * local storage database.
         */
        @Volatile
        private var instance: AppDatabase? = null

        /**
         * Returns the instance of the local storage database,
         * if it is null it proceeds to build one using a given context.
         * @param ctx Context required to build the database, if needed.
         */
        fun getInstance(ctx: Context) =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(ctx).also { instance = it }
            }

        /**
         * Builds the instance for the local storage database,
         * allows this DB to rollback to destructive migrations
         * (wipe the entire DB if a migration is needed).
         *
         * @param ctx The context required to build the database instance.
         */
        private fun buildDatabase(ctx: Context) =
            Room.databaseBuilder(ctx, AppDatabase::class.java, BuildConfig.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }
}