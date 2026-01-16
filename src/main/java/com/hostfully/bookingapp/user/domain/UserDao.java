package com.hostfully.bookingapp.user.domain;

import java.util.Optional;
import java.util.UUID;

public interface UserDao {
    Optional<User> get(UUID userId);
}
