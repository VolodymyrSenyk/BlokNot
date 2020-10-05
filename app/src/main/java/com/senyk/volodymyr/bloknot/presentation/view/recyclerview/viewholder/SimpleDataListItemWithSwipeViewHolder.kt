package com.senyk.volodymyr.bloknot.presentation.view.recyclerview.viewholder

import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.senyk.volodymyr.bloknot.R
import com.senyk.volodymyr.bloknot.presentation.view.recyclerview.SimpleDataListItem
import com.senyk.volodymyr.bloknot.presentation.view.recyclerview.viewholder.base.BaseViewHolder
import kotlinx.android.synthetic.main.list_item_simple_data_item.listItemText
import kotlinx.android.synthetic.main.list_item_simple_data_item.listItemTitle
import kotlinx.android.synthetic.main.list_item_with_swipe.*

class SimpleDataListItemWithSwipeViewHolder(
    parent: ViewGroup,
    private val itemClickListener: ((item: SimpleDataListItem) -> Unit)? = null
) : BaseViewHolder<SimpleDataListItem>(parent, R.layout.list_item_with_swipe) {

    override fun bind(item: SimpleDataListItem) {
        super.bind(item)
        handleVisibility(item)
        listItemTitle.text = item.title
        listItemText.text =
            item.data.joinToString(prefix = "", separator = "\n", postfix = "")
        itemClickListener?.let { listener ->
            listItem.setOnClickListener { listener(item) }
        }
    }

    private fun handleVisibility(item: SimpleDataListItem) {
        if (item.title.isEmpty()) {
            listItemTitle.isGone = true
        } else {
            listItemTitle.isVisible = true
        }
        if (item.data.isEmpty()) {
            listItemText.isGone = true
        } else {
            listItemText.isVisible = true
        }
    }
}
