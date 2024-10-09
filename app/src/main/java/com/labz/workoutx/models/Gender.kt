package com.labz.workoutx.models

enum class Gender(val gender: String) {
    MALE("Male"),
    FEMALE("Female"),
    PREFER_NOT_TO_SAY("Prefer not to say");

    override fun toString(): String {
        return this.gender
    }

    companion object {
        fun valueOf(gender: String): Gender = when (gender) {
            "Male" -> MALE
            "Female" -> FEMALE
            "Prefer not to say" -> PREFER_NOT_TO_SAY
            else -> throw IllegalArgumentException("Invalid Gender value: $gender")
        }
    }
}
