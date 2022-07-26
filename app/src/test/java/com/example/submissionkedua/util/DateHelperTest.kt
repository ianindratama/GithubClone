package com.example.submissionkedua.util

import org.junit.Assert.assertEquals
import org.junit.Test
class DateHelperTest {

    @Test
    fun testGetCurrentDate() {

        assertEquals("2022/03/29", DateHelper.getCurrentDateTest())

    }
}