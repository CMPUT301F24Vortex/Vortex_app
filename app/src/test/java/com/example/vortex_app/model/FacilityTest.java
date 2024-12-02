package com.example.vortex_app.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FacilityTest {
    private Facility facility;

    @Before
    public void setUp() {
        // Initialize a Facility object before each test
        facility = new Facility("123", "Community Hall", "123 Main St");
    }

    @Test
    public void testConstructorWithAllFields() {
        // Verify constructor initializes all fields correctly
        assertEquals("123", facility.getId());
        assertEquals("Community Hall", facility.getFacilityName());
        assertEquals("123 Main St", facility.getAddress());
    }

    @Test
    public void testConstructorWithoutId() {
        // Test the constructor without ID
        Facility facilityWithoutId = new Facility("Library", "456 Elm St");
        assertNull(facilityWithoutId.getId());
        assertEquals("Library", facilityWithoutId.getFacilityName());
        assertEquals("456 Elm St", facilityWithoutId.getAddress());
    }

    @Test
    public void testGettersAndSetters() {
        // Modify fields using setters and verify with getters
        facility.setId("456");
        facility.setFacilityName("New Hall");
        facility.setAddress("789 Oak St");

        assertEquals("456", facility.getId());
        assertEquals("New Hall", facility.getFacilityName());
        assertEquals("789 Oak St", facility.getAddress());
    }

    @Test
    public void testEmptyConstructor() {
        // Test the no-arg constructor
        Facility emptyFacility = new Facility();
        assertNull(emptyFacility.getId());
        assertNull(emptyFacility.getFacilityName());
        assertNull(emptyFacility.getAddress());
    }
}
