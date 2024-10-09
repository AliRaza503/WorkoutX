package com.labz.workoutx.models

enum class Goal(val string: String) {
    LOSE_WEIGHT("Lose Weight"),
    MAINTAIN_WEIGHT("Maintain Weight"),
    GAIN_WEIGHT("Gain Weight"),
    BUILD_MUSCLE("Build Muscle");

    override fun toString(): String {
        return this.string
    }

    companion object {
        fun valueOf(goal: String): Goal =
            when (goal) {
                "Lose Weight" -> LOSE_WEIGHT
                "Maintain Weight" -> MAINTAIN_WEIGHT
                "Gain Weight" -> GAIN_WEIGHT
                "Build Muscle" -> BUILD_MUSCLE
                else -> throw IllegalArgumentException("Invalid Goal value: $goal")
            }
    }
}