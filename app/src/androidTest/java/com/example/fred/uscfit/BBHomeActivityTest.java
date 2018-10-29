package com.example.fred.uscfit;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


public class BBHomeActivityTest {
    private String mEmail;

    @Rule
    public ActivityTestRule<HomeActivity> mActivityRule
            = new ActivityTestRule<>(HomeActivity.class, false, false);
    private IntentsTestRule<HomeActivity> mHomeActivityRule =
            new IntentsTestRule<>(HomeActivity.class, false, false);
//    private IntentsTestRule<AddSportActivity> mAddSportRule =
//            new IntentsTestRule<>(AddSportActivity.class, false, false);
//    private IntentsTestRule<AddPlanActivity> mAddPlanRule =
//            new IntentsTestRule<>(AddPlanActivity.class, false, false);
//    private IntentsTestRule<ProgressActivity> mProgessRule =
//            new IntentsTestRule<>(ProgressActivity.class, false, false);

    @Before
    public void initValidString() {
        // Specify a valid string.
        mEmail = "siyuanx@usc.edu";
    }

    // check if user can be directed to correct activities after clicking on the module
    @Test
    public void addActivityIntent_HomeActivity(){
        Intent input = new Intent();
        input.putExtra("email", mEmail);
        mHomeActivityRule.launchActivity(input);


        Intent resultData = new Intent();
        resultData.putExtra("email", mEmail);

        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);


        onView(withId(R.id.addActivityLayout)).perform(click());

        intended(toPackage("com.example.fred.uscfit"));

        mHomeActivityRule.finishActivity();
    }

    @Test
    public void addSportIntent_HomeActivity(){
        Intent input = new Intent();
        input.putExtra("email", mEmail);
        mHomeActivityRule.launchActivity(input);


        Intent resultData = new Intent();
        resultData.putExtra("email", mEmail);

        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);


        onView(withId(R.id.addActivityLayout)).perform(click());

        intended(toPackage("com.example.fred.uscfit"));

        mHomeActivityRule.finishActivity();
    }
    @Test
    public void addPlanIntent_HomeActivity(){
        Intent input = new Intent();
        input.putExtra("email", mEmail);
        mHomeActivityRule.launchActivity(input);


        Intent resultData = new Intent();
        resultData.putExtra("email", mEmail);

        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);


        onView(withId(R.id.addActivityLayout)).perform(click());

        intended(toPackage("com.example.fred.uscfit"));

        mHomeActivityRule.finishActivity();
    }
    @Test
    public void progressIntent_HomeActivity(){
        Intent input = new Intent();
        input.putExtra("email", mEmail);
        mHomeActivityRule.launchActivity(input);


        Intent resultData = new Intent();
        resultData.putExtra("email", mEmail);

        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);


        onView(withId(R.id.addActivityLayout)).perform(click());

        intended(toPackage("com.example.fred.uscfit"));

        mHomeActivityRule.finishActivity();
    }
    @Test
    public void footstepText_HomeActivity() {
        Intent input = new Intent();
        input.putExtra("email", mEmail);
        mActivityRule.launchActivity(input);


        // Check that the footstep number is shown after button is clicked
        onView(withId(R.id.footstepBtn)).perform(click());

        onView(withId(R.id.footstepBtn))
                .check(matches(hasFootstepNumber()));
        mActivityRule.finishActivity();
    }

    // customized matcher to check footstep number
    private Matcher<View> hasFootstepNumber(){
        return new TypeSafeMatcher<View>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("Has EditText/TextView the value:  ");
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof TextView) && !(view instanceof EditText)) {
                    return false;
                }
                if (view != null) {
                    String text;
                    if (view instanceof TextView) {
                        text = ((TextView) view).getText().toString();
                    } else {
                        text = ((EditText) view).getText().toString();
                    }

                    return (isInteger(text));
                }
                return false;
            }
        };

    }

    // helper function to check if string can be converted into an integer
    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        }
        return true;
    }

}
