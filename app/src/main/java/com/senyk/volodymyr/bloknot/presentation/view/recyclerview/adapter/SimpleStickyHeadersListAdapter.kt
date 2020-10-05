package com.senyk.volodymyr.bloknot.presentation.view.recyclerview.adapter

import android.view.View
import android.widget.TextView
import com.senyk.volodymyr.bloknot.R
import com.senyk.volodymyr.bloknot.presentation.view.recyclerview.SimpleDataListItem
import com.senyk.volodymyr.bloknot.presentation.view.recyclerview.SimpleEmptyState
import com.senyk.volodymyr.bloknot.presentation.view.recyclerview.SimpleHeaderListItem
import com.senyk.volodymyr.bloknot.presentation.view.recyclerview.itemdecoration.StickyHeaderItemDecoration

open class SimpleStickyHeadersListAdapter(
    emptyStateClickListener: ((item: SimpleEmptyState) -> Unit)? = null,
    private val headerItemClickListener: ((item: SimpleHeaderListItem) -> Unit)? = null,
    dataItemClickListener: ((item: SimpleDataListItem) -> Unit)? = null,
    dataActionClickListener: ((item: SimpleDataListItem) -> Unit)? = null
) : SimpleListAdapter(
    emptyStateClickListener = emptyStateClickListener,
    headerItemClickListener = headerItemClickListener,
    dataItemClickListener = dataItemClickListener,
    dataActionClickListener = dataActionClickListener
), StickyHeaderItemDecoration.StickyHeaderInterface {

    override fun getHeaderPositionForItem(itemPosition: Int): Int {
        var currentPosition = itemPosition
        do {
            if (isHeader(currentPosition)) {
                return currentPosition
            }
            currentPosition -= 1
        } while (currentPosition >= 0)
        return 0
    }

    override fun getHeaderLayout(headerPosition: Int): Int =
        R.layout.list_item_simple_header

    override fun bindHeaderData(header: View?, headerPosition: Int) {
        val item = getItem(headerPosition) as SimpleHeaderListItem
        header?.let { view ->
            view.findViewById<TextView>(R.id.listHeader).text = item.text
            headerItemClickListener?.let { listener ->
                view.findViewById<TextView>(R.id.listItemActionButton)
                    .setOnClickListener { listener(item) }
            }
        }
    }

    override fun isHeader(itemPosition: Int): Boolean =
        getItem(itemPosition) is SimpleHeaderListItem
}
