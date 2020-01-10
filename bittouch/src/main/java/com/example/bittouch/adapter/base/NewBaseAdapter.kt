package com.example.bittouch.adapter.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import net.runsystem.runner.adapter.util.ItemHeader
import net.runsystem.runner.adapter.util.ItemHeaderSticky
import android.support.v7.util.DiffUtil
import com.example.bittouch.adapter.base.holder.NewBaseViewHolder
import com.example.bittouch.adapter.base.holder.NewDefaultLoadMoreViewHolder

abstract class NewBaseAdapter<T>(open var context: Context, var hasLoadingView: Boolean = false, var hasAddEmptyItem: Boolean = false) : AdapterWithItemClick<T>(context) {

    companion object {
        @JvmStatic
        var VIEW_NORMAL = 0
        @JvmStatic
        var VIEW_LOADING = 1
        @JvmStatic
        var VIEW_HEADER = 2
    }

    var dataSet: MutableList<T> = mutableListOf()

    private var isLoadingMore: Boolean = false

    /**
     * Set data in first time.
     *
     * @param initialDataSet
     */
    fun loadInitialDataSet(initialDataSet: List<T>) {
        internalInit(initialDataSet)
    }

    /**
     * Pre load adapter with data
     * Thí function will not trigger [onAdapterInitial] method
     */
    fun preLoadDataSet(initialDataSet: List<T>) {
        internalInit(initialDataSet)
    }

    /**
     * Add more data to list
     */
    fun addMoreData(moreItems: List<T>) {
        internalAddMore(moreItems)
    }

    /**
     * Internal method, please proceed with caution
     */
    private fun internalInit(moreItems: List<T>) {
        dataSet.clear()
        dataSet.addAll(moreItems)
        notifyDataSetChanged()
    }

    /**
     * Clear data
     */
    fun clearData() {
        isLoadingMore = false
        dataSet.clear()
        notifyDataSetChanged()
    }

    /**
     * Internal method, please proceed with caution
     */
    open fun internalAddMore(moreItems: List<T>) {
        if (hasLoadingView) {
            notifyItemRemoved(getLoadingViewPos())
        }
        if (moreItems.isEmpty()) {
            return
        }
        val beforePosition = dataSet.size
        dataSet.addAll(moreItems)
        notifyItemRangeInserted(beforePosition, getCurrentAdapterSize() - beforePosition)
    }

    /**
     * Remove item at [position]
     */
    fun removeItem(position: Int) {
        if (hasLoadingView && position == getLoadingViewPos()) {
            return
        }
        this.dataSet.removeAt(position)
        notifyItemRemoved(position)
    }

    fun dismissProgressLoadmoreIfCan() {
        if (hasLoadingView) {
            notifyItemRemoved(getLoadingViewPos())
            hasLoadingView = false
        }
    }

    fun enableProgressLoadmore() {
        hasLoadingView = true
    }

    /**
     * Refresh list content
     */
    fun refreshData(newList: List<T>) {
        this.dataSet.clear()
        this.dataSet.addAll(newList)
        this.notifyDataSetChanged()
    }

    /**
     * Refresh list content
     */
    fun refreshRow(position: Int, newList: List<T>) {
        this.dataSet.clear()
        this.dataSet.addAll(newList)
        this.notifyItemChanged(position)
    }

    fun dispatchData(newList: List<T>, diffCallback: DiffUtil.Callback){
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        this.dataSet.clear()
        this.dataSet.addAll(newList)
    }

    protected fun getCurrentAdapterSize(): Int {
        return when {
            hasLoadingView && isLoadingMore -> dataSet.size + 1
            else -> dataSet.size
        }
    }

    /**
     * Return loading view position if present
     */
    protected fun getLoadingViewPos(): Int {
        return if (hasLoadingView) {
            dataSet.size
        } else {
            dataSet.size - 1
        }
    }

