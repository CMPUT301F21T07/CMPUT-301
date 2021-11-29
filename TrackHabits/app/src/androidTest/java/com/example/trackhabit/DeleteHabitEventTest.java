package com.example.trackhabit;

import static org.junit.Assert.*;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DeleteHabitEventTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<LogInActivity> rule =
            new ActivityTestRule<>(LogInActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        solo.assertCurrentActivity("Wrong", LogInActivity.class);
        solo.enterText((EditText) solo.getView(R.id.logUserName), "test-login");
        solo.enterText((EditText) solo.getView(R.id.logPassword),"test-password");
        solo.clickOnView(solo.getView(R.id.logIn));
        assertTrue(solo.waitForActivity(HabitsActivity.class,1000));
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
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        String[] arrOfDate = date.split("/");
        solo.setDatePicker(0, Integer.parseInt(arrOfDate[2]), Integer.parseInt(arrOfDate[1])-1, Integer.parseInt(arrOfDate[0]));
        solo.clickOnButton("OK");
        solo.clickOnView(solo.getView(R.id.select_privacy));
        solo.clickOnText("Public");
        solo.clickOnCheckBox(0);
        solo.clickOnCheckBox(1);
        solo.clickOnCheckBox(2);
        solo.clickOnCheckBox(3);
        solo.clickOnCheckBox(4);
        solo.clickOnCheckBox(5);
        solo.clickOnCheckBox(6);
        solo.clickOnButton("Ok");
        assertTrue(solo.waitForDialogToClose(1000));
        solo.clickOnView(solo.getView(R.id.open_menu_button));
        solo.clickLongOnText("test-Habit");
        solo.clickOnText("View habit");
        solo.waitForDialogToOpen(1000);
        solo.clickOnButton("Add Habit Event");
        solo.waitForDialogToOpen(1000);
        solo.enterText((EditText) solo.getView(R.id.comment_editText), "test-habit-event");
        solo.clickOnButton("Select Image");
        assertTrue(solo.waitForActivity(TakePictureActivity.class,1000));
        solo.clickOnButton("Back");
        solo.clickOnButton("Ok");
        assertTrue(solo.waitForLogMessage("Habit Event Successfully Added!"));
    }
    /**
     * Gets the Activity
     * @throws Exception
     */

    @Test
    public void deleteHabitEvent(){
        //solo.clickOnButton();
        //solo.clickOnToggleButton("All Habits");
        solo.clickOnView(solo.getView(R.id.open_menu_button));
        solo.clickOnView(solo.getView(R.id.view_habit_events));
        //assertTrue(solo.waitForActivity(ViewEvent.class,1000));
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        String[] arrOfDate = date.split("/");
        //solo.clickOnText(arrOfDate[0]);
        CalendarView vc = (CalendarView) solo.getView(R.id.calendarView);
        //View child = vc.getChildAt(29);
        //solo.clickOnView(child);
        //solo.setDatePicker((DatePicker)vc, Integer.parseInt(arrOfDate[2]), Integer.parseInt(arrOfDate[1])-1, Integer.parseInt(arrOfDate[0]));
        //long time= System.currentTimeMillis();
        //vc.setDate(time);
        //assertTrue(solo.waitForActivity(ViewEvent.class,1000));
        //solo.clickOnText("test-Habit");
        //assertTrue(solo.waitForActivity(ViewSingleEvent.class,1000));
        //solo.clickOnButton("Delete");
        //assertTrue(solo.waitForActivity(ViewEvent.class,1000));
        //solo.clickOnButton("Back");
        assertTrue(solo.waitForActivity(CalendarActivity.class, 1000));
        solo.clickOnButton("Back");

    }

    @After
    public void cleanUp() throws Exception{
        solo.assertCurrentActivity("Wrong", HabitsActivity.class);
        solo.clickLongOnText("test-Habit");
        solo.clickOnText("Delete habit");
        assertTrue(solo.waitForLogMessage("Data has been deleted successfully!"));
    }
}
