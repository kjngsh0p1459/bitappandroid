package com.example.bittouch.adapter.base

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.bittouch.adapter.base.holder.NewBaseViewHolder
import net.runsystem.runner.callback.OnRecyclerViewItemClick
import net.runsystem.runner.callback.OnRecyclerViewItemLongClick

abstract class AdapterWithItemClick<T>(private val context: Context) : RecyclerView.Adapter<NewBaseViewHolder<T>>() {

    var onRecyclerViewItemClick: OnRecyclerViewItemClick<T>? = null

    var onRecyclerViewItemLongClick: OnRecyclerViewItemLongClick<T>? = null

    var onRecyclerViewItemTouch: View.OnTouchListener? = null


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NewBaseViewHolder<T> {
        val holder = createItemViewHolder(p0, p1)

        onRecyclerViewItemClick?.run {holder.setOnRecyclerViewItemClick(this)}
        onRecyclerViewItemLongClick?.run {holder.setOnRecyclerViewItemLongClick(this)}
        onRecyclerViewItemTouch?.run {holder.setOnRecyclerViewItemTouch(this)}

        return holder
    }

    abstract fun createItemViewHolder(p0: ViewGroup, p1: Int) : NewBaseViewHolder<T>
}