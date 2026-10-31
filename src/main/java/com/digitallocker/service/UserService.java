package com.digitallocker.service;

import com.digitallocker.entity.User;
import com.digitallocker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String register(String name, String email, String password) {
        if (userRepository.existsByEmail(email)) {
            return "An account with this email already exists.";
        }

        User user = new User(name, email, passwordEncoder.encode(password));
        userRepository.save(user);
        return null;
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}
