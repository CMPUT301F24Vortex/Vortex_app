package com.example.vortex_app.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class NotificationModelTest {
    private NotificationModel notification;

    @Before
    public void setUp() {
        // Initialize a NotificationModel object before each test
        notification = new NotificationModel(
                "System Update",
                "A new update is available.",
                "unread",
                "notif123",
                new Date(1700000000000L) // Fixed timestamp for consistency
        );
    }

    @Test
    public void testConstructor() {
        // Verify the constructor initializes all fields correctly
        assertEquals("System Update", notification.getTitle());
        assertEquals("A new update is available.", notification.getMessage());
        assertEquals("unread", notification.getStatus());
        assertEquals("notif123", notification.getId());
        assertEquals(new Date(1700000000000L), notification.getTimeStamp());
    }

    @Test
    public void testGetTitle() {
        // Test the getTitle method
        assertEquals("System Update", notification.getTitle());
    }

    @Test
    public void testGetMessage() {
        // Test the getMessage method
        assertEquals("A new update is available.", notification.getMessage());
    }

    @Test
    public void testGetStatus() {
        // Test the getStatus method
        assertEquals("unread", notification.getStatus());
    }

    @Test
    public void testGetId() {
        // Test the getId method
        assertEquals("notif123", notification.getId());
    }

    @Test
    public void testGetTimeStamp() {
        // Test the getTimeStamp method
        assertEquals(new Date(1700000000000L), notification.getTimeStamp());
    }
}
