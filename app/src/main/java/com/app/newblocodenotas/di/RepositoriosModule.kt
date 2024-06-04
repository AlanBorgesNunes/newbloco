package com.app.newblocodenotas.di

import com.app.newblocodenotas.repositorios.RepositorioImple
import com.app.newblocodenotas.repositorios.Repository
import com.google.android.gms.ads.AdRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoriosModule {

    @Provides
    @Singleton
    fun providesRepositorios(
        auth: FirebaseAuth,
        database: FirebaseFirestore,
        adRequest: AdRequest
    ): Repository{
        return RepositorioImple(database, auth, adRequest)
    }

}