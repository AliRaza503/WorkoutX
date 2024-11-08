package com.labz.workoutx.models

/**
 *        Actual Goal  Encoded Goal
 * 0    Build Muscles             0
 * 1  Maintain Weight             3
 * 2      Lose Weight             2
 * 3      Gain Weight             1
 */
enum class Goal(val string: String) {
    BUILD_MUSCLE("Build Muscle"),
    GAIN_WEIGHT("Gain Weight"),
    LOSE_WEIGHT("Lose Weight"),
    MAINTAIN_WEIGHT("Maintain Weight");

    override fun toString(): String {
        return this.string
    }

    companion object {
        fun valueOf(goal: String?): Goal? =
            when (goal) {
                "Lose Weight" -> LOSE_WEIGHT
                "Maintain Weight" -> MAINTAIN_WEIGHT
                "Gain Weight" -> GAIN_WEIGHT
                "Build Muscle" -> BUILD_MUSCLE
                "" -> null
                null -> null
                else -> throw IllegalArgumentException("Invalid Goal value: $goal")
            }
    }
}