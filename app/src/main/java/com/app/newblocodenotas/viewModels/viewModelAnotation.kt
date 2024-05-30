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


    private val _getNote = MutableLiveData<UiState<ArrayList<Anotation>>>()
    val getNote: LiveData<UiState<ArrayList<Anotation>>>
        get() = _getNote

    private val _getNotePrivate = MutableLiveData<UiState<ArrayList<Anotation>>>()
    val getNotePrivate: LiveData<UiState<ArrayList<Anotation>>>
        get() = _getNotePrivate



    fun newNote(
        id: String,
        privateOrPublic: String,
        anotation: Anotation
    ){
        viewModelScope.launch {
            _newNote.value = UiState.Loading
            repository.createAnotation(
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
            repository.getAnotation(
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
            repository.getAnotationPivate(
                id
            ){listState->
                _getNotePrivate.value = listState
            }
        }
    }


}