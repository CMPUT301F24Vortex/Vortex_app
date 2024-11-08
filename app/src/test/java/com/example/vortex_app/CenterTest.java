package com.example.vortex_app;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Before;
import org.junit.Test;



public class CenterTest {

    private Center center;

    @Before
    public void setUp() {
        // Set up a sample Center instance for testing
        center = new Center("Vortex Science Center", "123 Vortex Avenue, SciCity");
    }

    @Test
    public void testGetName() {
        // Test that getName returns the correct center name
        String expectedName = "Vortex Science Center";
        assertEquals(expectedName, center.getName());
    }

    @Test
    public void testGetAddress() {
        // Test that getAddress returns the correct address
        String expectedAddress = "123 Vortex Avenue, SciCity";
        assertEquals(expectedAddress, center.getAddress());
    }

    @Test
    public void testConstructor() {
        // Test that the constructor initializes the center correctly
        Center newCenter = new Center("Galaxy Research Hub", "456 Nebula Road, SpaceTown");
        assertEquals("Galaxy Research Hub", newCenter.getName());
        assertEquals("456 Nebula Road, SpaceTown", newCenter.getAddress());
    }

    @Test
    public void testEmptyName() {
        // Test the case where the center name is empty
        Center emptyNameCenter = new Center("", "456 Nebula Road, SpaceTown");
        assertEquals("", emptyNameCenter.getName()); // Should return an empty string
    }

    @Test
    public void testEmptyAddress() {
        // Test the case where the center address is empty
        Center emptyAddressCenter = new Center("SpaceLab", "");
        assertEquals("", emptyAddressCenter.getAddress()); // Should return an empty string
    }

    @Test
    public void testNullName() {
        // Test if the center name can be null
        Center nullNameCenter = new Center(null, "123 Nebula Street, Galaxy");
        assertNull(nullNameCenter.getName()); // Should return null
    }

    @Test
    public void testNullAddress() {
        // Test if the center address can be null
        Center nullAddressCenter = new Center("Stellar Institute", null);
        assertNull(nullAddressCenter.getAddress()); // Should return null
    }
}
