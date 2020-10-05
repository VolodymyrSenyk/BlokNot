package com.senyk.volodymyr.bloknot.presentation.viewmodel.model

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import com.senyk.volodymyr.bloknot.domain.interactor.GetRequiredPermissionsInteractor
import com.senyk.volodymyr.bloknot.presentation.viewmodel.base.BaseRxViewModel
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.livedata.HandledEventLiveData
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.livedata.event.HandledEvent
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.livedata.event.navigation.RequestPermissionsEvent
import javax.inject.Inject

open class PermissionsViewModel @Inject constructor(
    private val getRequiredPermissionsInteractor: GetRequiredPermissionsInteractor
) : BaseRxViewModel() {

    private val _allPermissionsGranted = HandledEventLiveData<Boolean>()
    val allPermissionsGranted: LiveData<HandledEvent<Boolean>>
        get() = _allPermissionsGranted

    private val _allNotGrantedPermissions = mutableListOf<String>()

    open fun onStart(context: Context) {
        loadNotGrantedPermissions(context)
    }

    fun onRequestPermissions() {
        if (allPermissionsGranted.value?.eventData != true) {
            val event = RequestPermissionsEvent(_allNotGrantedPermissions, RC_PERMISSIONS)
            _navigationEvent.setHandledValue(event)
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, context: Context) {
        when (requestCode) {
            RC_PERMISSIONS -> {
                loadNotGrantedPermissions(context)
            }
        }
    }

    private fun loadNotGrantedPermissions(context: Context) {
        subscribe(
            upstream = getRequiredPermissionsInteractor(),
            onSuccess = { requiredPermissions ->
                val notGrantedPermissions = mutableListOf<String>()
                requiredPermissions.forEach { permission ->
                    val checkResult = ContextCompat.checkSelfPermission(context, permission)
                    if (checkResult != PackageManager.PERMISSION_GRANTED) {
                        notGrantedPermissions.add(permission)
                    }
                }
                _allNotGrantedPermissions.apply {
                    clear()
                    addAll(notGrantedPermissions)
                }
                _allPermissionsGranted.setHandledValue(_allNotGrantedPermissions.isEmpty())
            }
        )
    }

    companion object {
        private const val RC_PERMISSIONS = 1
    }
}
