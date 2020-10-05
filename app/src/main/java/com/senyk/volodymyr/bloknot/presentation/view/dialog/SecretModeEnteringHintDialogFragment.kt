package com.senyk.volodymyr.bloknot.presentation.view.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.senyk.volodymyr.bloknot.R

class SecretModeEnteringHintDialogFragment : DialogFragment() {

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext(), R.style.AlertDialog)
            .setTitle(R.string.dialog_secret_mode_entering_hint_title)
            .setMessage(
                getString(
                    R.string.dialog_secret_mode_entering_hint_message,
                    getString(R.string.name_pattern, getString(R.string.app_name))
                )
            )
            .setNeutralButton(android.R.string.ok) { _, _ -> }
            .create()

    companion object {
        fun newInstance(): SecretModeEnteringHintDialogFragment =
            SecretModeEnteringHintDialogFragment()
    }
}
