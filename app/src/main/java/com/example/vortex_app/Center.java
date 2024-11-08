package com.example.vortex_app;

/**
 * {@code Center} represents a community center with a name and address.
 * This class provides a model for storing and retrieving basic information about centers
 * that may be used for event locations within the application.
 */
public class Center {

    private String name;
    private String address;

    /**
     * Constructs a {@code Center} with the specified name and address.
     *
     * @param name    The name of the center.
     * @param address The address of the center.
     */
    public Center(String name, String address) {
        this.name = name;
        this.address = address;
    }

    /**
     * Returns the name of the center.
     *
     * @return A {@code String} representing the name of the center.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the address of the center.
     *
     * @return A {@code String} representing the address of the center.
     */
    public String getAddress() {
        return address;
    }
}
