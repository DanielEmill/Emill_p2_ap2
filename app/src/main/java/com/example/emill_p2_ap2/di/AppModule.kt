package com.example.emill_p2_ap2.di

import android.content.Context
import com.example.emill_p2_ap2.data.repository.CounterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providePreferences(@ApplicationContext context: Context) = CounterRepository(context)
}