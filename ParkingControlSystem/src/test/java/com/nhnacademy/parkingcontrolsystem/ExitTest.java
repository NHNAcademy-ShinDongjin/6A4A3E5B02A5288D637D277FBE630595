package com.nhnacademy.parkingcontrolsystem;

import static com.nhnacademy.parkingcontrolsystem.customer.CarType.COMPACT;
import static com.nhnacademy.parkingcontrolsystem.customer.CarType.MIDSIZE;
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
import com.nhnacademy.parkingcontrolsystem.parkingsystem.Exit;
import com.nhnacademy.parkingcontrolsystem.parkingsystem.ParkingTicket;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ExitTest {
    Clock clock;
    Exit exit;
    Car car;

    @BeforeEach
    void setUp() {
        clock = new Clock();
        exit = new Exit();
        car = mock(Car.class);
    }

    @DisplayName("요금계산: 잔액 부족 시 예외 발생")
    @Test
    @Order(1)
    void pay_lackOfMoney_IllegalArgumentException() {
        String userId = "Shin-Dong-Jin";
        int money = 500;
        User user = new User(userId, money);

        when(car.getUser()).thenReturn(user);

        LocalDateTime entranceTime = LocalDateTime.of(2022, 4, 9, 12, 0);
        LocalDateTime exitTime = LocalDateTime.of(2022, 4, 9, 15, 0);

        clock.setTestMode(true, entranceTime, exitTime);
        exit.setClock(clock);

        assertThatIllegalArgumentException().isThrownBy(() -> exit.pay(car, entranceTime));
    }


    @DisplayName("요금 계산: 29분 59초, 무료")
    @Test
    @Order(2)
    void payTest1() {
        String userId = "Shin-Dong-Jin";
        int money = 100_000;
        User user = new User(userId, money);

        when(car.getUser()).thenReturn(user);

        LocalDateTime entranceTime = LocalDateTime.of(2022, 4, 9, 12, 0, 0);
        LocalDateTime exitTime = LocalDateTime.of(2022, 4, 9, 12, 29, 59);

        clock.setTestMode(true, entranceTime, exitTime);
        exit.setClock(clock);
        exit.pay(car, entranceTime);

        assertThat(user.getMoney()).isEqualTo(money);
    }

    @DisplayName("요금 계산: 30분 00초, 무료")
    @Test
    @Order(3)
    void payTest2() {
        String userId = "Shin-Dong-Jin";
        int money = 100_000;
        User user = new User(userId, money);

        when(car.getUser()).thenReturn(user);

        LocalDateTime entranceTime = LocalDateTime.of(2022, 4, 9, 12, 0, 0);
        LocalDateTime exitTime = LocalDateTime.of(2022, 4, 9, 12, 30, 00);

        clock.setTestMode(true, entranceTime, exitTime);
        exit.setClock(clock);
        exit.pay(car, entranceTime);

        assertThat(user.getMoney()).isEqualTo(money);
    }

    @DisplayName("요금 계산: 30분 01초, 1000원")
    @Test
    @Order(4)
    void payTest3() {
        String userId = "Shin-Dong-Jin";
        int money = 100_000;
        User user = new User(userId, money);

        when(car.getUser()).thenReturn(user);

        LocalDateTime entranceTime = LocalDateTime.of(2022, 4, 9, 12, 0, 0);
        LocalDateTime exitTime = LocalDateTime.of(2022, 4, 9, 12, 30, 1);

        clock.setTestMode(true, entranceTime, exitTime);
        exit.setClock(clock);
        exit.pay(car, entranceTime);

        assertThat(user.getMoney()).isEqualTo(money - 1_000);
    }

    @DisplayName("요금 계산: 1시간, 1000원")
    @Test
    @Order(5)
    void payTest4() {
        String userId = "Shin-Dong-Jin";
        int money = 100_000;
        User user = new User(userId, money);

        when(car.getUser()).thenReturn(user);

        LocalDateTime entranceTime = LocalDateTime.of(2022, 4, 9, 12, 0, 0);
        LocalDateTime exitTime = LocalDateTime.of(2022, 4, 9, 13, 0, 0);

        clock.setTestMode(true, entranceTime, exitTime);
        exit.setClock(clock);
        exit.pay(car, entranceTime);

        assertThat(user.getMoney()).isEqualTo(money - 1_000);
    }

    @DisplayName("요금 계산: 1시간 1초, 1500원")
    @Test
    @Order(6)
    void payTest5() {
        String userId = "Shin-Dong-Jin";
        int money = 100_000;
        User user = new User(userId, money);

        when(car.getUser()).thenReturn(user);

        LocalDateTime entranceTime = LocalDateTime.of(2022, 4, 9, 12, 0, 0);
        LocalDateTime exitTime = LocalDateTime.of(2022, 4, 9, 13, 0, 1);

        clock.setTestMode(true, entranceTime, exitTime);
        exit.setClock(clock);
        exit.pay(car, entranceTime);

        assertThat(user.getMoney()).isEqualTo(money - 1_500);
    }

    @DisplayName("요금 계산: 1시간 10분 1초, 2000원")
    @Test
    @Order(7)
    void payTest6() {
        String userId = "Shin-Dong-Jin";
        int money = 100_000;
        User user = new User(userId, money);

        when(car.getUser()).thenReturn(user);

        LocalDateTime entranceTime = LocalDateTime.of(2022, 4, 9, 12, 0, 0);
        LocalDateTime exitTime = LocalDateTime.of(2022, 4, 9, 13, 10, 1);

        clock.setTestMode(true, entranceTime, exitTime);
        exit.setClock(clock);
        exit.pay(car, entranceTime);

        assertThat(user.getMoney()).isEqualTo(money - 2_000);
    }

    @DisplayName("요금 계산: 일일주차(24:00이전), 15000원")
    @Test
    @Order(8)
    void payTest7() {
        String userId = "Shin-Dong-Jin";
        int money = 100_000;
        User user = new User(userId, money);

        when(car.getUser()).thenReturn(user);

        LocalDateTime entranceTime = LocalDateTime.of(2022, 4, 9, 12, 0, 0);
        LocalDateTime exitTime = LocalDateTime.of(2022, 4, 9, 18, 0, 0);

        clock.setTestMode(true, entranceTime, exitTime);
        exit.setClock(clock);
        exit.pay(car, entranceTime);

        assertThat(user.getMoney()).isEqualTo(money - 15_000);
    }

    @DisplayName("요금 계산: 일일주차 + 자정이후 10분, 15000원 + 500원")
    @Test
    @Order(9)
    void payTest8() {
        String userId = "Shin-Dong-Jin";
        int money = 100_000;
        User user = new User(userId, money);

        when(car.getUser()).thenReturn(user);

        LocalDateTime entranceTime = LocalDateTime.of(2022, 4, 9, 12, 0, 0);
        LocalDateTime exitTime = LocalDateTime.of(2022, 4, 10, 0, 10, 0);

        clock.setTestMode(true, entranceTime, exitTime);
        exit.setClock(clock);
        exit.pay(car, entranceTime);

        assertThat(user.getMoney()).isEqualTo(money - 15_500);
    }

    @DisplayName("요금 계산: 최대요금 이전 자정 지남 + 자정이후 10분, 4000원 + 500원")
    @Test
    @Order(10)
    void payTest9() {
        String userId = "Shin-Dong-Jin";
        int money = 100_000;
        User user = new User(userId, money);

        when(car.getUser()).thenReturn(user);

        LocalDateTime entranceTime = LocalDateTime.of(2022, 4, 9, 22, 0, 0);
        LocalDateTime exitTime = LocalDateTime.of(2022, 4, 10, 0, 10, 0);

        clock.setTestMode(true, entranceTime, exitTime);
        exit.setClock(clock);
        exit.pay(car, entranceTime);

        assertThat(user.getMoney()).isEqualTo(money - 4_500);
    }

    @DisplayName("요금 계산: 2일 연속 주차, 30000원")
    @Test
    @Order(11)
    void payTest10() {
        String userId = "Shin-Dong-Jin";
        int money = 100_000;
        User user = new User(userId, money);

        when(car.getUser()).thenReturn(user);

        LocalDateTime entranceTime = LocalDateTime.of(2022, 4, 9, 12, 0, 0);
        LocalDateTime exitTime = LocalDateTime.of(2022, 4, 10, 23, 59, 59);

        clock.setTestMode(true, entranceTime, exitTime);
        exit.setClock(clock);
        exit.pay(car, entranceTime);

        assertThat(user.getMoney()).isEqualTo(money - 30_000);
    }

    @DisplayName("요금 계산: 자정 이후 무료주차인 경우, 1000원")
    @Test
    @Order(12)
    void payTest11() {
        String userId = "Shin-Dong-Jin";
        int money = 100_000;
        User user = new User(userId, money);

        when(car.getUser()).thenReturn(user);

        LocalDateTime entranceTime = LocalDateTime.of(2022, 4, 9, 23, 30, 0);
        LocalDateTime exitTime = LocalDateTime.of(2022, 4, 10, 0, 30, 0);

        clock.setTestMode(true, entranceTime, exitTime);
        exit.setClock(clock);
        exit.pay(car, entranceTime);

        assertThat(user.getMoney()).isEqualTo(money - 1_000);
    }

    @DisplayName("요금 계산: 경차 50% 할인, 500원")
    @Test
    @Order(13)
    void payTest12() {
        String userId = "Shin-Dong-Jin";
        int money = 100_000;
        User user = new User(userId, money);

        when(car.getUser()).thenReturn(user);
        when(car.getCarType()).thenReturn(COMPACT);

        LocalDateTime entranceTime = LocalDateTime.of(2022, 4, 9, 23, 30, 0);
        LocalDateTime exitTime = LocalDateTime.of(2022, 4, 10, 0, 30, 0);

        clock.setTestMode(true, entranceTime, exitTime);
        exit.setClock(clock);
        exit.pay(car, entranceTime);

        assertThat(user.getMoney()).isEqualTo(money - 500);
    }

    @DisplayName("요금 계산: 페이코 회원 10% 할인, 900원")
    @Test
    @Order(14)
    void payTest13() {
        String userId = "Shin-Dong-Jin";
        int money = 100_000;
        User user = new User(userId, money);

        user.setIdentification(true);

        when(car.getUser()).thenReturn(user);
        when(car.getCarType()).thenReturn(MIDSIZE);

        LocalDateTime entranceTime = LocalDateTime.of(2022, 4, 9, 23, 30, 0);
        LocalDateTime exitTime = LocalDateTime.of(2022, 4, 10, 0, 30, 0);

        clock.setTestMode(true, entranceTime, exitTime);
        exit.setClock(clock);
        exit.pay(car, entranceTime);

        assertThat(user.getMoney()).isEqualTo(money - 900);
    }

    @DisplayName("요금 계산: 3시간 주차 후 2시간 주차권 사용, 1000원")
    @Test
    @Order(15)
    void payTest14() {
        String userId = "Shin-Dong-Jin";
        int money = 100_000;
        User user = new User(userId, money);

        Map<ParkingTicket, Integer> userTickets = new EnumMap<>(ParkingTicket.class);
        userTickets.put(HOUR1, 0);
        userTickets.put(HOUR2, 1);
        userTickets.put(HOUR3, 0);

        user.setIdentification(false);
        user.setTickets(userTickets);

        when(car.getUser()).thenReturn(user);
        when(car.getCarType()).thenReturn(MIDSIZE);

        LocalDateTime entranceTime = LocalDateTime.of(2022, 4, 10, 15, 0, 0);
        LocalDateTime exitTime = LocalDateTime.of(2022, 4, 10, 18, 0, 0);

        clock.setTestMode(true, entranceTime, exitTime);
        exit.setClock(clock);
        exit.setTicketUse(true, HOUR2);

        exit.pay(car, entranceTime);

        assertThat(user.getMoney()).isEqualTo(money - 1000);
        assertThat(user.getTickets().get(HOUR2)).isEqualTo(0);
    }

    @DisplayName("요금 계산: 59분 주차 후 1시간 주차권 사용, 무료")
    @Test
    @Order(16)
    void payTest15() {
        String userId = "Shin-Dong-Jin";
        int money = 100_000;
        User user = new User(userId, money);

        Map<ParkingTicket, Integer> userTickets = new EnumMap<>(ParkingTicket.class);
        userTickets.put(HOUR1, 1);
        userTickets.put(HOUR2, 0);
        userTickets.put(HOUR3, 0);

        user.setIdentification(false);
        user.setTickets(userTickets);

        when(car.getUser()).thenReturn(user);
        when(car.getCarType()).thenReturn(MIDSIZE);

        LocalDateTime entranceTime = LocalDateTime.of(2022, 4, 10, 15, 0, 0);
        LocalDateTime exitTime = LocalDateTime.of(2022, 4, 10, 15, 59, 0);

        clock.setTestMode(true, entranceTime, exitTime);
        exit.setClock(clock);
        exit.setTicketUse(true, HOUR1);

        exit.pay(car, entranceTime);

        assertThat(user.getMoney()).isEqualTo(money);
        assertThat(user.getTickets().get(HOUR2)).isEqualTo(0);
    }



    @DisplayName("요금 계산: 차감 확인 // 스펙1")
    @ParameterizedTest(name = "ParkingTime: {0}seconds")
    @ValueSource(longs = {1799L, 1800L, 1801L, 2399L, 2400L, 2401L, 21600L})
    @Disabled
    void payTest_spec1(long duration) {
        String userId = "Shin-Dong-Jin";
        int money = 100_000;
        User user = new User(userId, money);

        String carNumber = "123가4567";
        CarType carType = MIDSIZE;
        car = new Car(carNumber, carType, user);

        LocalDateTime entranceTime = LocalDateTime.now().minusSeconds(duration);


        exit.pay(car, entranceTime);

        if(duration == 1799L) {
            assertThat(user.getMoney()).isEqualTo(money - 1_000);
        }
        else if(duration == 1800L) {
            assertThat(user.getMoney()).isEqualTo(money - 1_000);
        }
        else if(duration == 1801L) {
            assertThat(user.getMoney()).isEqualTo(money - 1_500);
        }
        else if(duration == 2399L) {
            assertThat(user.getMoney()).isEqualTo(money - 1_500);
        }
        else if(duration == 2400L) {
            assertThat(user.getMoney()).isEqualTo(money - 1_500);
        }
        else if(duration == 2401L) {
            assertThat(user.getMoney()).isEqualTo(money - 2_000);
        }
        else if(duration == 21600L) {
            assertThat(user.getMoney()).isEqualTo(money - 10_000);
        }
    }

}