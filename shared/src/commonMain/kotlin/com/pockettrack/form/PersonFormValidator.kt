package com.pockettrack.form

enum class NameError { TooShort }

enum class AgeError { Invalid, TooHigh }

object PersonFormValidator {
    const val MIN_NAME_LENGTH = 3
    const val MAX_AGE_EXCLUSIVE = 100

    fun validateName(name: String): NameError? = if (name.trim().length < MIN_NAME_LENGTH) NameError.TooShort else null

    fun validateAge(age: String): AgeError? {
        val value = age.trim().toIntOrNull() ?: return AgeError.Invalid
        return when {
            value < 0 -> AgeError.Invalid
            value >= MAX_AGE_EXCLUSIVE -> AgeError.TooHigh
            else -> null
        }
    }
}
