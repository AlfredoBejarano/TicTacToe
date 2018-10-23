package me.alfredobejarano.tictactoe.utilities

import android.app.Application

/**
 *
 * Object class that provides Singleton access to this
 * application dagger component that allows to perform injection
 * for classes that use the @Inject annotations on its fields.
 *
 * @author Alfredo Bejarano
 * @since 23/10/18
 * @version 1.0
 **/
object Injector {
    /**
     * Reference to this project [Application] class,
     * this helps having a [Context] without having to
     * keep it statically and have memory leaks because of that.
     */
    @Volatile
    private lateinit var mApp: Application

    /**
     * Singleton access for the [component][AppComponent]
     * dagger implementation for performing injection.
     */
    val component by lazy {
        DaggerAppComponent
            .builder()
            .scoreboardDaoModule(ScoreboardDaoModule(mApp))
            .scoreboardRepositoryModule(ScoreboardRepositoryModule())
            .build()
    }

    /**
     * Initializes this injector object, basically
     * assigns a value to the mApp property of this class.
     */
    fun initialize(app: Application) {
        mApp = app
    }
}