    /**
     * Indicate list start load more > show loading if any
     */
    fun beginLoadingMore() {
        isLoadingMore = true
        if (hasLoadingView) {
            this.notifyItemInserted(getLoadingViewPos())
        }
    }


    /**
     * Indicate list finish load more > add more data and hide loading if any
     */
    fun finishLoadingMore(moreItems: List<T>) {
        isLoadingMore = false
        addMoreData(moreItems)
    }

    /**
     * Indicate list finish load more > add more data and hide loading if any
     */
    fun finishLoadingMoreWithError() {
        isLoadingMore = false
        if (hasLoadingView) {
            notifyItemRemoved(getLoadingViewPos())
        }
    }

    override fun getItemCount(): Int {
        return getCurrentAdapterSize()
    }

    override fun getItemViewType(position: Int): Int {
//        return super.getItemViewType(position)
        return if (hasLoadingView && isLoadingMore && position == getLoadingViewPos()) {
            VIEW_LOADING
    } else if (dataSet[position] is ItemHeader || dataSet[position] is ItemHeaderSticky) {
            onGetHeaderItemViewType(position)
        } else {
            onGetNormalItemViewType(position)
        }
    }

    open fun onGetHeaderItemViewType(position: Int): Int {
        return VIEW_HEADER
    }

    open fun onGetNormalItemViewType(position: Int): Int {
        return VIEW_NORMAL
    }

    override fun createItemViewHolder(parent: ViewGroup, viewType: Int): NewBaseViewHolder<T> {
        return when (viewType) {
            VIEW_LOADING -> onCreateLoadMoreViewHolder(parent)!!
            else -> if (isHeaderView(viewType)) onCreateHeaderViewHolder(parent, viewType)!! else onCreateNormalViewHolder(parent, viewType)!!
        }
    }

    open fun isHeaderView(viewType: Int): Boolean {
        return when (viewType) {
            VIEW_HEADER -> true
            else -> false
        }
    }

    /**
     * Create normal view holder view
     */
    abstract fun onCreateNormalViewHolder(parent: ViewGroup, viewType: Int): NewBaseViewHolder<T>?

    open fun onCreateHeaderViewHolder(parent: ViewGroup, viewType: Int): NewBaseViewHolder<T>? {
        val view = LayoutInflater.from(context).inflate(NewDefaultLoadMoreViewHolder.LAYOUT_ID_LOADMORE, parent, false)
        return NewDefaultLoadMoreViewHolder(view)
    }

    /**
     * Get Load More View Holder
     * @param parent
     * @return
     */
    open fun onCreateLoadMoreViewHolder(parent: ViewGroup): NewBaseViewHolder<T>? {
        val view = LayoutInflater.from(context).inflate(if (hasAddEmptyItem) NewDefaultLoadMoreViewHolder.LAYOUT_ID_BLANK else NewDefaultLoadMoreViewHolder.LAYOUT_ID_LOADMORE, parent, false)
        return NewDefaultLoadMoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewBaseViewHolder<T>, position: Int) {
        val viewType = getItemViewType(position)
        return when (viewType) {
            VIEW_LOADING -> onBindLoadingViewHolder(holder, position)
            else -> {
                if (isHeaderView(viewType)) {
                    onBindHeaderViewHolder(holder, position)
                } else {
                    onBindNormalViewHolder(holder, position)
                }
            }
        }
    }

    open fun onBindNormalViewHolder(holder: NewBaseViewHolder<T>, position: Int) {
        if (position < dataSet.size)
            holder.bindData(dataSet[position], position)
    }

    open fun onBindLoadingViewHolder(holder: NewBaseViewHolder<T>, position: Int) {
        //Handle loading view
    }

    open fun onBindHeaderViewHolder(holder: NewBaseViewHolder<T>, position: Int) {
        if (position < dataSet.size)
            holder.bindData(dataSet[position], position)
    }

    fun getItem(position: Int): T? {
        return if (dataSet.size > position) {
            dataSet[position]
        } else {
            null
        }
    }
}