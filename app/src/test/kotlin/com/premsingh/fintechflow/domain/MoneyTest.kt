package com.premsingh.fintechflow.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MoneyTest {
    @Test fun parsesWholeAndFractionalAmountsToMinorUnits() {
        assertEquals(500L, parseAmountMinor("5"))
        assertEquals(1234L, parseAmountMinor("12.34"))
        assertEquals(5L, parseAmountMinor("0.05"))
    }

    @Test fun rejectsInvalidZeroAndOverPreciseAmounts() {
        listOf("", "0", "-1", "1.999", "1,25", "one", ".50").forEach {
            assertNull("Expected $it to be rejected", parseAmountMinor(it))
        }
    }
}
