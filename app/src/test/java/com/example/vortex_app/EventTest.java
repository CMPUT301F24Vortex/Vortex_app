package com.example.vortex_app;

import org.junit.Before;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.example.vortex_app.model.Event;

public class EventTest {

    private Event event;

    @Before
    public void setUp() {

        event = new Event("Test Event", R.drawable.sample_event_image, "Monday", "10:00 AM", "Morning",
                "2024-11-10", "2024-11-01", "20.00", "Test Location", 100, "Easy", true);
    }


    @Test
    public void testEventConstructor() {

        assertNotNull(event);
        assertEquals("Test Event", event.getName());
        assertEquals(R.drawable.sample_event_image, event.getImageResId());
        assertEquals("Monday", event.getClassDay());
        assertEquals("10:00 AM", event.getTime());
        assertEquals("Morning", event.getPeriod());
        assertEquals("2024-11-10", event.getRegistrationDueDate());
        assertEquals("2024-11-01", event.getRegistrationOpenDate());
        assertEquals(20.00, event.getPrice(), 0.001);  // Use a delta for floating point comparisons
        assertEquals("Test Location", event.getLocation());
        assertEquals(100, event.getMaxPeople());
        assertEquals("Easy", event.getDifficulty());
        assertTrue(event.isRequiresGeolocation());
    }

    // Test the constructor with name and event ID only
    @Test
    public void testEventConstructorWithNameAndID() {
        // Arrange: Create an Event with name and eventID
        Event event = new Event("Test Event", "12345");

        // Assert: Check if the fields are set correctly
        assertNotNull(event);
        assertEquals("Test Event", event.getName());
        assertEquals("12345", event.getEventID());
    }

    // Test getter methods
    @Test
    public void testGetters() {
        // Arrange: Initialize the event with known data
        event = new Event("Test Event", R.drawable.sample_event_image, "Monday", "10:00 AM", "Morning",
                "2024-11-10", "2024-11-01", "20.00", "Test Location", 100, "Easy", true);

        // Assert: Ensure all getter methods return the expected results
        assertEquals("Test Event", event.getName());
        assertEquals(R.drawable.sample_event_image, event.getImageResId());
        assertEquals("Monday", event.getClassDay());
        assertEquals("10:00 AM", event.getTime());
        assertEquals("Morning", event.getPeriod());
        assertEquals("2024-11-10", event.getRegistrationDueDate());
        assertEquals("2024-11-01", event.getRegistrationOpenDate());
        assertEquals(20.00, event.getPrice(), 0.001);  // Ensure correct value for price
        assertEquals("Test Location", event.getLocation());
        assertEquals(100, event.getMaxPeople());
        assertEquals("Easy", event.getDifficulty());
        assertTrue(event.isRequiresGeolocation());
    }

    // Test price parsing from String to double
    @Test
    public void testPriceParsing() {
        // Arrange: Create Event with price as String
        String priceString = "15.99";
        event = new Event("Test Event", R.drawable.sample_event_image, "Monday", "10:00 AM", "Morning",
                "2024-11-10", "2024-11-01", priceString, "Test Location", 100, "Easy", true);

        // Assert: Verify the price has been parsed correctly into double
        assertEquals(15.99, event.getPrice(), 0.001);
    }

    // Test invalid price format (this should throw a NumberFormatException)
    @Test(expected = NumberFormatException.class)
    public void testInvalidPriceFormat() {
        // Try creating an Event with an invalid price format
        new Event("Invalid Event", R.drawable.sample_event_image, "Monday", "10:00 AM", "Morning",
                "2024-11-10", "2024-11-01", "Invalid Price", "Test Location", 100, "Easy", true);
    }


}
