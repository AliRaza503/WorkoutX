package com.labz.workoutx.models

import android.util.Log
import com.labz.workoutx.utils.Consts
import java.util.Calendar

object User {
    var name: String? = null
        private set

    var email: String? = null
        private set

    var photoUrl: String? = null
        private set

    var gender: Gender? = null
        private set

    var dateOfBirth: Calendar? = null
        private set
    var goal: Goal? = null
        private set

    var uid: String? = null
        set(value) {
            if (field == null || value == null) {
                field = value
            } else {
                Log.e("${Consts.LOG_TAG}_User.kt", "uid: Cannot set uid again")
            }
        }

    // Editable properties
    var weightInKgs: Double = 0.0
        private set

    var heightInCms: Double = 0.0
        private set

    var minutesOfExercisePerDay: Double = 0.0
        private set

    fun userLoggedOut() {
        name = null
        email = null
        photoUrl = null
        gender = null
        weightInKgs = 0.0
        heightInCms = 0.0
        dateOfBirth = null
        uid = null
        minutesOfExercisePerDay = 0.0
        goal = null
    }

    /**
     * Check if the user information is well set
     */
    fun infoIsWellSet(): Boolean = gender != null
            && weightInKgs > 0.0
            && heightInCms > 0.0
            && dateOfBirth != null
            && minutesOfExercisePerDay > 0.0

    fun setData(
        weightInKgs: Double,
        heightInCms: Double,
        dateOfBirth: Calendar,
        gender: Gender,
        minutes: Double
    ) {
        this.weightInKgs = weightInKgs
        this.heightInCms = heightInCms
        this.dateOfBirth = dateOfBirth
        this.gender = gender
        this.minutesOfExercisePerDay = minutes

        Log.d(
            "${Consts.LOG_TAG}_User.kt",
            "setData: User data set. ${this.weightInKgs}, ${this.heightInCms}, ${this.dateOfBirth}, ${this.gender}, ${this.minutesOfExercisePerDay}"
        )
    }

    fun updateGoal(goal: Goal?) {
        // Set the goal
        this.goal = goal
    }

    fun setCreds(name: String?, email: String?, photoUrl: String?) {
        this.name = name
        this.email = email
        this.photoUrl = photoUrl
    }
}