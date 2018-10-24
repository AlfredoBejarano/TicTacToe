package me.alfredobejarano.tictactoe.callback

import androidx.recyclerview.widget.DiffUtil

/**
 *
 * [DiffUtil] callback that reports only
 * the changed cell instead of re-rendering
 * the whole board.
 *
 * @author Alfredo Bejarano
 * @since 24/10/18
 * @version 1.0
 **/
class BoardDiffCallback(private val oldBoard: CharArray, private val newBoard: CharArray) : DiffUtil.Callback() {
    /**
     * Checks if the items at a position are the same or not.
     * @param oldItemPosition The position of the old item.
     * @param newItemPosition The position of the new item.
     */
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldBoard[oldItemPosition] == newBoard[newItemPosition]

    /**
     * Always when replacing a board, the size is
     * expected to be 9 as the grid size is 3 by 3.
     */
    override fun getOldListSize() = 9

    /**
     * Always when replacing a board, the size is
     * expected to be 9 as the grid size is 3 by 3.
     */
    override fun getNewListSize() = 9

    /**
     * Checks if the contents of the cells are the same.
     */
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        areItemsTheSame(oldItemPosition, newItemPosition)
}