package com.example.vortex_app.controller.adapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = {28})
public class ExampleTest {
    @Before
    public void setup() {
        // Initialization code here
    }

    @Test
    public void testExample() {
        assertEquals(2, 1 + 1);
    }
}

