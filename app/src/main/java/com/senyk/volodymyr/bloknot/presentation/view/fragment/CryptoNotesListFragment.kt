package com.senyk.volodymyr.bloknot.presentation.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.senyk.volodymyr.bloknot.R
import com.senyk.volodymyr.bloknot.presentation.view.dialog.DeleteNoteDialogFragment
import com.senyk.volodymyr.bloknot.presentation.view.dialog.EnterPasswordDialogFragment
import com.senyk.volodymyr.bloknot.presentation.view.dialog.NoBackupPasswordDialogFragment
import com.senyk.volodymyr.bloknot.presentation.view.fragment.base.BaseCryptoNotepadCryptoFragment
import com.senyk.volodymyr.bloknot.presentation.view.recyclerview.adapter.SimpleListWithSwipeAdapter
import com.senyk.volodymyr.bloknot.presentation.view.recyclerview.swipe.RecyclerTouchListener
import com.senyk.volodymyr.bloknot.presentation.viewmodel.model.CryptoBackupViewModel
import com.senyk.volodymyr.bloknot.presentation.viewmodel.model.CryptoNotesListViewModel
import kotlinx.android.synthetic.main.fragment_notes_list.*

class CryptoNotesListFragment : BaseCryptoNotepadCryptoFragment(),
    EnterPasswordDialogFragment.OnButtonClickListener,
    NoBackupPasswordDialogFragment.OnButtonClickListener,
    DeleteNoteDialogFragment.OnButtonClickListener {

    override val layoutRes = R.layout.fragment_crypto_notes_list
    override val toolbarRes = R.layout.toolbar_default
    override val menuRes = R.menu.menu_crypto_notes_list
    private val cryptoBackupViewModel by viewModels<CryptoBackupViewModel>(factoryProducer = { viewModelFactory })
    override val viewModel by viewModels<CryptoNotesListViewModel>(factoryProducer = { viewModelFactory })

    private lateinit var adapter: SimpleListWithSwipeAdapter
    private lateinit var touchListener: RecyclerTouchListener

    override fun onResume() {
        super.onResume()
        getToolbar()?.navigationIcon = null
        getToolbar()?.title = getString(R.string.app_name)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.onBackPressed()
            }
        })

        adapter = SimpleListWithSwipeAdapter(dataItemClickListener = viewModel::onListItemClick)
        notesList.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(
            notesList.context,
            DividerItemDecoration.VERTICAL
        )
        notesList.addItemDecoration(dividerItemDecoration)
        initRecyclerTouchListener()

        viewModel.dataList.observe(viewLifecycleOwner, { dataList ->
            adapter.submitList(dataList)
        })

        addNewNote.setOnClickListener { viewModel.onCreateNewNoteClick() }

        setBaseObservers(cryptoBackupViewModel)

        viewModel.onStart()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.menuActionAddNote -> {
                viewModel.onCreateNewNoteClick(); true
            }

            R.id.menuActionMakeBackup -> {
                cryptoBackupViewModel.onCreateBackupClick(); true
            }

            R.id.menuActionUseBackup -> {
                cryptoBackupViewModel.onApplyBackupClick(); true
            }

            R.id.menuActionChangePassword -> {
                viewModel.onChangePassword(); true
            }

            else -> super.onOptionsItemSelected(item)
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        cryptoBackupViewModel.onActivityResult(requestCode, resultCode, data, requireContext())
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onConfirmPasswordClickListener(password: String) {
        cryptoBackupViewModel.onPasswordReturned(password)
    }

    override fun onCancelEmptyBackupPasswordClickListener() {
        cryptoBackupViewModel.onCancelEmptyBackupPasswordClick()
    }

    override fun onConfirmEmptyBackupPasswordClickListener() {
        cryptoBackupViewModel.onConfirmEmptyBackupPasswordClick()
    }

    override fun onConfirmNoteDeleteClickListener(noteId: String) {
        viewModel.onConfirmNoteDeleteClick(noteId = noteId)
    }

    private fun initRecyclerTouchListener() {
        touchListener = RecyclerTouchListener(
            requireActivity(),
            notesList
        ).apply {
            setSwipeOptionViews(R.id.action_delete, R.id.action_edit)
                .setSwipe(R.id.listItem, R.id.swipe_row, object :
                    RecyclerTouchListener.OnSwipeOptionsClickListener {
                    override fun onSwipeOptionClicked(viewID: Int, position: Int) {
                        val listItem = adapter.getDataItem(position) ?: return
                        when (viewID) {
                            R.id.action_delete -> {
                                viewModel.onDeleteNoteClick(listItem = listItem)
                            }
                            R.id.action_edit -> {
                                viewModel.onEditNoteClick(listItem = listItem)
                            }
                        }
                    }
                })
        }
        touchListener.let(notesList::addOnItemTouchListener)
    }
}
