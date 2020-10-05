package com.senyk.volodymyr.bloknot.presentation.view.fragment

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.senyk.volodymyr.bloknot.R
import com.senyk.volodymyr.bloknot.databinding.FragmentEditCryptoNoteBinding
import com.senyk.volodymyr.bloknot.presentation.view.dialog.DeleteNoteDialogFragment
import com.senyk.volodymyr.bloknot.presentation.view.dialog.ExitWithoutSaveDialogFragment
import com.senyk.volodymyr.bloknot.presentation.view.dialog.SaveEmptyNoteDialogFragment
import com.senyk.volodymyr.bloknot.presentation.view.fragment.base.BaseCryptoNotepadCryptoFragment
import com.senyk.volodymyr.bloknot.presentation.viewmodel.model.EditCryptoNoteViewModel
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.AttrValuesProvisionUtil
import kotlinx.android.synthetic.main.fragment_edit_crypto_note.*

class EditCryptoNoteFragment : BaseCryptoNotepadCryptoFragment(),
    ExitWithoutSaveDialogFragment.OnButtonClickListener,
    DeleteNoteDialogFragment.OnButtonClickListener,
    SaveEmptyNoteDialogFragment.OnButtonClickListener {

    override val layoutRes = R.layout.fragment_edit_crypto_note
    override val toolbarRes = R.layout.toolbar_edit_note
    override val menuRes = R.menu.menu_edit_note
    override val viewModel by viewModels<EditCryptoNoteViewModel>(factoryProducer = { viewModelFactory })

    private val args: EditCryptoNoteFragmentArgs by navArgs()
    private lateinit var binding: FragmentEditCryptoNoteBinding

    override fun onResume() {
        super.onResume()
        getToolbar()?.setNavigationIcon(
            AttrValuesProvisionUtil.getThemeDrawable(
                requireActivity().theme,
                R.attr.iconCancel
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
        noteContentInputField.requestFocus()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.onBackPressed(
                    noteName = getToolbarViewById<EditText>(R.id.toolbarEditText)?.text.toString(),
                    noteContent = noteContentInputField.text.toString()
                )
            }
        })

        viewModel.note.observe(viewLifecycleOwner, { note ->
            getToolbarViewById<EditText>(R.id.toolbarEditText)?.setText(note.name)
        })

        viewModel.onStart(noteId = args.noteId)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuRes = if (args.noteId == null) {
            R.menu.menu_create_note
        } else {
            R.menu.menu_edit_note
        }
        inflater.inflate(menuRes, menu)
        val changeThemeMenuItem = menu.findItem(R.id.menuActionChangeDayNightTheme) ?: return
        dayNightModeViewModel.nightModeEnabled.observe(viewLifecycleOwner, { inNightMode ->
            val iconAttrRes = if (inNightMode) R.attr.iconDay else R.attr.iconNight
            val currentAppTheme = requireActivity().theme
            val icon = AttrValuesProvisionUtil.getThemeDrawable(currentAppTheme, iconAttrRes)
            changeThemeMenuItem.setIcon(icon)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.menuActionSaveNote -> {
                viewModel.onSaveNoteClick(
                    noteName = getToolbarViewById<EditText>(R.id.toolbarEditText)?.text.toString(),
                    noteContent = noteContentInputField.text.toString()
                ); true
            }

            R.id.menuActionDeleteNote -> {
                viewModel.onDeleteNoteClick(); true
            }

            else -> super.onOptionsItemSelected(item)
        }

    override fun onConfirmExitWithoutSaveClickListener() {
        viewModel.onConfirmExitWithoutSaveClick()
    }

    override fun onConfirmNoteDeleteClickListener(noteId: String) {
        viewModel.onConfirmNoteDeleteClick(noteId = noteId)
    }

    override fun onConfirmSaveEmptyNoteClickListener(noteId: String) {
        viewModel.onConfirmSaveEmptyNoteClick(noteId = noteId)
    }
}
