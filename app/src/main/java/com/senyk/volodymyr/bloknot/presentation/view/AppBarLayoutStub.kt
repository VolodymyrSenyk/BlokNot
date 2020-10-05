package com.senyk.volodymyr.bloknot.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.google.android.material.appbar.AppBarLayout
import com.senyk.volodymyr.bloknot.presentation.view.util.extensions.inflateView

class AppBarLayoutStub(context: Context, attr: AttributeSet? = null) : AppBarLayout(context, attr) {

    @LayoutRes
    var layoutResource: Int = 0

    fun inflate(): AppBarLayoutStub {
        val parentView = when {
            parent == null -> throw NullPointerException("Parent view can't be null")
            parent !is ViewGroup -> throw IllegalStateException("Parent view should be child of ViewGroup")
            layoutResource == 0 -> throw IllegalStateException("Invalid layout resource id")
            else -> parent as ViewGroup
        }

        val appBarLayoutStub = parentView.inflateView(layoutResource) as AppBarLayoutStub
        appBarLayoutStub.layoutResource = layoutResource
        replaceStub(parentView, appBarLayoutStub)
        return appBarLayoutStub
    }

    private fun replaceStub(parentView: ViewGroup, appBarLayoutStub: AppBarLayoutStub) {
        val stubIndex = parentView.indexOfChild(this)
        parentView.removeViewInLayout(this)
        parentView.addView(appBarLayoutStub, stubIndex)
    }
}
