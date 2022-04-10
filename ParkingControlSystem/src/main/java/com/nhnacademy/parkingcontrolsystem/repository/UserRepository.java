package com.nhnacademy.parkingcontrolsystem.repository;

import com.nhnacademy.parkingcontrolsystem.customer.User;
import java.time.LocalDateTime;

public interface UserRepository {
    void insertUser(User user, LocalDateTime entranceTime);

    void removeUser(User user);

    public LocalDateTime getEntranceTime(User user);
}