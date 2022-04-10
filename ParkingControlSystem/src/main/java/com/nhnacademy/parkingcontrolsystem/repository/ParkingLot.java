package com.nhnacademy.parkingcontrolsystem.repository;

import com.nhnacademy.parkingcontrolsystem.customer.Car;
import com.nhnacademy.parkingcontrolsystem.parkingsystem.ParkingSpace;
import com.nhnacademy.parkingcontrolsystem.customer.User;

public interface ParkingLot {
    void enter(ParkingSpace parkingSpace, Car car);

    void exit(ParkingSpace parkingSpace);

    public Car getCar(ParkingSpace parkingSpace);

    public User getUser(ParkingSpace parkingSpace);
}
