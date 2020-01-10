package com.example.bittouch.extension

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Build
import android.provider.Settings
import com.example.bittouch.R
import com.example.bittouch.event.ApiLifecycleEvent
import com.example.bittouch.manager.BitViewManager
import io.realm.RealmObject
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import org.greenrobot.eventbus.EventBus

fun Activity.startInvestigateLog() {
    BitViewManager.initData(this)
    if (!this.checkLogPermission()) {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle(resources.getString(R.string.hint))
            alertDialog.setMessage(resources.getString(R.string.hint_content_window))
            alertDialog.setCancelable(false)
            alertDialog.setPositiveButton(resources.getString(R.string.ok)) { dialogInterface, i ->
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                startActivityForResult(intent, 0)
            }
            alertDialog.setNegativeButton(resources.getString(R.string.cancel)) { dialogInterface, i ->
            }
            alertDialog.show()
    } else {
        showBitLogView()
    }
}

fun defineRealmObject(appInfoData: PackageInfo, vararg objectRealm: Class<out RealmObject>) {
    BitViewManager.appInfoData = appInfoData
    BitViewManager.arrRealmObject = objectRealm.toList()
}

fun handleInterceptorResponse(chain: Interceptor.Chain, request: Request): Response {
    val response = chain.proceed(request)
    val contentType = response.body()?.contentType()
    val content = response.body()?.string()
    val body = ResponseBody.create(contentType, content)
    sendDataForTouchView(ApiLifecycleEvent(request, response, content))
    return response.newBuilder().body(body).build()
}

fun sendDataForTouchView(bitData: ApiLifecycleEvent) {
    EventBus.getDefault().post(bitData)
}

fun showBitLogView() {
    BitViewManager.showBitTouchView()
}

fun hideBitLogView() {
    BitViewManager.hideBitTouchView()
}

fun Activity.checkLogPermission(): Boolean {
    if (Build.VERSION.SDK_INT >= 23) {
        if (Settings.canDrawOverlays(this)) {
            return true
        }
    } else {
        return true
    }
    return false
}
