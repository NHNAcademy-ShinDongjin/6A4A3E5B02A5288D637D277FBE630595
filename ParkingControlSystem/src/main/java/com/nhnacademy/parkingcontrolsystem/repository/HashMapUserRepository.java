package com.nhnacademy.parkingcontrolsystem.repository;

import com.nhnacademy.parkingcontrolsystem.customer.User;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class HashMapUserRepository implements UserRepository{
    private final Map<User, LocalDateTime> userSource = new HashMap<>();

    @Override
    public void insertUser(User user, LocalDateTime entranceTime) { userSource.put(user, entranceTime); }

    @Override
    public void removeUser(User user) {
        userSource.remove(user);
    }

    @Override
    public LocalDateTime getEntranceTime(User user) {
        return userSource.get(user);
    }
}
