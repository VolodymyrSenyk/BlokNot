package com.senyk.volodymyr.bloknot.presentation.view.recyclerview.swipe

import android.view.MotionEvent

interface OnActivityTouchListener {
    fun getTouchCoordinates(ev: MotionEvent)
}
