package com.example.vortex_app.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class OrgNotificationTest {
    private OrgNotification orgNotification;

    @Before
    public void setUp() {
        // Initialize an OrgNotification object before each test
        orgNotification = new OrgNotification(
                "Maintenance Alert",
                "System maintenance is scheduled for tonight.",
                new Date(1700000000000L), // Fixed timestamp for consistency
                "notif001"
        );
    }

    @Test
    public void testConstructor() {
        // Verify the constructor initializes all fields correctly
        assertEquals("Maintenance Alert", orgNotification.getTitle());
        assertEquals("System maintenance is scheduled for tonight.", orgNotification.getMessage());
        assertEquals(new Date(1700000000000L), orgNotification.getDate());
        assertEquals("notif001", orgNotification.getNotificationId());
    }

    @Test
    public void testGetNotificationId() {
        // Test the getNotificationId method
        assertEquals("notif001", orgNotification.getNotificationId());
    }

    @Test
    public void testGetTitle() {
        // Test the getTitle method
        assertEquals("Maintenance Alert", orgNotification.getTitle());
    }

    @Test
    public void testGetMessage() {
        // Test the getMessage method
        assertEquals("System maintenance is scheduled for tonight.", orgNotification.getMessage());
    }

    @Test
    public void testGetDate() {
        // Test the getDate method
        assertEquals(new Date(1700000000000L), orgNotification.getDate());
    }
}
