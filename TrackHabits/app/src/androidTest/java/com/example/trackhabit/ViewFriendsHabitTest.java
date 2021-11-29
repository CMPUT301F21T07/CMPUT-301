package com.example.trackhabit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ViewFriendsHabitTest {
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
        solo.assertCurrentActivity("Wrong", ViewFriend.class);
        solo.clickOnView(solo.getView(R.id.open_friend_menu_button));
        solo.clickOnView(solo.getView(R.id.add_friend));
        assertTrue(solo.waitForActivity(SearchFriend.class, 1000));
        solo.enterText((EditText) solo.getView(R.id.search_friend_edit),"Sagan");
        solo.clickOnView(solo.getView(R.id.add_friend_button));
        assertFalse(solo.waitForLogMessage("Friend Request Not Sent Successfully!", 1000));
        solo.clickOnView(solo.getView(R.id.open_friend_menu_button));
        solo.clickOnView(solo.getView(R.id.go_back));
        solo.assertCurrentActivity("Wrong", HabitsActivity.class);
        solo.clickOnView(solo.getView(R.id.open_menu_button));
        solo.clickOnView(solo.getView(R.id.log_out_button));
        solo.assertCurrentActivity("Wrong", LogInActivity.class);
        LogInActivity activity1 = (LogInActivity) solo.getCurrentActivity();
        solo.enterText((EditText) solo.getView(R.id.logUserName), "Sagan");
        solo.enterText((EditText) solo.getView(R.id.logPassword),"Cake");
        solo.clickOnView(solo.getView(R.id.logIn));
        solo.clickOnView(solo.getView(R.id.open_menu_button));
        solo.clickOnView(solo.getView(R.id.view_friends));
        solo.clickOnView(solo.getView(R.id.open_friend_menu_button));
        solo.clickOnView(solo.getView(R.id.view_requests));
        solo.clickLongOnText("test-login");
        solo.clickOnText("Accept Friend Request");
        solo.assertCurrentActivity("Wrong", FriendRequest.class);
        solo.clickOnView(solo.getView(R.id.go_back_req));
    }

    @Test
    public void AcceptRequest(){
        solo.clickLongOnText("test-login");
        solo.clickOnText("Show habits");
        solo.assertCurrentActivity("Wrong", ViewFriendHabit.class);
    }

    @After
    public void cleanUp() throws Exception{
        solo.clickOnView(solo.getView(R.id.open_menu_button));
        solo.clickOnView(solo.getView(R.id.friend_go_back));
        solo.clickLongOnText("test-login");
        solo.clickOnText("Remove friend");
    }

}
