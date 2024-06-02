package com.app.newblocodenotas.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.newblocodenotas.models.User
import com.app.newblocodenotas.repositorios.Repository
import com.app.newblocodenotas.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelAuth @Inject constructor(
    private val repository: Repository
): ViewModel() {

    private val _authUser = MutableLiveData<UiState<String>>()
    val authUser: LiveData<UiState<String>>
        get() = _authUser

    private val _createUser = MutableLiveData<UiState<String>>()
    val createUser: LiveData<UiState<String>>
        get() = _createUser


    fun authUser(
        user: User
    ){
        viewModelScope.launch {
            _authUser.value = UiState.Loading
            repository.authUser(
                user
            ){state ->
               _authUser.value = state
            }
        }
    }

    fun createUSer(
        user: User
    ){
        viewModelScope.launch {
            _createUser.value = UiState.Loading
            repository.createUser(
                user
            ){state ->
                _createUser.value = state
            }
        }
    }


    fun logout(
        result: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                repository.logout(result)
            }catch (e: Exception){
                println(e.localizedMessage)
            }
        }

    }
}