package com.example.vortex_app.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntrantTest {
    private Entrant entrant;

    @Before
    public void setUp() {
        // Initialize an Entrant object before each test
        entrant = new Entrant("John", "Doe", 37.7749, -122.4194); // San Francisco coordinates
    }

    @Test
    public void testConstructor() {
        // Verify that the constructor initializes all fields correctly
        assertEquals("John", entrant.getFirstName());
        assertEquals("Doe", entrant.getLastName());
        assertEquals(37.7749, entrant.getLatitude(), 0.0001);
        assertEquals(-122.4194, entrant.getLongitude(), 0.0001);
    }

    @Test
    public void testGetFirstName() {
        // Test the getFirstName method
        assertEquals("John", entrant.getFirstName());
    }

    @Test
    public void testGetLastName() {
        // Test the getLastName method
        assertEquals("Doe", entrant.getLastName());
    }

    @Test
    public void testGetLatitude() {
        // Test the getLatitude method
        assertEquals(37.7749, entrant.getLatitude(), 0.0001);
    }

    @Test
    public void testGetLongitude() {
        // Test the getLongitude method
        assertEquals(-122.4194, entrant.getLongitude(), 0.0001);
    }
}
