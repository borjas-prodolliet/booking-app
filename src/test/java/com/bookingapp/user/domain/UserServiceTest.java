package com.bookingapp.user.domain;

import com.bookingapp.exception.AppException;
import com.bookingapp.exception.AppExceptionDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDao;

    private UserService userService;

    @BeforeEach
    void setUp() {
        this.userService = new UserService(userDao);
    }

    @Test
    @DisplayName("Given a valid existing user id, when getting the user, it should return the user")
    void getUserSuccess() {
        UUID userId = UUID.randomUUID();
        User expected = User.builder()
                .id(userId)
                .email("email@example.com")
                .firstName("Jon")
                .lastName("Snow")
                .legalId("12341234")
                .legalIdType(LegalIdType.NATIONAL_ID)
                .build();

        when(userDao.get(userId)).thenReturn(Optional.of(expected));

        User actual = userService.getUser(userId);
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Given a non-existing user id, when getting the user, it should throw an exception")
    void getUserThrowsExceptionWhenNotExist() {
        UUID userId = UUID.randomUUID();

        when(userDao.get(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUser(userId))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(AppExceptionDetail.USER_NOT_FOUND.getMessage());
    }
}