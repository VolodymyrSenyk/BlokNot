package com.senyk.volodymyr.bloknot.presentation.view.recyclerview.adapter

import android.view.ViewGroup
import com.senyk.volodymyr.bloknot.R
import com.senyk.volodymyr.bloknot.presentation.view.recyclerview.ListItem
import com.senyk.volodymyr.bloknot.presentation.view.recyclerview.SimpleDataListItem
import com.senyk.volodymyr.bloknot.presentation.view.recyclerview.SimpleEmptyState
import com.senyk.volodymyr.bloknot.presentation.view.recyclerview.SimpleHeaderListItem
import com.senyk.volodymyr.bloknot.presentation.view.recyclerview.adapter.base.BaseListAdapter
import com.senyk.volodymyr.bloknot.presentation.view.recyclerview.viewholder.SimpleDataListItemViewHolder
import com.senyk.volodymyr.bloknot.presentation.view.recyclerview.viewholder.SimpleEmptyStateViewHolder
import com.senyk.volodymyr.bloknot.presentation.view.recyclerview.viewholder.SimpleHeaderListItemViewHolder
import com.senyk.volodymyr.bloknot.presentation.view.recyclerview.viewholder.base.BaseViewHolder

open class SimpleListAdapter(
    private val emptyStateClickListener: ((item: SimpleEmptyState) -> Unit)? = null,
    private val headerItemClickListener: ((item: SimpleHeaderListItem) -> Unit)? = null,
    private val dataItemClickListener: ((item: SimpleDataListItem) -> Unit)? = null,
    private val dataActionClickListener: ((item: SimpleDataListItem) -> Unit)? = null
) : BaseListAdapter<ListItem>(areItemsTheSame = { oldListItem, newListItem ->
    if (oldListItem is SimpleEmptyState && newListItem is SimpleEmptyState) {
        true
    } else if (oldListItem is SimpleHeaderListItem && newListItem is SimpleHeaderListItem) {
        oldListItem.text == newListItem.text
    } else if (oldListItem is SimpleDataListItem && newListItem is SimpleDataListItem) {
        oldListItem.id == newListItem.id
    } else false
}, areContentsTheSame = { oldListItem, newListItem ->
    if (oldListItem is SimpleEmptyState && newListItem is SimpleEmptyState) {
        true
    } else if (oldListItem is SimpleHeaderListItem && newListItem is SimpleHeaderListItem) {
        oldListItem.text == newListItem.text
    } else if (oldListItem is SimpleDataListItem && newListItem is SimpleDataListItem) {
        oldListItem.data == newListItem.data
    } else false
}) {

    fun getDataItem(position: Int): SimpleDataListItem? {
        val listItem = getItem(position)
        return if (listItem is SimpleDataListItem) listItem else null
    }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is SimpleEmptyState -> R.layout.list_item_empty_state
            is SimpleHeaderListItem -> R.layout.list_item_simple_header
            is SimpleDataListItem -> R.layout.list_item_simple_data_item
            else -> super.getItemViewType(position)
        }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ListItem> =
        when (viewType) {
            R.layout.list_item_empty_state -> {
                SimpleEmptyStateViewHolder(
                    parent = parent,
                    itemClickListener = emptyStateClickListener
                ) as BaseViewHolder<ListItem>
            }

            R.layout.list_item_simple_header -> {
                SimpleHeaderListItemViewHolder(
                    parent = parent,
                    itemClickListener = headerItemClickListener
                ) as BaseViewHolder<ListItem>
            }

            R.layout.list_item_simple_data_item -> {
                SimpleDataListItemViewHolder(
                    parent = parent,
                    itemClickListener = dataItemClickListener,
                    actionClickListener = dataActionClickListener
                ) as BaseViewHolder<ListItem>
            }

            else -> throw IllegalArgumentException("No ViewHolder for this type")
        }
}
