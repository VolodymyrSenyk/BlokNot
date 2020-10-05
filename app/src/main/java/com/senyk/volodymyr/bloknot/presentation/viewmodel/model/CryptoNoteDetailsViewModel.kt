package com.senyk.volodymyr.bloknot.presentation.viewmodel.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.senyk.volodymyr.bloknot.R
import com.senyk.volodymyr.bloknot.domain.usecase.notes.DeleteNoteUseCase
import com.senyk.volodymyr.bloknot.domain.usecase.notes.GetNoteUseCase
import com.senyk.volodymyr.bloknot.presentation.entity.NoteUi
import com.senyk.volodymyr.bloknot.presentation.mapper.NoteUiDtoMapper
import com.senyk.volodymyr.bloknot.presentation.view.dialog.DeleteNoteDialogFragment
import com.senyk.volodymyr.bloknot.presentation.view.fragment.CryptoNoteDetailsFragmentDirections
import com.senyk.volodymyr.bloknot.presentation.viewmodel.base.BaseRxViewModel
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.ResourcesProvider
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.livedata.event.navigation.NavigateBackToFragmentEvent
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.livedata.event.navigation.NavigateToFragmentEvent
import javax.inject.Inject

class CryptoNoteDetailsViewModel @Inject constructor(
    private val getNoteUseCase: GetNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val noteUiDtoMapper: NoteUiDtoMapper,
    private val resourcesProvider: ResourcesProvider
) : BaseRxViewModel() {

    private val _note = MutableLiveData<NoteUi>()
    val note: LiveData<NoteUi>
        get() = _note

    fun onStart(noteId: String) {
        subscribe(
            upstream = getNoteUseCase(noteId = noteId),
            onSuccess = { note -> _note.value = noteUiDtoMapper(note) }
        )
    }

    fun onEditNoteClick() {
        note.value?.id?.let { noteId ->
            val action =
                CryptoNoteDetailsFragmentDirections.actionCryptoNoteDetailsFragmentToEditCryptoNoteFragment(
                    noteId = noteId
                )
            _navigationEvent.setHandledValue(NavigateToFragmentEvent(action))
        }
    }

    fun onDeleteNoteClick() {
        note.value?.let { note ->
            _dialogFragment.setValue(
                DeleteNoteDialogFragment.newInstance(
                    noteName = note.name,
                    noteId = note.id
                )
            )
        }
    }

    fun onConfirmNoteDeleteClick(noteId: String) {
        subscribe(
            upstream = deleteNoteUseCase(noteId = noteId),
            onComplete = {
                _navigationEvent.setHandledValue(NavigateBackToFragmentEvent(R.id.cryptoNotesListFragment))
                _toastMessage.setValue(resourcesProvider.getString(R.string.note_successfully_deleted_message))
            }
        )
    }
}
