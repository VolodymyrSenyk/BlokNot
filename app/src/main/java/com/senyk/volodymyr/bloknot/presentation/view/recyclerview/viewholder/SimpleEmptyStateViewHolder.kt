package com.senyk.volodymyr.bloknot.presentation.view.recyclerview.viewholder

import android.view.ViewGroup
import com.senyk.volodymyr.bloknot.R
import com.senyk.volodymyr.bloknot.presentation.view.recyclerview.SimpleEmptyState
import com.senyk.volodymyr.bloknot.presentation.view.recyclerview.viewholder.base.BaseViewHolder
import kotlinx.android.synthetic.main.list_item_simple_data_item.*

class SimpleEmptyStateViewHolder(
    parent: ViewGroup,
    private val itemClickListener: ((item: SimpleEmptyState) -> Unit)? = null
) : BaseViewHolder<SimpleEmptyState>(parent, R.layout.list_item_empty_state) {

    override fun bind(item: SimpleEmptyState) {
        super.bind(item)
        listItemText.text = item.text
    }
}
