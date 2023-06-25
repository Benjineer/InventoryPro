package com.alamu817group4.inventorypro.services;

import com.alamu817group4.inventorypro.dtos.RegisterRequest;
import com.alamu817group4.inventorypro.entities.User;
import com.alamu817group4.inventorypro.enums.Role;
import com.alamu817group4.inventorypro.exceptions.InventoryProClientException;
import com.alamu817group4.inventorypro.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.FetchNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(RegisterRequest request) {

        if (request.getRole().equals(Role.ADMIN)) {
            throw new InventoryProClientException("Cannot register admin user");
        }

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        return userRepository.save(user);
    }

    public User getUser(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User getUser(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public List<User> getUsers() {

        return userRepository.findAll();
    }

    public void updateUser(String username, User updatedUser) {
        userRepository.findByEmail(username)
                .ifPresentOrElse(user -> {
                    user.setFirstname(updatedUser.getFirstname());
                    user.setLastname(updatedUser.getLastname());
                    user.setEmail(updatedUser.getEmail());
                    userRepository.save(user);
                }, () -> {
                    throw new FetchNotFoundException(User.class.getSimpleName(), username);
                });
    }

    public void updateUser(Long id, User updatedUser) {
        userRepository.findById(id)
                .ifPresentOrElse(user -> {
                    user.setFirstname(updatedUser.getFirstname());
                    user.setLastname(updatedUser.getLastname());
                    user.setEmail(updatedUser.getEmail());
                    user.setRole(updatedUser.getRole());
                    userRepository.save(user);
                }, () -> {
                    throw new FetchNotFoundException(User.class.getSimpleName(), id.toString());
                });
    }

    public void removeUser(Long id) {
        userRepository.deleteById(id);
    }
}
