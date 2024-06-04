package com.app.newblocodenotas.repositorios

import android.app.Activity
import android.content.Context
import com.app.newblocodenotas.models.Anotation
import com.app.newblocodenotas.models.User
import com.app.newblocodenotas.utils.UiState

interface Repository {

    //Auth
    suspend fun authUser(user:User, result: (UiState<String>)-> Unit)
    suspend fun createUser(user:User, result: (UiState<String>)-> Unit)
    suspend fun logout(result: () -> Unit)

    //Create user in Database
    fun createUserInDatabase(user: User, result: (UiState<String>) -> Unit)

    //POST and GET Anotation
    suspend fun createNote(id: String, privateOrPublic: String, anotation: Anotation, result: (UiState<String>) -> Unit)
    suspend fun updateNote(id: String,hashMap: HashMap<String,Any>, anotation: Anotation, result: (UiState<String>) -> Unit)
    suspend fun getNote(id: String, result: (UiState<ArrayList<Anotation>>) -> Unit)
    suspend fun getNotePivate(id: String, result: (UiState<ArrayList<Anotation>>) -> Unit)
    suspend fun deleteNote(id: String, anotation: Anotation, result: (UiState<String>) -> Unit)

    //Admob
    suspend fun loadInterstitialAd(context: Context, result:(UiState<String>) -> Unit)
    suspend fun showInterstitialAd(activity: Activity, result: (UiState<String>) -> Unit)

}