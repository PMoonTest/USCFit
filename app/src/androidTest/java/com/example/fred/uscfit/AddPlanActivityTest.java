package com.example.fred.uscfit;

import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

public class AddPlanActivityTest {

    @Rule
    public ActivityTestRule<AddPlanActivity> mLoginActivity = new ActivityTestRule<AddPlanActivity>(AddPlanActivity.class);

    private AddPlanActivity mActivity = null;

    @Before
    public void setUp() throws Exception {
        mActivity = mLoginActivity.getActivity();

    }

    @After
    public void tearDown() throws Exception {
        mActivity = null; 
    }
}