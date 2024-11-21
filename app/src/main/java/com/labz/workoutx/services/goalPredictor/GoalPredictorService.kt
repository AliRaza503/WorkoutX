package com.labz.workoutx.services.goalPredictor

import android.app.Application
import com.labz.workoutx.models.Goal

interface GoalPredictorService {
    fun preProcessData(application: Application, avgCalories: Double, avgMinutes: Double)
    fun predictGoal(application: Application): Goal
}