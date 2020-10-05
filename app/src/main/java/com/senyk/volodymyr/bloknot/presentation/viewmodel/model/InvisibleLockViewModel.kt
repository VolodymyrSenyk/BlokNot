package com.senyk.volodymyr.bloknot.presentation.viewmodel.model

import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.senyk.volodymyr.bloknot.R
import com.senyk.volodymyr.bloknot.domain.usecase.lock.CheckPasswordCorrectnessUseCase
import com.senyk.volodymyr.bloknot.domain.usecase.lock.CheckPasswordIsAlreadySetUseCase
import com.senyk.volodymyr.bloknot.domain.usecase.lock.SetPasswordUseCase
import com.senyk.volodymyr.bloknot.presentation.view.dialog.CryptoModePasswordDialogFragment
import com.senyk.volodymyr.bloknot.presentation.view.dialog.NoCryptoModePasswordDialogFragment
import com.senyk.volodymyr.bloknot.presentation.view.fragment.InvisibleLockFragmentDirections
import com.senyk.volodymyr.bloknot.presentation.viewmodel.base.BaseRxViewModel
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.ResourcesProvider
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.livedata.event.navigation.NavigateToFragmentEvent
import javax.inject.Inject

class InvisibleLockViewModel @Inject constructor(
    private val setPasswordUseCase: SetPasswordUseCase,
    private val checkPasswordIsAlreadySetUseCase: CheckPasswordIsAlreadySetUseCase,
    private val checkPasswordCorrectnessUseCase: CheckPasswordCorrectnessUseCase,
    private val resourcesProvider: ResourcesProvider
) : BaseRxViewModel() {

    private var enteredPassword = ""

    private val _newPassword = MutableLiveData<String>()
    val newPassword: LiveData<String>
        get() = _newPassword

    private val passwordSetUp = MutableLiveData<Boolean>()
    val isPasswordSetUp: LiveData<Boolean>
        get() = passwordSetUp

    fun onStart() {
        enteredPassword = ""
        subscribe(
            upstream = checkPasswordIsAlreadySetUseCase(),
            onSuccess = { isAlreadySet ->
                passwordSetUp.value = !isAlreadySet
                if (isAlreadySet) checkEmptyPassword()
            }
        )
    }

    private fun checkEmptyPassword() {
        subscribe(
            upstream = checkPasswordCorrectnessUseCase(""),
            onSuccess = { correct ->
                if (correct) {
                    navigateToCryptoNotesListFragment()
                }
            }
        )
    }

    fun onButtonClick(view: View) {
        enteredPassword += (view as TextView).text
        if (isPasswordSetUp.value == true) {
            setCurrentPassword()
            return
        }
        subscribe(
            upstream = checkPasswordCorrectnessUseCase(enteredPassword),
            onSuccess = { correct ->
                if (correct) {
                    navigateToCryptoNotesListFragment()
                }
            }
        )
    }

    fun onDeleteButtonClick() {
        enteredPassword = if (enteredPassword.length > 1) {
            enteredPassword.substring(0, enteredPassword.length - 1)
        } else {
            ""
        }
        setCurrentPassword()
    }

    fun onConfirmButtonClick() {
        if (enteredPassword.isEmpty()) {
            _dialogFragment.setValue(NoCryptoModePasswordDialogFragment.newInstance())
        } else {
            subscribe(
                upstream = setPasswordUseCase(password = enteredPassword),
                onComplete = {
                    _dialogFragment.setValue(CryptoModePasswordDialogFragment.newInstance(password = enteredPassword))
                    navigateToCryptoNotesListFragment()
                }
            )
        }
    }

    fun onConfirmEmptyCryptoModePasswordClick() {
        subscribe(
            upstream = setPasswordUseCase(password = enteredPassword),
            onComplete = {
                navigateToCryptoNotesListFragment()
            }
        )
    }

    private fun navigateToCryptoNotesListFragment() {
        val action =
            InvisibleLockFragmentDirections.actionInvisibleLockFragmentToCryptoNotesListFragment()
        _navigationEvent.setHandledValue(NavigateToFragmentEvent(action))
    }

    private fun setCurrentPassword() {
        _newPassword.value = resourcesProvider.getString(
            R.string.crypto_mode_password_output, enteredPassword
        )
    }
}
