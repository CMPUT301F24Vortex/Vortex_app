package com.example.vortex_app;

import java.util.ArrayList;
import java.util.List;

/**
 * WaitingListManager manages a list of users who are on a waiting list. It provides methods to add users to the list,
 * remove users from the list, and retrieve the entire list of users.
 */
public class WaitingListManager {
    private static List<User> waitingList = new ArrayList<>();
    // Populate the waiting list with some example users for demonstration
    static {
        waitingList.add(new User("John", "Doe", "john.doe@example.com", "1234567890"));
        waitingList.add(new User("Jane", "Smith", "jane.smith@example.com", "0987654321"));
        waitingList.add(new User("Alice", "Johnson", "alice.johnson@example.com", "5678901234"));
        waitingList.add(new User("Bob", "Brown", "bob.brown@example.com", "4561237890"));
    }

    /**
     * Adds a user to the waiting list if they are not already present.
     *
     * @param user The User object to be added to the waiting list.
     */
    public static void addUserToWaitingList(User user) {
        if (!waitingList.contains(user)) {
            waitingList.add(user);
        }
    }

    /**
     * Retrieves the current waiting list of users.
     *
     * @return A list of User objects representing the current waiting list.
     */
    public static List<User> getWaitingList() {
        return waitingList;
    }

    /**
     * Removes a user from the waiting list.
     *
     * @param user The User object to be removed from the waiting list.
     */
    public static void removeUserFromWaitingList(User user) {
        waitingList.remove(user);
    }
}
