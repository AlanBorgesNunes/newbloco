package com.app.newblocodenotas.repositorios

import com.app.newblocodenotas.models.Anotation
import com.app.newblocodenotas.models.User
import com.app.newblocodenotas.utils.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore

class RepositorioImple(
    private val database: FirebaseFirestore,
    private val auth: FirebaseAuth
) : Repository {
    override suspend fun authUser(
        user: User,
        result: (UiState<String>) -> Unit
    ) {
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
        result: (UiState<String>) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(
            user.email!!,
            user.senha!!
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                createUserInDatabase(user) { state ->
                    when (state) {
                        is UiState.Failure -> {
                            result.invoke(
                                UiState.Failure(
                                    state.error
                                )
                            )
                        }

                        UiState.Loading -> {}
                        is UiState.Success -> {
                            result.invoke(
                                UiState.Success(
                                    "User register successfully"
                                )
                            )
                        }
                    }
                }
            } else {
                try {
                    throw it.exception ?: java.lang.Exception("Invalid authentication")
                } catch (e: FirebaseAuthWeakPasswordException) {
                    result.invoke(UiState.Failure("Authentication failed, Password should be at least 6 characters"))
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    result.invoke(UiState.Failure("Authentication failed, Invalid email entered"))
                } catch (e: FirebaseAuthUserCollisionException) {
                    result.invoke(UiState.Failure("Authentication failed, Email already registered."))
                } catch (e: Exception) {
                    result.invoke(UiState.Failure(e.message))
                }
            }
        }.addOnFailureListener {
            result.invoke(
                UiState.Failure(
                    it.localizedMessage
                )
            )
        }

    }

    override fun createUserInDatabase(user: User, result: (UiState<String>) -> Unit) {
        val document = database.collection("users")
            .document(auth.currentUser?.uid!!)
        document.set(user)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success(
                        "Succes create user in database"
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
        privateOrPublic: String,
        anotation: Anotation,
        result: (UiState<String>) -> Unit
    ) {
        val document = database.collection(id)
            .document("Notas")
            .collection(privateOrPublic)
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


    override suspend fun getAnotation(
        id: String,
        result: (UiState<ArrayList<Anotation>>) -> Unit
    ) {
        val document = database.collection(id)
            .document("Notas")
            .collection("Public")
        document.addSnapshotListener { value, error ->
            if (value != null) {
                val list: ArrayList<Anotation> = arrayListOf()
                val notesSimple = value.toObjects(Anotation::class.java)
                list.addAll(notesSimple)
                if (notesSimple.isNotEmpty()) {
                    result.invoke(
                        UiState.Success(
                            list
                        )
                    )
                } else {
                    result.invoke(
                        UiState.Failure(
                            "Notes isEmpty"
                        )
                    )
                }
            } else {
                result.invoke(
                    UiState.Failure(
                        "Value false"
                    )
                )
            }
        }
    }

    override suspend fun getAnotationPivate(
        id: String,
        result: (UiState<ArrayList<Anotation>>) -> Unit
    ) {
        val document = database.collection(id)
            .document("Notas")
            .collection("Private")
        document.addSnapshotListener { value, error ->
            if (value != null) {
                val list: ArrayList<Anotation> = arrayListOf()
                val notesSimple = value.toObjects(Anotation::class.java)
                list.addAll(notesSimple)
                if (notesSimple.isNotEmpty()) {
                    result.invoke(
                        UiState.Success(
                            list
                        )
                    )
                } else {
                    result.invoke(
                        UiState.Failure(
                            "Notes isEmpty"
                        )
                    )
                }
            } else {
                result.invoke(
                    UiState.Failure(
                        "Value false"
                    )
                )
            }
        }
    }

}


