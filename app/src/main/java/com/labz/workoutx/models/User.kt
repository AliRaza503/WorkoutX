package com.labz.workoutx.models

import java.time.LocalDateTime

object User {
    var name: String? = null
    var email: String? = null
    var photoUrl: String? = null
    var uid: String? = null
    var gender: Gender? = null
    var weightInKgs: Double? = null
    var heightInCms: Double? = null
    var dateOfBirth: LocalDateTime? = null
    var goal: Goal? = null
}