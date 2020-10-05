package com.senyk.volodymyr.bloknot.presentation.view.recyclerview.viewholder

import android.view.ViewGroup
import com.senyk.volodymyr.bloknot.R
import com.senyk.volodymyr.bloknot.presentation.view.recyclerview.SimpleHeaderListItem
import com.senyk.volodymyr.bloknot.presentation.view.recyclerview.viewholder.base.BaseViewHolder
import kotlinx.android.synthetic.main.list_item_simple_header.*

class SimpleHeaderListItemViewHolder(
    parent: ViewGroup,
    private val itemClickListener: ((item: SimpleHeaderListItem) -> Unit)? = null
) : BaseViewHolder<SimpleHeaderListItem>(parent, R.layout.list_item_simple_header) {

    override fun bind(item: SimpleHeaderListItem) {
        super.bind(item)
        listHeader.text = item.text
    }
}
