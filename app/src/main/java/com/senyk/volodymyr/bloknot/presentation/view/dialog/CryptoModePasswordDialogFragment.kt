package com.senyk.volodymyr.bloknot.presentation.view.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.senyk.volodymyr.bloknot.R

class CryptoModePasswordDialogFragment : DialogFragment() {

    private lateinit var password: String

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext(), R.style.AlertDialog)
            .setTitle(R.string.dialog_crypto_mode_password_title)
            .setMessage(getString(R.string.dialog_crypto_mode_password_message, password))
            .setNeutralButton(android.R.string.ok) { _, _ -> }
            .create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            password = args.get(BK_PASSWORD).toString()
        }
    }

    companion object {
        private const val BK_PASSWORD = "BK_PASSWORD"

        fun newInstance(password: String): CryptoModePasswordDialogFragment =
            CryptoModePasswordDialogFragment().apply {
                arguments = bundleOf(BK_PASSWORD to password)
            }
    }
}
