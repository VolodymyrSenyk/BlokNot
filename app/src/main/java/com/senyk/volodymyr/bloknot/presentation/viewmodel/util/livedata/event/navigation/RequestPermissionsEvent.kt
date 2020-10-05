package com.senyk.volodymyr.bloknot.presentation.viewmodel.util.livedata.event.navigation

data class RequestPermissionsEvent(
    val permissions: List<String>,
    val requestCode: Int
) : NavigationEvent
