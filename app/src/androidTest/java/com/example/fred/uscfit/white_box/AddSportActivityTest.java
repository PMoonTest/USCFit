package com.example.fred.uscfit.white_box;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import com.example.fred.uscfit.AddSportActivity;
import com.example.fred.uscfit.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.annotation.UiThreadTest;
import androidx.test.rule.ActivityTestRule;

import static org.junit.Assert.assertTrue;

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
        mAddSportBtn = (Button) mActivity.findViewById(R.id.addSportBtn);
        mSportNameInput = (EditText) mActivity.findViewById(R.id.sportNameText);
        mCaloriesInput = (EditText) mActivity.findViewById(R.id.caloriesText);
    }

    @Test
    @UiThreadTest
    public void emptySportNameTest() {
        // perform click on button without sport name input
        assertTrue(mSportNameInput.getText().toString().length() == 0);
        mAddSportBtn.performClick();
    }

    @Test
    @UiThreadTest
    public void emptyCaloriesTest() {
        // perform click on button with sport name but without calories input
        mSportNameInput.requestFocus();
        mSportNameInput.setText("random");
        assertTrue(mSportNameInput.getText().toString().equals("random"));
        mAddSportBtn.performClick();
    }

    @Test
    @UiThreadTest
    public void correctInput() {
        // perform click on button with sport name (not duplicate) and calories input
        mSportNameInput.requestFocus();
        mSportNameInput.setText("random");
        mCaloriesInput.requestFocus();
        mCaloriesInput.setText("100");
        assertTrue(mSportNameInput.getText().toString().equals("random"));
        assertTrue(mCaloriesInput.getText().toString().equals("100"));
//        mAddSportBtn.performClick();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}