package com.hostfully.bookingapp.user.infrastructure;

import com.hostfully.bookingapp.user.domain.User;
import com.hostfully.bookingapp.user.domain.UserDao;
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
