package com.nhnacademy.parkingcontrolsystem.customer;

public class Car {
    String carNumber;
    CarType carType;
    User user;

    public Car(String carNumber, CarType carType, User user) {
        this.carNumber = carNumber;
        this.carType = carType;
        this.user = user;
    }

    public String getCarNumber() { return this.carNumber; }

    public CarType getCarType() { return carType; }

    public User getUser() {
        return this.user;
    }
}
