package com.example.bittouch.manager

import android.content.Context
import android.content.pm.PackageInfo
import android.graphics.PixelFormat
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import com.example.bittouch.instance.BitManagerInstance
import com.example.bittouch.view.BitDatabaseView
import com.example.bittouch.view.BitLogApiView
import com.example.bittouch.view.BitTableView
import com.example.bittouch.view.BitTouchView
import io.realm.RealmObject

class BitViewManager {

    companion object {
        var appInfoData: PackageInfo? = null
        var arrRealmObject : List<Class<out RealmObject>>? = null
        private val TAG = "MyViewHolder"
        private lateinit var applicationContext: Context
        private var mBitTouchView: BitTouchView? = null
        private var mBitLogApiView: BitLogApiView? = null
        private var mBitDatabaseView: BitDatabaseView? = null
        private var mBitTableView: BitTableView? = null
        private var mWindowManager: WindowManager? = null

        var mLayoutParams: WindowManager.LayoutParams? = null
        var isServiceRunning = false

        fun initData(context: Context) {
            applicationContext = context.applicationContext
            mBitTouchView = BitTouchView(applicationContext, null)
            mBitLogApiView = BitLogApiView(applicationContext, null)
            mBitDatabaseView = BitDatabaseView(applicationContext, null)
            mBitTableView = BitTableView(applicationContext, null)
            mLayoutParams = WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    0,
                    0,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                    } else {
                        WindowManager.LayoutParams.TYPE_PHONE;
                    },
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSPARENT
            )
            mLayoutParams?.gravity = Gravity.CENTER


            BitManagerInstance.setApplicationContext(applicationContext)
            mWindowManager = BitManagerInstance.newInstance()

        }

        fun showBitTouchView() {
            if (!BitTouchView.isAlive) {
                val layoutParams = mBitTouchView?.getmLayoutParams()
                BitTouchView.isAlive = true
                mWindowManager?.addView(mBitTouchView, layoutParams)
                Log.d(TAG, "show")
            }
        }

        fun hideBitTouchView() {
            if (BitTouchView.isAlive) {
                BitTouchView.isAlive = false
                mWindowManager?.removeView(mBitTouchView)
                Log.d(TAG, "hide")
            }
        }

        fun showDatabaseView() {
            val layoutParams = mLayoutParams
            mWindowManager?.addView(mBitDatabaseView, layoutParams)
            mBitDatabaseView?.showNameTableRealm()
        }

        fun hideDatabaseView() {
            mWindowManager?.removeView(mBitDatabaseView)
        }

        fun showTableView(classRealm: Class<out RealmObject>) {
            val layoutParams = mLayoutParams
            mWindowManager?.addView(mBitTableView, layoutParams)
            mBitTableView?.addDefineObjectForTableView(classRealm)
        }

        fun hideTableView() {
            mWindowManager?.removeView(mBitTableView)
        }

        fun showLogApiView() {
            val layoutParams = mLayoutParams
            mWindowManager?.addView(mBitLogApiView, layoutParams)
        }

        fun hideLogApiView() {
            mWindowManager?.removeView(mBitLogApiView)
        }

        fun openLogApiWindow() {
            hideBitTouchView()
            showLogApiView()
        }

        fun setIsServiceRunning(isRunning: Boolean) {
            isServiceRunning = isRunning
        }
    }
}
