package com.senyk.volodymyr.bloknot.presentation.viewmodel.util

import android.view.View

data class InfoWithAction(
    val text: String,
    val actionName: String,
    val action: View.OnClickListener
)
