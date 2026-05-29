package com.example.guidemetestapp.di

import com.example.guidemetestapp.data.repository.AuthRepositoryImpl
import com.example.guidemetestapp.data.repository.TourRepositoryImpl
import com.example.guidemetestapp.domain.repository.AuthRepository
import com.example.guidemetestapp.domain.repository.TourRepository
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
    abstract fun bindTourRepository(
        tourRepositoryImpl: TourRepositoryImpl
    ): TourRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}
