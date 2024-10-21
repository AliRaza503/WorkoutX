package com.labz.workoutx.services.modules

import com.labz.workoutx.services.permissions.PermissionsService
import com.labz.workoutx.services.permissions.PermissionsServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PermissionsModule {
    @Provides
    @Singleton
    fun providePermissionsService(): PermissionsService {
        return PermissionsServiceImpl()
    }
}