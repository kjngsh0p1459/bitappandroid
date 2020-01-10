package com.example.bittouch.adapter.base.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import net.runsystem.runner.callback.OnRecyclerViewItemClick
import net.runsystem.runner.callback.OnRecyclerViewItemLongClick

open class NewBaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var onRecyclerViewItemClick: OnRecyclerViewItemClick<T>? = null
    private var onRecyclerViewItemLongClick: OnRecyclerViewItemLongClick<T>? = null
    private var onRecyclerViewItemTouch: View.OnTouchListener? = null

    protected var item: T? = null
    protected open var position: Int? = null

    open fun getOnRecyclerViewItemClick() = onRecyclerViewItemClick
    open fun getOnRecyclerViewItemLongClick() = onRecyclerViewItemLongClick
    open fun getOnRecyclerViewItemTouch() = onRecyclerViewItemTouch

    open fun setOnRecyclerViewItemClick(onRecyclerViewItemClick: OnRecyclerViewItemClick<T>?) {
        itemView.setOnClickListener { onRecyclerViewItemClick?.onItemClick(adapterPosition, item) }
        this.onRecyclerViewItemClick = onRecyclerViewItemClick
    }

    open fun setOnRecyclerViewItemLongClick(onRecyclerViewItemLongClick: OnRecyclerViewItemLongClick<T>?) {
        itemView.setOnLongClickListener {
            onRecyclerViewItemLongClick?.onItemLongClick(adapterPosition, item)
            true
        }
        this.onRecyclerViewItemLongClick = onRecyclerViewItemLongClick
    }

    open fun setOnRecyclerViewItemTouch(onRecyclerViewItemTouch: View.OnTouchListener?) {
        itemView.setOnTouchListener{ v, event ->
            onRecyclerViewItemTouch?.onTouch(v, event) ?: false
        }
        this.onRecyclerViewItemTouch = onRecyclerViewItemTouch
    }

    open fun bindData(item: T, position: Int) {
        this.item = item
        this.position = position
    }
}
