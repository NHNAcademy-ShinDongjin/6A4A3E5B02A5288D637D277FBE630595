package com.nhnacademy.parkingcontrolsystem;

import static com.nhnacademy.parkingcontrolsystem.customer.CarType.MIDSIZE;
import static com.nhnacademy.parkingcontrolsystem.parkingsystem.ParkingSpace.A1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.parkingcontrolsystem.customer.Car;
import com.nhnacademy.parkingcontrolsystem.customer.User;
import com.nhnacademy.parkingcontrolsystem.parkingsystem.Entrance;
import com.nhnacademy.parkingcontrolsystem.parkingsystem.Exit;
import com.nhnacademy.parkingcontrolsystem.parkingsystem.ParkingSpace;
import com.nhnacademy.parkingcontrolsystem.parkingsystem.ParkingSystem;
import com.nhnacademy.parkingcontrolsystem.parkingsystem.Server;
import com.nhnacademy.parkingcontrolsystem.repository.ParkingLot;
import com.nhnacademy.parkingcontrolsystem.repository.UserRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ParkingSystemTest {
    ParkingSystem parkingSystem;
    Entrance entrance;
    Exit exit;
    ParkingLot parkingLot;
    UserRepository userRepository;
    Server server;
    ParkingSpace parkingSpace;
    Car car;
    User user;

    @BeforeEach
    void setUp() {
        entrance = mock(Entrance.class);
        exit = mock(Exit.class);
        parkingLot = mock(ParkingLot.class);
        userRepository = mock(UserRepository.class);
        server = mock(Server.class);
        parkingSystem = new ParkingSystem(parkingLot, userRepository, server);
    }

    @DisplayName("주차시스템: 주차 테스트")
    @Test
    void enter() {
        parkingSpace = A1;
        user = new User("Shin-Dong-Jin", 100_000);
        car = new Car("123가5678", MIDSIZE, user);

        when(entrance.getUser()).thenReturn(user);
        when(entrance.getCar()).thenReturn(car);
        when(parkingLot.getCar(parkingSpace)).thenReturn(car);

        parkingSystem.enterFromParkingSystem(parkingSpace, entrance);

        assertThat(parkingSystem.getCar(parkingSpace)).isEqualTo(car);

        verify(entrance, times(1)).getUser();
        verify(entrance, times(1)).getCar();
        verify(parkingLot, times(1)).getCar(parkingSpace);
    }


    @DisplayName("주차시스템: 출차 테스트")
    @Test
    void exit() {
        parkingSpace = A1;
        user = new User("Shin-Dong-Jin", 100_000);
        car = new Car("123가5678", MIDSIZE, user);

        when(entrance.getUser()).thenReturn(user);
        when(entrance.getCar()).thenReturn(car);
        when(parkingLot.getCar(parkingSpace)).thenReturn(car);

        when(parkingLot.getUser(parkingSpace)).thenReturn(user);
        when(parkingLot.getCar(parkingSpace)).thenReturn(null);
        when(userRepository.getEntranceTime(user)).thenReturn(LocalDateTime.now());

        parkingSystem.exitFromParkingSystem(parkingSpace, exit);

        assertThat(parkingSystem.getCar(parkingSpace)).isNull();

        verify(parkingLot, times(4)).getUser(parkingSpace);
        verify(parkingLot, times(2)).getCar(parkingSpace);
        verify(userRepository, times(1)).getEntranceTime(user);
    }
}