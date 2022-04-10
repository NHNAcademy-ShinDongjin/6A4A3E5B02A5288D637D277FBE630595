package com.nhnacademy.parkingcontrolsystem.customer;

import com.nhnacademy.parkingcontrolsystem.parkingsystem.ParkingTicket;
import com.nhnacademy.parkingcontrolsystem.parkingsystem.Server;
import java.util.Map;

public class User {
    Server server = new Server();
    String userId;
    int money;
    boolean identification;
    Map<ParkingTicket, Integer> tickets;

    public User(String userId, int money) {
        if(userId == null) {
            throw new IllegalArgumentException("UserInit Failure: UserId is null");
        }

        if(money < 0) {
            throw new IllegalArgumentException("UserInit Failure: UserMoney is negative");
        }
        this.userId = userId;
        this.money = money;
    }

    public String getUserId() {
        return userId;
    }

    public int getMoney() {
        return money;
    }

    public void subtractMoney(int fee) {
        this.money -= fee;
    }

    public void setIdentification(boolean identification) {
        this.identification = identification;
    }

    public boolean isIdentification() {
        return identification;
    }

    public void setTickets(Map<ParkingTicket, Integer> tickets) {
        this.tickets = tickets;
    }

    public Map<ParkingTicket, Integer> getTickets() { return tickets; }

    public void useTickets(ParkingTicket parkingTicket) {
        tickets.put(parkingTicket, tickets.get(parkingTicket) - 1);
    }
}
