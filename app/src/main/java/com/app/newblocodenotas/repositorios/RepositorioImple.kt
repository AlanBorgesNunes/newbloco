package com.app.newblocodenotas.repositorios

import com.app.newblocodenotas.models.Anotation
import com.app.newblocodenotas.models.User
import com.app.newblocodenotas.utils.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RepositorioImple(
    private val database: FirebaseFirestore,
    private val auth: FirebaseAuth
): Repository {
    override suspend fun authUser(
        user: User,
        result: (UiState<String>) -> Unit) {
        auth.signInWithEmailAndPassword(
            user.email!!,
            user.senha!!
        ).addOnSuccessListener {
            result.invoke(
                UiState.Success(
                    "Succes auth"
                )
            )
        }.addOnFailureListener {
            result.invoke(
                UiState.Failure(
                    it.localizedMessage
                )
            )
        }
    }

    override suspend fun createUser(
        user: User,
        result: (UiState<String>) -> Unit) {
        auth.createUserWithEmailAndPassword(
            user.email!!,
            user.senha!!
        ).addOnSuccessListener {
            result.invoke(
                UiState.Success(
                    "Secces create user"
                )
            )
        }.addOnFailureListener {
            result.invoke(
                UiState.Failure(
                    it.localizedMessage
                )
            )
        }
    }

    override suspend fun createAnotation(
        id: String,
        anotation: Anotation,
        result: (UiState<String>) -> Unit) {
        val document = database.collection(id)
            .document("Notas")
            .collection("Simples")
            .document(anotation.id!!)
        document.set(anotation)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success(
                        "Secces create note"
                    )
                )
            }.addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }

    override suspend fun createAnotationPrivate(
        id: String,
        anotation: Anotation,
        result: (UiState<String>) -> Unit
    ) {
        val document = database.collection(id)
            .document("Notas")
            .collection("Privadas")
            .document(anotation.id!!)
        document.set(anotation)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success(
                        "Secces create note private"
                    )
                )
            }.addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }

    override suspend fun getAnotation(
        id: String,
        result: (UiState<ArrayList<Anotation>>) -> Unit
    ) {
        val document = database.collection(id)
            .document("Notas")
            .collection("Simples")
        document.addSnapshotListener { value, error ->
            if (value != null){
                val list : ArrayList<Anotation> = arrayListOf()
                val notesSimple = value.toObjects(Anotation::class.java)
                list.addAll(notesSimple)
                if (notesSimple.isNotEmpty()){
                    result.invoke(
                        UiState.Success(
                            list
                        )
                    )
                }else{
                    result.invoke(
                        UiState.Failure(
                            "Notes isEmpty"
                        )
                    )
                }
            }else{
                result.invoke(
                    UiState.Failure(
                        "Value false"
                    )
                )
            }
        }
    }

    override suspend fun getAnotationPrivete(
        id: String,
        result: (UiState<ArrayList<Anotation>>) -> Unit
    ) {
        val document = database.collection(id)
            .document("Notas")
            .collection("Privadas")
        document.addSnapshotListener { value, error ->
            if (value != null){
                val listPrivate : ArrayList<Anotation> = arrayListOf()
                val notesSimple = value.toObjects(Anotation::class.java)
                listPrivate.addAll(notesSimple)
                if (notesSimple.isNotEmpty()){
                    result.invoke(
                        UiState.Success(
                            listPrivate
                        )
                    )
                }else{
                    result.invoke(
                        UiState.Failure(
                            "Notes isEmpty"
                        )
                    )
                }
            }else{
                result.invoke(
                    UiState.Failure(
                        "Value false"
                    )
                )
            }
        }
    }
}