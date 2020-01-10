package net.runsystem.runner.callback

interface OnRecyclerViewItemLongClick<T> {
    fun onItemLongClick(position: Int, item: T?)
}
