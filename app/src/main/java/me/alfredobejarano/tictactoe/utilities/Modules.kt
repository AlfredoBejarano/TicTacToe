package me.alfredobejarano.tictactoe.utilities

import android.content.Context
import dagger.Module
import dagger.Provides
import me.alfredobejarano.tictactoe.data.AppDatabase
import me.alfredobejarano.tictactoe.data.ScoreboardDao
import me.alfredobejarano.tictactoe.data.ScoreboardRepository
import me.alfredobejarano.tictactoe.viewmodel.ScoreboardViewModel
import javax.inject.Singleton

/**
 *
 * This kotlin file will contain all the module classes
 * for Dagger to provide injection with.
 *
 * @author Alfredo Bejarano
 * @since 22/10/18 - 11:21 PM
 * @version 1.0
 **/

/**
 * Module class that tells to dagger how to provide
 * a [ScoreboardDao] instance when requested from injection.
 */
@Module
class ScoreboardDaoModule(private val ctx: Context) {
    /**
     * Calls or initializes the local app database instance
     * to retrieve the [ScoreboardDao] implementation from
     * within and provide it when requested.
     * @see AppDatabase
     * @return A Room [ScoreboardDao] implementation.
     */
    @Provides
    @Singleton
    fun provideScoreboardDao() =
        AppDatabase.getInstance(ctx).getScoreboardDao()
}

/**
 * Module class that tells to dagger how to provide
 * a [ScoreboardRepository] instance when requested from injection.
 */
@Module
class ScoreboardRepositoryModule {
    /**
     * Creates a new [ScoreboardRepository] object
     * and provides it when requested.
     * @return [ScoreboardRepository] object with a [ScoreboardDao] injected to it.
     */
    @Provides
    @Singleton
    fun provideScoreboardRepositoryModule(dao: ScoreboardDao) =
        ScoreboardRepository(dao)
}

/**
 * Module class that tells to dagger how to provide
 * a [ScoreboardViewModel.Factory] instance when requested from injection.
 */
@Module
class ScoreboardViewModelFactoryModule {
    /**
     * Creates a new [ScoreboardViewModel.Factory] object
     * and provides it when requested using the [AppComponent] [ScoreboardRepository].
     * @return [ScoreboardRepository] object with a [ScoreboardDao] injected to it.
     */
    @Provides
    @Singleton
    fun provideScoreboardViewModelFactory() =
        ScoreboardViewModel.Factory(Injector.component.provideScoreboardRepository())
}
