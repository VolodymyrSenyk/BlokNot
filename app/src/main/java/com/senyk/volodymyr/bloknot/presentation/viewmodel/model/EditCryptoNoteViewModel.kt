package com.senyk.volodymyr.bloknot.presentation.viewmodel.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.senyk.volodymyr.bloknot.R
import com.senyk.volodymyr.bloknot.domain.entity.NoteDto
import com.senyk.volodymyr.bloknot.domain.usecase.notes.DeleteNoteUseCase
import com.senyk.volodymyr.bloknot.domain.usecase.notes.GetNoteUseCase
import com.senyk.volodymyr.bloknot.domain.usecase.notes.SaveNoteUseCase
import com.senyk.volodymyr.bloknot.presentation.entity.NoteUi
import com.senyk.volodymyr.bloknot.presentation.mapper.NoteUiDtoMapper
import com.senyk.volodymyr.bloknot.presentation.view.dialog.DeleteNoteDialogFragment
import com.senyk.volodymyr.bloknot.presentation.view.dialog.ExitWithoutSaveDialogFragment
import com.senyk.volodymyr.bloknot.presentation.view.dialog.SaveEmptyNoteDialogFragment
import com.senyk.volodymyr.bloknot.presentation.view.fragment.EditCryptoNoteFragmentDirections
import com.senyk.volodymyr.bloknot.presentation.viewmodel.base.BaseRxViewModel
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.DateFormatterUtil
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.ResourcesProvider
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.livedata.event.navigation.NavigateBackEvent
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.livedata.event.navigation.NavigateBackToFragmentEvent
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.livedata.event.navigation.NavigateToFragmentEvent
import javax.inject.Inject

class EditCryptoNoteViewModel @Inject constructor(
    private val getNoteUseCase: GetNoteUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val noteUiDtoMapper: NoteUiDtoMapper,
    private val resourcesProvider: ResourcesProvider
) : BaseRxViewModel() {

    private val _note = MutableLiveData<NoteUi>()
    val note: LiveData<NoteUi>
        get() = _note

    fun onStart(noteId: String?) {
        if (noteId != null) {
            subscribe(
                upstream = getNoteUseCase(noteId = noteId),
                onSuccess = { note -> _note.value = noteUiDtoMapper(note) }
            )
        }
    }

    fun onSaveNoteClick(noteName: String, noteContent: String) {
        val note = NoteDto(
            id = note.value?.id ?: "",
            name = noteName,
            content = noteContent,
            isSecret = true
        )
        if (note.isEmpty()) {
            onSaveEmptyNoteClick(currentNoteName = noteName)
        } else {
            subscribe(
                upstream = saveNoteUseCase(
                    note = if (note.name.trim()
                            .isEmpty()
                    ) note.copy(name = DateFormatterUtil.getShortInternationalDate()) else note
                ),
                onSuccess = { updatedNote ->
                    navigateToNoteDetailsFragment(noteId = updatedNote.id)
                    _toastMessage.setValue(resourcesProvider.getString(R.string.note_successfully_saved_message))
                }
            )
        }
    }

    fun onBackPressed(noteName: String, noteContent: String) {
        val currentNote = NoteUi(
            id = note.value?.id ?: "",
            name = noteName,
            content = noteContent
        )
        val oldNote = NoteUi(
            id = note.value?.id ?: "",
            name = note.value?.name ?: "",
            content = note.value?.content ?: ""
        )
        if (currentNote == oldNote) {
            _navigationEvent.setHandledValue(NavigateBackEvent())
        } else {
            _dialogFragment.setValue(ExitWithoutSaveDialogFragment.newInstance())
        }
    }

    fun onConfirmExitWithoutSaveClick() {
        _navigationEvent.setHandledValue(NavigateBackEvent())
    }

    fun onSaveEmptyNoteClick(currentNoteName: String) {
        val emptyNoteId = note.value?.id ?: ""
        val emptyNoteName = if (currentNoteName.trim().isEmpty()) {
            note.value?.name ?: ""
        } else {
            currentNoteName
        }
        _dialogFragment.setValue(
            SaveEmptyNoteDialogFragment.newInstance(noteId = emptyNoteId, noteName = emptyNoteName)
        )
    }

    fun onDeleteNoteClick() {
        note.value?.let { note ->
            _dialogFragment.setValue(
                DeleteNoteDialogFragment.newInstance(
                    noteId = note.id,
                    noteName = note.name
                )
            )
        }
    }

    fun onConfirmSaveEmptyNoteClick(noteId: String) {
        onConfirmNoteDeleteClick(noteId = noteId)
    }

    fun onConfirmNoteDeleteClick(noteId: String) {
        if (noteId.isEmpty()) {
            _navigationEvent.setHandledValue(NavigateBackToFragmentEvent(R.id.cryptoNotesListFragment))
        } else {
            subscribe(
                upstream = deleteNoteUseCase(noteId = noteId),
                onComplete = {
                    _navigationEvent.setHandledValue(NavigateBackToFragmentEvent(R.id.cryptoNotesListFragment))
                    _toastMessage.setValue(resourcesProvider.getString(R.string.note_successfully_deleted_message))
                }
            )
        }
    }

    private fun navigateToNoteDetailsFragment(noteId: String) {
        val action =
            EditCryptoNoteFragmentDirections.actionEditCryptoNoteFragmentToCryptoNoteDetailsFragment(
                noteId = noteId
            )
        _navigationEvent.setHandledValue(NavigateToFragmentEvent(action))
    }
}
