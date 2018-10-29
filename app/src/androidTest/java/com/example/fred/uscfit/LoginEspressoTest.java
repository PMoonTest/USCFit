package com.example.fred.uscfit;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginEspressoTest {


    @Rule
    public IntentsTestRule<LoginActivity> intentsTestRule =
        new IntentsTestRule<>(LoginActivity.class);

    @Test
    public void test_login_failed() {
//        onView(withId(R.id.email)).perform(typeText("siyuanx@usc.edu"));
//        onView(withId(R.id.password)).perform(typeText("23444"));
//        onView(withId(R.id.email_sign_in_button)).perform(scrollTo(),click());
//        onView(withId(R.id.password)).check(matches(hasErrorText("This password is incorrect")));
    }

    @Test
    public void test_login_success(){
//        onView(withId(R.id.email)).perform(typeText("siyuanx@usc.edu"));
//        onView(withId(R.id.password)).perform(typeText("13444"));
//        onView(withId(R.id.email_sign_in_button)).perform(scrollTo(),click());
//        intended(toPackage("com.example.fred.uscfit"));
    }
    @Test
    public void test_email_exist(){
        //onView(withId(R.id.email_sign_in_button)).perform(scrollTo(),click());
        //onView(withId(R.id.email)).check(matches(hasErrorText("This field is required")));
    }
    @Test
    public void test_password_exist(){
//        onView(withId(R.id.email)).perform(typeText("siyuanx@usc.edu"));
//        onView(withId(R.id.email_sign_in_button)).perform(scrollTo(),click());
//        onView(withId(R.id.password)).check(matches(hasErrorText("This password is too short")));
    }
    @Test
    public void test_email_format(){
//        onView(withId(R.id.email)).perform(typeText("siyuanx"));
//        onView(withId(R.id.email_sign_in_button)).perform(scrollTo(),click());
//        onView(withId(R.id.email)).check(matches(hasErrorText("This email address is invalid")));
    }
    @Test
    public void test_onlyPasswordExist(){
//        onView(withId(R.id.password)).perform(typeText("123456"));
//        onView(withId(R.id.email_sign_in_button)).perform(scrollTo(),click());
//        onView(withId(R.id.email)).check(matches(hasErrorText("This field is required")));
    }
}
