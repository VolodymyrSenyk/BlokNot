package com.senyk.volodymyr.bloknot.presentation.view.util

abstract class DoubleClickListener {

    private var lastClickTime = 0L

    open fun onClick() {
        val clickTime = System.currentTimeMillis()
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA_IN_MS) {
            onDoubleClick()
        } else {
            onSingleClick()
        }
        lastClickTime = clickTime
    }

    open fun onSingleClick() {}
    open fun onDoubleClick() {}

    companion object {
        private const val DOUBLE_CLICK_TIME_DELTA_IN_MS = 600
    }
}
