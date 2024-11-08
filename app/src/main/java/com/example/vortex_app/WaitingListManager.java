package com.example.vortex_app;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code WaitingListManager} is a utility class that manages a static list of {@link User} objects representing users
 * on a waiting list for an event. It provides methods to add, retrieve, and remove users from the waiting list.
 */
public class WaitingListManager {
    private static List<User> waitingList = new ArrayList<>();

    /**
     * Adds a user to the waiting list if they are not already present.
     *
     * @param user The {@link User} to be added to the waiting list.
     */
    public static void addUserToWaitingList(User user) {
        if (!waitingList.contains(user)) {
            waitingList.add(user);
        }
    }

    /**
     * Returns the current list of users on the waiting list.
     *
     * @return A {@link List} of {@link User} objects representing the users on the waiting list.
     */
    public static List<User> getWaitingList() {
        return waitingList;
    }

    /**
     * Removes a specified user from the waiting list if they are present.
     *
     * @param user The {@link User} to be removed from the waiting list.
     */
    public static void removeUserFromWaitingList(User user) {
        waitingList.remove(user);
    }
}
