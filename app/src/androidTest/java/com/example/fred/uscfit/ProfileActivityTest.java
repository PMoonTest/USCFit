package com.example.fred.uscfit;

import android.content.Intent;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ProfileActivityTest {

    @Rule
    public ActivityTestRule<ProfileActivity> mProfileActivity
            = new ActivityTestRule<ProfileActivity>(ProfileActivity.class, false, false);

    private ProfileActivity mActivity = null;


    @Before
    public void setUp() throws Exception {
        Intent intent = new Intent();
        intent.putExtra("email", "siyuanx@usc.edu");
        mActivity = mProfileActivity.launchActivity(intent);
    }

    @Test
    @UiThreadTest
    public void renderAllSportTest() {
        TextView allSportsLabel = (TextView) mActivity.findViewById(R.id.allSportsLabel);
        assertTrue(allSportsLabel.getText().toString().equals("All Sports:"));
        TextView allSportsText = (TextView) mActivity.findViewById(R.id.allSportsText);
        assertTrue(allSportsText.getText().toString().length() != 0);
    }

    @Test
    @UiThreadTest
    public void renderAllActivitiesTest() {
        TextView allActivitiessLabel = (TextView) mActivity.findViewById(R.id.allActivitiesLabel);
        assertTrue(allActivitiessLabel.getText().toString().equals("All Activities:"));
        TextView allActivitiesText = (TextView) mActivity.findViewById(R.id.allActivitiesText);
        assertTrue(allActivitiesText.getText().toString().length() != 0);
    }


    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}