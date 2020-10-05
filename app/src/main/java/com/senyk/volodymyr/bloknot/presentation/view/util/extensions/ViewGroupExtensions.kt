package com.senyk.volodymyr.bloknot.presentation.view.util.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View =
    LayoutInflater.from(context).inflate(layoutRes, this, false)
