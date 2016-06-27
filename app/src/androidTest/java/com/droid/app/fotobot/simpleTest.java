package com.droid.app.fotobot;

import android.content.ComponentName;
import android.test.ActivityInstrumentationTestCase2;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

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

        onView(withId(R.id.config)).perform(click());
        intended(hasComponent(new ComponentName(getTargetContext(), Settings.class)));

       // onView(withId(R.id.textViewInfo)).check(matches(withText("Hello Barsik")));
    }
}
