package com.app.newblocodenotas.repositorios

import com.app.newblocodenotas.models.Anotation
import com.app.newblocodenotas.models.User
import com.app.newblocodenotas.utils.UiState

interface Repository {

    //Auth
    suspend fun authUser(user:User, result: (UiState<String>)-> Unit)
    suspend fun createUser(user:User, result: (UiState<String>)-> Unit)

    //Create user in Database
    fun createUserInDatabase(user: User, result: (UiState<String>) -> Unit)

    //POST and GET Anotation
    suspend fun createAnotation(id: String,privateOrPublic: String,anotation: Anotation, result: (UiState<String>) -> Unit)
    suspend fun getAnotation(id: String, result: (UiState<ArrayList<Anotation>>) -> Unit)
    suspend fun getAnotationPivate(id: String, result: (UiState<ArrayList<Anotation>>) -> Unit)

}