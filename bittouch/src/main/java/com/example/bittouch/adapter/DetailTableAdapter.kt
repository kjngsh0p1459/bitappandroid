package com.example.bittouch.adapter

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bittouch.R
import com.example.bittouch.adapter.base.NewBaseAdapter
import com.example.bittouch.adapter.base.holder.NewBaseViewHolder
import kotlinx.android.synthetic.main.item_detail_table.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class DetailTableAdapter(context: Context) : NewBaseAdapter<List<String>>(context) {

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateNormalViewHolder(parent: ViewGroup, viewType: Int): NewBaseViewHolder<List<String>>? =
            ViewHolderTable(LayoutInflater.from(parent.context).inflate(ViewHolderTable.LAYOUT_ID, parent, false))

    override fun onBindNormalViewHolder(holder: NewBaseViewHolder<List<String>>, position: Int) {
        super.onBindNormalViewHolder(holder, position)
        getItem(position)?.let {
            (holder as? ViewHolderTable)?.bindData(it, position)
        }
    }

    class ViewHolderTable(itemView: View) : NewBaseViewHolder<List<String>>(itemView) {

        companion object {
            @JvmStatic
            val LAYOUT_ID = R.layout.item_detail_table
        }

        override fun bindData(item: List<String>, position: Int) {
            super.bindData(item, position)
            doAsync {
                uiThread {
                    itemView?.rvDetailTable?.run {
                        val deepAdapter = DetailDeepTableAdapter(context)
                        this@run.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        this@run.adapter = deepAdapter
                        deepAdapter.refreshData(item)
                    }
                }
            }
        }
    }
}