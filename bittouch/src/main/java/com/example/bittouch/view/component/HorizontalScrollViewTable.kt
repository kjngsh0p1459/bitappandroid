package com.example.bittouch.view.component

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.HorizontalScrollView

class HorizontalScrollViewTable : HorizontalScrollView {

    private var touchSlop = 0

    private var isEnableScroll = true

    private val syncScrollList: ArrayList<HorizontalScrollViewTable> = ArrayList()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    fun clearSyncScrollItems() {
        syncScrollList.clear()
    }

    fun addSyncScrollItem(item: HorizontalScrollViewTable) {
        if (syncScrollList.indexOf(item) < 0) {
            syncScrollList.add(item)
        }
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (isEnableScroll) {
            for (item in syncScrollList) {
                item.isEnableScroll = false
                item.smoothScrollTo(l, t)
                item.isEnableScroll = true
            }
        }
    }
    override fun getSolidColor(): Int {
        return Color.rgb(245, 250, 255);
    }
}
