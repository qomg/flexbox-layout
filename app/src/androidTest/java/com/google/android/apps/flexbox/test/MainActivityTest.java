/*
 * Copyright 2016 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.apps.flexbox.test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.google.android.apps.flexbox.MainActivity;
import com.google.android.apps.flexbox.R;
import com.google.android.libraries.flexbox.FlexboxLayout;

import android.content.pm.ActivityInfo;
import android.support.design.widget.NavigationView;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.MenuItemCompat;
import android.test.FlakyTest;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Integration tests for {@link MainActivity}.
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    @FlakyTest(tolerance = 3)
    public void testAddFlexItem() {
        MainActivity activity = mActivityRule.getActivity();
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.flexbox_layout);
        assertNotNull(flexboxLayout);
        int beforeCount = flexboxLayout.getChildCount();
        onView(withId(R.id.add_fab)).perform(click());

        assertThat(flexboxLayout.getChildCount(), is(beforeCount + 1));
    }

    @Test
    @FlakyTest(tolerance = 3)
    public void testRemoveFlexItem() {
        MainActivity activity = mActivityRule.getActivity();
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.flexbox_layout);
        assertNotNull(flexboxLayout);
        int beforeCount = flexboxLayout.getChildCount();
        onView(withId(R.id.remove_fab)).perform(click());

        assertThat(flexboxLayout.getChildCount(), is(beforeCount - 1));
    }

    @Test
    @FlakyTest(tolerance = 3)
    public void testConfigurationChange() {
        MainActivity activity = mActivityRule.getActivity();
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.flexbox_layout);
        assertNotNull(flexboxLayout);
        onView(withId(R.id.add_fab)).perform(click());
        onView(withId(R.id.add_fab)).perform(click());
        int beforeCount = flexboxLayout.getChildCount();

        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        // Verify the flex items are restored across the configuration change.
        assertThat(flexboxLayout.getChildCount(), is(beforeCount));
    }

    @Test
    @SuppressWarnings("unchecked")
    @FlakyTest(tolerance = 3)
    public void testFlexDirectionSpinner() {
        MainActivity activity = mActivityRule.getActivity();
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.flexbox_layout);
        assertNotNull(flexboxLayout);
        NavigationView navigationView = (NavigationView) activity.findViewById(R.id.nav_view);
        assertNotNull(navigationView);
        Menu menu = navigationView.getMenu();
        final Spinner spinner = (Spinner) MenuItemCompat
                .getActionView(menu.findItem(R.id.menu_item_flex_direction));
        ArrayAdapter<CharSequence> spinnerAdapter = (ArrayAdapter<CharSequence>)
                spinner.getAdapter();

        final int columnPosition = spinnerAdapter.getPosition(activity.getString(R.string.column));
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinner.setSelection(columnPosition);
            }
        });
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        assertThat(flexboxLayout.getFlexDirection(), is(FlexboxLayout.FLEX_DIRECTION_COLUMN));

        final int rowReversePosition = spinnerAdapter
                .getPosition(activity.getString(R.string.row_reverse));
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinner.setSelection(rowReversePosition);
            }
        });
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        assertThat(flexboxLayout.getFlexDirection(), is(FlexboxLayout.FLEX_DIRECTION_ROW_REVERSE));
    }

    @Test
    @SuppressWarnings("unchecked")
    @FlakyTest(tolerance = 3)
    public void testFlexWrapSpinner() {
        MainActivity activity = mActivityRule.getActivity();
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.flexbox_layout);
        assertNotNull(flexboxLayout);
        NavigationView navigationView = (NavigationView) activity.findViewById(R.id.nav_view);
        assertNotNull(navigationView);
        Menu menu = navigationView.getMenu();
        final Spinner spinner = (Spinner) MenuItemCompat
                .getActionView(menu.findItem(R.id.menu_item_flex_wrap));
        ArrayAdapter<CharSequence> spinnerAdapter = (ArrayAdapter<CharSequence>)
                spinner.getAdapter();

        final int wrapReversePosition = spinnerAdapter
                .getPosition(activity.getString(R.string.wrap_reverse));
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinner.setSelection(wrapReversePosition);
            }
        });
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        assertThat(flexboxLayout.getFlexWrap(), is(FlexboxLayout.FLEX_WRAP_WRAP_REVERSE));

        final int noWrapPosition = spinnerAdapter
                .getPosition(activity.getString(R.string.nowrap));
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinner.setSelection(noWrapPosition);
            }
        });
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        assertThat(flexboxLayout.getFlexWrap(), is(FlexboxLayout.FLEX_WRAP_NOWRAP));
    }

    @Test
    @SuppressWarnings("unchecked")
    @FlakyTest(tolerance = 3)
    public void testJustifyContentSpinner() {
        MainActivity activity = mActivityRule.getActivity();
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.flexbox_layout);
        assertNotNull(flexboxLayout);
        NavigationView navigationView = (NavigationView) activity.findViewById(R.id.nav_view);
        assertNotNull(navigationView);
        Menu menu = navigationView.getMenu();
        final Spinner spinner = (Spinner) MenuItemCompat
                .getActionView(menu.findItem(R.id.menu_item_justify_content));
        ArrayAdapter<CharSequence> spinnerAdapter = (ArrayAdapter<CharSequence>)
                spinner.getAdapter();

        final int spaceBetweenPosition = spinnerAdapter
                .getPosition(activity.getString(R.string.space_between));
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinner.setSelection(spaceBetweenPosition);
            }
        });
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        assertThat(flexboxLayout.getJustifyContent(),
                is(FlexboxLayout.JUSTIFY_CONTENT_SPACE_BETWEEN));

        final int centerPosition = spinnerAdapter
                .getPosition(activity.getString(R.string.center));
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinner.setSelection(centerPosition);
            }
        });
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        assertThat(flexboxLayout.getJustifyContent(), is(FlexboxLayout.JUSTIFY_CONTENT_CENTER));
    }

    @Test
    @SuppressWarnings("unchecked")
    @FlakyTest(tolerance = 3)
    public void testAlignItemsSpinner() {
        MainActivity activity = mActivityRule.getActivity();
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.flexbox_layout);
        assertNotNull(flexboxLayout);
        NavigationView navigationView = (NavigationView) activity.findViewById(R.id.nav_view);
        assertNotNull(navigationView);
        Menu menu = navigationView.getMenu();
        final Spinner spinner = (Spinner) MenuItemCompat
                .getActionView(menu.findItem(R.id.menu_item_align_items));
        ArrayAdapter<CharSequence> spinnerAdapter = (ArrayAdapter<CharSequence>)
                spinner.getAdapter();

        final int baselinePosition = spinnerAdapter
                .getPosition(activity.getString(R.string.baseline));
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinner.setSelection(baselinePosition);
            }
        });
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        assertThat(flexboxLayout.getAlignItems(),
                is(FlexboxLayout.ALIGN_ITEMS_BASELINE));

        final int flexEndPosition = spinnerAdapter
                .getPosition(activity.getString(R.string.flex_end));
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinner.setSelection(flexEndPosition);
            }
        });
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        assertThat(flexboxLayout.getAlignItems(), is(FlexboxLayout.ALIGN_ITEMS_FLEX_END));
    }

    @Test
    @SuppressWarnings("unchecked")
    @FlakyTest(tolerance = 3)
    public void testAlignContentSpinner() {
        MainActivity activity = mActivityRule.getActivity();
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.flexbox_layout);
        assertNotNull(flexboxLayout);
        NavigationView navigationView = (NavigationView) activity.findViewById(R.id.nav_view);
        assertNotNull(navigationView);
        Menu menu = navigationView.getMenu();
        final Spinner spinner = (Spinner) MenuItemCompat
                .getActionView(menu.findItem(R.id.menu_item_align_content));
        ArrayAdapter<CharSequence> spinnerAdapter = (ArrayAdapter<CharSequence>)
                spinner.getAdapter();

        final int spaceAroundPosition = spinnerAdapter
                .getPosition(activity.getString(R.string.space_around));
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinner.setSelection(spaceAroundPosition);
            }
        });
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        assertThat(flexboxLayout.getAlignContent(),
                is(FlexboxLayout.ALIGN_CONTENT_SPACE_AROUND));

        final int stretchPosition = spinnerAdapter
                .getPosition(activity.getString(R.string.stretch));
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinner.setSelection(stretchPosition);
            }
        });
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        assertThat(flexboxLayout.getAlignContent(), is(FlexboxLayout.ALIGN_CONTENT_STRETCH));
    }
}
