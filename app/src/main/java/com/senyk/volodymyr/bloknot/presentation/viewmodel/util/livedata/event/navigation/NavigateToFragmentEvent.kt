package com.senyk.volodymyr.bloknot.presentation.viewmodel.util.livedata.event.navigation

import androidx.navigation.NavDirections

data class NavigateToFragmentEvent(val action: NavDirections) : NavigationEvent
