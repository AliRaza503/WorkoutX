package com.labz.workoutx.services.db

import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import com.labz.workoutx.exts.Exts.getKey
import com.labz.workoutx.models.Gender
import com.labz.workoutx.models.Goal
import com.labz.workoutx.models.User
import com.labz.workoutx.utils.Consts
import com.labz.workoutx.utils.DateFormatters.toCalendarObj
import java.time.LocalDateTime
import kotlin.collections.get
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class DBServiceImpl : DBService {
    override suspend fun setUserData(
        weightInKgs: Double,
        heightInCms: Double,
        dateOfBirth: String,
        gender: String,
        minutesOfExercisePerDay: Double
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
        userRef.child("minutesOfExercisePerDay").setValue(minutesOfExercisePerDay)

        User.setData(
            weightInKgs = weightInKgs,
            heightInCms = heightInCms,
            dateOfBirth = dateOfBirth.toCalendarObj(),
            gender = Gender.valueOf(gender = gender),
            minutes = minutesOfExercisePerDay
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
        val name = Firebase.auth.currentUser?.displayName
        val email = Firebase.auth.currentUser?.email
        val photoUrl = Firebase.auth.currentUser?.photoUrl?.toString()
        User.setCreds(name = name, email = email, photoUrl = photoUrl)
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
            val weightInKgs = (userData["weightInKgs"] as Number).toDouble()
            val heightInCms = (userData["heightInCms"] as Number).toDouble()
            val dateOfBirth = (userData["dateOfBirth"] as String).toCalendarObj()
            val gender = Gender.valueOf(gender = userData["gender"] as String)
            val minutesOfExercisePerDay =
                (userData["minutesOfExercisePerDay"] as Number).toDouble()
            User.setData(
                weightInKgs = weightInKgs,
                heightInCms = heightInCms,
                dateOfBirth = dateOfBirth,
                gender = gender,
                minutes = minutesOfExercisePerDay
            )

            User.updateGoal(Goal.valueOf(goal = userData["goal"] as String))
            Log.d(
                "${Consts.LOG_TAG}_RealTimeDataBaseServiceImpl",
                "User Loaded: ${User.gender} ${User.weightInKgs} ${User.heightInCms} ${User.dateOfBirth} ${User.minutesOfExercisePerDay}"
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
                "User: ${User.gender} ${User.weightInKgs} ${User.heightInCms} ${User.dateOfBirth} ${User.minutesOfExercisePerDay}"
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

    override suspend fun getActivityMinutesForMonth(): Map<String, Double> {
        return suspendCoroutine { continuation ->
            val activityDataMap = mutableMapOf<String, Double>()
            try {
                val database = Firebase.database
                val userRef = database.getReference("users/${Firebase.auth.currentUser!!.uid}")
                val activityDataRef = userRef.child("activityData")

                // Retrieve data for the entire month
                activityDataRef.get().addOnSuccessListener { dataSnapshot ->
                    for (dateSnapshot in dataSnapshot.children) {
                        val date = dateSnapshot.key ?: continue
                        val minutes =
                            dateSnapshot.child("minutes").getValue(Double::class.java) ?: 0.0
                        activityDataMap[date] = minutes
                    }
                    Log.d(
                        "${Consts.LOG_TAG}_RealTimeDataBaseServiceImpl",
                        "Fetched monthly activity data: $activityDataMap"
                    )
                    continuation.resume(activityDataMap)
                }.addOnFailureListener { e ->
                    Log.e(
                        "${Consts.LOG_TAG}_RealTimeDataBaseServiceImpl",
                        "Failed to fetch monthly activity data: ${e.localizedMessage}"
                    )
                    continuation.resumeWithException(e)
                }
            } catch (e: Exception) {
                Log.e(
                    "${Consts.LOG_TAG}_RealTimeDataBaseServiceImpl",
                    "getActivityMinutesForMonth: ${e.localizedMessage}"
                )
                continuation.resumeWithException(e)
            }
        }
    }

    override suspend fun addActivityMinutesOfToday(activityMinutes: Double) {
        try {
            val database = Firebase.database
            val userRef = database.getReference("users/${Firebase.auth.currentUser!!.uid}")
            val today = LocalDateTime.now().getKey()
            val activityDataRef = userRef.child("activityData").child(today)

            // First, get the existing minutes for today (if any)
            activityDataRef.child("minutes").get().addOnSuccessListener { dataSnapshot ->
                val existingMinutes = dataSnapshot.getValue(Double::class.java) ?: 0.0

                // If there are no minutes set for today, initialize them
                if (existingMinutes == 0.0) {
                    activityDataRef.child("minutes").setValue(activityMinutes)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(
                                    "${Consts.LOG_TAG}_RealTimeDataBaseServiceImpl",
                                    "Initialized today's activity minutes: $activityMinutes"
                                )
                            } else {
                                Log.e(
                                    "${Consts.LOG_TAG}_RealTimeDataBaseServiceImpl",
                                    "Failed to initialize today's activity minutes: ${task.exception?.localizedMessage}"
                                )
                            }
                        }
                } else {
                    Log.d(
                        "${Consts.LOG_TAG}_RealTimeDataBaseServiceImpl",
                        "Today's activity minutes already set: $existingMinutes"
                    )
                }
            }.addOnFailureListener { e ->
                Log.e(
                    "${Consts.LOG_TAG}_RealTimeDataBaseServiceImpl",
                    "Failed to retrieve today's activity minutes: ${e.localizedMessage}"
                )
            }
        } catch (e: Exception) {
            Log.e(
                "${Consts.LOG_TAG}_RealTimeDataBaseServiceImpl",
                "setTodaysActivityMinutes: ${e.localizedMessage}"
            )
        }
    }

    override suspend fun setGoal(goal: Goal) {
        try {
            val database = Firebase.database
            val userRef = database.getReference("users/${Firebase.auth.currentUser!!.uid}")
            userRef.child("goal").setValue(goal.name)
            User.updateGoal(goal)
            Log.d(
                "${Consts.LOG_TAG}_RealTimeDataBaseServiceImpl",
                "setGoal: $goal"
            )
        } catch (e: Exception) {
            Log.e(
                "${Consts.LOG_TAG}_RealTimeDataBaseServiceImpl",
                "setGoal: ${e.localizedMessage}"
            )
        }
    }

}