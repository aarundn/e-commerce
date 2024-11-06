package com.example.e_commerce.di

import android.app.Application
import android.preference.PreferenceDataStore
import com.example.e_commerce.data.datasource.datastore.AppPreferencesDataSource
import com.example.e_commerce.data.repository.auth.FireBaseAuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuthRepository(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
    @Provides
    @Singleton
    fun providePreferenceDataStore(context: Application): AppPreferencesDataSource {
        return AppPreferencesDataSource(context = context)
    }


}