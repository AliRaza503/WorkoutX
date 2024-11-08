package com.labz.workoutx.modules

import com.labz.workoutx.services.db.DBService
import com.labz.workoutx.services.db.DBServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DBModule {
    @Provides
    @Singleton
    fun provideDBService(): DBService {
        return DBServiceImpl()
    }
}