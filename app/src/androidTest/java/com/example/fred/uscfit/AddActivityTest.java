package com.example.fred.uscfit;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.annotation.UiThreadTest;
import androidx.test.rule.ActivityTestRule;

import static org.junit.Assert.assertTrue;

public class AddActivityTest {

    @Rule
    public ActivityTestRule<AddActivity> mLoginActivity1 = new ActivityTestRule<AddActivity>(AddActivity.class, false, false);

    private AddActivity mActivity = null;
    private EditText email = null;
    private Button uploadActivity = null;
    private Button cancelActivity = null;
    private Button showTimePicker = null;
    private Button showTimePickerEnd = null;
    private Button showDatePicker = null;

    @Before
    public void setUp() throws Exception {
        Intent i = new Intent();
        i.putExtra("email", "siyuanx@usc.edu");
        mActivity = mLoginActivity1.launchActivity(i);

        email = mActivity.findViewById(R.id.email);
        uploadActivity = (Button) mActivity.findViewById(R.id.upload_activity);
        cancelActivity = (Button) mActivity.findViewById(R.id.cancel_activity);
        showTimePicker = (Button) mActivity.findViewById(R.id.show_time_picker);
        showTimePickerEnd = (Button) mActivity.findViewById(R.id.show_time_picker_end);
        showDatePicker = (Button) mActivity.findViewById(R.id.show_date_picker);
    }

    @Test
    @UiThreadTest
    public void completeTest() {
        assertTrue(showTimePicker.callOnClick());
        assertTrue(showTimePickerEnd.callOnClick());
        assertTrue(showDatePicker.callOnClick());
        assertTrue(uploadActivity.callOnClick());
    }

    @Test
    @UiThreadTest
    public void submitTest() {
        assertTrue(uploadActivity.callOnClick());
    }

    @Test
    @UiThreadTest
    public void cancelTest() {
        assertTrue(cancelActivity.callOnClick());
    }

    @After
    public void tearDown() throws Exception {

    }
}