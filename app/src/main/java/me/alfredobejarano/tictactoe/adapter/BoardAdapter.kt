package me.alfredobejarano.tictactoe.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.alfredobejarano.tictactoe.R
import me.alfredobejarano.tictactoe.viewmodel.GameViewModel

/**
 *
 * Adapter for a RecyclerView that displays
 * the TicTacToe board.
 *
 * @author Alfredo Bejarano
 * @since 24/10/18
 * @version 1.0
 **/
class BoardAdapter(private var board: CharArray) : RecyclerView.Adapter<BoardAdapter.CellViewHolder>() {
    /**
     * Creates a Cell for the RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CellViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_cell, parent, false)
        )

    /**
     * As a 3 by 3 grid, the amount of cells has to be 9.
     * @return 9
     */
    override fun getItemCount() = 9

    /**
     * Binds a ViewHolder at a given position.
     */
    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        // Set the text color depending on the char being rendered.
        holder.cell?.setTextColor(
            when (board[position]) {
                GameViewModel.PLAYER_1_CHAR -> Color.RED
                GameViewModel.PLAYER_2_CHAR -> Color.BLUE
                else -> Color.BLACK
            }
        )
        // Set the board char as the cell text.
        holder.cell?.text = board[position].toString()

        // Enable the clicking behaviour for performing plays.
        holder.itemView.setOnClickListener {
            if (it.context is OnCellClickedListener) {
                (it.context as OnCellClickedListener).onCellClicked(position)
            }
        }
    }

    /**
     * Updates the board with a new board.
     */
    fun setBoard(newBoard: CharArray) {
        // Change the board.
        board = newBoard
        notifyDataSetChanged()
    }

    class CellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /**
         * The cell view that can be clicked for performing a play.
         */
        internal val cell = itemView.findViewById<TextView?>(R.id.cell)
    }

    /**
     * Interface that defines functions for interacting with a cell.
     */
    interface OnCellClickedListener {
        /**
         * Reports which cell was clicked.
         * @param cellPosition The position of this cell.
         */
        fun onCellClicked(cellPosition: Int)
    }
}