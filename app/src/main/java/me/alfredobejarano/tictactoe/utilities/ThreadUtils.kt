package me.alfredobejarano.tictactoe.utilities

import java.util.concurrent.Executors

/**
 *
 * Utils class that provides easy access to multi-thread operations.
 *
 * @author Alfredo Bejarano
 * @since 22/10/18 - 11:02 PM
 * @version 1.0
 **/
/**
 * Single threaded executor that will do a given work.
 */
private val IO_THREAD_EXECUTOR = Executors.newSingleThreadExecutor()

/**
 * Executes a given f work in a worker thread, preventing
 * long time operations or heavy workloads from being executed
 * in the UI thread and also preventing ANRs, this comes in handy
 * when using the Room local database, for example.
 */
fun runOnIOThread(f: () -> Unit) = IO_THREAD_EXECUTOR.execute(f)