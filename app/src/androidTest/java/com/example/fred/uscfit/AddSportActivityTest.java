package com.example.fred.uscfit;

import android.content.Intent;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.widget.Button;
import android.widget.EditText;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class AddSportActivityTest {

    @Rule
    public ActivityTestRule<AddSportActivity> mAddSportActivity
            = new ActivityTestRule<AddSportActivity>(AddSportActivity.class, false, false);

    private AddSportActivity mActivity = null;
    private Button mAddSportBtn = null;
    private EditText mSportNameInput = null;
    private EditText mCaloriesInput = null;
    private String mEmail = null;

    @Before
    public void setUp() throws Exception {
        mEmail = "siyuanx@usc.edu";
        Intent intent = new Intent();
        intent.putExtra("email", mEmail);
        mActivity = mAddSportActivity.launchActivity(intent);
        mAddSportBtn = mActivity.findViewById(R.id.addSportBtn);
        mSportNameInput = mActivity.findViewById(R.id.sportNameText);
        mCaloriesInput = mActivity.findViewById(R.id.caloriesText);
    }

    @Test
    @UiThreadTest
    public void emptySportNameTest() {
        // perform click on button without sport name input

//        mAddSportBtn.performClick();
//
//        mSportNameInput.requestFocus();
//        mSportNameInput.setText("random");
//        mAddSportBtn.performClick();

        mSportNameInput.requestFocus();
        mSportNameInput.setText("random");
        mCaloriesInput.requestFocus();
        mCaloriesInput.setText("100");
        mAddSportBtn.performClick();
    }

//    @Test
//    @UiThreadTest
//    public void emptyCaloriesTest() {
//        initialization();
//        // perform click on button with sport name but without calories input
//        mSportNameInput.requestFocus();
//        mSportNameInput.setText("random");
//        mAddSportBtn.performClick();
//    }
//
//    @Test
//    @UiThreadTest
//    public void correctInput() {
//        initialization();
//        // perform click on button with sport name (not duplicate) and calories input
//        mSportNameInput.requestFocus();
//        mSportNameInput.setText("random");
//        mCaloriesInput.requestFocus();
//        mCaloriesInput.setText("100");
//        mAddSportBtn.performClick();
//    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}