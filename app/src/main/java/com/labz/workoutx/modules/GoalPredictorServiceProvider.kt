package com.labz.workoutx.modules

import com.labz.workoutx.services.goalPredictor.GoalPredictorService
import com.labz.workoutx.services.goalPredictor.GoalPredictorServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object GoalPredictorServiceProvider {
    @Provides
    @Singleton
    fun provideGoalPredictorService(): GoalPredictorService {
        return GoalPredictorServiceImpl()
    }
}