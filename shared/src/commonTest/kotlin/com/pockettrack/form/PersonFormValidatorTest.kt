package com.pockettrack.form

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PersonFormValidatorTest {
    @Test
    fun nameShorterThanThreeCharsIsInvalid() {
        assertEquals(NameError.TooShort, PersonFormValidator.validateName(""))
        assertEquals(NameError.TooShort, PersonFormValidator.validateName("Al"))
        assertEquals(NameError.TooShort, PersonFormValidator.validateName("  Al  "))
    }

    @Test
    fun nameWithThreeOrMoreCharsIsValid() {
        assertNull(PersonFormValidator.validateName("Ana"))
        assertNull(PersonFormValidator.validateName("Marcio"))
    }

    @Test
    fun nonNumericOrNegativeAgeIsInvalid() {
        assertEquals(AgeError.Invalid, PersonFormValidator.validateAge(""))
        assertEquals(AgeError.Invalid, PersonFormValidator.validateAge("abc"))
        assertEquals(AgeError.Invalid, PersonFormValidator.validateAge("-1"))
    }

    @Test
    fun ageOfOneHundredOrMoreIsTooHigh() {
        assertEquals(AgeError.TooHigh, PersonFormValidator.validateAge("100"))
        assertEquals(AgeError.TooHigh, PersonFormValidator.validateAge("150"))
    }

    @Test
    fun ageBelowOneHundredIsValid() {
        assertNull(PersonFormValidator.validateAge("0"))
        assertNull(PersonFormValidator.validateAge("99"))
    }
}
