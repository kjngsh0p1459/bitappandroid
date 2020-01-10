package com.example.bittouch.view.component

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet

class SwipeToRefreshTable(context: Context, attrs: AttributeSet) :
        SwipeRefreshLayout(context, attrs) {

    //  Recycler's Linearlayout if it is CustomSwipeToRefresh's child view
    var linearLayoutManager: LinearLayoutManager? = null
    var hardDisable: Boolean = true

    private val ITEM_FIRST_POSITION = 0

    // Indicate if we've already declined the move event
    init {
        setProgressViewOffset(false, progressViewStartOffset, progressViewEndOffset)
    }

    override fun isEnabled(): Boolean {
        if (hardDisable) return false
        val enable = super.isEnabled()
        if (enable) linearLayoutManager?.let {
            //  SwipeToRefresh is disable when Recyclerview actually scroll to first item or is empty
            return it.findFirstVisibleItemPosition() <= ITEM_FIRST_POSITION
        }
        return enable
    }
}