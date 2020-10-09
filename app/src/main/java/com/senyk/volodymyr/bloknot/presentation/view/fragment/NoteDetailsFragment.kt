package com.senyk.volodymyr.bloknot.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.senyk.volodymyr.bloknot.R
import com.senyk.volodymyr.bloknot.databinding.FragmentNoteDetailsBinding
import com.senyk.volodymyr.bloknot.presentation.view.dialog.DeleteNoteDialogFragment
import com.senyk.volodymyr.bloknot.presentation.view.fragment.base.BaseCryptoNotepadFragment
import com.senyk.volodymyr.bloknot.presentation.viewmodel.model.NoteDetailsViewModel
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.AttrValuesProvisionUtil

class NoteDetailsFragment : BaseCryptoNotepadFragment(),
    DeleteNoteDialogFragment.OnButtonClickListener {

    override val layoutRes = R.layout.fragment_note_details
    override val toolbarRes = R.layout.toolbar_default
    override val menuRes = R.menu.menu_note_details
    override val viewModel by viewModels<NoteDetailsViewModel>(factoryProducer = { viewModelFactory })

    private val args: NoteDetailsFragmentArgs by navArgs()
    private lateinit var binding: FragmentNoteDetailsBinding

    override fun onResume() {
        super.onResume()
        getToolbar()?.setNavigationIcon(
            AttrValuesProvisionUtil.getThemeDrawable(
                requireActivity().theme,
                R.attr.iconBack
            )
        )
    }

    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        viewModel.note.observe(viewLifecycleOwner, { note ->
            getToolbar()?.title = note.name
        })

        viewModel.onStart(noteId = args.noteId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.menuActionEditNote -> {
                viewModel.onEditNoteClick()
                true
            }

            R.id.menuActionDeleteNote -> {
                viewModel.onDeleteNoteClick()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    override fun onConfirmNoteDeleteClickListener(noteId: String) {
        viewModel.onConfirmNoteDeleteClick(noteId = noteId)
    }
}
