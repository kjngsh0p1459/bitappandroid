package com.example.bittouch.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bittouch.R
import com.example.bittouch.adapter.base.NewBaseAdapter
import com.example.bittouch.adapter.base.holder.NewBaseViewHolder
import com.example.bittouch.event.ApiLifecycleEvent
import kotlinx.android.synthetic.main.item_logging_menu.view.*

class ApiAdapter(context: Context) : NewBaseAdapter<ApiLifecycleEvent>(context) {

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateNormalViewHolder(parent: ViewGroup, viewType: Int): NewBaseViewHolder<ApiLifecycleEvent>? =
            ViewHolderTable(LayoutInflater.from(parent.context).inflate(ViewHolderTable.LAYOUT_ID, parent, false))

    override fun onBindViewHolder(holder: NewBaseViewHolder<ApiLifecycleEvent>, position: Int) {
        super.onBindViewHolder(holder, position)
        getItem(position)?.let {
            (holder as? ViewHolderTable)?.bindData(it, position)
        }
    }

    class ViewHolderTable(itemView: View) : NewBaseViewHolder<ApiLifecycleEvent>(itemView) {

        companion object {
            @JvmStatic
            val LAYOUT_ID = R.layout.item_logging_menu
        }

        override fun bindData(item: ApiLifecycleEvent, position: Int) {
            super.bindData(item, position)
            itemView?.run {
                tvApiRequest?.text = item.request.url().toString()
                tvDesignation?.text = item.response.headers().toString()
                tvRequestTrue?.text = item.response.code().toString()
            }
        }
    }
}

