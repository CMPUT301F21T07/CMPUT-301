package com.example.trackhabit;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class HabitActivityTest {
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

        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        solo.assertCurrentActivity("Wrong", LogInActivity.class);
        solo.enterText((EditText) solo.getView(R.id.logUserName), "test-login");
        solo.enterText((EditText) solo.getView(R.id.logPassword),"test-password");
        solo.clickOnView(solo.getView(R.id.logIn));
        assertTrue(solo.waitForActivity(HabitsActivity.class,1000));

        HabitsActivity activity = (HabitsActivity) solo.getCurrentActivity();
        final ListView listView = activity.habitListView;
    }
    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void openAddHabitDialog(){
        solo.assertCurrentActivity("Wrong", HabitsActivity.class);
        solo.clickOnView(solo.getView(R.id.open_menu_button));
        solo.clickOnView(solo.getView(R.id.add_habit));
        // assertTrue
        assertTrue(solo.waitForDialogToOpen(1000));
    }
    @Test
    public void openAllHabitEvents(){
        solo.assertCurrentActivity("Wrong", HabitsActivity.class);
        solo.clickOnView(solo.getView(R.id.open_menu_button));
        solo.clickOnView(solo.getView(R.id.view_habit_events));
        //assertTrue
        assertTrue(solo.waitForActivity(CalendarActivity.class, 1000));
    }
    @Test
    public void ViewFriends(){
        solo.assertCurrentActivity("Wrong", HabitsActivity.class);
        solo.clickOnView(solo.getView(R.id.open_menu_button));
        solo.clickOnView(solo.getView(R.id.view_friends));
        //assertTrue
        assertTrue(solo.waitForActivity(ViewFriends.class, 1000));
    }
    @Test
    public void LogOut(){
        solo.assertCurrentActivity("Wrong", HabitsActivity.class);
        solo.clickOnView(solo.getView(R.id.open_menu_button));
        solo.clickOnView(solo.getView(R.id.log_out_button));
        //assertTrue
        assertTrue(solo.waitForActivity(LogInActivity.class, 1000));
    }
}
