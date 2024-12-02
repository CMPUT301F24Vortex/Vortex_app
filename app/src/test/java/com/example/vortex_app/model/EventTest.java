package com.example.vortex_app.model;

import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;

import static org.junit.Assert.*;

public class EventTest {
    private Event event;

    @Before
    public void setUp() {
        // Initialize an Event object before each test
        event = new Event("Yoga Class", 101, "Monday", "10:00 AM", "1 Hour",
                "2024-12-15", "2024-11-01", "20.0",
                "Community Center", 20, "Beginner", true);
    }

    @Test
    public void testConstructorWithAllFields() {
        // Verify constructor initializes all fields correctly
        assertEquals("Yoga Class", event.getName());
        assertEquals(101, event.getImageResId());
        assertEquals("10:00 AM", event.getTime());
        assertEquals("1 Hour", event.getPeriod());
        assertEquals("2024-12-15", event.getRegistrationDueDate());
        assertEquals("2024-11-01", event.getRegistrationOpenDate());
        assertEquals(20.0, event.getPrice(), 0.001);
        assertEquals("Community Center", event.getLocation());
        assertEquals(20, event.getMaxPeople());
        assertEquals("Beginner", event.getDifficulty());
        assertTrue(event.isRequiresGeolocation());
    }

    @Test
    public void testConstructorWithNameAndEventID() {
        // Test the constructor with name and event ID
        Event eventWithID = new Event("Dance Class", "event123");
        assertEquals("Dance Class", eventWithID.getName());
        assertEquals("event123", eventWithID.getEventID());
    }

    @Test
    public void testConstructorWithEventID() {
        // Test the constructor with event ID only
        Event eventWithIDOnly = new Event("event123");
        assertEquals("event123", eventWithIDOnly.getEventID());
    }

    @Test
    public void testSettersAndGetters() {
        // Modify fields using setters and verify with getters
        event.setName("Painting Class");
        event.setImageResId(202);
        event.setClassDays(Arrays.asList("Tuesday", "Thursday"));
        event.setTime("2:00 PM");
        event.setPeriod("2 Hours");
        event.setRegistrationDueDate("2024-12-20");
        event.setRegistrationOpenDate("2024-11-05");
        event.setPrice(30.5);
        event.setLocation("Art Studio");
        event.setMaxPeople(15);
        event.setDifficulty("Intermediate");
        event.setRequiresGeolocation(false);

        assertEquals("Painting Class", event.getName());
        assertEquals(202, event.getImageResId());
        assertEquals(Arrays.asList("Tuesday", "Thursday"), event.getClassDays());
        assertEquals("2:00 PM", event.getTime());
        assertEquals("2 Hours", event.getPeriod());
        assertEquals("2024-12-20", event.getRegistrationDueDate());
        assertEquals("2024-11-05", event.getRegistrationOpenDate());
        assertEquals(30.5, event.getPrice(), 0.001);
        assertEquals("Art Studio", event.getLocation());
        assertEquals(15, event.getMaxPeople());
        assertEquals("Intermediate", event.getDifficulty());
        assertFalse(event.isRequiresGeolocation());
    }

    @Test
    public void testParsePrice() {
        // Test the parsePrice utility method
        event.setPrice("50.0");
        assertEquals(50.0, event.getPrice(), 0.001);

        event.setPrice("invalid");
        assertEquals(0.0, event.getPrice(), 0.001); // Invalid input defaults to 0.0
    }

    @Test
    public void testSetUserID() {
        event.setUserID("user456");
        assertEquals("user456", event.userID);
    }
}
