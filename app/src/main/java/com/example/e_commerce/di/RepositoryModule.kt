package com.example.e_commerce.di

import com.example.e_commerce.data.repository.auth.FireBaseAuthRepository
import com.example.e_commerce.data.repository.auth.FireBaseAuthRepositoryImpl
import com.example.e_commerce.data.repository.common.AppDataStoreRepositoryImpl
import com.example.e_commerce.data.repository.common.AppPreferenceRepository
import com.example.e_commerce.data.repository.user.UserFirestoreRepository
import com.example.e_commerce.data.repository.user.UserFirestoreRepositoryImpl
import com.example.e_commerce.data.repository.user.UserPreferenceRepository
import com.example.e_commerce.data.repository.user.UserPreferenceRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFireBaseAuthRepository(
        fireBaseAuthRepositoryImpl: FireBaseAuthRepositoryImpl
    ): FireBaseAuthRepository

    @Binds
    @Singleton
    abstract fun bindUserPreferenceRepository(
        userPreferenceRepositoryImpl: UserPreferenceRepositoryImpl
    ): UserPreferenceRepository

    @Binds
    @Singleton
    abstract fun appPreferenceRepository(
        appPreferenceRepository: AppDataStoreRepositoryImpl
    ): AppPreferenceRepository

    @Binds
    @Singleton
    abstract fun userFireStoreRepository(
        userFireStoreRepository : UserFirestoreRepositoryImpl
    ): UserFirestoreRepository
}

