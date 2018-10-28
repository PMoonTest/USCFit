package com.example.fred.uscfit;

import android.content.Intent;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.widget.Button;
import android.widget.LinearLayout;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class HomeActivityTest {

    @Rule
    public ActivityTestRule<HomeActivity> mLoginActivity = new ActivityTestRule<HomeActivity>(HomeActivity.class, false, false);

    private HomeActivity mActivity = null;
    private Button footstepBtn = null;
    private LinearLayout addActivityLayout = null;
    private LinearLayout addSportLayout = null;
    private LinearLayout addPlanLayout = null;
    private LinearLayout progressLayout = null;
    private Button profileBtn = null;

    @Before
    public void setUp() throws Exception {
        Intent i1 = new Intent();
        i1.putExtra("email", "siyuanx@usc.edu");
        mActivity = mLoginActivity.launchActivity(i1);
        footstepBtn = (Button) mActivity.findViewById(R.id.footstepBtn);
        addActivityLayout = (LinearLayout) mActivity.findViewById(R.id.addActivityLayout);
        addSportLayout = (LinearLayout) mActivity.findViewById(R.id.addSportLayout);
        addPlanLayout = (LinearLayout) mActivity.findViewById(R.id.addPlanLayout);
        progressLayout = (LinearLayout) mActivity.findViewById(R.id.progressLayout);
        profileBtn = (Button) mActivity.findViewById(R.id.profile_btn);
    }

    @Test
    @UiThreadTest
    public void footstepTest() {
        // activates footstep
        assertTrue(footstepBtn.callOnClick());
        // deactivates footstep
        assertTrue(footstepBtn.callOnClick());
    }

    @Test
    @UiThreadTest
    public void addActivityTest() {
        assertTrue(addActivityLayout.callOnClick());
    }

    @Test
    @UiThreadTest
    public void addPlanLayoutTest() {
        assertTrue(addPlanLayout.callOnClick());
    }

    @Test
    @UiThreadTest
    public void addSportLayoutTest() {
        assertTrue(addSportLayout.callOnClick());
    }

    @Test
    @UiThreadTest
    public void progressTest() {
        assertTrue(progressLayout.callOnClick());
    }

    @Test
    @UiThreadTest
    public void profileTest() {
        assertTrue(profileBtn.callOnClick());
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}