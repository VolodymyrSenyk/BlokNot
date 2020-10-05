package com.senyk.volodymyr.bloknot.presentation.viewmodel.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.senyk.volodymyr.bloknot.R
import com.senyk.volodymyr.bloknot.domain.usecase.lock.ClearPasswordUseCase
import com.senyk.volodymyr.bloknot.domain.usecase.notes.DeleteNoteUseCase
import com.senyk.volodymyr.bloknot.domain.usecase.notes.GetNotesListUseCase
import com.senyk.volodymyr.bloknot.presentation.mapper.NoteDtoListItemMapper
import com.senyk.volodymyr.bloknot.presentation.view.dialog.DeleteNoteDialogFragment
import com.senyk.volodymyr.bloknot.presentation.view.fragment.CryptoNotesListFragmentDirections
import com.senyk.volodymyr.bloknot.presentation.view.recyclerview.ListItem
import com.senyk.volodymyr.bloknot.presentation.view.recyclerview.SimpleDataListItem
import com.senyk.volodymyr.bloknot.presentation.view.recyclerview.SimpleEmptyState
import com.senyk.volodymyr.bloknot.presentation.viewmodel.base.BaseRxViewModel
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.ResourcesProvider
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.livedata.event.navigation.NavigateBackToFragmentEvent
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.livedata.event.navigation.NavigateToFragmentEvent
import javax.inject.Inject

class CryptoNotesListViewModel @Inject constructor(
    private val getNotesListUseCase: GetNotesListUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val noteDtoListItemMapper: NoteDtoListItemMapper,
    private val resourcesProvider: ResourcesProvider,
    private val clearPasswordUseCase: ClearPasswordUseCase
) : BaseRxViewModel() {

    private val _dataList = MutableLiveData<List<ListItem>>()
    val dataList: LiveData<List<ListItem>>
        get() = _dataList

    fun onStart() {
        subscribe(
            upstream = getNotesListUseCase(secret = true),
            onNext = { notes ->
                if (notes.isEmpty()) {
                    _dataList.value =
                        listOf(SimpleEmptyState(text = resourcesProvider.getString(R.string.notes_list_empty_state)))
                } else {
                    _dataList.value = notes.map(noteDtoListItemMapper::invoke)
                }
            }
        )
    }

    fun onListItemClick(listItem: SimpleDataListItem) {
        val action =
            CryptoNotesListFragmentDirections.actionCryptoNotesListFragmentToCryptoNoteDetailsFragment(
                noteId = listItem.id
            )
        _navigationEvent.setHandledValue(NavigateToFragmentEvent(action))
    }

    fun onCreateNewNoteClick() {
        val action =
            CryptoNotesListFragmentDirections.actionCryptoNotesListFragmentToEditCryptoNoteFragment()
        _navigationEvent.setHandledValue(NavigateToFragmentEvent(action))
    }

    fun onEditNoteClick(listItem: SimpleDataListItem) {
        val note = noteDtoListItemMapper(listItem)
        val action =
            CryptoNotesListFragmentDirections.actionCryptoNotesListFragmentToEditCryptoNoteFragment(
                noteId = note.id
            )
        _navigationEvent.setHandledValue(NavigateToFragmentEvent(action))
    }

    fun onDeleteNoteClick(listItem: SimpleDataListItem) {
        val note = noteDtoListItemMapper(listItem)
        _dialogFragment.setValue(
            DeleteNoteDialogFragment.newInstance(noteId = note.id, noteName = note.name)
        )
    }

    fun onConfirmNoteDeleteClick(noteId: String) {
        subscribe(
            upstream = deleteNoteUseCase(noteId = noteId),
            onComplete = {
                _toastMessage.setValue(resourcesProvider.getString(R.string.note_successfully_deleted_message))
            }
        )
    }

    fun onBackPressed() {
        val direction = R.id.notesListFragment
        _navigationEvent.setHandledValue(NavigateBackToFragmentEvent(direction))
    }

    fun onChangePassword() {
        subscribe(
            upstream = clearPasswordUseCase(),
            onComplete = {
                val direction = R.id.invisibleLockFragment
                _navigationEvent.setHandledValue(NavigateBackToFragmentEvent(direction))
            }
        )
    }
}
