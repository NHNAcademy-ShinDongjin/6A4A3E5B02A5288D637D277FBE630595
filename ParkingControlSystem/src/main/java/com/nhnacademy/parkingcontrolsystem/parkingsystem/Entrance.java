package com.nhnacademy.parkingcontrolsystem.parkingsystem;

import static com.nhnacademy.parkingcontrolsystem.customer.CarType.BIGSIZE;

import com.nhnacademy.parkingcontrolsystem.customer.Car;
import com.nhnacademy.parkingcontrolsystem.customer.User;
import java.time.LocalDateTime;

public class Entrance {
    private Car car;
    private Clock clock;
    private LocalDateTime entranceTime;

    public void carScan(Car car) {
        if(car.getCarNumber() == null) {
            throw new IllegalArgumentException("CarScan Failure: CarNumber is null.");
        }

        if(car.getCarType() == null) {
            throw new IllegalArgumentException("CarScan Failure: CarType is null");
        }

        if(car.getCarType() == BIGSIZE) {
            throw new IllegalArgumentException("CarType Failure: Can't enter BIGSIZE Car");
        }

        this.car = car;
        this.entranceTime = clock.getEntranceTime();
    }

    public Car getCar() {
        return this.car;
    }

    public User getUser() {
        return this.car.getUser();
    }

    public LocalDateTime getEntranceTime() { return entranceTime; }

    public void setClock(Clock clock) {
        this.clock = clock;
    }
}