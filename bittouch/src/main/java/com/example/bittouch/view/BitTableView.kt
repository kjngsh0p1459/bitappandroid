package com.example.bittouch.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.bittouch.R
import com.example.bittouch.adapter.ContentTableAdapter
import com.example.bittouch.adapter.DetailTableAdapter
import com.example.bittouch.adapter.HeaderScrollableAdapter
import com.example.bittouch.manager.BitViewManager
import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlinx.android.synthetic.main.common_table_layout.view.*
import kotlinx.android.synthetic.main.item_freeze_header_table.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import kotlin.reflect.full.memberProperties

class BitTableView(private val mContext: Context, attrs: AttributeSet?) : RelativeLayout(mContext, attrs) {

    private var rlTableView: View? = null
    private var layoutParam: LayoutParams? = null

    private var headerAdapter: HeaderScrollableAdapter? = null
    private var contentAdapter: ContentTableAdapter? = null
    private var detailTableAdapter: DetailTableAdapter? = null
    private val WIDTH_UNIT = context.resources.getDimension(R.dimen.dimen_120dp).toInt()

    init {
        initLayoutParams()
        initView()
    }

    private fun initView() {
        findViewById<ImageView>(R.id.btnBack)?.setOnClickListener {
            BitViewManager.hideTableView()
            BitViewManager.showDatabaseView()
        }
        findViewById<TextView>(R.id.tvVersionApp)?.text = context.getString(R.string.version_app_describe) + BitViewManager.appInfoData?.versionName

        setUpTable()
        setFreezeHeader(R.layout.item_freeze_header_table)

    }

    private fun setUpTable() {
        this.context?.let {
            rvContent?.run {
                setHasFixedSize(false)
                contentAdapter = ContentTableAdapter(context)
                layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
                adapter = contentAdapter
            }

            rvDetail?.run {
                setHasFixedSize(false)
                detailTableAdapter = DetailTableAdapter(context)
                layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
                adapter = detailTableAdapter
            }

            rvHeader?.run {
                setHasFixedSize(false)
                headerAdapter = HeaderScrollableAdapter(context)
                layoutManager = LinearLayoutManager(it, LinearLayoutManager.HORIZONTAL, false)
                adapter = headerAdapter
            }

            applySyncScroll()
        }
    }

    private fun setFreezeHeader(idLayout: Int) {
        llFreeze?.run { LayoutInflater.from(mContext).inflate(idLayout, this@run) }
    }

    private fun applySyncScroll() {
        clearSyncScroll()
        rvContent?.addSyncScrollItem(rvDetail)
        rvDetail?.addSyncScrollItem(rvContent)
        hsvcHeader?.addSyncScrollItem(chsvMain)
        chsvMain?.addSyncScrollItem(hsvcHeader)
        rvContent?.isEnableScroll = false
    }

    private fun clearSyncScroll() {
        rvContent?.clearSyncScrollItems()
        rvDetail?.clearSyncScrollItems()
        hsvcHeader?.clearSyncScrollItems()
        chsvMain?.clearSyncScrollItems()
    }

    fun addDefineObjectForTableView(classRealm: Class<out RealmObject>) {

        var listOtherFieldName = ArrayList<String>()
        var primaryFieldName = ""
        var widthDetailTable = 0
        var listContentData = ArrayList<String>()
        var listDetailData = ArrayList<List<String>>()

        doAsync {
            val arrField = classRealm.declaredFields
            arrField.forEach { field ->
                val listAnnotation = field.declaredAnnotations
                if (listAnnotation.size != 0 && listAnnotation.filter { annotation -> annotation is PrimaryKey }.isNotEmpty()) {
                    primaryFieldName = field.name
                } else {
                    listOtherFieldName.add(field.name.toString())
                }
            }
            Realm.getDefaultInstance()?.use { realm ->
                val detailData = if (primaryFieldName.isNotEmpty()) {
                    realm.copyToRealm(realm.where(classRealm).findAll().sort(primaryFieldName)) as ArrayList<RealmObject>
                } else {
                    realm.copyToRealm(realm.where(classRealm).findAll())
                }
                if (detailData.isNotEmpty()) {
                    detailData.forEach { realmObject ->
                        if (primaryFieldName.isNotEmpty()) {
                            val contentFreeze = realmObject.javaClass.kotlin.memberProperties.first { it.name == primaryFieldName }.get(realmObject).toString()
                            listContentData.add(contentFreeze)
                        }
                        val listDataBaseOnField = ArrayList<String>()
                        listOtherFieldName.forEach { fieldName ->
                            val content = realmObject.javaClass.kotlin.memberProperties.first { it.name == fieldName }.get(realmObject).toString()
                            listDataBaseOnField.add(content)
                        }
                        listDetailData.add(listDataBaseOnField)
                    }
                }
                widthDetailTable = WIDTH_UNIT.times(listOtherFieldName.size)
            }

            uiThread {
                if (primaryFieldName.isNotEmpty()) {
                    llFreeze?.visibility = View.VISIBLE
                    rvContent?.visibility = View.VISIBLE
                    tvFreezeHeader?.text = primaryFieldName
                    contentAdapter?.refreshData(listContentData)
                    rvHeader?.layoutParams?.width = widthDetailTable.plus(WIDTH_UNIT)
                    rvDetail?.layoutParams?.width = widthDetailTable.plus(WIDTH_UNIT)
                } else {
                    llFreeze?.visibility = View.GONE
                    rvContent?.visibility = View.GONE
                    rvHeader?.layoutParams?.width = widthDetailTable
                    rvDetail?.layoutParams?.width = widthDetailTable
                }
                headerAdapter?.refreshData(listOtherFieldName)
                detailTableAdapter?.refreshData(listDetailData)
            }
        }
    }

    @Throws(IllegalAccessException::class, ClassCastException::class)
    inline fun <reified T> Any.getField(fieldName: String): T? {
        this::class.memberProperties.forEach { kCallable ->
            if (fieldName == kCallable.name) {
                return kCallable.getter.call(this) as T?
            }
        }
        return null
    }

    private fun initLayoutParams() {
        rlTableView = LayoutInflater.from(mContext).inflate(R.layout.common_table_layout, null)
        layoutParam = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        layoutParam?.width = context.resources.getDimension(R.dimen.dimen_400dp).toInt()
        layoutParam?.height = context.resources.getDimension(R.dimen.dimen_600dp).toInt()
        layoutParam?.addRule(CENTER_IN_PARENT)
        this.addView(rlTableView, layoutParam)
    }
}