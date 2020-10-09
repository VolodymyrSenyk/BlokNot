package com.senyk.volodymyr.bloknot.presentation.view.util

import android.view.View

abstract class DoubleViewClickListener : View.OnClickListener {

    private var lastClickTime = 0L

    override fun onClick(view: View) {
        val clickTime = System.currentTimeMillis()
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA_IN_MS) {
            onDoubleClick(view)
        } else {
            onSingleClick(view)
        }
        lastClickTime = clickTime
    }

    open fun onSingleClick(view: View) {}
    open fun onDoubleClick(view: View) {}

    companion object {
        private const val DOUBLE_CLICK_TIME_DELTA_IN_MS = 600
    }
}
