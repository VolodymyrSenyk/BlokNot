package com.senyk.volodymyr.bloknot.presentation.view.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.senyk.volodymyr.bloknot.R

class SaveEmptyNoteDialogFragment : DialogFragment() {

    private var onButtonClickListener: OnButtonClickListener? = null
    private lateinit var noteId: String
    private lateinit var noteName: String

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
            .setTitle(R.string.dialog_save_empty_note_title)
            .setMessage(
                getString(
                    R.string.dialog_save_empty_note_message,
                    if (noteName.isEmpty()) "" else getString(R.string.name_pattern, noteName)
                )
            )
            .setNegativeButton(R.string.dialog_answer_cancel) { _, _ -> }
            .setPositiveButton(R.string.dialog_answer_continue) { _, _ ->
                onButtonClickListener?.onConfirmSaveEmptyNoteClickListener(noteId = noteId)
            }
            .create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            noteId = args.get(BK_NOTE_ID).toString()
            noteName = args.get(BK_NOTE_NAME).toString()
        }
    }

    override fun onDetach() {
        super.onDetach()
        onButtonClickListener = null
    }

    interface OnButtonClickListener {
        fun onConfirmSaveEmptyNoteClickListener(noteId: String)
    }

    companion object {
        private const val BK_NOTE_ID = "BK_NOTE_ID"
        private const val BK_NOTE_NAME = "BK_NOTE_NAME"

        fun newInstance(noteId: String, noteName: String): SaveEmptyNoteDialogFragment =
            SaveEmptyNoteDialogFragment().apply {
                arguments = bundleOf(
                    BK_NOTE_ID to noteId,
                    BK_NOTE_NAME to noteName
                )
            }
    }
}
