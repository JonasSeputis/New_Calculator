package com.example.pc.calculator;

import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Jonas on 2017-07-05.
 */
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    private MainActivity mActivity = null;
    private Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(
            MainActivity.class.getName(), null, false);

    @Before
    public void setUp() throws Exception {
        mActivity = mainActivityActivityTestRule.getActivity();
    }

    @Test
    public void sumTest() {
        assertNotNull(mActivity.findViewById(R.id.one_button));
        assertNotNull(mActivity.findViewById(R.id.two_button));
        assertNotNull(mActivity.findViewById(R.id.plus_button));
        assertNotNull(mActivity.findViewById(R.id.equal_button));
        assertNotNull(mActivity.findViewById(R.id.input_text_view));
        onView(withId(R.id.one_button)).perform(click());
        onView(withId(R.id.plus_button)).perform(click());
        onView(withId(R.id.two_button)).perform(click());
        onView(withId(R.id.equal_button)).perform(click());

        getInstrumentation().waitForMonitorWithTimeout(monitor, 3000);
    }

    @Test
    public void subtractionTest() {
        assertNotNull(mActivity.findViewById(R.id.one_button));
        assertNotNull(mActivity.findViewById(R.id.two_button));
        assertNotNull(mActivity.findViewById(R.id.minus_button));
        assertNotNull(mActivity.findViewById(R.id.equal_button));
        onView(withId(R.id.one_button)).perform(click());
        onView(withId(R.id.minus_button)).perform(click());
        onView(withId(R.id.two_button)).perform(click());
        onView(withId(R.id.equal_button)).perform(click());

        getInstrumentation().waitForMonitorWithTimeout(monitor, 3000);
    }

    @Test
    public void multiplicationTest() {
        assertNotNull(mActivity.findViewById(R.id.one_button));
        assertNotNull(mActivity.findViewById(R.id.two_button));
        assertNotNull(mActivity.findViewById(R.id.multiply_button));
        assertNotNull(mActivity.findViewById(R.id.equal_button));
        onView(withId(R.id.one_button)).perform(click());
        onView(withId(R.id.multiply_button)).perform(click());
        onView(withId(R.id.two_button)).perform(click());
        onView(withId(R.id.equal_button)).perform(click());

        getInstrumentation().waitForMonitorWithTimeout(monitor, 3000);
    }

    @Test
    public void divisionTest() {
        assertNotNull(mActivity.findViewById(R.id.one_button));
        assertNotNull(mActivity.findViewById(R.id.two_button));
        assertNotNull(mActivity.findViewById(R.id.divide_button));
        assertNotNull(mActivity.findViewById(R.id.equal_button));
        onView(withId(R.id.one_button)).perform(click());
        onView(withId(R.id.divide_button)).perform(click());
        onView(withId(R.id.two_button)).perform(click());
        onView(withId(R.id.equal_button)).perform(click());

        getInstrumentation().waitForMonitorWithTimeout(monitor, 3000);
    }

    @Test
    public void squareRootTest() {
        assertNotNull(mActivity.findViewById(R.id.nine_button));
        assertNotNull(mActivity.findViewById(R.id.square_root_button));
        assertNotNull(mActivity.findViewById(R.id.equal_button));
        onView(withId(R.id.square_root_button)).perform(click());
        onView(withId(R.id.nine_button)).perform(click());
        onView(withId(R.id.equal_button)).perform(click());

        getInstrumentation().waitForMonitorWithTimeout(monitor, 3000);
    }

    @Test
    public void bracketsTest() {
        assertNotNull(mActivity.findViewById(R.id.nine_button));
        assertNotNull(mActivity.findViewById(R.id.square_root_button));
        assertNotNull(mActivity.findViewById(R.id.brackets_button));
        assertNotNull(mActivity.findViewById(R.id.two_button));
        assertNotNull(mActivity.findViewById(R.id.plus_button));
        assertNotNull(mActivity.findViewById(R.id.equal_button));
        onView(withId(R.id.square_root_button)).perform(click());
        onView(withId(R.id.nine_button)).perform(click());
        onView(withId(R.id.brackets_button)).perform(click());
        onView(withId(R.id.two_button)).perform(click());
        onView(withId(R.id.plus_button)).perform(click());
        onView(withId(R.id.two_button)).perform(click());
        onView(withId(R.id.brackets_button)).perform(click());
        onView(withId(R.id.plus_button)).perform(click());
        onView(withId(R.id.two_button)).perform(click());
        onView(withId(R.id.equal_button)).perform(click());

        getInstrumentation().waitForMonitorWithTimeout(monitor, 3000);
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}