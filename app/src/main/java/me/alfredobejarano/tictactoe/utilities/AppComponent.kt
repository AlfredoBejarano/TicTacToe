package me.alfredobejarano.tictactoe.utilities

import dagger.Component
import me.alfredobejarano.tictactoe.data.ScoreboardDao
import me.alfredobejarano.tictactoe.data.ScoreboardRepository
import javax.inject.Singleton

/**
 *
 * Class that defines the dependency injection graph for this project using Dagger.
 *
 * @author Alfredo Bejarano
 * @since 23/10/18
 * @version 1.0
 **/
@Singleton
@Component(
    modules = [
        ScoreboardDaoModule::class,
        ScoreboardRepositoryModule::class,
        ScoreboardViewModelFactoryModule::class
    ]
)
interface AppComponent {
    /**
     * Provides injection for the [ScoreboardDao] class.
     */
    fun provideScoreboardDao(): ScoreboardDao

    /**
     * Provides injection for the [ScoreboardRepository] class.
     */
    fun provideScoreboardRepository(): ScoreboardRepository
}