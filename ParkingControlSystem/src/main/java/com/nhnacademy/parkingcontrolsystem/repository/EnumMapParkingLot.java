package com.nhnacademy.parkingcontrolsystem.repository;

import com.nhnacademy.parkingcontrolsystem.customer.Car;
import com.nhnacademy.parkingcontrolsystem.parkingsystem.ParkingSpace;
import com.nhnacademy.parkingcontrolsystem.customer.User;
import java.util.EnumMap;
import java.util.Map;

public class EnumMapParkingLot implements ParkingLot {
    private final Map<ParkingSpace, Car> carSource = new EnumMap<>(ParkingSpace.class);

    @Override
    public void enter(ParkingSpace parkingSpace, Car car) {
        this.carSource.put(parkingSpace, car);
    }

    @Override
    public void exit(ParkingSpace parkingSpace) {
        this.carSource.remove(parkingSpace);
    }

    @Override
    public Car getCar(ParkingSpace parkingSpace) { return carSource.get(parkingSpace); }

    @Override
    public User getUser(ParkingSpace parkingSpace) { return carSource.get(parkingSpace).getUser(); }
}
