package com.nhnacademy.parkingcontrolsystem;

import static com.nhnacademy.parkingcontrolsystem.customer.CarType.BIGSIZE;
import static com.nhnacademy.parkingcontrolsystem.customer.CarType.COMPACT;
import static com.nhnacademy.parkingcontrolsystem.customer.CarType.MIDSIZE;
import static com.nhnacademy.parkingcontrolsystem.parkingsystem.ParkingSpace.A1;
import static com.nhnacademy.parkingcontrolsystem.parkingsystem.ParkingSpace.A2;
import static com.nhnacademy.parkingcontrolsystem.parkingsystem.ParkingTicket.HOUR1;
import static com.nhnacademy.parkingcontrolsystem.parkingsystem.ParkingTicket.HOUR2;
import static com.nhnacademy.parkingcontrolsystem.parkingsystem.ParkingTicket.HOUR3;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.nhnacademy.parkingcontrolsystem.customer.Car;
import com.nhnacademy.parkingcontrolsystem.customer.CarType;
import com.nhnacademy.parkingcontrolsystem.customer.User;
import com.nhnacademy.parkingcontrolsystem.parkingsystem.Clock;
import com.nhnacademy.parkingcontrolsystem.parkingsystem.Entrance;
import com.nhnacademy.parkingcontrolsystem.parkingsystem.Exit;
import com.nhnacademy.parkingcontrolsystem.parkingsystem.ParkingSystem;
import com.nhnacademy.parkingcontrolsystem.parkingsystem.ParkingTicket;
import com.nhnacademy.parkingcontrolsystem.parkingsystem.Server;
import com.nhnacademy.parkingcontrolsystem.repository.EnumMapParkingLot;
import com.nhnacademy.parkingcontrolsystem.repository.HashMapUserRepository;
import com.nhnacademy.parkingcontrolsystem.repository.ParkingLot;
import com.nhnacademy.parkingcontrolsystem.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class TotalTest {
    Server server;
    ParkingLot parkingLot;
    UserRepository userRepository;
    ParkingSystem parkingSystem;
    Entrance entrance;
    Exit exit;
    Clock clock;

    @BeforeEach
    void setUp() {
        server = mock(Server.class);
        parkingLot = new EnumMapParkingLot();
        userRepository = new HashMapUserRepository();
        parkingSystem = new ParkingSystem(parkingLot, userRepository, server);
        clock = new Clock();
    }

    @Test
    @DisplayName("통합테스트: 대형차 테스트")
    @Order(1)
    void CarTypeTest_throwIllegalArgumentException_BIGSIZE() {
        String userId = "Shin-Dong-Jin";
        int money = 100_000;
        User user = new User(userId, money);

        String carNumber = "123가4567";
        CarType cartype = BIGSIZE;
        Car car = new Car(carNumber, cartype, user);

        entrance = new Entrance();
        entrance.setClock(clock);

        assertThatIllegalArgumentException().isThrownBy(() -> entrance.carScan(car));
    }

    @Nested
    @DisplayName("통합테스트: 중형차 테스트")
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class MIDSIZECarTypeTest {

        @DisplayName("1시간 주차, 비회원")
        @Test
        @Order(1)
        void notIdentifiedUserTest_MIDSIZE() {
            String userId = "Shin-Dong-Jin";
            int money = 100_000;
            when(server.paycoIdentify(userId)).thenReturn(false);
            User user = new User(userId, money);

            String carNumber = "123가4567";
            CarType cartype = MIDSIZE;
            Car car = new Car(carNumber, cartype, user);

            LocalDateTime entranceTime = LocalDateTime.of(2022, 4, 10, 12, 0);
            LocalDateTime exitTime = LocalDateTime.of(2022, 4, 10, 13, 0);

            clock.setTestMode(true, entranceTime, exitTime);

            entrance = new Entrance();
            entrance.setClock(clock);
            exit = new Exit();
            exit.setClock(clock);

            entrance.carScan(car);
            parkingSystem.enterFromParkingSystem(A1, entrance);
            parkingSystem.exitFromParkingSystem(A1, exit);

            assertThat(user.getMoney()).isEqualTo(money - 1_000);
        }

        @DisplayName("1시간 주차, 페이코 회원")
        @Test
        @Order(2)
        void identifiedUserTest_MIDSIZE() {
            String userId = "Shin-Dong-Jin";
            int money = 100_000;
            when(server.paycoIdentify(userId)).thenReturn(true);
            User user = new User(userId, money);

            String carNumber = "123가4567";
            CarType cartype = MIDSIZE;
            Car car = new Car(carNumber, cartype, user);

            LocalDateTime entranceTime = LocalDateTime.of(2022, 4, 10, 12, 0);
            LocalDateTime exitTime = LocalDateTime.of(2022, 4, 10, 13, 0);

            clock.setTestMode(true, entranceTime, exitTime);

            entrance = new Entrance();
            entrance.setClock(clock);
            exit = new Exit();
            exit.setClock(clock);

            entrance.carScan(car);
            parkingSystem.enterFromParkingSystem(A1, entrance);
            parkingSystem.exitFromParkingSystem(A1, exit);

            assertThat(user.getMoney()).isEqualTo(money - 900);
        }

        @DisplayName("주차장 입구 2개 출구 2개, 페이코회원 비회원 1명씩 서로 다른 입구로 들어와서 돌아가며 같은 자리에 1시간씩 주차하고 다른 출구로 나가기")
        @Test
        @Order(3)
        void manyGates_MIDSIZE() {
            String userId1 = "Shin-Dong-Jin";
            int money1 = 100_000;
            when(server.paycoIdentify(userId1)).thenReturn(true);
            User user1 = new User(userId1, money1);
            String carNumber1 = "123가4567";
            CarType cartype1 = MIDSIZE;
            Car car1 = new Car(carNumber1, cartype1, user1);


            String userId2 = "Dong-Jin-Shin";
            int money2 = 100_000;
            when(server.paycoIdentify(userId2)).thenReturn(false);
            User user2 = new User(userId2, money2);
            String carNumber2 = "890가1234";
            CarType cartype2 = MIDSIZE;
            Car car2 = new Car(carNumber2, cartype2, user2);

            LocalDateTime entranceTime1 = LocalDateTime.of(2022, 4, 10, 12, 0);
            LocalDateTime exitTime1 = LocalDateTime.of(2022, 4, 10, 13, 0);

            clock.setTestMode(true, entranceTime1, exitTime1);

            Entrance entrance1 = new Entrance();
            entrance1.setClock(clock);
            Exit exit1 = new Exit();
            exit1.setClock(clock);

            LocalDateTime entranceTime2 = LocalDateTime.of(2022, 4, 10, 13, 0);
            LocalDateTime exitTime2 = LocalDateTime.of(2022, 4, 10, 14, 0);

            clock.setTestMode(true, entranceTime2, exitTime2);

            Entrance entrance2 = new Entrance();
            entrance2.setClock(clock);
            Exit exit2 = new Exit();
            exit2.setClock(clock);

            entrance1.carScan(car1);
            parkingSystem.enterFromParkingSystem(A1, entrance1);
            parkingSystem.exitFromParkingSystem(A1, exit1);
            entrance2.carScan(car2);
            parkingSystem.enterFromParkingSystem(A1, entrance2);
            parkingSystem.exitFromParkingSystem(A1, exit2);

            assertThat(user1.getMoney()).isEqualTo(money1 - 900);
            assertThat(user2.getMoney()).isEqualTo(money2 - 1_000);
        }
    }

    @Nested
    @DisplayName("통합테스트: 소형차 테스트")
    @Order(3)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class COMPACTCarTypeTest {

        @DisplayName("1시간 주차, 비회원")
        @Test
        @Order(1)
        void notIdentifiedUserTest_COMPACT() {
            String userId = "Shin-Dong-Jin";
            int money = 100_000;
            when(server.paycoIdentify(userId)).thenReturn(false);
            User user = new User(userId, money);

            String carNumber = "123가4567";
            CarType cartype = COMPACT;
            Car car = new Car(carNumber, cartype, user);

            LocalDateTime entranceTime = LocalDateTime.of(2022, 4, 10, 12, 0);
            LocalDateTime exitTime = LocalDateTime.of(2022, 4, 10, 13, 0);

            clock.setTestMode(true, entranceTime, exitTime);

            entrance = new Entrance();
            entrance.setClock(clock);
            exit = new Exit();
            exit.setClock(clock);

            entrance.carScan(car);
            parkingSystem.enterFromParkingSystem(A1, entrance);
            parkingSystem.exitFromParkingSystem(A1, exit);

            assertThat(user.getMoney()).isEqualTo(money - 500);
        }

        @DisplayName("1시간 주차, 페이코 회원")
        @Test
        @Order(2)
        void identifiedUserTest_COMPACT() {
            String userId = "Shin-Dong-Jin";
            int money = 100_000;
            when(server.paycoIdentify(userId)).thenReturn(true);
            User user = new User(userId, money);

            String carNumber = "123가4567";
            CarType cartype = COMPACT;
            Car car = new Car(carNumber, cartype, user);

            LocalDateTime entranceTime = LocalDateTime.of(2022, 4, 10, 12, 0);
            LocalDateTime exitTime = LocalDateTime.of(2022, 4, 10, 13, 0);

            clock.setTestMode(true, entranceTime, exitTime);

            entrance = new Entrance();
            entrance.setClock(clock);
            exit = new Exit();
            exit.setClock(clock);

            entrance.carScan(car);
            parkingSystem.enterFromParkingSystem(A1, entrance);
            parkingSystem.exitFromParkingSystem(A1, exit);

            assertThat(user.getMoney()).isEqualTo(money - 450);
        }

        @DisplayName("주차장 입구 2개 출구 2개, 페이코 회원 비회원 1명씩 경차로 동시에 들어와서 페이코 회원은 1시간 주차권 사용, 비회원은 사용하려 했으나 주차권이 없어서 경차 할인만 받음")
        @Test
        @Order(3)
        void parkingTicketUse_COMPACT() {
            String userId1 = "Shin-Dong-Jin";
            int money1 = 100_000;
            when(server.paycoIdentify(userId1)).thenReturn(true);
            User user1 = new User(userId1, money1);
            String carNumber1 = "123가4567";
            CarType cartype1 = COMPACT;
            Car car1 = new Car(carNumber1, cartype1, user1);

            Map<ParkingTicket, Integer> user1Tickets = new EnumMap<>(ParkingTicket.class);
            user1Tickets.put(HOUR1, 1);
            user1Tickets.put(HOUR2, 0);
            user1Tickets.put(HOUR3, 0);
            when(server.getParkingTicket(userId1)).thenReturn(user1Tickets);


            String userId2 = "Dong-Jin-Shin";
            int money2 = 100_000;
            when(server.paycoIdentify(userId2)).thenReturn(false);
            User user2 = new User(userId2, money2);
            String carNumber2 = "890가1234";
            CarType cartype2 = COMPACT;
            Car car2 = new Car(carNumber2, cartype2, user2);

            Map<ParkingTicket, Integer> user2Tickets = new EnumMap<>(ParkingTicket.class);
            user2Tickets.put(HOUR1, 0);
            user2Tickets.put(HOUR2, 0);
            user2Tickets.put(HOUR3, 0);
            when(server.getParkingTicket(userId2)).thenReturn(user2Tickets);


            LocalDateTime entranceTime = LocalDateTime.of(2022, 4, 10, 12, 0);
            LocalDateTime exitTime = LocalDateTime.of(2022, 4, 10, 13, 0);

            clock.setTestMode(true, entranceTime, exitTime);

            Entrance entrance1 = new Entrance();
            entrance1.setClock(clock);
            Exit exit1 = new Exit();
            exit1.setClock(clock);

            Entrance entrance2 = new Entrance();
            entrance2.setClock(clock);
            Exit exit2 = new Exit();
            exit2.setClock(clock);

            entrance1.carScan(car1);
            entrance2.carScan(car2);
            parkingSystem.enterFromParkingSystem(A1, entrance1);
            parkingSystem.enterFromParkingSystem(A2, entrance2);
            exit1.setTicketUse(true, HOUR1);
            exit2.setTicketUse(true, HOUR1);
            parkingSystem.exitFromParkingSystem(A2, exit1);
            parkingSystem.exitFromParkingSystem(A1, exit2);

            assertThat(user1.getMoney()).isEqualTo(money1);
            assertThat(user2.getMoney()).isEqualTo(money2 - 500);
        }
    }
}