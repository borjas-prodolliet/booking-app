package com.bookingapp.user.infrastructure;

import com.bookingapp.user.domain.User;
import com.bookingapp.user.domain.UserDao;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserJpaDataAccessService implements UserDao {

    private final UserRepository userRepository;

    public UserJpaDataAccessService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> get(UUID userId) {
        return userRepository.findById(userId);
    }
}
