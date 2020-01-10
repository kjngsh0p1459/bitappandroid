package com.example.bittouch.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent

import com.example.bittouch.event.BitMenuEvent
import com.example.bittouch.manager.BitViewManager
import com.example.bittouch.view.BitLogApiView

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class BitService : AccessibilityService() {

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
        EventBus.getDefault().unregister(this)
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        BitViewManager.setIsServiceRunning(true)
        Log.d(TAG, "isServiceRunning")
    }

    override fun onAccessibilityEvent(accessibilityEvent: AccessibilityEvent) {

    }

    override fun onInterrupt() {
        Log.d(TAG, "onInterrupt")
    }

    override fun onUnbind(intent: Intent): Boolean {
        BitViewManager.setIsServiceRunning(false)
        Log.d(TAG, intent.toString())
        return super.onUnbind(intent)
    }

    @Subscribe
    fun onEventMainThread(event: BitMenuEvent) {
        val msg = event.msg
        if (BitLogApiView.GLOBAL_ACTION_BACK == msg) {
            performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
        }
        if (BitLogApiView.GLOBAL_ACTION_RECENTS == msg) {
            performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS)
        }
    }

    companion object {
        private val TAG = "MyService"
    }

}
