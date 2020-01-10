package com.example.bittouch.view

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PixelFormat
import android.graphics.Rect
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.WindowManager
import com.example.bittouch.R
import com.example.bittouch.event.ApiLifecycleEvent
import com.example.bittouch.instance.BitManagerInstance
import com.example.bittouch.manager.BitViewManager

class BitTouchView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {
    private var mLayoutParams: WindowManager.LayoutParams? = null
    private var bitData: ApiLifecycleEvent? = null

    init {
        this.bitData = bitData
        screenWidth = this.resources.displayMetrics.widthPixels
        screenHeight = this.resources.displayMetrics.heightPixels
        initLayoutParams()
        LayoutInflater.from(context).inflate(R.layout.bittouch_view_layout, this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewWidth = this.width
        viewHeight = this.height
    }

    fun initLayoutParams() {
        mLayoutParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                0,
                0,
                PixelFormat.TRANSPARENT
        )
        mLayoutParams?.type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            WindowManager.LayoutParams.TYPE_PHONE;
        }
        mLayoutParams?.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        mLayoutParams?.gravity = Gravity.TOP or Gravity.LEFT
        mLayoutParams?.x = screenWidth
        mLayoutParams?.y = screenHeight / 2
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                xInView = event.x
                yInView = event.y
                xDownInScreen = event.rawX
                yDownInScreen = event.rawY - getStatusBarHeight()
                xInScreen = event.rawX
                yInScreen = event.rawY - getStatusBarHeight()
            }
            MotionEvent.ACTION_MOVE -> {
                xInScreen = event.rawX
                yInScreen = event.rawY - getStatusBarHeight()
                mLayoutParams!!.x = (xInScreen - xInView).toInt()
                mLayoutParams!!.y = (yInScreen - yInView).toInt()
                updateViewPosition()
            }
            MotionEvent.ACTION_UP -> if (xInScreen == xDownInScreen && yInScreen == yDownInScreen) {
                BitViewManager.openLogApiWindow()
            } else {
                startViewPositionAnimator()
            }
        }
        return super.onTouchEvent(event)
    }

    fun updateViewPosition() {
        try {
            val windowManager = BitManagerInstance.newInstance()
            windowManager?.updateViewLayout(this, mLayoutParams)
        } catch (e: IllegalArgumentException) {
        }
    }

    fun startViewPositionAnimator() {
        val valueAnimator = ObjectAnimator.ofFloat(0f, 1f)
        valueAnimator.addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->
            val value = animation.animatedValue as Float
            if (mLayoutParams!!.x + viewWidth / 2 <= screenWidth / 2) {
                mLayoutParams!!.x = (mLayoutParams!!.x.toFloat() * (1 - value)).toInt()
            } else {
                mLayoutParams!!.x += ((screenWidth - mLayoutParams!!.x).toFloat() * value).toInt()
            }
            updateViewPosition()
        })
        valueAnimator.start()
    }

    fun getStatusBarHeight(): Float {
        val rectangle = Rect()
        this.getWindowVisibleDisplayFrame(rectangle)
        statusBarHeight = rectangle.top
        return statusBarHeight.toFloat()
    }

    fun getmLayoutParams(): WindowManager.LayoutParams? {
        return mLayoutParams
    }

    companion object {
        private var statusBarHeight: Int = 0
        private var screenWidth: Int = 0
        private var screenHeight: Int = 0
        private var viewWidth: Int = 0
        private var viewHeight: Int = 0
        var isAlive = false

        private var xInScreen: Float = 0.toFloat()

        private var yInScreen: Float = 0.toFloat()

        private var xDownInScreen: Float = 0.toFloat()

        private var yDownInScreen: Float = 0.toFloat()

        private var xInView: Float = 0.toFloat()

        private var yInView: Float = 0.toFloat()
    }

}
