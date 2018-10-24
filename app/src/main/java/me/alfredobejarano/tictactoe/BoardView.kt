package me.alfredobejarano.tictactoe

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * Simple [RecyclerView] subclass that
 * represents a board for playing TicTacToe.
 *
 * @author Alfredo Bejarano
 * @since 24/10/18
 * @version 1.0
 **/
class BoardView(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs) {
    init {
        layoutManager = GridLayoutManager(context, 3)
    }
}