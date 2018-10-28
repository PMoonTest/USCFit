package com.example.fred.uscfit;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mLoginActivity = new ActivityTestRule<LoginActivity>(LoginActivity.class);

    private LoginActivity mActivity = null;

    @Before
    public void setUp() throws Exception {
        mActivity = mLoginActivity.getActivity();
    }

    @Test
    public void test() {
        View view = mActivity.findViewById(R.id.email);
        assertNotNull(view);
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}