package com.example.fred.uscfit.white_box;

import android.content.Intent;

import com.example.Activity;
import com.example.fred.uscfit.GetAllPlansTask;
import com.example.fred.uscfit.ProgressActivity;
import com.google.firebase.Timestamp;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Calendar;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertTrue;

import androidx.test.rule.ActivityTestRule;

public class ProgressActivityTest {
    @Rule
    public ActivityTestRule<ProgressActivity> mProgressActivity = new ActivityTestRule<ProgressActivity>(ProgressActivity.class, false, false);

    private ProgressActivity mActivity = null;
    private String testEmail = "testUser@usc.edu";

    @Before
    public void setUp() {
        Intent i = new Intent();
        i.putExtra("email", testEmail);
        String date = "2018_10_28";
        i.putExtra("date", date);
        mProgressActivity.launchActivity(i);
        mActivity = mProgressActivity.getActivity();
    }


    @Test
    public void testProgress () throws Throwable {
        // create  a signal to let us know when our task is done.
        final CountDownLatch signal = new CountDownLatch(1);
        final GetAllPlansTask myTask = new GetAllPlansTask(testEmail, mActivity) {
            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                signal.countDown();
            }
        };

        myTask.execute();
        signal.await();

    }

    @Test
    public void testFinishPlan() {
        Calendar time_9am = Calendar.getInstance();
        time_9am.set(Calendar.HOUR_OF_DAY, 9);
        Calendar time_10am = Calendar.getInstance();
        time_10am.set(Calendar.HOUR_OF_DAY, 10);
        Calendar time_11am = Calendar.getInstance();
        time_11am.set(Calendar.HOUR_OF_DAY,11);
        Activity activity1 = new Activity("running", new Timestamp(time_9am.getTime()), new Timestamp(time_10am.getTime()));
        Activity activity2 = new Activity("running", new Timestamp(time_9am.getTime()), new Timestamp(time_10am.getTime()));
        Activity activity3 = new Activity("swimming", new Timestamp(time_9am.getTime()), new Timestamp(time_10am.getTime()));
        Activity activity4 = new Activity("running", new Timestamp(time_9am.getTime()), new Timestamp(time_11am.getTime()));
        assertTrue(mActivity.isFinishedActivity(activity1, activity2));
        assertTrue(!mActivity.isFinishedActivity(activity3, activity1));
        assertTrue(mActivity.isFinishedActivity(activity4, activity1));
        assertTrue(!mActivity.isFinishedActivity(activity1, activity4));

    }

}
