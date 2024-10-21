package com.labz.workoutx.services.db

import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import com.labz.workoutx.models.Gender
import com.labz.workoutx.models.Goal
import com.labz.workoutx.models.User
import com.labz.workoutx.utils.Consts
import com.labz.workoutx.utils.DateFormatters.toCalendarObj
import kotlin.collections.get

class DBServiceImpl : DBService {
    override suspend fun setUserData(
        weightInKgs: Double,
        heightInCms: Double,
        dateOfBirth: String,
        gender: String,
        goal: String
    ) {
        if (Firebase.auth.currentUser == null) {
            Log.e(
                "${Consts.LOG_TAG}_RealTimeDataBaseServiceImpl",
                "setUserData: User is not logged in"
            )
            return
        }
        val database = Firebase.database
        val userRef = database.getReference("users/${Firebase.auth.currentUser!!.uid}")
        userRef.child("weightInKgs").setValue(weightInKgs)
        userRef.child("heightInCms").setValue(heightInCms)
        userRef.child("dateOfBirth").setValue(dateOfBirth)
        userRef.child("gender").setValue(gender)
        userRef.child("goal").setValue(goal)

        User.setData(
            weightInKgs = weightInKgs,
            heightInCms = heightInCms,
            dateOfBirth = dateOfBirth.toCalendarObj(),
            gender = Gender.valueOf(gender = gender),
            goal = Goal.valueOf(goal = goal)
        )
    }

    override suspend fun getUserDataToUserObj() {
        if (User.infoIsWellSet()) {
            Log.d(
                "${Consts.LOG_TAG}_RealTimeDataBaseServiceImpl",
                "getUserDataToUserObj: User Info is already set"
            )
            return
        }
        val database = Firebase.database
        User.uid = Firebase.auth.currentUser?.uid
        if (User.uid == null) {
            Log.e(
                "${Consts.LOG_TAG}_RealTimeDataBaseServiceImpl",
                "getUserDataToUserObj: User is not logged in"
            )
            return
        }
        Log.d("${Consts.LOG_TAG}_RealTimeDataBaseServiceImpl", "getUserDataToUserObj: ${User.uid}")
        User.name = Firebase.auth.currentUser?.displayName
        User.email = Firebase.auth.currentUser?.email
        User.photoUrl = Firebase.auth.currentUser?.photoUrl?.toString()
        Log.d("${Consts.LOG_TAG}_RealTimeDataBaseServiceImpl", "getUserDataToUserObj: ${User.name}")
        Log.d(
            "${Consts.LOG_TAG}_RealTimeDataBaseServiceImpl",
            "getUserDataToUserObj: ${User.email}"
        )
        Log.d(
            "${Consts.LOG_TAG}_RealTimeDataBaseServiceImpl",
            "getUserDataToUserObj: ${User.photoUrl}"
        )
        val userRef = database.getReference("users/${Firebase.auth.currentUser!!.uid}")

        try {
            val snapshot = Tasks.await(userRef.get()) // Use Tasks.await() to wait for the result
            val userData = snapshot.value as Map<*, *>
            User.weightInKgs = (userData["weightInKgs"] as Number).toDouble()
            User.heightInCms = (userData["heightInCms"] as Number).toDouble()
            User.dateOfBirth = (userData["dateOfBirth"] as String).toCalendarObj()
            User.gender = Gender.valueOf(gender = userData["gender"] as String)
            User.goal = Goal.valueOf(goal = userData["goal"] as String)
            Log.d(
                "${Consts.LOG_TAG}_RealTimeDataBaseServiceImpl",
                "User Loaded: ${User.gender} ${User.weightInKgs} ${User.heightInCms} ${User.dateOfBirth} ${User.goal}"
            )
        } catch (e: Exception) {
            Log.e(
                "${Consts.LOG_TAG}_RealTimeDataBaseServiceImpl",
                "getUserDataToUserObj: ${e.message}"
            )
        }
    }

    override suspend fun checkIfUserInfoLoaded(): Boolean {
        try {
            Log.d(
                "${Consts.LOG_TAG}_RealTimeDataBaseServiceImpl",
                "User: ${User.gender} ${User.weightInKgs} ${User.heightInCms} ${User.dateOfBirth} ${User.goal}"
            )
            return User.infoIsWellSet()
        } catch (e: Exception) {
            Log.e(
                "${Consts.LOG_TAG}_RealTimeDataBaseServiceImpl",
                "checkIfUserInfoExists: ${e.message}"
            )
            return false
        }
    }
}