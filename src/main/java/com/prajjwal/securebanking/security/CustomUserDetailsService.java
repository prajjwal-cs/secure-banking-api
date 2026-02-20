package com.prajjwal.securebanking.security;

import com.prajjwal.securebanking.model.User;
import com.prajjwal.securebanking.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private static final long LOCK_DURATION = 5;
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found by username: " + username));

        if (!user.isActive()) {
            if (user.getLockTime().plusSeconds(LOCK_DURATION * 60).isBefore(Instant.now())) {
                user.setActive(true);
                user.setFailedAttempts(0);
                user.setLockTime(null);

                userRepository.save(user);
            }
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isActive(),
                true,
                true,
                true,
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }
}