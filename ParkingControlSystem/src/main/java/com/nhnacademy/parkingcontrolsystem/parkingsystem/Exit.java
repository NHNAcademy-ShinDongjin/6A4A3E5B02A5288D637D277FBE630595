package com.nhnacademy.parkingcontrolsystem.parkingsystem;

import static com.nhnacademy.parkingcontrolsystem.customer.CarType.COMPACT;

import com.nhnacademy.parkingcontrolsystem.customer.Car;
import com.nhnacademy.parkingcontrolsystem.customer.CarType;
import com.nhnacademy.parkingcontrolsystem.customer.User;
import java.time.LocalDateTime;

public class Exit {
    private static final int BASIC_FEE = 1_000;
    private static final int DAY_MAXIMUM_FEE = 15_000;
    private static final int EXTRA_CHARGE_UNIT = 500;
    private static final long FREE_DURATION = 1800L;
    private static final long BASIC_DURATION = 3600L;
    private static final long EXTRA_TIME_UNIT = 600L;
    private static final float COMPACT_DISCOUNT_RATE = 0.5F;
    private static final float PAYCO_DISCOUNT_RATE = 0.1F;
    private ParkingTicket usedTicket;
    private boolean isTicketUse = false;
    private Clock clock;

    public void pay(Car car, LocalDateTime entranceTime) {
        User user = car.getUser();
        LocalDateTime exitTime = clock.getExitTime();
        LocalDateTime tomorrowMidnight = clock.getTomorrowMidnight(entranceTime);

        int parkingFee = 0;
        long inOutDuration = clock.calculateParkingDuration(entranceTime, exitTime);
        long toMidnightDuration = clock.calculateMidnightDuration(entranceTime, tomorrowMidnight);
        int day = clock.calculateParkingDays(entranceTime, exitTime);

        if(isTicketUse && user.getTickets().get(usedTicket) > 0) {
            inOutDuration = useParkingTickets(inOutDuration, usedTicket);
            user.useTickets(usedTicket);
        }

        parkingFee += calculateTotalCharge(day, inOutDuration, toMidnightDuration);

        parkingFee = getDiscount(parkingFee, car.getCarType(), user.isIdentification());

        if(user.getMoney() < parkingFee) {
            throw new IllegalArgumentException("Payment Failure: Insufficient money");
        }
        user.subtractMoney(parkingFee);
    }

    private int getDiscount(int parkingFee, CarType carType, boolean identification) {
        if(carType == COMPACT) {
            parkingFee *= (1 - COMPACT_DISCOUNT_RATE);
        }

        if(identification) {
            parkingFee *= (1 - PAYCO_DISCOUNT_RATE);
        }

        return parkingFee;
    }

    private int calculateTotalCharge(int day, long inOutDuration, long toMidnightDuration) {
        int parkingFee = 0;
        if(day == 2) {
            parkingFee += 3 * DAY_MAXIMUM_FEE;
            return parkingFee;
        }
        else if(day == 1 && inOutDuration > BASIC_DURATION && toMidnightDuration > BASIC_DURATION) {
            parkingFee += basicCharge(inOutDuration);
            parkingFee += firstExtraCharge(toMidnightDuration - BASIC_DURATION) + nextExtraCharge(inOutDuration - toMidnightDuration);
            return parkingFee;
        }
        else if(day == 1 && inOutDuration > BASIC_DURATION) {
            parkingFee += basicCharge(BASIC_DURATION);
            inOutDuration -= BASIC_DURATION;
            parkingFee += firstExtraCharge(inOutDuration - BASIC_DURATION);
            return parkingFee;
        }
        else if(day == 0 && inOutDuration > BASIC_DURATION) {
            parkingFee += basicCharge(inOutDuration);
            inOutDuration -= BASIC_DURATION;
            parkingFee += firstExtraCharge(inOutDuration);
            return parkingFee;
        }
        else if(day == 0 || day == 1) {
            parkingFee += basicCharge(inOutDuration);
            return parkingFee;
        }
        throw new IllegalArgumentException("Calculate Failure: ParkingTime is over 2 days");
    }

    private int basicCharge(long duration) {
        int parkingFee = 0;
        if(duration <= FREE_DURATION) {
            return parkingFee;
        }
        parkingFee += BASIC_FEE;
        return parkingFee;
    }

    private int firstExtraCharge(long duration) {
        int parkingFee = 0;
        parkingFee += EXTRA_CHARGE_UNIT * (duration / EXTRA_TIME_UNIT);
        if(duration % EXTRA_TIME_UNIT > 0) {
            parkingFee += EXTRA_CHARGE_UNIT;
        }
        if(parkingFee > DAY_MAXIMUM_FEE - BASIC_FEE) {
            parkingFee = DAY_MAXIMUM_FEE - BASIC_FEE;
        }
        return parkingFee;
    }

    private int nextExtraCharge(long duration) {
        int parkingFee = 0;
        parkingFee += EXTRA_CHARGE_UNIT * (duration / EXTRA_TIME_UNIT);
        if(duration % EXTRA_TIME_UNIT > 0) {
            parkingFee += EXTRA_CHARGE_UNIT;
        }
        if(parkingFee > DAY_MAXIMUM_FEE) {
            parkingFee = DAY_MAXIMUM_FEE;
        }
        return parkingFee;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    public void setTicketUse(boolean ticketUse, ParkingTicket usedTicket) {
        this.isTicketUse = ticketUse;
        this.usedTicket = usedTicket;
    }

    private long useParkingTickets(long inOutDuration, ParkingTicket usedTicket) {
        switch(usedTicket) {
            case HOUR1:
                inOutDuration -= 3600L;
                break;
            case HOUR2:
                inOutDuration -= 7200L;
                break;
            case HOUR3:
                inOutDuration -= 10800L;
                break;
        }
        if(inOutDuration < 0) {
            inOutDuration = 0L;
        }
        return inOutDuration;
    }
}