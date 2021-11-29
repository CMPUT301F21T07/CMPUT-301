package com.example.trackhabit;

import static org.junit.Assert.*;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ViewFriendsTest {
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
        solo.assertCurrentActivity("Wrong", HabitsActivity.class);
        solo.clickOnView(solo.getView(R.id.open_menu_button));
        solo.clickOnView(solo.getView(R.id.view_friends));
        //assertTrue
        assertTrue(solo.waitForActivity(ViewFriend.class, 1000));
        ViewFriend activity1 = (ViewFriend) solo.getCurrentActivity();
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
    public void addFriendRequest(){
        solo.clickOnView(solo.getView(R.id.open_friend_menu_button));
        solo.clickOnView(solo.getView(R.id.add_friend));
        assertTrue(solo.waitForActivity(SearchFriend.class, 1000));
        solo.enterText((EditText) solo.getView(R.id.search_friend_edit),"Sagan");
        solo.clickOnView(solo.getView(R.id.add_friend_button));
        assertFalse(solo.waitForLogMessage("Friend Request Not Sent Successfully!", 1000));
    }
}