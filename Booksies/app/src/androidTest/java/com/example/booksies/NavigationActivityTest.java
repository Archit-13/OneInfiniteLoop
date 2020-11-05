package com.example.booksies;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.booksies.controller.NavigationActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.assertFalse;


public class NavigationActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<NavigationActivity> rule =
            new ActivityTestRule<NavigationActivity>(NavigationActivity.class, true, true);


    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

    }

    @Test
    public void checkMyBooks(){
        solo.assertCurrentActivity("Wrong activity", NavigationActivity.class);
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.action_add_book));
        solo.enterText((EditText) solo.getView(R.id.titleEditText), "Calculus");
        solo.enterText((EditText) solo.getView(R.id.authorEditText), "Stewart");
        solo.enterText((EditText) solo.getView(R.id.ISBNEditText), "34555631");
        solo.enterText((EditText) solo.getView(R.id.commentEditText), "Good Condition");


        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.addButton));
        assertTrue(solo.waitForText("Calculus",1,2000));
        assertTrue(solo.waitForText("Stewart",1,2000));


    }

    @Test
    public void checkFilter(){
        solo.assertCurrentActivity("Wrong activity", NavigationActivity.class);

        Spinner spinner = (Spinner)solo.getView(R.id.filter);
        spinner.setSelection(1, true);

        assertFalse(solo.waitForText("accepted",1,2000));
        assertFalse(solo.waitForText("requested",1,2000));
        assertFalse(solo.waitForText("borrowed",1,2000));



    }



}
