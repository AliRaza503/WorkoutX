package com.labz.workoutx.models


enum class Goal(val string: String) {
    BUILD_MUSCLE("Build Muscles"),
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
                "Build Muscles" -> BUILD_MUSCLE
                "" -> null
                null -> null
                else -> throw IllegalArgumentException("Invalid Goal value: $goal")
            }
    }
}