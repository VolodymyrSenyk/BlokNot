package com.senyk.volodymyr.bloknot.presentation.viewmodel.util.livedata.event.navigation

import android.content.Intent

data class StartServiceEvent(
    val intent: Intent
) : NavigationEvent
