package com.example.submissionkedua.util

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.submissionkedua.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class SettingsActivityTest{

    @Before
    fun setup(){
        ActivityScenario.launch(SettingsActivity::class.java)
    }

    @Test
    fun assertGetCircumference() {

        onView(withId(R.id.switch_theme)).check(matches(isDisplayed()))
        onView(withId(R.id.about_developer)).check(matches(isDisplayed()))

        onView(withId(R.id.switch_theme)).perform(click())
        onView(withId(R.id.about_developer)).perform(click())

    }

}