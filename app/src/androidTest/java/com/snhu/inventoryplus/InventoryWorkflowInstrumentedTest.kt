package com.snhu.inventoryplus

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InventoryWorkflowInstrumentedTest {

    private var activity: MainActivity? = null

    @Before
    fun launchFreshApp() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val context = instrumentation.targetContext
        context.deleteDatabase("inventory_plus.db")

        val intent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        activity = instrumentation.startActivitySync(intent) as MainActivity
        instrumentation.waitForIdleSync()
    }

    @After
    fun finishActivity() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        instrumentation.runOnMainSync {
            activity?.finish()
        }
        activity = null
    }

    @Test
    fun userCanRegisterAndManageInventoryItems() {
        onView(withId(R.id.editTextUsername))
            .perform(replaceText("newuser"))
        onView(withId(R.id.editTextPassword))
            .perform(replaceText("password123"))
        onView(withId(R.id.buttonCreateAccount)).perform(click())

        onView(withId(R.id.textInventorySubtitle)).check(matches(isDisplayed()))
        onView(withText(R.string.inventory_empty)).check(matches(isDisplayed()))

        onView(withId(R.id.buttonOpenAddItem)).perform(click())
        onView(withId(R.id.editTextItemName))
            .perform(replaceText("Notebook"))
        onView(withId(R.id.editTextQuantity))
            .perform(replaceText("15"))
        onView(withId(R.id.editTextLowAlert))
            .perform(replaceText("3"))
        onView(withId(R.id.buttonSaveItem)).perform(click())

        onView(withText("Notebook")).check(matches(isDisplayed()))
        onView(withText("15")).check(matches(isDisplayed()))
        onView(withText("3")).check(matches(isDisplayed()))

        onView(withText("Notebook")).perform(click())
        onView(withId(R.id.editTextQuantity))
            .perform(replaceText("20"))
        onView(withId(R.id.buttonSaveChanges)).perform(click())

        onView(withText("Notebook")).check(matches(isDisplayed()))
        onView(withText("20")).check(matches(isDisplayed()))

        onView(withText(R.string.delete)).perform(click())
        onView(withText(R.string.inventory_empty)).check(matches(isDisplayed()))
    }
}
