package com.senyk.volodymyr.bloknot.presentation.view.recyclerview.diffutil

import androidx.recyclerview.widget.DiffUtil

class DefaultDiffUtilItemCallback<T>(
    val overrideAreItemsTheSame: (T, T) -> Boolean,
    val overrideAreContentsTheSame: (T, T) -> Boolean
) : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        overrideAreItemsTheSame(oldItem, newItem)

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        overrideAreContentsTheSame(oldItem, newItem)
}
