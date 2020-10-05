package com.senyk.volodymyr.bloknot.presentation.viewmodel.util.livedata.event.navigation

import android.content.Intent

data class StartActivityEvent(
    val intent: Intent,
    val finishCurrentActivity: Boolean = false
) : NavigationEvent
