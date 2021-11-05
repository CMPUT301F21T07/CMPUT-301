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
    private Solo solo, soloLogin;

    @Rule
    public ActivityTestRule<HabitsActivity> rule =
            new ActivityTestRule<>(HabitsActivity.class, true, true);
    public ActivityTestRule<LogInActivity> ruleLogin =
            new ActivityTestRule<>(LogInActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        soloLogin = new Solo(InstrumentationRegistry.getInstrumentation(), ruleLogin.getActivity());
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
        soloLogin.assertCurrentActivity("Wrong", LogInActivity.class);
        soloLogin.enterText((EditText) solo.getView(R.id.logUserName), "test-login");
        soloLogin.enterText((EditText) solo.getView(R.id.logPassword),"test-password2");
        soloLogin.clickOnButton("Log In");
        assertTrue(soloLogin.waitForActivity(HabitsActivity.class));

        solo.assertCurrentActivity("Wrong", HabitsActivity.class);
        solo.clickOnButton(R.id.open_menu_button);
        solo.clickOnButton(R.id.add_habit);
        // assertTrue
        assertTrue(solo.waitForDialogToOpen());
    }
}
