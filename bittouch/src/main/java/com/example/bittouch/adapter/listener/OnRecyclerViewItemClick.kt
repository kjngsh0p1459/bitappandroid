package net.runsystem.runner.callback

interface OnRecyclerViewItemClick<T> {
    fun onItemClick(position: Int, item: T?)
}
