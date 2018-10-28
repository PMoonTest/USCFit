package com.example.fred.uscfit;

import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mLoginActivity = new ActivityTestRule<LoginActivity>(LoginActivity.class);

    private LoginActivity mActivity = null;
    private AutoCompleteTextView username = null;
    private EditText password = null;
    private Button loginButton = null;

    @Before
    public void setUp() throws Exception {
        mActivity = mLoginActivity.getActivity();
        username = (AutoCompleteTextView) mActivity.findViewById(R.id.email);
        password = (EditText) mActivity.findViewById(R.id.password);
        loginButton = (Button) mActivity.findViewById(R.id.email_sign_in_button);
    }

    @Test
    @UiThreadTest
    public void testCorrectPassword() {
        username.requestFocus();
        username.setText("siyuanx@usc.edu");
        password.requestFocus();
        password.setText("13444");
        loginButton.callOnClick();
        assertTrue(mActivity.loginSuccessful());
    }

    @Test
    @UiThreadTest
    public void testIncorrectPassword() {
        username.requestFocus();
        username.setText("siyuanx@usc.edu");
        password.requestFocus();
        password.setText("abcderf");
        loginButton.callOnClick();
        assertTrue(mActivity.loginSuccessful());
    }

    @Test
    @UiThreadTest
    public void testEmptyPassword() {
        username.requestFocus();
        username.setText("siyuanx@usc.edu");
        password.requestFocus();
        password.setText("");
        loginButton.callOnClick();
        assertFalse(mActivity.loginSuccessful());
    }

    @Test
    @UiThreadTest
    public void testEmptyUsername() {
        username.requestFocus();
        username.setText("");
        password.requestFocus();
        password.setText("13444");
        loginButton.callOnClick();
        assertFalse(mActivity.loginSuccessful());
    }

    @Test
    @UiThreadTest
    public void testInvalidEmail() {
        username.requestFocus();
        username.setText("siyuanxusc");
        password.requestFocus();
        password.setText("13444");
        loginButton.callOnClick();
        assertFalse(mActivity.loginSuccessful());
    }

    @Test
    @UiThreadTest
    // for password length < 4
    public void testInvalidPassword() {
        username.requestFocus();
        username.setText("siyuanx@usc.edu");
        password.requestFocus();
        password.setText("123");
        loginButton.callOnClick();
        assertFalse(mActivity.loginSuccessful());
    }

    @Test
    @UiThreadTest
    public void testAddNewUser() {
        username.requestFocus();
        username.setText("newUser@usc.edu");
        password.requestFocus();
        password.setText("12345");
        loginButton.callOnClick();
        assertTrue(mActivity.loginSuccessful());
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}