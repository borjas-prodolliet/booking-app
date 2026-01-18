package com.hostfully.bookingapp.user.domain;

import com.hostfully.bookingapp.exception.AppException;
import com.hostfully.bookingapp.exception.AppExceptionDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User getUser(UUID userId) {
        log.info("Getting user {}", userId);

        return userDao.get(userId)
                .orElseThrow(() -> {
                    log.error("User not found: {}", userId);
                    return new AppException(AppExceptionDetail.USER_NOT_FOUND);
                });
    }
}
