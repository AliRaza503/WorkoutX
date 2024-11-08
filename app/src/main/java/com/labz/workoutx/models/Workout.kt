package com.labz.workoutx.models

import java.util.UUID


data class Workout(
    val id: UUID = UUID.randomUUID(),      // Unique identifier for the workout
    val name: String,                      // Name of the workout
    val description: String,               // A brief description of the workout
    val targetMinutes: Int,                // Suggested time to perform the activity
    val benefit: String,                   // Benefits of doing the activity
    val steps: List<String>,               // Steps to perform the workout
    val imagePath: String                  // Path to the image in assets
)
