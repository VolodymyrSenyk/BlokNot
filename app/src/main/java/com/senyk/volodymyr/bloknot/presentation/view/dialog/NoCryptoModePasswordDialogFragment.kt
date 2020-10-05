package com.senyk.volodymyr.bloknot.presentation.view.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.senyk.volodymyr.bloknot.R

class NoCryptoModePasswordDialogFragment : DialogFragment() {

    private var onButtonClickListener: OnButtonClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onButtonClickListener = if (targetFragment != null) {
            targetFragment as OnButtonClickListener
        } else {
            activity as OnButtonClickListener
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext(), R.style.AlertDialog)
            .setTitle(R.string.dialog_no_crypto_mode_password_title)
            .setMessage(R.string.dialog_no_crypto_mode_password_message)
            .setNegativeButton(R.string.dialog_answer_cancel) { _, _ -> }
            .setPositiveButton(R.string.dialog_answer_continue) { _, _ ->
                onButtonClickListener?.onConfirmEmptyCryptoModePasswordClickListener()
            }
            .create()

    override fun onDetach() {
        super.onDetach()
        onButtonClickListener = null
    }

    interface OnButtonClickListener {
        fun onConfirmEmptyCryptoModePasswordClickListener()
    }

    companion object {
        fun newInstance(): NoCryptoModePasswordDialogFragment = NoCryptoModePasswordDialogFragment()
    }
}
