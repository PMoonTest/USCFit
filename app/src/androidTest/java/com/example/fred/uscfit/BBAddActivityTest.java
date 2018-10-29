package com.example.fred.uscfit;
import android.content.Intent;

import org.junit.*;
import org.junit.runner.RunWith;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;


@RunWith(AndroidJUnit4.class)

public class BBAddActivityTest {

    private String mEmail;

    @Rule
    public ActivityTestRule<AddActivity> mActivityRule
            = new ActivityTestRule<>(AddActivity.class, false, false);

    @Before
    public void initValidString() {
        mEmail = "siyuanx@usc.edu";
    }


    // Check that the user email is dispalyed properly
    @Test
    public void checkEmail_AddActivity(){
        Intent input = new Intent();
        input.putExtra("email", mEmail);
        mActivityRule.launchActivity(input);
        onView(withId(R.id.email))
                .check(matches(withText(mEmail)));
        mActivityRule.finishActivity();
    }


    // test if a user can toggle the timepicker
    @Test
    public void openTimepicker_AddActivity() {
        Intent input = new Intent();
        input.putExtra("email", mEmail);
        mActivityRule.launchActivity(input);

        onView(withId(R.id.show_time_picker))
                .perform(click());
        onView(withId(R.id.time_picker)).check(matches(isDisplayed()));
        onView(withId(R.id.timer_confirm_btn))
                .perform(scrollTo(), click());
        onView(withId(R.id.time_picker)).check(matches(not(isDisplayed())));
        mActivityRule.finishActivity();
    }

    // test if a user can toggle the calendar picker
    @Test
    public void openDatepicker_AddActivity(){
        Intent input = new Intent();
        input.putExtra("email", mEmail);
        mActivityRule.launchActivity(input);

        // Check that the user can open and close time picker correctly
        onView(withId(R.id.show_date_picker))
                .perform(click());
        onView(withId(R.id.date_picker)).check(matches(isDisplayed()));
        onView(withId(R.id.date_confirm_btn))
                .perform(scrollTo(),click());
        onView(withId(R.id.date_picker)).check(matches(not(isDisplayed())));
        mActivityRule.finishActivity();
    }

}
