package com.nhnacademy.parkingcontrolsystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import com.nhnacademy.parkingcontrolsystem.customer.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

    @DisplayName("유저생성: 사용자 아이디가 null일 때 예외 발생")
    @Test
    void user_userIdIsNull_IllegalArgumentException() {
        String userId = null;
        int userMoney = 100_000;
        assertThatIllegalArgumentException().isThrownBy(() -> new User(userId, userMoney));
    }

    @DisplayName("유저생성: 사용자 돈이 음수일 때 예외 발생")
    @Test
    void user_userMoneyIsNegative_IllegalArgumentException() {
        String userId = "Shin-Dong-Jin";
        int userMoney = -100_000;
        assertThatIllegalArgumentException().isThrownBy(() -> new User(userId, userMoney));
    }

    @DisplayName("유저생성: 사용자 생성 확인")
    @Test
    void user() {
        String userId = "Shin-Dong-Jin";
        int userMoney = 100_000;
        User user = new User(userId, userMoney);
        assertThat(user.getUserId()).isEqualTo(userId);
        assertThat(user.getMoney()).isEqualTo(userMoney);
    }
}