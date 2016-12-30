package com.droid.app.fotobot;

import android.test.ActivityInstrumentationTestCase2;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;


/**
 * Created by voran on 6/27/16.
 */
public class simpleTest extends ActivityInstrumentationTestCase2<MainActivity> {
    public simpleTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void testEnterName() throws Exception {
        //  Intents.init();
        onView(withId(R.id.config)).perform(click());
      //  onView(withId(R.id.camera_name)).check(matches(withText("default")));
        onView(withId(R.id.camera_name)).perform(clearText(),typeText("Espresso_cam"));

        onView(withId(R.id.camera_name)).check(matches(withText("Espresso_cam")));

        //intended(hasComponent(Settings.class.getName()));
        // intended(hasComponent(new ComponentName(getTargetContext(), Settings.class)));

        // onView(withId(R.id.textViewInfo)).check(matches(withText("Hello Barsik")));
    }

    public void testNetworkSettings() throws Exception {
        onView(withId(R.id.config)).perform(click());
        onView(withText("Network")).perform(click());
        onView(withText("Foto")).perform(click());
        onView(withText("onView(withText(\"APPLY\")).perform(scrollTo(),click());\n" +
                "        onView(withText(\"MAIN WINDOW\")).perform(scrollTo(),click());\n" +
                "        onView(withId(R.id.config)).perform(click());Video")).perform(click());

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText("Behavior")).perform(click());
        onView(withId(R.id.behavior_back_button)).perform(scrollTo(),click());
    }

    public void testNetworkStatus() throws Exception {
        onView(withId(R.id.config)).perform(click());
        onView(withText("Network")).perform(click());
        onView(withId(R.id.spinner_network_status)).check(matches(withSpinnerText("on")));
    }

    /*
    Change Network status
     */
    public void testChangeNetworkStatus() throws Exception {
        onView(withId(R.id.config)).perform(click());
        onView(withText("Network")).perform(click());
        onView(withId(R.id.spinner_network_status)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("on"))).perform(click());

        onView(withId(R.id.network_apply_button)).perform(scrollTo(),click());
        onView(withId(R.id.network_back_button)).perform(scrollTo(),click());

        onView(withId(R.id.config)).perform(click());
        onView(withText("Network")).perform(click());
        onView(withId(R.id.spinner_network_status)).check(matches(withSpinnerText("on")));
    }
}