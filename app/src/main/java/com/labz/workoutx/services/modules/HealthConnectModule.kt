package com.labz.workoutx.services.modules

import com.labz.workoutx.services.healthConnect.HealthConnectService
import com.labz.workoutx.services.healthConnect.HealthConnectServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object HealthConnectModule {
    @Provides
    @Singleton
    fun provideHealthConnectService(): HealthConnectService {
        return HealthConnectServiceImpl()
    }
}