package com.example.bittouch.view.component

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

class RecycleViewTable: RecyclerView {

    private val syncScrollList: ArrayList<RecycleViewTable> = ArrayList()
    var isEnableScroll = true

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs,
            defStyle)

    fun clearSyncScrollItems() {
        syncScrollList.clear()
    }

    fun addSyncScrollItem(item: RecycleViewTable) {
        if (syncScrollList.indexOf(item) < 0) {
            syncScrollList.add(item)
        }
    }

    override fun onScrolled(dx: Int, dy: Int) {
        super.onScrolled(dx, dy)
        if (isEnableScroll) {
            for(item in syncScrollList) {
                item.isEnableScroll = false
                item.scrollBy(dx, dy)
                item.isEnableScroll = true
            }
        }
    }
}