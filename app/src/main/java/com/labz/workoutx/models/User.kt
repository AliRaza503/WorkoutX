package com.labz.workoutx.models

import android.util.Log
import com.labz.workoutx.utils.Consts
import java.util.Calendar

object User {
    var name: String? = null
        set(value) {
            if (field == null || value == null) {
                field = value
            } else {
                Log.e("${Consts.LOG_TAG}_User.kt", "name: Cannot set name again")
            }
        }

    var email: String? = null
        set(value) {
            if (field == null || value == null) {
                field = value
            } else {
                Log.e("${Consts.LOG_TAG}_User.kt", "email: Cannot set email again")
            }
        }

    var photoUrl: String? = null
        set(value) {
            if (field == null || value == null) {
                field = value
            } else {
                Log.e("${Consts.LOG_TAG}_User.kt", "photoUrl: Cannot set photoUrl again")
            }
        }

    var gender: Gender? = null
        set(value) {
            if (field == null || value == null) {
                field = value
            } else {
                Log.e("${Consts.LOG_TAG}_User.kt", "gender: Cannot set gender again")
            }
        }

    var dateOfBirth: Calendar? = null
        set(value) {
            if (field == null || value == null) {
                field = value
            } else {
                Log.e("${Consts.LOG_TAG}_User.kt", "dateOfBirth: Cannot set dateOfBirth again")
            }
        }

    var goal: Goal? = null
        set(value) {
            if (field == null || value == null) {
                field = value
            } else {
                Log.e("${Consts.LOG_TAG}_User.kt", "goal: Cannot set goal again")
            }
        }

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

    var heightInCms: Double = 0.0

    fun userLoggedOut() {
        name = null
        email = null
        photoUrl = null
        gender = null
        weightInKgs = 0.0
        heightInCms = 0.0
        dateOfBirth = null
        goal = null
        uid = null
    }

    /**
     * Check if the user information is well set
     */
    fun infoIsWellSet(): Boolean = gender != null
            && weightInKgs > 0.0
            && heightInCms > 0.0
            && dateOfBirth != null
            && goal != null

    fun setData(
        weightInKgs: Double,
        heightInCms: Double,
        dateOfBirth: Calendar,
        gender: Gender,
        goal: Goal
    ) {
        this.weightInKgs = weightInKgs
        this.heightInCms = heightInCms
        this.dateOfBirth = dateOfBirth
        this.gender = gender
        this.goal = goal
    }
}