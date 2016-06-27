package com.droid.app.fotobot;

import android.test.ActivityInstrumentationTestCase2;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

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
        onView(withId(R.id.camera_name)).check(matches(withText("default")));
       // intended(hasComponent(new ComponentName(getTargetContext(), Settings.class)));

       // onView(withId(R.id.textViewInfo)).check(matches(withText("Hello Barsik")));
    }
}
