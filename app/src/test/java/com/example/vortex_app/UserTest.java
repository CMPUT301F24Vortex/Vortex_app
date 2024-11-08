package com.example.vortex_app;


import org.junit.Before;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;

    @Before
    public void setUp() {
        // Set up a sample User instance for testing
        user = new User("John", "Doe", "john.doe@example.com", "123-456-7890");
    }

    @Test
    public void testGetFullName() {
        // Test that getFullName returns the correct full name
        String expectedFullName = "John Doe";
        assertEquals(expectedFullName, user.getFullName());
    }

    @Test
    public void testGetEmail() {
        // Test that getEmail returns the correct email address
        String expectedEmail = "john.doe@example.com";
        assertEquals(expectedEmail, user.getEmail());
    }

    @Test
    public void testGetPhoneNumber() {
        // Test that getPhoneNumber returns the correct phone number
        String expectedPhoneNumber = "123-456-7890";
        assertEquals(expectedPhoneNumber, user.getPhoneNumber());
    }

    @Test
    public void testGetName() {
        // Test that getName returns the correct name (firstName + lastName)
        String expectedName = "John Doe";
        assertEquals(expectedName, user.getName());
    }

    @Test
    public void testConstructor() {
        // Test that the constructor correctly initializes the User object
        User newUser = new User("Jane", "Smith", "jane.smith@example.com", "098-765-4321");
        assertEquals("Jane", newUser.getName().split(" ")[0]);
        assertEquals("Smith", newUser.getName().split(" ")[1]);
        assertEquals("jane.smith@example.com", newUser.getEmail());
        assertEquals("098-765-4321", newUser.getPhoneNumber());
    }

    // Additional tests could include edge cases like empty or null values.
    @Test
    public void testEmptyFullName() {
        User emptyNameUser = new User("", "", "", "");
        assertEquals(" ", emptyNameUser.getFullName());  // We expect just a space since both names are empty
    }

}

