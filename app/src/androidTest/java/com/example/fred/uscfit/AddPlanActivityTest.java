package com.example.fred.uscfit;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class AddPlanActivityTest {

    @Rule
    public ActivityTestRule<AddPlanActivity> mLoginActivity1 = new ActivityTestRule<AddPlanActivity>(AddPlanActivity.class, false, false);

    private AddPlanActivity mActivity1 = null;
    private EditText planNameInput1 = null;
    private FloatingActionButton addActivityBtn = null;
    private Button submitBtn = null;

    @Before
    public void setUp() throws Exception {

        Intent i1 = new Intent();
        i1.putExtra("email", "siyuanx@usc.edu");
        mActivity1 = mLoginActivity1.launchActivity(i1);
        planNameInput1 = (EditText) mActivity1.findViewById(R.id.planNameInput);
        addActivityBtn = (FloatingActionButton) mActivity1.findViewById(R.id.addActivityBtn);
        submitBtn = (Button) mActivity1.findViewById(R.id.submitBtn);

    }


    @Test
    @UiThreadTest
    public void submitCompleteTest() {
        planNameInput1.requestFocus();
        planNameInput1.setText("My Test Plan");

        addActivityBtn.callOnClick();

        submitBtn.callOnClick();
    }

    @Test
    @UiThreadTest
    public void invalidActivityTest() {
        planNameInput1.requestFocus();
        planNameInput1.setText("My Test Plan");

        addActivityBtn.callOnClick();


        submitBtn.callOnClick();
    }

    @Test
    @UiThreadTest
    // submits an empty form
    public void invalidSubmitTest() {
        submitBtn.callOnClick();
    }

    @Test
    @UiThreadTest
    public void addActivityTest() {
        addActivityBtn.callOnClick();
    }

    @Test
    @UiThreadTest
    // When the user clicks on back button
    public void clickBackTest() {
        KeyEvent kdown = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
        mActivity1.dispatchKeyEvent(kdown);
    }


    @After
    public void tearDown() throws Exception {
        mActivity1 = null;
    }
}