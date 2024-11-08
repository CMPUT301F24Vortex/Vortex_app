package com.example.vortex_app;

/**
 * The Center class represents a physical or organizational center with a name and address.
 */
public class Center {
    private String name;
    private String address;

    /**
     * Constructs a new Center with the specified name and address.
     *
     * @param name    The name of the center.
     * @param address The address of the center.
     */
    public Center(String name, String address) {
        this.name = name;
        this.address = address;
    }

    /**
     * Gets the name of the center.
     *
     * @return The name of the center.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the address of the center.
     *
     * @return The address of the center.
     */
    public String getAddress() {
        return address;
    }
}
