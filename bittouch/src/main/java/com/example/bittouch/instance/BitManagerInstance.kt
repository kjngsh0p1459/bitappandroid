package com.example.bittouch.instance

import android.content.Context
import android.view.WindowManager

object BitManagerInstance {
    private var windowManager: WindowManager? = null
    private var applicationContext: Context? = null

    fun newInstance(): WindowManager? {
        if (windowManager == null) {
            windowManager = applicationContext?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        }
        return windowManager
    }

    fun setApplicationContext(context: Context?) {
        applicationContext = context
    }
}
