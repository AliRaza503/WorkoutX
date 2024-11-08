package com.labz.workoutx.modules

import com.labz.workoutx.services.auth.AccountService
import com.labz.workoutx.services.auth.AccountServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AccountModule {
    @Provides
    @Singleton
    fun provideAccountService(): AccountService {
        return AccountServiceImpl()
    }
}