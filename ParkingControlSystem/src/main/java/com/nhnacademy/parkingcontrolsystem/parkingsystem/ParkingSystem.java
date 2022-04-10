package com.nhnacademy.parkingcontrolsystem.parkingsystem;

import com.nhnacademy.parkingcontrolsystem.customer.Car;
import com.nhnacademy.parkingcontrolsystem.customer.User;
import com.nhnacademy.parkingcontrolsystem.repository.ParkingLot;
import com.nhnacademy.parkingcontrolsystem.repository.UserRepository;

public class ParkingSystem {
    private final Server server;
    private final UserRepository userRepository;
    private final ParkingLot parkingLot;

    public ParkingSystem(ParkingLot parkingLot, UserRepository userRepository, Server server) {
        this.parkingLot = parkingLot;
        this.userRepository = userRepository;
        this.server = server;
    }

    public void enterFromParkingSystem(ParkingSpace parkingSpace, Entrance entrance) {
        parkingLot.enter(parkingSpace, entrance.getCar());
        userRepository.insertUser(entrance.getUser(), entrance.getEntranceTime());
    }

    public void exitFromParkingSystem(ParkingSpace parkingSpace, Exit exit) {
        identifyPaycoUser(parkingLot.getUser(parkingSpace));
        setParkingTicket(parkingLot.getUser(parkingSpace));
        exit.pay(parkingLot.getCar(parkingSpace), userRepository.getEntranceTime(parkingLot.getUser(parkingSpace)));
        userRepository.removeUser(parkingLot.getUser(parkingSpace));
        parkingLot.exit(parkingSpace);
    }

    public Car getCar(ParkingSpace parkingSpace) {
        return this.parkingLot.getCar(parkingSpace);
    }

    public void identifyPaycoUser(User user) {
        user.setIdentification(server.paycoIdentify(user.getUserId()));
    }

    public void setParkingTicket(User user) {
        user.setTickets(server.getParkingTicket(user.getUserId()));
    }
}