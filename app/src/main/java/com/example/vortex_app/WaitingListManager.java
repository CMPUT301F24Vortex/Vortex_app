package com.example.vortex_app;

import java.util.ArrayList;
import java.util.List;

public class WaitingListManager {
    private static List<User> waitingList = new ArrayList<>();

    public static void addUserToWaitingList(User user) {
        if (!waitingList.contains(user)) {
            waitingList.add(user);
        }
    }

    public static List<User> getWaitingList() {
        return waitingList;
    }

    public static void removeUserFromWaitingList(User user) {
        waitingList.remove(user);
    }
}
