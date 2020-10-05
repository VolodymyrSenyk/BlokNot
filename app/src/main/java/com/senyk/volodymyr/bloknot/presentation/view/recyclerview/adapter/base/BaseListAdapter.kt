package com.senyk.volodymyr.bloknot.presentation.view.recyclerview.adapter.base

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.senyk.volodymyr.bloknot.presentation.view.recyclerview.diffutil.DefaultDiffUtilItemCallback
import com.senyk.volodymyr.bloknot.presentation.view.recyclerview.viewholder.base.BaseViewHolder

abstract class BaseListAdapter<T : Any>(
    areItemsTheSame: (T, T) -> Boolean = { oldListItem, newListItem ->
        oldListItem === newListItem
    },
    areContentsTheSame: (T, T) -> Boolean = { oldListItem, newListItem ->
        oldListItem == newListItem
    },
    callback: DiffUtil.ItemCallback<T> = DefaultDiffUtilItemCallback(
        areItemsTheSame,
        areContentsTheSame
    )
) : ListAdapter<T, BaseViewHolder<T>>(callback) {

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) =
        holder.bind(getItem(position))
}
