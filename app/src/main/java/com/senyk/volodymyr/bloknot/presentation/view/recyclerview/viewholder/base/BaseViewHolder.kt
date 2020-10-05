package com.senyk.volodymyr.bloknot.presentation.view.recyclerview.viewholder.base

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.senyk.volodymyr.bloknot.presentation.view.util.extensions.inflate
import kotlinx.android.extensions.LayoutContainer

open class BaseViewHolder<T : Any>(
    parent: ViewGroup,
    @LayoutRes layoutRes: Int,
    override val containerView: View = parent.inflate(layoutRes)
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    protected val context: Context get() = itemView.context

    protected val resources: Resources get() = context.resources

    lateinit var item: T
        private set

    @CallSuper
    open fun bind(item: T) {
        this.item = item
    }
}
