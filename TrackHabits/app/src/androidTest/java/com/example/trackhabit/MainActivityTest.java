package com.example.trackhabit;

import static org.junit.Assert.*;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test class for MainActivity. Testing a user creating a login ID and password. Robotium test
 * framework is used
 */
public class MainActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
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
    public void createNewUserSuccess(){
        solo.assertCurrentActivity("Wrong",MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.suUserName), "USERNAME");
        solo.enterText((EditText) solo.getView(R.id.suPassword),"passwordForTest");
        solo.enterText((EditText) solo.getView(R.id.suConPassword),"passwordForTest");
        solo.clickOnButton("Sign Up");

        // assertTrue
        assertTrue(solo.waitForActivity(HabitsActivity.class));
        assertFalse(solo.waitForText("User exists already",1, 1000));
    }

}

