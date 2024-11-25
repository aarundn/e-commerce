package com.example.e_commerce.di

import com.example.e_commerce.data.repository.auth.CountriesRepository
import com.example.e_commerce.data.repository.auth.CountriesRepositoryImpl
import com.example.e_commerce.data.repository.auth.FireBaseAuthRepository
import com.example.e_commerce.data.repository.auth.FireBaseAuthRepositoryImpl
import com.example.e_commerce.data.repository.category.CategoryRepository
import com.example.e_commerce.data.repository.category.CategoryRepositoryImpl
import com.example.e_commerce.data.repository.common.AppDataStoreRepositoryImpl
import com.example.e_commerce.data.repository.common.AppPreferenceRepository
import com.example.e_commerce.data.repository.home.SalesAdsRepository
import com.example.e_commerce.data.repository.home.SalesAdsRepositoryImpl
import com.example.e_commerce.data.repository.products.ProductRepository
import com.example.e_commerce.data.repository.products.ProductRepositoryImpl
import com.example.e_commerce.data.repository.special_sections.SpecialSectionsRepository
import com.example.e_commerce.data.repository.special_sections.SpecialSectionsRepositoryImpl
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

    @Binds
    @Singleton
    abstract fun salesAdsRepository(
        salesAdsRepositoryImpl: SalesAdsRepositoryImpl
    ): SalesAdsRepository

    @Binds
    @Singleton
    abstract fun categoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    @Singleton
    abstract fun productRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun countryRepository(
        countryRepositoryImpl: CountriesRepositoryImpl
    ): CountriesRepository

    @Binds
    @Singleton
    abstract fun specialSectionRepository(
        specialSectionsRepositoryImpl: SpecialSectionsRepositoryImpl
    ): SpecialSectionsRepository

}

