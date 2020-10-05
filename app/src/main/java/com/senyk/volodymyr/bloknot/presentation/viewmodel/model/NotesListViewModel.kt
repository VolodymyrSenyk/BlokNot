package com.senyk.volodymyr.bloknot.presentation.viewmodel.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.senyk.volodymyr.bloknot.R
import com.senyk.volodymyr.bloknot.domain.usecase.notes.DeleteNoteUseCase
import com.senyk.volodymyr.bloknot.domain.usecase.notes.GetNotesListUseCase
import com.senyk.volodymyr.bloknot.presentation.mapper.NoteDtoListItemMapper
import com.senyk.volodymyr.bloknot.presentation.view.dialog.DeleteNoteDialogFragment
import com.senyk.volodymyr.bloknot.presentation.view.fragment.NotesListFragmentDirections
import com.senyk.volodymyr.bloknot.presentation.view.recyclerview.ListItem
import com.senyk.volodymyr.bloknot.presentation.view.recyclerview.SimpleDataListItem
import com.senyk.volodymyr.bloknot.presentation.view.recyclerview.SimpleEmptyState
import com.senyk.volodymyr.bloknot.presentation.viewmodel.base.BaseRxViewModel
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.ResourcesProvider
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.livedata.event.navigation.NavigateToFragmentEvent
import javax.inject.Inject

class NotesListViewModel @Inject constructor(
    private val getNotesListUseCase: GetNotesListUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val noteDtoListItemMapper: NoteDtoListItemMapper,
    private val resourcesProvider: ResourcesProvider
) : BaseRxViewModel() {

    private val _dataList = MutableLiveData<List<ListItem>>()
    val dataList: LiveData<List<ListItem>>
        get() = _dataList

    fun onStart() {
        subscribe(
            upstream = getNotesListUseCase(),
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

    fun onSecretClick() {
        val action =
            NotesListFragmentDirections.actionNotesListFragmentToInvisibleLockFragment()
        _navigationEvent.setHandledValue(NavigateToFragmentEvent(action))
    }

    fun onListItemClick(listItem: SimpleDataListItem) {
        val action =
            NotesListFragmentDirections.actionNotesListFragmentToNoteDetailsFragment(
                noteId = listItem.id
            )
        _navigationEvent.setHandledValue(NavigateToFragmentEvent(action))
    }

    fun onCreateNewNoteClick() {
        val action =
            NotesListFragmentDirections.actionNotesListFragmentToEditNoteFragment()
        _navigationEvent.setHandledValue(NavigateToFragmentEvent(action))
    }

    fun onEditNoteClick(listItem: SimpleDataListItem) {
        val note = noteDtoListItemMapper(listItem)
        val action =
            NotesListFragmentDirections.actionNotesListFragmentToEditNoteFragment(noteId = note.id)
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
}
