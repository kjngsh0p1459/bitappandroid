package com.example.bittouch.view

import android.content.Context
import android.graphics.PixelFormat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.bittouch.R
import com.example.bittouch.adapter.ApiAdapter
import com.example.bittouch.event.ApiLifecycleEvent
import com.example.bittouch.manager.BitViewManager
import com.yuyh.jsonviewer.library.JsonRecyclerView
import net.runsystem.runner.callback.OnRecyclerViewItemClick
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class BitLogApiView(private val mContext: Context, attrs: AttributeSet?) : RelativeLayout(mContext, attrs), OnRecyclerViewItemClick<ApiLifecycleEvent> {

    private var rlLoggingView: View? = null
    private var layoutParam: LayoutParams? = null
    private var listApiEvent: ArrayList<ApiLifecycleEvent>?= null
    private var hasViewAddedToWindow = false;

    init {
        initLayoutParams()
        initLoggingView()
        EventBus.getDefault().register(this)
        listApiEvent = ArrayList()
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        hasViewAddedToWindow = true
        addDataForListApi()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        hasViewAddedToWindow = false
    }

    private fun addDataForListApi() {
        if (!hasViewAddedToWindow) return
        listApiEvent?.let {
            val rvApiEvent = findViewById<RecyclerView>(R.id.rvContent)
            val layoutManager = LinearLayoutManager(context)
            val adapter = ApiAdapter(context)
            adapter.onRecyclerViewItemClick = this
            rvApiEvent?.setLayoutManager(layoutManager)
            rvApiEvent?.setAdapter(adapter)
            adapter.refreshData(it)

            findViewById<ImageView>(R.id.imgClearList)?.setOnClickListener {
                adapter.clearData()
            }
        }
    }

    override fun onItemClick(position: Int, item: ApiLifecycleEvent?) {
        replaceToDetailView(position)
    }

    private fun initLoggingView() {
        findViewById<ImageView>(R.id.btnCancel)?.setOnClickListener {
            BitViewManager.hideLogApiView()
            BitViewManager.showBitTouchView()
        }
        findViewById<ImageView>(R.id.btnReadRealm)?.setOnClickListener {
            BitViewManager.hideLogApiView()
            BitViewManager.showDatabaseView()
        }
        findViewById<TextView>(R.id.tvVersionApp)?.text = context.getString(R.string.version_app_describe) + BitViewManager.appInfoData?.versionName
        addDataForListApi()
    }

    private fun initDetailView(position: Int) {
        findViewById<ImageView>(R.id.btnBack)?.setOnClickListener {
            replaceToMainView()
        }
        try {
            findViewById<JsonRecyclerView>(R.id.rvJson)?.visibility = View.VISIBLE
            findViewById<TextView>(R.id.tvHtml)?.visibility = View.GONE
            findViewById<JsonRecyclerView>(R.id.rvJson)?.bindJson(listApiEvent?.get(position)?.contentResponse)
        } catch (e: Exception) {
            findViewById<JsonRecyclerView>(R.id.rvJson)?.visibility = View.GONE
            findViewById<TextView>(R.id.tvHtml)?.visibility = View.VISIBLE
            findViewById<TextView>(R.id.tvHtml)?.text = listApiEvent?.get(position)?.contentResponse
        }
    }

    private fun initLayoutParams() {
        rlLoggingView = LayoutInflater.from(mContext).inflate(R.layout.logging_layout, null)
        layoutParam = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParam?.addRule(CENTER_IN_PARENT)
        this.addView(rlLoggingView, layoutParam)
    }

    private fun replaceToDetailView(position: Int) {
        this.removeView(rlLoggingView)
        rlLoggingView = LayoutInflater.from(mContext).inflate(R.layout.detail_loggin_layout, null)
        this.addView(rlLoggingView, layoutParam)
        initDetailView(position)
    }

    private fun replaceToMainView() {
        this.removeView(rlLoggingView)
        rlLoggingView = LayoutInflater.from(mContext).inflate(R.layout.logging_layout, null)
        this.addView(rlLoggingView, layoutParam)
        initLoggingView()
    }


    @Subscribe
    public fun updateBitData(apiEvent: ApiLifecycleEvent) {
        listApiEvent?.add(0, apiEvent)
    }

    companion object {
        private val TAG = "MutiTaskView"
        val GLOBAL_ACTION_BACK = "BACK"
        val GLOBAL_ACTION_RECENTS = "RECENTS"
    }
}
