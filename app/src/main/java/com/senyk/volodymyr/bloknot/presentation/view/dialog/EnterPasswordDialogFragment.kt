package com.senyk.volodymyr.bloknot.presentation.view.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.senyk.volodymyr.bloknot.R
import kotlinx.android.synthetic.main.fragment_enter_password.*

class EnterPasswordDialogFragment : DialogFragment() {

    private var onButtonClickListener: OnButtonClickListener? = null

    private lateinit var passwordField: TextInputEditText

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onButtonClickListener = if (targetFragment != null) {
            targetFragment as OnButtonClickListener
        } else {
            activity as OnButtonClickListener
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        return AlertDialog.Builder(requireContext(), R.style.AlertDialog)
            .setView(inflater.inflate(R.layout.fragment_enter_password, null))
            .setNegativeButton(R.string.dialog_answer_cancel) { _, _ -> }
            .setPositiveButton(R.string.dialog_answer_continue) { _, _ ->
                onButtonClickListener?.onConfirmPasswordClickListener(passwordField.text.toString())
            }
            .create()
    }

    override fun onStart() {
        super.onStart()
        dialog?.let { thisDialog ->
            passwordField = thisDialog.backupPasswordInputField
        }
    }

    override fun onDetach() {
        super.onDetach()
        onButtonClickListener = null
    }

    interface OnButtonClickListener {
        fun onConfirmPasswordClickListener(password: String)
    }

    companion object {
        fun newInstance(): EnterPasswordDialogFragment = EnterPasswordDialogFragment()
    }
}
