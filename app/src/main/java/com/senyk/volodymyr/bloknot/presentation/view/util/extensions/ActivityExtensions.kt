package com.senyk.volodymyr.bloknot.presentation.view.util.extensions

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.getRootView(): View =
    (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
