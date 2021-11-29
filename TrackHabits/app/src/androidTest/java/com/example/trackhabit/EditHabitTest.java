package com.example.trackhabit;

import static org.junit.Assert.*;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.jetbrains.annotations.TestOnly;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class EditHabitTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<LogInActivity> rule =
            new ActivityTestRule<>(LogInActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.assertCurrentActivity("Wrong", LogInActivity.class);
        solo.enterText((EditText) solo.getView(R.id.logUserName), "test-login");
        solo.enterText((EditText) solo.getView(R.id.logPassword), "test-password");
        solo.clickOnView(solo.getView(R.id.logIn));
        assertTrue(solo.waitForActivity(HabitsActivity.class, 1000));
        solo.assertCurrentActivity("Wrong", HabitsActivity.class);
        solo.clickOnView(solo.getView(R.id.open_menu_button));
        solo.clickOnView(solo.getView(R.id.add_habit));
        // assertTrue
        assertTrue(solo.waitForDialogToOpen(1000));
        HabitsActivity activity = (HabitsActivity) solo.getCurrentActivity();
        solo.enterText((EditText) solo.getView(R.id.add_habit_name), "test-Habit");
        solo.enterText((EditText) solo.getView(R.id.add_habit_title), "test-Title");
        solo.enterText((EditText) solo.getView(R.id.add_habit_reason), "test-Reason");
        solo.clickOnView(solo.getView(R.id.add_date_button));
        solo.setDatePicker(0, 2021, 1, 1);
        solo.clickOnButton("OK");
        solo.clickOnView(solo.getView(R.id.select_privacy));
        solo.clickOnText("Public");
        solo.clickOnCheckBox(0);
        solo.clickOnCheckBox(1);
        solo.clickOnButton("Ok");
        assertTrue(solo.waitForDialogToClose(1000));
    }

    @Test
    public void editHabit(){
        solo.assertCurrentActivity("Wrong", HabitsActivity.class);
        solo.clickLongOnText("test-Habit");
        solo.clickOnText("Edit habit");
        assertTrue(solo.waitForDialogToOpen(1000));
        solo.clickOnCheckBox(2);
        solo.clickOnCheckBox(3);
        solo.clickOnButton("Confirm");
        assertTrue(solo.waitForDialogToClose(1000));
    }

    /**
     * Gets the Activity
     *
     * @throws Exception
     */

    @After
    public void cleanUp() throws Exception{
        //solo.clickOnButton();
        solo.assertCurrentActivity("Wrong", HabitsActivity.class);
        solo.clickLongOnText("test-Habit");
        solo.clickOnText("Delete habit");
        assertTrue(solo.waitForLogMessage("Data has been deleted successfully!"));
    }
}