package com.example.vortex_app.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {
    private User user;

    @Before
    public void setUp() {
        // Initialize a User object before each test
        user = new User("John", "Doe", "john.doe@example.com", "123456789", "Android");
    }

    @Test
    public void testConstructorMandatoryFields() {
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("123456789", user.getContactInfo());
        assertEquals("Android", user.getDevice());
    }

    @Test
    public void testConstructorWaitingListUser() {
        User waitingListUser = new User("Alice", "Smith", "user123");
        assertEquals("Alice", waitingListUser.getFirstName());
        assertEquals("Smith", waitingListUser.getLastName());
        assertEquals("user123", waitingListUser.getUserID());
    }

    @Test
    public void testGetterSetterLatitudeLongitude() {
        user.setLatitude(37.7749);
        user.setLongitude(-122.4194);

        assertEquals(37.7749, user.getLatitude(), 0.0001);
        assertEquals(-122.4194, user.getLongitude(), 0.0001);
    }

    @Test
    public void testGetFullName() {
        assertEquals("John Doe", user.getFullName());
    }

    @Test
    public void testSettersAndGetters() {
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setEmail("jane.doe@example.com");
        user.setContactInfo("987654321");
        user.setDevice("iOS");

        assertEquals("Jane", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("jane.doe@example.com", user.getEmail());
        assertEquals("987654321", user.getContactInfo());
        assertEquals("iOS", user.getDevice());
    }

    @Test
    public void testToString() {
        user.setUserID("user123");
        user.setEventID("event456");
        user.setStatus("confirmed");

        String expected = "User{" +
                ", firstName='John'" +
                ", lastName='Doe'" +
                ", email='john.doe@example.com'" +
                ", contactInfo='123456789'" +
                ", avatarUrl='null'" +
                ", userID='user123'" +
                ", device='Android'" +
                ", eventID='event456'" +
                ", status='confirmed'" +
                '}';

        assertEquals(expected, user.toString());
    }
}
