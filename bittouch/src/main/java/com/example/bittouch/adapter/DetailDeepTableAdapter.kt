package com.example.bittouch.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bittouch.R
import com.example.bittouch.adapter.base.NewBaseAdapter
import com.example.bittouch.adapter.base.holder.NewBaseViewHolder
import kotlinx.android.synthetic.main.item_detail_deep_table.view.*
import kotlinx.android.synthetic.main.item_freeze_header_table.view.*

class DetailDeepTableAdapter(context: Context) : NewBaseAdapter<String>(context) {

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateNormalViewHolder(parent: ViewGroup, viewType: Int): NewBaseViewHolder<String>? =
            ViewHolderTable(LayoutInflater.from(parent.context).inflate(ViewHolderTable.LAYOUT_ID, parent, false))

    override fun onBindNormalViewHolder(holder: NewBaseViewHolder<String>, position: Int) {
        super.onBindNormalViewHolder(holder, position)
        getItem(position)?.let {
            (holder as? ViewHolderTable)?.bindData(it, position)
        }
    }

    class ViewHolderTable(itemView: View) : NewBaseViewHolder<String>(itemView) {

        companion object {
            @JvmStatic
            val LAYOUT_ID = R.layout.item_detail_deep_table
        }

        override fun bindData(item: String, position: Int) {
            super.bindData(item, position)
            itemView?.tvDetailDeepContent?.text = item
        }
    }
}