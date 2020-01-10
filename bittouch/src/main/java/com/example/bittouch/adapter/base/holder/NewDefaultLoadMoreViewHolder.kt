package com.example.bittouch.adapter.base.holder

import android.view.View
import com.example.bittouch.R

class NewDefaultLoadMoreViewHolder<T>(itemView: View) : NewBaseViewHolder<T>(itemView) {

    companion object {
        @JvmField
        val LAYOUT_ID_LOADMORE: Int = R.layout.item_load_more
        val LAYOUT_ID_BLANK: Int = R.layout.item_blank
    }
}