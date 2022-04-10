package com.nhnacademy.parkingcontrolsystem.parkingsystem;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;

public class Clock {
    private boolean testMode = false;
    private LocalDateTime testEntranceTime;
    private LocalDateTime testExitTime;

    public void setTestMode(boolean selector, LocalDateTime testEntranceTime, LocalDateTime testExitTime) {
        this.testMode = selector;
        this.testEntranceTime = testEntranceTime;
        this.testExitTime = testExitTime;
    }

    public LocalDateTime getEntranceTime() {
        if(this.testMode) {
            return this.testEntranceTime;
        }
        return LocalDateTime.now();
    }

    public LocalDateTime getExitTime() {
        if(this.testMode) {
            return this.testExitTime;
        }
        return LocalDateTime.now();
    }

    public long calculateParkingDuration(LocalDateTime entranceTime, LocalDateTime exitTime) {
        return Duration.between(entranceTime, exitTime).getSeconds();
    }

    public long calculateMidnightDuration(LocalDateTime localDateTime, LocalDateTime tomorrowMidnight) {
        return Duration.between(localDateTime, tomorrowMidnight).getSeconds();
    }

    public LocalDateTime getTomorrowMidnight(LocalDateTime entranceTime) {
        if(testMode) {
            return this.testEntranceTime.toLocalDate().atStartOfDay().plusDays(1);
        }
        return entranceTime.toLocalDate().atStartOfDay().plusDays(1);
    }

    public int calculateParkingDays(LocalDateTime entranceTime, LocalDateTime exitTime) {
        return Period.between(entranceTime.toLocalDate(), exitTime.toLocalDate()).getDays();
    }
}