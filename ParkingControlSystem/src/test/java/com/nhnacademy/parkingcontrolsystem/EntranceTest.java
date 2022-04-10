package com.nhnacademy.parkingcontrolsystem;

import static com.nhnacademy.parkingcontrolsystem.customer.CarType.BIGSIZE;
import static com.nhnacademy.parkingcontrolsystem.customer.CarType.MIDSIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.parkingcontrolsystem.customer.Car;
import com.nhnacademy.parkingcontrolsystem.parkingsystem.Clock;
import com.nhnacademy.parkingcontrolsystem.parkingsystem.Entrance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EntranceTest {
    Clock clock;
    Entrance entrance;
    Car car;

    @BeforeEach
    void setUp() {
        clock = new Clock();
        entrance = new Entrance();
        entrance.setClock(clock);
        car = mock(Car.class);
    }

    @DisplayName("차량스캔: 차량번호가 null일 때 예외 발생")
    @Test
    void scanner_carNumberIsNull_throwIllegalArgumentException() {
        when(car.getCarNumber()).thenReturn(null);
        when(car.getCarType()).thenReturn(MIDSIZE);
        assertThatIllegalArgumentException().isThrownBy(() -> entrance.carScan(car));
        verify(car, times(1)).getCarNumber();
    }

    @DisplayName("차량스캔: 차량타입이 null일 때 예외 발생")
    @Test
    void scanner_carTypeIsNull_throwIllegalArgumentException() {
        when(car.getCarNumber()).thenReturn("123가4567");
        when(car.getCarType()).thenReturn(null);
        assertThatIllegalArgumentException().isThrownBy(() -> entrance.carScan(car));
        verify(car, times(1)).getCarType();
    }

    @DisplayName("차량스캔: 차량타입이 대형차일 때 예외 발생")
    @Test
    void scanner_carTypeIsBIGSIZE_throwIllegalArgumentException() {
        when(car.getCarNumber()).thenReturn("123가4567");
        when(car.getCarType()).thenReturn(BIGSIZE);
        assertThatIllegalArgumentException().isThrownBy(() -> entrance.carScan(car));
        verify(car, times(2)).getCarType();
    }

    @DisplayName("차량스캔: 차량 인식 확인")
    @Test
    void scanner_carScanTest() {
        when(car.getCarNumber()).thenReturn("123가4567");
        when(car.getCarType()).thenReturn(MIDSIZE);
        entrance.carScan(car);
        assertThat(entrance.getCar()).isEqualTo(car);
        verify(car, times(1)).getCarNumber();
        verify(car, times(2)).getCarType();
    }
}