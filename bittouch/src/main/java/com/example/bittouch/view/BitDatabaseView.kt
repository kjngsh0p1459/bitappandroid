package com.example.bittouch.view

import android.content.Context
import android.graphics.PixelFormat
import android.util.AttributeSet
import android.view.*
import android.widget.*
import com.example.bittouch.R
import com.example.bittouch.manager.BitViewManager
import io.realm.Realm

class BitDatabaseView(private val mContext: Context, attrs: AttributeSet?) : RelativeLayout(mContext, attrs) {

    private var mLayoutParams: WindowManager.LayoutParams? = null
    private var rlDatabaseView: View? = null
    private var layoutParam: LayoutParams? = null

    init {
        initLayoutParams()
        initDatabaseView()
    }

    fun showNameTableRealm() {
        val lvMainData = findViewById<ListView>(R.id.lvMainData)
        BitViewManager.arrRealmObject?.first()?.canonicalName
        val listArrRealm = ArrayList<String>()
        BitViewManager.arrRealmObject?.forEach {
            listArrRealm.add(it.canonicalName.split(".").last())
        }
        lvMainData?.adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, listArrRealm)

        lvMainData?.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    BitViewManager.arrRealmObject?.get(position)?.let {
                        BitViewManager.hideDatabaseView()
                        BitViewManager.showTableView(it)
                    }
            }
        }
    }

    private fun initDatabaseView() {
        findViewById<ImageView>(R.id.btnCancel)?.setOnClickListener {
            BitViewManager.hideDatabaseView()
            BitViewManager.showBitTouchView()
        }
        findViewById<TextView>(R.id.tvVersionApp)?.text = context.getString(R.string.version_app_describe) + BitViewManager.appInfoData?.versionName
    }


    private fun initLayoutParams() {
        rlDatabaseView = LayoutInflater.from(mContext).inflate(R.layout.database_main, null)
        layoutParam = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParam!!.addRule(CENTER_IN_PARENT)
        this.addView(rlDatabaseView, layoutParam)
    }
}
