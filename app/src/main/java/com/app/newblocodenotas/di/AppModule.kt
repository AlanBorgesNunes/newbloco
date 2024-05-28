package com.app.newblocodenotas.di

import android.content.Context
import android.content.SharedPreferences
import com.app.newblocodenotas.utils.SharedPreferencesConstant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun sharedPreferense(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
            SharedPreferencesConstant.SharedPreferencesContant,
            Context.MODE_PRIVATE
        )
    }

}