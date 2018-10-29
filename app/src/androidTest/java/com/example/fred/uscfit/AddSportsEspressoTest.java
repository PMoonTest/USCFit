package com.example.fred.uscfit;

import android.content.Intent;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddSportsEspressoTest {
    @Rule
    public ActivityTestRule<AddSportActivity> mActivityRule =
            new ActivityTestRule<>(AddSportActivity.class,false,false);
    @Test
    public void test_add_sport_failed(){
//        Intent i = new Intent();
//        i.putExtra("email","siyuanx@usc.edu");
//        mActivityRule.launchActivity(i);
//        onView(withId(R.id.sportNameText)).perform(typeText("Running"));
//        onView(withId(R.id.caloriesText)).perform(typeText("300"));
//        onView(withId(R.id.addSportBtn)).perform(click());
//        //onView(withText("Successfully added sport category1."));
//        onView(withId(2131230770)).check(matches(withText("Oops...")));;
    }
    @Test
    public void test_add_sport_success(){
//        Intent i = new Intent();
//        i.putExtra("email","siyuanx@usc.edu");
//        mActivityRule.launchActivity(i);
//        onView(withId(R.id.sportNameText)).perform(typeText("Running2"));
//        onView(withId(R.id.caloriesText)).perform(typeText("300"));
//        onView(withId(R.id.addSportBtn)).perform(click());
//        //onView(withText("Successfully added sport category1."));
//        onView(withId(2131230770)).check(matches(withText("Yeah!")));;
    }

}
