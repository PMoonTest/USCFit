package com.example.fred.uscfit;

import android.content.Intent;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.widget.EditText;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class AddPlanActivityTest {

    @Rule
    public ActivityTestRule<AddPlanActivity> mLoginActivity1 = new ActivityTestRule<AddPlanActivity>(AddPlanActivity.class, false, false);

//    @Rule
//    public ActivityTestRule<AddPlanActivity> mLoginActivity2 = new ActivityTestRule<AddPlanActivity>(AddPlanActivity.class, false, false);


    private AddPlanActivity mActivity1 = null;
    private EditText planNameInput1 = null;



//    private AddPlanActivity mActivity2 = null;

    @Before
    public void setUp() throws Exception {

        Intent i1 = new Intent();
        i1.putExtra("email", "newUser@usc.edu");
        mActivity1 = mLoginActivity1.launchActivity(i1);

        Intent i2 = new Intent();
        i2.putExtra("email", "siyuanx@usc.edu");
//        mActivity2 = mLoginActivity2.launchActivity(i2);
    }

    @Test
    @UiThreadTest
    public void testEmptySport() {
        //assertNotNull();
    }

    @After
    public void tearDown() throws Exception {
        mActivity1 = null;
    }
}