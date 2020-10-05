package com.senyk.volodymyr.bloknot.presentation.view.recyclerview

sealed class ListItem

data class SimpleEmptyState(val text: String) : ListItem()

data class SimpleHeaderListItem(val text: String) : ListItem()

data class SimpleDataListItem(val id: String, val title: String, val data: List<String>) :
    ListItem()
