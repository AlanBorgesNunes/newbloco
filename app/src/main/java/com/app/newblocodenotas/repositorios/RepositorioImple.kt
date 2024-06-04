package com.app.newblocodenotas.repositorios

import android.app.Activity
import android.content.Context
import com.app.newblocodenotas.models.Anotation
import com.app.newblocodenotas.models.User
import com.app.newblocodenotas.utils.UiState
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore

class RepositorioImple(
    private val database: FirebaseFirestore,
    private val auth: FirebaseAuth,
    var adRequest: AdRequest
) : Repository {


    private var interstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null

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

    override suspend fun logout(result: () -> Unit) {
        auth.signOut()
        result.invoke()
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

    override suspend fun createNote(
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

    override suspend fun updateNote(
        id: String,
        hashMap: HashMap<String, Any>,
        anotation: Anotation,
        result: (UiState<String>) -> Unit
    ) {
        val document = database.collection(id)
            .document("Notas")
            .collection(anotation.privateOrPublic!!)
            .document(anotation.id!!)
        document.update(hashMap)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success(
                        "succes update note"
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


    override suspend fun getNote(
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

    override suspend fun getNotePivate(
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

    override suspend fun deleteNote(
        id: String,
        anotation: Anotation,
        result: (UiState<String>) -> Unit
    ) {
        val document = database.collection(id).document("Notas")
            .collection(anotation.privateOrPublic!!).document(anotation.id!!)
        document.delete()
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success(
                        "Succes deleted"
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

    override suspend fun loadInterstitialAd(context: Context, result: (UiState<String>) -> Unit) {
        InterstitialAd.load(context,"ca-app-pub-6827886217820908/3517396302",
            adRequest,object  : InterstitialAdLoadCallback(){
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    result.invoke(UiState.Failure(
                        "Null"
                    ))
                    interstitialAd = null
                }

                override fun onAdLoaded(MinterstitialAd: InterstitialAd) {
                    result.invoke(UiState.Success(
                        "Sucess"
                    ))
                    interstitialAd = MinterstitialAd
                }
            })
    }

    override suspend fun showInterstitialAd(activity: Activity, result: (UiState<String>) -> Unit) {
        if (interstitialAd != null){
            interstitialAd!!.fullScreenContentCallback = object : FullScreenContentCallback(){
                override fun onAdDismissedFullScreenContent() {
                    result.invoke(UiState.Success("den"))
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    super.onAdFailedToShowFullScreenContent(p0)
                    result.invoke(UiState.Success("failed"))
                }
            }

            interstitialAd?.show(activity).run {
                result.invoke(UiState.Success(
                    "show inter"
                ))
            }
        }else{
            result.invoke(UiState.Failure(
                "innter NUll"
            ))
        }
    }

}


