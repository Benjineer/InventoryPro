package com.alamu817group4.inventorypro.services;

import com.alamu817group4.inventorypro.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(userEntity -> UserDetailsImpl.builder()
                        .username(userEntity.getUsername())
                        .password(userEntity.getPassword())
                        .authorities(userEntity.getAuthorities())
                        .build()
                )
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
