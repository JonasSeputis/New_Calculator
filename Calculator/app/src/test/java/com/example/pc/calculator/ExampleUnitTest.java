package com.example.pc.calculator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(JUnit4.class)
public class ExampleUnitTest {

//    private MainActivity mainActivity;
//
//    @Before
//    public void createMainActivity(){mainActivity = new MainActivity();}


    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

        assertEquals(5, 3 - 2^6 +10 - 8 );
    }
}