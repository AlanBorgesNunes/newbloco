package com.app.newblocodenotas.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.newblocodenotas.models.Anotation
import com.app.newblocodenotas.repositorios.Repository
import com.app.newblocodenotas.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class viewModelAnotation @Inject constructor(
    private val repository: Repository
): ViewModel(){

    private val _newNote = MutableLiveData<UiState<String>>()
    val newNote: LiveData<UiState<String>>
        get() = _newNote

    private val _updateNote = MutableLiveData<UiState<String>>()
    val updateNote: LiveData<UiState<String>>
        get() = _updateNote


    private val _getNote = MutableLiveData<UiState<ArrayList<Anotation>>>()
    val getNote: LiveData<UiState<ArrayList<Anotation>>>
        get() = _getNote

    private val _getNotePrivate = MutableLiveData<UiState<ArrayList<Anotation>>>()
    val getNotePrivate: LiveData<UiState<ArrayList<Anotation>>>
        get() = _getNotePrivate

    private val _deleteNote = MutableLiveData<UiState<String>>()
    val deleteNote: LiveData<UiState<String>>
        get() = _deleteNote



    fun newNote(
        id: String,
        privateOrPublic: String,
        anotation: Anotation
    ){
        viewModelScope.launch {
            _newNote.value = UiState.Loading
            repository.createNote(
                id,
                privateOrPublic,
                anotation
            ){state ->
                _newNote.value = state
            }
        }
    }




    fun getNote(
        id: String
    ){
        viewModelScope.launch {
            _getNote.value = UiState.Loading
            repository.getNote(
                id
            ){listState->
                _getNote.value = listState
            }
        }
    }

    fun getNotePrivate(
        id: String
    ){
        viewModelScope.launch {
            _getNotePrivate.value = UiState.Loading
            repository.getNotePivate(
                id
            ){listState->
                _getNotePrivate.value = listState
            }
        }
    }

    fun updateNote(
        id: String,
        hashMap: HashMap<String, Any>,
        anotation: Anotation
    ){
        viewModelScope.launch {
            _updateNote.value = UiState.Loading
            repository.updateNote(
                id,
                hashMap,
                anotation
            ){
                _updateNote.value = it
            }
        }
    }

    fun deleteNote(
        id: String,
        anotation: Anotation
    ){
        viewModelScope.launch {
            _deleteNote.value = UiState.Loading
            repository.deleteNote(
                id,
                anotation
            ){
                _deleteNote.value = it
            }
        }
    }


}